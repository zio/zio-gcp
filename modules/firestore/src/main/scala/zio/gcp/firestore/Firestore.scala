package zio.gcp.firestore

import com.google.api.core.ApiFutureToListenableFuture
import com.google.cloud.firestore.{ Firestore => _, _ }

import scala.jdk.CollectionConverters._

import zio._
import zio.interop.guava._

object Firestore {

  trait Service {
    def allCollections: Task[List[CollectionReference]]
    def allDocuments(collectionPath: CollectionPath): Task[List[QueryDocumentSnapshot]]
    def batch: Task[WriteBatch]
    def collection(collectionPath: CollectionPath): Task[CollectionReference]
    def collectionGroup(collectionPath: CollectionPath): Task[Query]
    def commit(batch: WriteBatch): Task[List[WriteResult]]
    def createDocument[A](collectionRef: CollectionReference, collectionPath: DocumentPath, data: A): Task[WriteResult]
    def delete(collectionPath: CollectionPath, documentPath: DocumentPath): Task[WriteResult]
    def document(collectionRef: CollectionReference, documentPath: DocumentPath): Task[DocumentReference]
    def documentSnapshot(collectionPath: CollectionPath, documentPath: DocumentPath): Task[DocumentSnapshot]
    def set[A](collectionPath: CollectionPath, documentPath: DocumentPath, document: A): Task[WriteResult]
    def subCollection(documentRef: DocumentReference, collectionPath: CollectionPath): Task[CollectionReference]
  }

  def live: Layer[Throwable, Firestore] =
    ZLayer.fromManaged {
      val acquire = IO.effect(FirestoreOptions.getDefaultInstance().toBuilder().build().getService())
      val release = (firestore: com.google.cloud.firestore.Firestore) => IO.effect(firestore.close()).orDie

      Managed.make(acquire)(release).map { firestore =>
        new Service {
          def allCollections: Task[List[CollectionReference]] =
            Task(firestore.listCollections.asScala.toList)

          def allDocuments(
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

          def batch: Task[WriteBatch] = Task(firestore.batch)

          def collection(collectionPath: CollectionPath): Task[CollectionReference] =
            Task(firestore.collection(collectionPath.value))

          def collectionGroup(collectionPath: CollectionPath): Task[Query] =
            Task(firestore.collectionGroup(collectionPath.value))

          def commit(batch: WriteBatch): Task[List[WriteResult]] =
            fromListenableFuture(
              UIO(
                new ApiFutureToListenableFuture[java.util.List[WriteResult]](
                  batch.commit
                )
              )
            ).map(writeResults => writeResults.asScala.toList)

          def createDocument[A](
            collectionRef: CollectionReference,
            documentPath: DocumentPath,
            data: A
          ): Task[WriteResult] =
            fromListenableFuture(
              UIO(
                new ApiFutureToListenableFuture[WriteResult](
                  firestore
                    .collection(collectionRef.getPath)
                    .document(documentPath.value)
                    .create(data)
                )
              )
            )

          def delete(
            collectionPath: CollectionPath,
            documentPath: DocumentPath
          ): Task[WriteResult] =
            fromListenableFuture(
              UIO(
                new ApiFutureToListenableFuture[WriteResult](
                  firestore.collection(collectionPath.value).document(documentPath.value).delete
                )
              )
            )

          def document(
            collectionRef: CollectionReference,
            documentPath: DocumentPath
          ): Task[DocumentReference] =
            Task(collectionRef.document(documentPath.value))

          def documentSnapshot(
            collectionPath: CollectionPath,
            documentPath: DocumentPath
          ): Task[DocumentSnapshot] =
            fromListenableFuture(
              UIO(
                new ApiFutureToListenableFuture[DocumentSnapshot](
                  firestore.collection(collectionPath.value).document(documentPath.value).get
                )
              )
            )

          def set[A](
            collectionPath: CollectionPath,
            documentPath: DocumentPath,
            document: A
          ): Task[WriteResult] =
            fromListenableFuture(
              UIO(
                new ApiFutureToListenableFuture[WriteResult](
                  firestore
                    .collection(collectionPath.value)
                    .document(documentPath.value)
                    .set(document)
                )
              )
            )

          def subCollection(
            documentReference: DocumentReference,
            collectionPath: CollectionPath
          ): Task[CollectionReference] =
            Task(documentReference.collection(collectionPath.value))
        }
      }
    }
}
