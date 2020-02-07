package zio.gcp

import com.google.cloud.firestore._
import zio.{RIO, URIO, ZIO}

package object firestore {

  final case class DocumentPath(value: String) extends AnyVal
  final case class DocumentId(value: String) extends AnyVal
  final case class CollectionPath(value: String) extends AnyVal

  trait Firestore[T] extends FirestoreDB.Service[FirestoreDB, T] {

    def collectionPath: CollectionPath

    def batch: URIO[FirestoreDB, WriteBatch] = ZIO.accessM(_.firestore.batch)

    def close: RIO[FirestoreDB, Unit] = ZIO.accessM(_.firestore.close)

    def collection(
      collectionPath: CollectionPath
    ): URIO[FirestoreDB, CollectionReference] =
      ZIO.accessM(_.firestore.collection(collectionPath))

    def commit(batch: WriteBatch): RIO[FirestoreDB, List[WriteResult]] =
      ZIO.accessM(_.firestore.commit(batch))

    def collectionGroup(collectionId: CollectionPath): URIO[FirestoreDB, Query] =
      ZIO.accessM(_.firestore.collectionGroup(collectionId))

    def create[T](documentId: DocumentId, document: T): RIO[FirestoreDB, WriteResult] =
      ZIO.accessM(_.firestore.create(collectionPath, documentId, document))

    def delete(documentId: DocumentId): RIO[FirestoreDB, WriteResult] =
      ZIO.accessM(_.firestore.delete(collectionPath, documentId))

    def document(documentPath: DocumentPath): URIO[FirestoreDB, DocumentReference] =
      ZIO.accessM(_.firestore.document(collectionPath, documentPath))

    def getDocumentSnapshot(documentId: DocumentId): RIO[FirestoreDB, DocumentSnapshot] =
      ZIO.accessM(_.firestore.getDocumentSnapshot(collectionPath, documentId))

    def getCollections(): RIO[FirestoreDB, Seq[CollectionReference]] =
      ZIO.accessM(_.firestore.getCollections())

    def set[T](documentId: DocumentId, document: T): RIO[FirestoreDB, WriteResult] =
      ZIO.accessM(_.firestore.set(collectionPath, documentId, document))
  }

}
