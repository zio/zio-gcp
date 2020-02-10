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

    def batch: URIO[R, WriteBatch]

    def close: RIO[R, Unit]

    def collection(collectionPath: CollectionPath): URIO[R, CollectionReference]

    def commit(batch: WriteBatch): RIO[R, List[WriteResult]]

    def collectionGroup(collectionId: CollectionPath): URIO[R, Query]

    def create[T](
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
    ): URIO[R, DocumentReference]

    def getDocumentSnapshot(
      collectionPath: CollectionPath,
      documentId: DocumentId
    ): RIO[R, DocumentSnapshot]

    def getCollections(): RIO[R, List[CollectionReference]]

    def set[T](
      collectionPath: CollectionPath,
      documentId: DocumentId,
      data: T
    ): RIO[R, WriteResult]

  }

  final class Live[T] private (firestore: cloud.firestore.Firestore) extends Service[Any, T] {

    override def batch: UIO[WriteBatch] = UIO(firestore.batch())

    override def close: Task[Unit] = Task(firestore.close())

    override def collection(collectionPath: CollectionPath): UIO[CollectionReference] =
      UIO(firestore.collection(collectionPath.value))

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
    ): UIO[Query] = UIO(firestore.collectionGroup(collectionPath.value))

    override def create[T](
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
    ): UIO[DocumentReference] =
      UIO(firestore.collection(collectionPath.value).document(documentPath.value))

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

    override def getCollections(): Task[List[CollectionReference]] =
      Task(firestore.listCollections.asScala.toList)

    override def set[T](
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
        .effect(withDB(FirestoreOptions.getDefaultInstance.toBuilder.build().getService))
        .refineToOrDie[Exception]

      ZManaged.make(instance)(_.firestore.close.catchAll(_ => IO.unit))
    }

    private def withDB(db: cloud.firestore.Firestore): FirestoreDB =
      new FirestoreDB {
        override val firestore: Service[Any, Any] = new FirestoreDB.Live[Any](db)
      }
  }

}
