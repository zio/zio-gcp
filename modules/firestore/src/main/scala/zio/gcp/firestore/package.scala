package zio.gcp

import com.google.cloud.firestore._
import zio.RIO

package object firestore extends FirestoreDB.Service[FirestoreDB] {

  def batch: RIO[FirestoreDB, WriteBatch] = RIO.accessM(_.firestore.batch)

  def collection(collectionName: CollectionPath): RIO[FirestoreDB, CollectionReference] =
    RIO.accessM(_.firestore.collection(collectionName))

  def collectionGroup(collectionId: CollectionPath): RIO[FirestoreDB, Query] =
    RIO.accessM(_.firestore.collectionGroup(collectionId))

  def commit(batch: WriteBatch): RIO[FirestoreDB, List[WriteResult]] =
    RIO.accessM(_.firestore.commit(batch))

  def createDocument[A](
    collectionReference: CollectionReference,
    documentPath: DocumentPath,
    document: A
  ): RIO[FirestoreDB, WriteResult] =
    RIO.accessM(_.firestore.createDocument(collectionReference, documentPath, document))

  def delete(
    collectionName: CollectionPath,
    documentPath: DocumentPath
  ): RIO[FirestoreDB, WriteResult] =
    RIO.accessM(_.firestore.delete(collectionName, documentPath))

  def document(
    collectionReference: CollectionReference,
    documentPath: DocumentPath
  ): RIO[FirestoreDB, DocumentReference] =
    RIO.accessM(_.firestore.document(collectionReference, documentPath))

  def getDocumentSnapshot(
    collectionName: CollectionPath,
    documentPath: DocumentPath
  ): RIO[FirestoreDB, DocumentSnapshot] =
    RIO.accessM(_.firestore.getDocumentSnapshot(collectionName, documentPath))

  def getCollections: RIO[FirestoreDB, List[CollectionReference]] =
    RIO.accessM(_.firestore.getCollections)

  def getAllDocuments(
    collectionName: CollectionPath
  ): RIO[FirestoreDB, List[QueryDocumentSnapshot]] =
    RIO.accessM(
      _.firestore
        .getAllDocuments(collectionName)
    )

  def set[A](
    collectionName: CollectionPath,
    documentPath: DocumentPath,
    document: A
  ): RIO[FirestoreDB, WriteResult] =
    RIO.accessM(_.firestore.set(collectionName, documentPath, document))

  def subCollection(
    documentReference: DocumentReference,
    collectionName: CollectionPath
  ): RIO[FirestoreDB, CollectionReference] =
    RIO.accessM(_.firestore.subCollection(documentReference, collectionName))

}
