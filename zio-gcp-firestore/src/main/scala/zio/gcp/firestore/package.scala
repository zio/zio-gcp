package zio.gcp

import com.google.cloud.firestore._
import zio.gcp.firestore.models.{CollectionPath, DocumentId, DocumentPath}
import zio.{RIO, URIO, ZIO}

package object firestore {

  trait Firestore[T] extends FirestoreDB.Service[FirestoreDB, T] {

    def collectionPath: CollectionPath

    def batch: URIO[FirestoreDB, WriteBatch] = ZIO.accessM(_.firestore.batch)

    def close: RIO[FirestoreDB, Unit] = ZIO.accessM(_.firestore.close)

    def collection(path: CollectionPath): URIO[FirestoreDB, CollectionReference] =
      ZIO.accessM(_.firestore.collection(path))

    def commit(batch: WriteBatch): RIO[FirestoreDB, List[WriteResult]] =
      ZIO.accessM(_.firestore.commit(batch))

    def collectionGroup(collectionId: CollectionPath): URIO[FirestoreDB, Query] =
      ZIO.accessM(_.firestore.collectionGroup(collectionId))

    def create[T](documentId: DocumentId, document: T): RIO[FirestoreDB, WriteResult] =
      ZIO.accessM(_.firestore.create(collectionPath, documentId, document))

    def delete(document: DocumentId): RIO[FirestoreDB, WriteResult] =
      ZIO.accessM(_.firestore.delete(collectionPath, document))

    def document(path: DocumentPath): URIO[FirestoreDB, DocumentReference] =
      ZIO.accessM(_.firestore.document(collectionPath, path))

    def getDocumentSnapshot(document: DocumentId): RIO[FirestoreDB, DocumentSnapshot] =
      ZIO.accessM(_.firestore.getDocumentSnapshot(collectionPath, document))

    def getCollections(): RIO[FirestoreDB, Seq[CollectionReference]] =
      ZIO.accessM(_.firestore.getCollections())

    def set[T](documentId: DocumentId, document: T): RIO[FirestoreDB, WriteResult] =
      ZIO.accessM(_.firestore.set(collectionPath, documentId, document))
  }

}
