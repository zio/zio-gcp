package zio.gcp.firestore

import com.google.api.core.ApiFutureToListenableFuture
import com.google.cloud.firestore._
import zio.gcp.firestore.ZFirestoreException._
import zio.gcp.firestore.models.{CollectionId, DocumentId, DocumentPath}
import zio.interop.guava._
import zio.{UIO, ZIO}

import scala.jdk.CollectionConverters._

trait Database {
  val firestore: Database.Service[Any, Any]
}

object Database {

  trait Service[R, T] {
    def getDocumentSnapshot(docId: DocumentId): ZIO[R, ZFirestoreException, DocumentSnapshot]

    def getCollection(): ZIO[R, Throwable, CollectionReference]

    def set[T](docId: DocumentId, data: T): ZIO[R, ZFirestoreException, WriteResult]

    def create[T](docId: DocumentId, data: T): ZIO[R, ZFirestoreException, WriteResult]

    def delete(docId: DocumentId): ZIO[R, ZFirestoreException, WriteResult]

    def batch: ZIO[R, Exception, WriteBatch]

    def commit(batch: WriteBatch): ZIO[Any, Throwable, List[WriteResult]]

    def collection(path: CollectionId): ZIO[R, Exception, CollectionReference]

    def document(path: DocumentPath): ZIO[R, Exception, DocumentReference]

    def getCollections(): ZIO[R, Exception, Seq[CollectionReference]]

    def collectionGroup(collectionId: CollectionId): ZIO[R, Exception, Query]

    def close(): ZIO[R, Exception, Unit]
  }

}

trait Firestore[T] extends Database.Service[FirestoreConnection, T] {

  def collectionId: CollectionId

  override def getDocumentSnapshot(
    docId: DocumentId
  ): ZIO[FirestoreConnection, ZFirestoreException, DocumentSnapshot] =
    ZIO
      .environment[FirestoreConnection]
      .flatMap { connection =>
        fromListenableFuture(
          UIO.apply(
            new ApiFutureToListenableFuture[DocumentSnapshot](
              connection.db
                .document(makeDocumentPath(docId.id))
                .get
            )
          )
        )
      }
      .mapError(e => handleRpcError(e))

  override def getCollection(): ZIO[FirestoreConnection, Throwable, CollectionReference] =
    collection(collectionId)

  override def getCollections(): ZIO[FirestoreConnection, Exception, List[CollectionReference]] =
    ZIO.access(_.db.listCollections.asScala.toList)

  override def document(
    path: DocumentPath
  ): ZIO[FirestoreConnection, Exception, DocumentReference] =
    ZIO.access(_.db.document(path.path))

  override def collectionGroup(
    collectionId: CollectionId
  ): ZIO[FirestoreConnection, Exception, Query] =
    ZIO.access(_.db.collectionGroup(collectionId.id))

  override def collection(
    path: CollectionId
  ): ZIO[FirestoreConnection, Exception, CollectionReference] =
    ZIO.access(_.db.collection(path.id))

  override def commit(batch: WriteBatch): ZIO[Any, Throwable, List[WriteResult]] =
    fromListenableFuture(
      UIO.apply(
        new ApiFutureToListenableFuture[java.util.List[WriteResult]](
          batch.commit
        )
      )
    ).map(wr => wr.asScala.toList)

  override def batch: ZIO[FirestoreConnection, Exception, WriteBatch] = ZIO.access(_.db.batch)

  override def delete(
    docId: DocumentId
  ): ZIO[FirestoreConnection, ZFirestoreException, WriteResult] =
    ZIO
      .environment[FirestoreConnection]
      .flatMap { connection =>
        fromListenableFuture(
          UIO.apply(
            new ApiFutureToListenableFuture[WriteResult](
              connection.db
                .document(makeDocumentPath(docId.id))
                .delete
            )
          )
        )
      }
      .mapError(e => handleRpcError(e))

  override def create[T](
    docId: DocumentId,
    doc: T
  ): ZIO[FirestoreConnection, ZFirestoreException, WriteResult] =
    ZIO
      .environment[FirestoreConnection]
      .flatMap { connection =>
        fromListenableFuture(
          UIO(
            new ApiFutureToListenableFuture[WriteResult](
              connection.db
                .document(makeDocumentPath(docId.id))
                .create(doc)
            )
          )
        )
      }
      .mapError(e => handleRpcError(e))

  override def set[T](
    docId: DocumentId,
    data: T
  ): ZIO[FirestoreConnection, ZFirestoreException, WriteResult] =
    ZIO
      .environment[FirestoreConnection]
      .flatMap { connection =>
        fromListenableFuture(
          UIO.apply(
            new ApiFutureToListenableFuture[WriteResult](
              connection.db
                .document(makeDocumentPath(docId.id))
                .set(data)
            )
          )
        )
      }
      .mapError(e => handleRpcError(e))

  override def close(): ZIO[FirestoreConnection, Exception, Unit] = ZIO.access(_.db.close())

  private def makeDocumentPath(documentId: String): String =
    s"${collectionId.id}/$documentId"

}
