package zio.gcp.storage

import java.net.URL
import java.util.concurrent.TimeUnit

import com.google.api.gax.paging.Page
import com.google.cloud.storage.HmacKey.HmacKeyState
import com.google.cloud.storage.Storage._
import com.google.cloud.storage.{ Acl, Blob, BlobId, BlobInfo, BucketInfo, CopyWriter, StorageBatch, StorageOptions }
import com.google.cloud.{ Policy, ReadChannel, WriteChannel }
import zio._

import scala.jdk.CollectionConverters._

object Storage {
  trait Service {
    def batch: Task[StorageBatch]
    def compose(composeRequest: ComposeRequest): Task[Blob]
    def copy(copyRequest: CopyRequest): Task[CopyWriter]
    def create(
      blobInfo: BlobInfo,
      content: Array[Byte],
      offset: Int,
      length: Int,
      options: List[BlobTargetOption]
    ): Task[Blob]
    def create(blobInfo: BlobInfo, content: Array[Byte], options: List[BlobTargetOption]): Task[Blob]
    def createAcl(blobId: BlobId, acl: Acl): Task[Acl]
    def createAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): Task[Acl]
    def createHmacKey(
      serviceAccount: com.google.cloud.storage.ServiceAccount,
      options: List[CreateHmacKeyOption]
    ): Task[com.google.cloud.storage.HmacKey]
    def delete(blobIds: List[BlobId]): Task[List[Boolean]]
    def delete(blobId: BlobId, options: List[BlobSourceOption]): Task[Boolean]
    def delete(blobIds: Iterable[BlobId]): Task[List[Boolean]]
    def delete(bucket: String, options: List[BucketSourceOption]): Task[Boolean]
    def delete(bucket: String, blob: String, options: List[BlobSourceOption]): Task[Boolean]
    def deleteAcl(blob: BlobId, entity: Acl.Entity): Task[Boolean]
    def deleteAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption]): Task[Boolean]
    def deleteDefaultAcl(bucket: String, entity: Acl.Entity): Task[Boolean]
    def deleteHmacKey(
      hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
      options: List[DeleteHmacKeyOption]
    ): Task[Unit]
    def get(blobIds: List[BlobId]): Task[List[Blob]]
    def get(blobId: BlobId, options: List[BlobGetOption]): Task[Option[Blob]]
    def get(blobIds: Iterable[BlobId]): Task[List[Blob]]
    def getAcl(blob: BlobId, entity: Acl.Entity): Task[Option[Acl]]
    def getAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption]): Task[Option[Acl]]
    def getDefaultAcl(bucket: String, entity: Acl.Entity): Task[Option[Acl]]
    def getHmacKey(
      accessId: String,
      options: List[GetHmacKeyOption]
    ): Task[com.google.cloud.storage.HmacKey.HmacKeyMetadata]
    def getIamPolicy(bucket: String, options: List[BucketSourceOption]): Task[Policy]
    def getServiceAccount(projectId: String): Task[com.google.cloud.storage.ServiceAccount]
    def list(options: List[BucketListOption]): Task[com.google.api.gax.paging.Page[com.google.cloud.storage.Bucket]]
    def list(bucket: String, options: List[BlobListOption]): Task[Page[Blob]]
    def listAcls(blob: BlobId): Task[List[Acl]]
    def listAcls(bucket: String, options: List[BucketSourceOption]): Task[List[Acl]]
    def listDefaultAcls(bucket: String): Task[List[Acl]]
    def listHmacKeys(
      options: List[ListHmacKeysOption]
    ): Task[com.google.api.gax.paging.Page[com.google.cloud.storage.HmacKey.HmacKeyMetadata]]
    def lockRetentionPolicy(
      bucketInfo: BucketInfo,
      options: List[BucketTargetOption]
    ): Task[com.google.cloud.storage.Bucket]
    def readAllBytes(blob: BlobId, options: List[BlobSourceOption]): Task[Array[Byte]]
    def readAllBytes(bucket: String, blob: String, options: List[BlobSourceOption]): Task[Array[Byte]]
    def reader(blob: BlobId, options: List[BlobSourceOption]): Task[ReadChannel]
    def reader(bucket: String, blob: String, options: List[BlobSourceOption]): Task[ReadChannel]
    def setIamPolicy(
      bucket: String,
      policy: Policy,
      options: List[BucketSourceOption]
    ): Task[Policy]
    def signUrl(blobInfo: BlobInfo, duration: Long, unit: TimeUnit, options: List[SignUrlOption]): Task[URL]
    def testIamPermissions(
      bucket: String,
      permissions: List[String],
      options: List[BucketSourceOption]
    ): Task[List[Boolean]]
    def update(blobInfos: List[BlobInfo]): Task[List[Blob]]
    def update(blobInfo: BlobInfo, options: List[BlobTargetOption]): Task[Blob]
    def update(bucketInfo: BucketInfo, options: List[BucketTargetOption]): Task[com.google.cloud.storage.Bucket]
    def updateAcl(blobId: BlobId, acl: Acl): Task[Acl]
    def updateAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): Task[Acl]
    def updateDefaultAcl(bucket: String, acl: Acl): Task[Acl]
    def updateHmacKeyState(
      hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
      state: HmacKeyState,
      options: List[UpdateHmacKeyOption]
    ): Task[com.google.cloud.storage.HmacKey.HmacKeyMetadata]
    def writer(blobInfo: BlobInfo, options: List[BlobWriteOption]): Task[WriteChannel]
    def writer(signedURL: URL): Task[WriteChannel]
  }

  def live: Layer[Throwable, Storage] =
    ZLayer.fromManaged {

      val acquire = IO.effect(StorageOptions.newBuilder().build().getService)

      Managed.fromEffect(acquire).map { storage =>
        new Service {
          override def batch: Task[StorageBatch] = Task(storage.batch())

          override def compose(composeRequest: ComposeRequest): Task[Blob] = Task(storage.compose(composeRequest))

          override def copy(copyRequest: CopyRequest): Task[CopyWriter] = Task(storage.copy(copyRequest))

          override def create(
            blobInfo: BlobInfo,
            content: Array[Byte],
            offset: Int,
            length: Int,
            options: List[BlobTargetOption]
          ): Task[Blob] = Task(storage.create(blobInfo, content, offset, length, options: _*))

          override def create(blobInfo: BlobInfo, content: Array[Byte], options: List[BlobTargetOption]): Task[Blob] =
            Task(storage.create(blobInfo, content, options: _*))

          override def createAcl(blobId: BlobId, acl: Acl): Task[Acl] = Task(storage.createAcl(blobId, acl))

          override def createAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): Task[Acl] =
            Task(storage.createAcl(bucket, acl, options: _*))

          override def createHmacKey(
            serviceAccount: com.google.cloud.storage.ServiceAccount,
            options: List[CreateHmacKeyOption]
          ): Task[com.google.cloud.storage.HmacKey] = Task(storage.createHmacKey(serviceAccount, options: _*))

          override def delete(blobIds: List[BlobId]): Task[List[Boolean]] =
            Task(storage.delete(blobIds: _*).asScala.toList.map(Boolean.unbox(_)))

          override def delete(blobId: BlobId, options: List[BlobSourceOption]): Task[Boolean] =
            Task(storage.delete(blobId, options: _*))

          override def delete(blobIds: Iterable[BlobId]): Task[List[Boolean]] =
            Task(storage.delete(blobIds.asJavaCollection).asScala.toList.map(Boolean.unbox(_)))

          override def delete(bucket: String, options: List[BucketSourceOption]): Task[Boolean] =
            Task(storage.delete(bucket, options: _*))

          override def delete(bucket: String, blob: String, options: List[BlobSourceOption]): Task[Boolean] =
            Task(storage.delete(bucket, blob, options: _*))

          override def deleteAcl(blob: BlobId, entity: Acl.Entity): Task[Boolean] =
            Task(storage.deleteAcl(blob, entity))

          override def deleteAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption]): Task[Boolean] =
            Task(storage.deleteAcl(bucket, entity, options: _*))

          override def deleteDefaultAcl(bucket: String, entity: Acl.Entity): Task[Boolean] =
            Task(storage.deleteDefaultAcl(bucket, entity))

          override def deleteHmacKey(
            hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
            options: List[DeleteHmacKeyOption]
          ): Task[Unit] =
            Task(storage.deleteHmacKey(hmacKeyMetaData, options: _*))

          override def get(blobIds: List[BlobId]): Task[List[Blob]] =
            Task(storage.get(blobIds.asJavaCollection).asScala.toList)

          override def get(blobId: BlobId, options: List[BlobGetOption]): Task[Option[Blob]] =
            Task(Option(storage.get(blobId, options: _*)))

          override def get(blobIds: Iterable[BlobId]): Task[List[Blob]] =
            Task(storage.get(blobIds.asJavaCollection).asScala.toList)

          override def getAcl(blob: BlobId, entity: Acl.Entity): Task[Option[Acl]] =
            Task(Option(storage.getAcl(blob, entity)))

          override def getAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption])
            : Task[Option[Acl]] = Task(Option(storage.getAcl(bucket, entity, options: _*)))

          override def getDefaultAcl(bucket: String, entity: Acl.Entity): Task[Option[Acl]] =
            Task(Option(storage.getDefaultAcl(bucket, entity)))

          override def getHmacKey(accessId: String, options: List[GetHmacKeyOption])
            : Task[com.google.cloud.storage.HmacKey.HmacKeyMetadata] =
            Task(storage.getHmacKey(accessId, options: _*))

          override def getIamPolicy(bucket: String, options: List[BucketSourceOption]): Task[Policy] =
            Task(storage.getIamPolicy(bucket, options: _*))

          override def getServiceAccount(projectId: String): Task[com.google.cloud.storage.ServiceAccount] =
            Task(storage.getServiceAccount(projectId))

          override def list(
            options: List[BucketListOption]
          ): Task[com.google.api.gax.paging.Page[com.google.cloud.storage.Bucket]] = Task(storage.list(options: _*))

          override def list(bucket: String, options: List[BlobListOption]): Task[com.google.api.gax.paging.Page[Blob]] =
            Task(storage.list(bucket, options: _*))

          override def listAcls(blob: BlobId): Task[List[Acl]] = Task(storage.listAcls(blob).asScala.toList)

          override def listAcls(bucket: String, options: List[BucketSourceOption]): Task[List[Acl]] =
            Task(storage.listAcls(bucket, options: _*).asScala.toList)

          override def listDefaultAcls(bucket: String): Task[List[Acl]] =
            Task(storage.listDefaultAcls(bucket).asScala.toList)

          override def listHmacKeys(
            options: List[ListHmacKeysOption]
          ): Task[com.google.api.gax.paging.Page[com.google.cloud.storage.HmacKey.HmacKeyMetadata]] =
            Task(storage.listHmacKeys(options: _*))

          override def lockRetentionPolicy(bucketInfo: BucketInfo, options: List[BucketTargetOption])
            : Task[com.google.cloud.storage.Bucket] =
            Task(storage.lockRetentionPolicy(bucketInfo, options: _*))

          override def readAllBytes(blob: BlobId, options: List[BlobSourceOption]): Task[Array[Byte]] =
            Task(storage.readAllBytes(blob, options: _*))

          override def readAllBytes(bucket: String, blob: String, options: List[BlobSourceOption]): Task[Array[Byte]] =
            Task(storage.readAllBytes(bucket, blob, options: _*))

          override def reader(blob: BlobId, options: List[BlobSourceOption]): Task[ReadChannel] =
            Task(storage.reader(blob, options: _*))

          override def reader(bucket: String, blob: String, options: List[BlobSourceOption]): Task[ReadChannel] =
            Task(storage.reader(bucket, blob, options: _*))

          override def setIamPolicy(bucket: String, policy: Policy, options: List[BucketSourceOption]): Task[Policy] =
            Task(storage.setIamPolicy(bucket, policy, options: _*))

          override def signUrl(blobInfo: BlobInfo, duration: Long, unit: TimeUnit, options: List[SignUrlOption])
            : Task[URL] = Task(storage.signUrl(blobInfo, duration, unit, options: _*))

          override def testIamPermissions(bucket: String, permissions: List[String], options: List[BucketSourceOption])
            : Task[List[Boolean]] =
            Task(
              storage.testIamPermissions(bucket, permissions.asJava, options: _*).asScala.toList.map(Boolean.unbox(_))
            )
          override def update(blobInfos: List[BlobInfo]): Task[List[Blob]] =
            Task(storage.update(blobInfos: _*).asScala.toList)

          override def update(blobInfo: BlobInfo, options: List[BlobTargetOption]): Task[Blob] =
            Task(storage.update(blobInfo, options: _*))

          override def update(bucketInfo: BucketInfo, options: List[BucketTargetOption])
            : Task[com.google.cloud.storage.Bucket] =
            Task(storage.update(bucketInfo, options: _*))

          override def updateAcl(blobId: BlobId, acl: Acl): Task[Acl] = Task(storage.updateAcl(blobId, acl))

          override def updateAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): Task[Acl] =
            Task(storage.updateAcl(bucket, acl, options: _*))

          override def updateDefaultAcl(bucket: String, acl: Acl): Task[Acl] =
            Task(storage.updateDefaultAcl(bucket, acl))

          override def updateHmacKeyState(
            hmacKeyMetaData: com.google.cloud.storage.HmacKey.HmacKeyMetadata,
            state: HmacKeyState,
            options: List[UpdateHmacKeyOption]
          ): Task[com.google.cloud.storage.HmacKey.HmacKeyMetadata] =
            Task(storage.updateHmacKeyState(hmacKeyMetaData, state, options: _*))

          override def writer(blobInfo: BlobInfo, options: List[BlobWriteOption]): Task[WriteChannel] =
            Task(storage.writer(blobInfo, options: _*))

          override def writer(signedURL: URL): Task[WriteChannel] = Task(storage.writer(signedURL))
        }
      }
    }

}
