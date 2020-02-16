package zio.gcp

import com.google.cloud.firestore._
import zio.RIO

package object firestore extends FirestoreDB.Service[FirestoreDB] {
    def batch: RIO[FirestoreDB, WriteBatch] = RIO.accessM(_.firestore.batch)

    def collection(
      collectionPath: CollectionPath
    ): RIO[FirestoreDB, CollectionReference] =
      RIO.accessM(_.firestore.collection(collectionPath))

    def commit(batch: WriteBatch): RIO[FirestoreDB, List[WriteResult]] =
      RIO.accessM(_.firestore.commit(batch))

    def collectionGroup(collectionId: CollectionPath): RIO[FirestoreDB, Query] =
      RIO.accessM(_.firestore.collectionGroup(collectionId))

    def create[A](
      collectionPath: CollectionPath,
      documentId: DocumentId,
      document: A
    ): RIO[FirestoreDB, WriteResult] =
      RIO.accessM(_.firestore.create(collectionPath, documentId, document))

    def delete(
      collectionPath: CollectionPath,
      documentId: DocumentId
    ): RIO[FirestoreDB, WriteResult] =
      RIO.accessM(_.firestore.delete(collectionPath, documentId))

    def document(
      collectionPath: CollectionPath,
      documentPath: DocumentPath
    ): RIO[FirestoreDB, DocumentReference] =
      RIO.accessM(_.firestore.document(collectionPath, documentPath))

    def getDocumentSnapshot(
      collectionPath: CollectionPath,
      documentId: DocumentId
    ): RIO[FirestoreDB, DocumentSnapshot] =
      RIO.accessM(_.firestore.getDocumentSnapshot(collectionPath, documentId))

    def getCollections: RIO[FirestoreDB, List[CollectionReference]] =
      RIO.accessM(_.firestore.getCollections)

    def getAllDocuments(
      collectionPath: CollectionPath,
      documentIds: List[DocumentId]
    ): RIO[FirestoreDB, List[QueryDocumentSnapshot]] =
      RIO.accessM(
        _.firestore
          .getAllDocuments(collectionPath, documentIds)
      )

    def set[A](
      collectionPath: CollectionPath,
      documentId: DocumentId,
      document: A
    ): RIO[FirestoreDB, WriteResult] =
      RIO.accessM(_.firestore.set(collectionPath, documentId, document))
  }
