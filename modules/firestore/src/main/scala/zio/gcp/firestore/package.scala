package zio.gcp

import com.google.cloud.firestore._
import zio.RIO

package object firestore extends FirestoreDB.Service[FirestoreDB] {

  def batch: RIO[FirestoreDB, WriteBatch] = RIO.accessM(_.firestore.batch)

  def collection(collectionPath: CollectionPath): RIO[FirestoreDB, CollectionReference] =
    RIO.accessM(_.firestore.collection(collectionPath))

  def collectionGroup(collectionPath: CollectionPath): RIO[FirestoreDB, Query] =
    RIO.accessM(_.firestore.collectionGroup(collectionPath))

  def commit(batch: WriteBatch): RIO[FirestoreDB, List[WriteResult]] =
    RIO.accessM(_.firestore.commit(batch))

  def createDocument[A](
    collectionReference: CollectionReference,
    documentPath: DocumentPath,
    document: A
  ): RIO[FirestoreDB, WriteResult] =
    RIO.accessM(_.firestore.createDocument(collectionReference, documentPath, document))

  def delete(
    collectionPath: CollectionPath,
    documentPath: DocumentPath
  ): RIO[FirestoreDB, WriteResult] =
    RIO.accessM(_.firestore.delete(collectionPath, documentPath))

  def document(
    collectionReference: CollectionReference,
    documentPath: DocumentPath
  ): RIO[FirestoreDB, DocumentReference] =
    RIO.accessM(_.firestore.document(collectionReference, documentPath))

  def getDocumentSnapshot(
    collectionPath: CollectionPath,
    documentPath: DocumentPath
  ): RIO[FirestoreDB, DocumentSnapshot] =
    RIO.accessM(_.firestore.getDocumentSnapshot(collectionPath, documentPath))

  def getCollections: RIO[FirestoreDB, List[CollectionReference]] =
    RIO.accessM(_.firestore.getCollections)

  def getAllDocuments(
    collectionPath: CollectionPath
  ): RIO[FirestoreDB, List[QueryDocumentSnapshot]] =
    RIO.accessM(
      _.firestore
        .getAllDocuments(collectionPath)
    )

  def set[A](
    collectionPath: CollectionPath,
    documentPath: DocumentPath,
    document: A
  ): RIO[FirestoreDB, WriteResult] =
    RIO.accessM(_.firestore.set(collectionPath, documentPath, document))

  def subCollection(
    documentReference: DocumentReference,
    collectionName: CollectionPath
  ): RIO[FirestoreDB, CollectionReference] =
    RIO.accessM(_.firestore.subCollection(documentReference, collectionName))

}
