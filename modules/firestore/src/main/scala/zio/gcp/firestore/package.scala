package zio.gcp

import com.google.cloud.firestore.{ Firestore => _, _ }

import zio.{ Has, RIO }

package object firestore {

  type Firestore = Has[Firestore.Service]

  def allCollections: RIO[Firestore, List[CollectionReference]] =
    RIO.accessM(_.get.allCollections)

  def allDocuments(collectionPath: CollectionPath): RIO[Firestore, List[QueryDocumentSnapshot]] =
    RIO.accessM(_.get.allDocuments(collectionPath))

  def batch: RIO[Firestore, WriteBatch] =
    RIO.accessM(_.get.batch)

  def collection(collectionPath: CollectionPath): RIO[Firestore, CollectionReference] =
    RIO.accessM(_.get.collection(collectionPath))

  def collectionGroup(collectionPath: CollectionPath): RIO[Firestore, Query] =
    RIO.accessM(_.get.collectionGroup(collectionPath))

  def commit(batch: WriteBatch): RIO[Firestore, List[WriteResult]] =
    RIO.accessM(_.get.commit(batch))

  def createDocument[A](
    collectionRef: CollectionReference,
    documentPath: DocumentPath,
    data: A
  ): RIO[Firestore, WriteResult] =
    RIO.accessM(_.get.createDocument(collectionRef, documentPath, data))

  def delete(collectionPath: CollectionPath, documentPath: DocumentPath): RIO[Firestore, WriteResult] =
    RIO.accessM(_.get.delete(collectionPath, documentPath))

  def document(collectionRef: CollectionReference, documentPath: DocumentPath): RIO[Firestore, DocumentReference] =
    RIO.accessM(_.get.document(collectionRef, documentPath))

  def documentSnapshot(collectionPath: CollectionPath, documentPath: DocumentPath): RIO[Firestore, DocumentSnapshot] =
    RIO.accessM(_.get.documentSnapshot(collectionPath, documentPath))

  def set[A](collectionPath: CollectionPath, documentPath: DocumentPath, document: A): RIO[Firestore, WriteResult] =
    RIO.accessM(_.get.set(collectionPath, documentPath, document))

  def subCollection(
    documentRef: DocumentReference,
    collectionPath: CollectionPath
  ): RIO[Firestore, CollectionReference] =
    RIO.accessM(_.get.subCollection(documentRef, collectionPath))
}
