package zio.gcp.firestore

import com.google.api.core.ApiFutureToListenableFuture
import com.google.cloud
import com.google.cloud.firestore._
import zio._
import zio.interop.guava._

import scala.jdk.CollectionConverters._

trait FirestoreDB {
  val firestore: FirestoreDB.Service[Any]
}

object FirestoreDB {

  trait Service[R] {

    def batch: RIO[R, WriteBatch]

    def collection(collectionPath: CollectionPath): RIO[R, CollectionReference]

    def collectionGroup(collectionId: CollectionPath): RIO[R, Query]

    def commit(batch: WriteBatch): RIO[R, List[WriteResult]]

    def createDocument[A](
      collectionReference: CollectionReference,
      documentPath: DocumentPath,
      data: A
    ): RIO[R, WriteResult]

    def delete(
      collectionPath: CollectionPath,
      documentId: DocumentPath
    ): RIO[R, WriteResult]

    def document(
      collectionReference: CollectionReference,
      documentPath: DocumentPath
    ): RIO[R, DocumentReference]

    def getDocumentSnapshot(
      collectionPath: CollectionPath,
      documentId: DocumentPath
    ): RIO[R, DocumentSnapshot]

    def getCollections: RIO[R, List[CollectionReference]]

    def getAllDocuments(
      collectionPath: CollectionPath
    ): RIO[R, List[QueryDocumentSnapshot]]

    def set[A](
      collectionPath: CollectionPath,
      documentId: DocumentPath,
      data: A
    ): RIO[R, WriteResult]

    def subCollection(
      documentReference: DocumentReference,
      collectionPath: CollectionPath
    ): RIO[R, CollectionReference]
  }

  final class Live private (firestore: cloud.firestore.Firestore) extends Service[Any] {

    override def batch: Task[WriteBatch] = Task(firestore.batch)

    override def collection(collectionPath: CollectionPath): Task[CollectionReference] =
      Task(firestore.collection(collectionPath.value))

    override def collectionGroup(
      collectionPath: CollectionPath
    ): Task[Query] = Task(firestore.collectionGroup(collectionPath.value))

    override def commit(batch: WriteBatch): Task[List[WriteResult]] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[java.util.List[WriteResult]](
            batch.commit
          )
        )
      ).map(writeResults => writeResults.asScala.toList)

    override def createDocument[A](
      collectionReference: CollectionReference,
      documentId: DocumentPath,
      document: A
    ): Task[WriteResult] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[WriteResult](
            firestore
              .collection(collectionReference.getPath)
              .document(documentId.value)
              .create(document)
          )
        )
      )

    override def delete(
      collectionPath: CollectionPath,
      documentId: DocumentPath
    ): Task[WriteResult] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[WriteResult](
            firestore.collection(collectionPath.value).document(documentId.value).delete
          )
        )
      )

    override def document(
      collectionReference: CollectionReference,
      documentPath: DocumentPath
    ): Task[DocumentReference] =
      Task(collectionReference.document(documentPath.value))

    override def getDocumentSnapshot(
      collectionPath: CollectionPath,
      documentId: DocumentPath
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

    override def getAllDocuments(
      collectionPath: CollectionPath
    ): Task[List[QueryDocumentSnapshot]] =
      fromListenableFuture(
        UIO(
          new ApiFutureToListenableFuture[QuerySnapshot](
            firestore
              .collection(collectionPath.value)
              .get
          )
        )
      ).map(querySnapshot => querySnapshot.getDocuments.asScala.toList)

    override def set[A](
      collectionPath: CollectionPath,
      documentId: DocumentPath,
      document: A
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

    override def subCollection(
      documentReference: DocumentReference,
      collectionPath: CollectionPath
    ): Task[CollectionReference] =
      Task(documentReference.collection(collectionPath.value))
  }

  object Live {

    def open: TaskManaged[FirestoreDB] = {
      val instance = IO
        .effect(FirestoreOptions.getDefaultInstance.toBuilder.build.getService)
        .refineToOrDie[Exception]

      ZManaged
        .make(instance)(firestore => IO.effect(firestore.close).orDie)
        .map(withDB)
    }

    private def withDB(db: cloud.firestore.Firestore): FirestoreDB =
      new FirestoreDB {
        override val firestore: Service[Any] = new FirestoreDB.Live(db)
      }
  }

}
