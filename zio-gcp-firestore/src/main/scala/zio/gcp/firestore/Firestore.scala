package zio.gcp.firestore

import com.google.api.core.ApiFutureToListenableFuture
import com.google.cloud
import com.google.cloud.firestore._
import zio._
import zio.interop.guava._

import scala.jdk.CollectionConverters._

trait FirestoreDB {
  val firestore: FirestoreDB.Service[Any, Any]
}

object FirestoreDB {

  trait Service[R, T] {

    def batch: RIO[R, WriteBatch]

    def collection(collectionPath: CollectionPath): RIO[R, CollectionReference]

    def commit(batch: WriteBatch): RIO[R, List[WriteResult]]

    def collectionGroup(collectionId: CollectionPath): RIO[R, Query]

    def create(
      collectionPath: CollectionPath,
      documentId: DocumentId,
      data: T
    ): RIO[R, WriteResult]

    def delete(
      collectionPath: CollectionPath,
      documentId: DocumentId
    ): RIO[R, WriteResult]

    def document(
      collectionPath: CollectionPath,
      documentPath: DocumentPath
    ): RIO[R, DocumentReference]

    def getDocumentSnapshot(
      collectionPath: CollectionPath,
      documentId: DocumentId
    ): RIO[R, DocumentSnapshot]

    def getCollections: RIO[R, List[CollectionReference]]

    def getAll(
      collectionPath: CollectionPath,
      documentIds: List[DocumentId]
    ): RIO[R, QuerySnapshot]

    def set(
      collectionPath: CollectionPath,
      documentId: DocumentId,
      data: T
    ): RIO[R, WriteResult]

  }

  final class Live[T] private (firestore: cloud.firestore.Firestore) extends Service[Any, T] {

    override def batch: Task[WriteBatch] = Task(firestore.batch)

    override def collection(collectionPath: CollectionPath): Task[CollectionReference] =
      Task(firestore.collection(collectionPath.value))

    override def commit(batch: WriteBatch): Task[List[WriteResult]] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[java.util.List[WriteResult]](
            batch.commit
          )
        )
      ).map(writeResults => writeResults.asScala.toList)

    override def collectionGroup(
      collectionPath: CollectionPath
    ): Task[Query] = Task(firestore.collectionGroup(collectionPath.value))

    override def create(
      collectionPath: CollectionPath,
      documentId: DocumentId,
      document: T
    ): Task[WriteResult] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[WriteResult](
            firestore
              .collection(collectionPath.value)
              .document(documentId.value)
              .create(document)
          )
        )
      )

    override def delete(
      collectionPath: CollectionPath,
      documentId: DocumentId
    ): Task[WriteResult] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[WriteResult](
            firestore.collection(collectionPath.value).document(documentId.value).delete
          )
        )
      )

    override def document(
      collectionPath: CollectionPath,
      documentPath: DocumentPath
    ): Task[DocumentReference] =
      Task(firestore.collection(collectionPath.value).document(documentPath.value))

    override def getDocumentSnapshot(
      collectionPath: CollectionPath,
      documentId: DocumentId
    ): Task[DocumentSnapshot] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[DocumentSnapshot](
            firestore.collection(collectionPath.value).document(documentId.value).get
          )
        )
      )

    override def getCollections: Task[List[CollectionReference]] =
      Task(firestore.listCollections.asScala.toList)

    override def getAll(
      collectionPath: CollectionPath,
      documentIds: List[DocumentId]
    ): Task[QuerySnapshot] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[QuerySnapshot](
            firestore
              .collection(collectionPath.value)
              .get
          )
        )
      )

    override def set(
      collectionPath: CollectionPath,
      documentId: DocumentId,
      document: T
    ): Task[WriteResult] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[WriteResult](
            firestore
              .collection(collectionPath.value)
              .document(documentId.value)
              .set(document)
          )
        )
      )
  }

  object Live {

    def open: TaskManaged[FirestoreDB] = {
      val instance = IO
        .effect(FirestoreOptions.getDefaultInstance.toBuilder.build.getService)
        .refineToOrDie[Exception]

      ZManaged
        .make(instance)(firestore => IO.effect(firestore.close).catchAll(_ => IO.unit))
        .map(withDB)
    }

    private def withDB(db: cloud.firestore.Firestore): FirestoreDB =
      new FirestoreDB {
        override val firestore: Service[Any, Any] = new FirestoreDB.Live[Any](db)
      }
  }

}
