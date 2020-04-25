package zio.gcp

import com.google.cloud.storage.{ Storage => _ }
import com.google.cloud.storage.StorageBatch
import com.google.cloud.storage.Storage.ComposeRequest
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Storage.CopyRequest
import com.google.cloud.storage.CopyWriter
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage.BlobTargetOption
import com.google.cloud.storage.Acl
import com.google.cloud.storage.Storage.BucketSourceOption
import com.google.cloud.storage.ServiceAccount
import com.google.cloud.storage.Storage.CreateHmacKeyOption
import com.google.cloud.storage.HmacKey
import com.google.cloud.storage.Storage.BlobSourceOption
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.Storage.DeleteHmacKeyOption
import com.google.cloud.storage.Storage.BlobGetOption
import com.google.cloud.storage.Storage.GetHmacKeyOption
import com.google.cloud.storage.Storage.BlobListOption
import com.google.cloud.storage.Storage.BucketListOption
import com.google.api.gax.paging.Page
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage.BucketTargetOption
import com.google.api.services.storage.model.HmacKeyMetadata
import java.util.concurrent.TimeUnit
import com.google.cloud.storage.Storage.SignUrlOption
import java.net.URL
import com.google.cloud.storage.Storage.UpdateHmacKeyOption
import com.google.cloud.storage.Storage.BlobWriteOption
import com.google.cloud.storage.HmacKey.HmacKeyState
import zio.{ Has, RIO }

package object storage {

  type Storage = Has[Storage.Service]

  def batch(): RIO[Storage, StorageBatch]                         = RIO.accessM(_.get.batch())
  def compose(composeRequest: ComposeRequest): RIO[Storage, Blob] = RIO.accessM(_.get.compose(composeRequest))
  def copy(copyRequest: CopyRequest): RIO[Storage, CopyWriter]    = RIO.accessM(_.get.copy(copyRequest))
  def create(
    blobInfo: BlobInfo,
    content: Array[Byte],
    offset: Int,
    length: Int,
    options: List[BlobTargetOption]
  ): RIO[Storage, Blob] = RIO.accessM(_.get.create(blobInfo, content, offset, length, options))
  def create(blobInfo: BlobInfo, content: Array[Byte], options: List[BlobTargetOption]): RIO[Storage, Blob] =
    RIO.accessM(_.get.create(blobInfo: BlobInfo, content, options))
  def create(blobInfo: BlobInfo, options: List[BlobTargetOption]): RIO[Storage, Blob] =
    RIO.accessM(_.get.create(blobInfo, options))
  def createAcl(blob: Blob, acl: Acl): RIO[Storage, Acl]     = RIO.accessM(_.get.createAcl(blob, acl))
  def createAcl(bucket: String, acl: Acl): RIO[Storage, Acl] = RIO.accessM(_.get.createAcl(bucket, acl))
  def createAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): RIO[Storage, Acl] =
    RIO.accessM(_.get.createAcl(bucket, acl, options))
  def createHmacKey(serviceAccount: ServiceAccount, options: List[CreateHmacKeyOption]): RIO[Storage, HmacKey] =
    RIO.accessM(_.get.createHmacKey(serviceAccount, options))
  def delete(blobIds: List[BlobId]): RIO[Storage, List[Boolean]] = RIO.accessM(_.get.delete(blobIds))
  def delete(blobId: BlobId): RIO[Storage, Boolean]              = RIO.accessM(_.get.delete(blobId))
  def delete(blobId: BlobId, options: List[BlobSourceOption]): RIO[Storage, Boolean] =
    RIO.accessM(_.get.delete(blobId, options))
  def delete(blobIds: Iterable[BlobId]): RIO[Storage, List[Boolean]] = RIO.accessM(_.get.delete(blobIds))
  def delete(bucket: String, options: List[BucketSourceOption]): RIO[Storage, Boolean] =
    RIO.accessM(_.get.delete(bucket, options))
  def delete(bucket: String, blob: String, options: List[BucketSourceOption]): RIO[Storage, Boolean] =
    RIO.accessM(_.get.delete(bucket, blob, options))
  def deleteAcl(blobId: BlobId, entity: Acl.Entity): RIO[Storage, Boolean] =
    RIO.accessM(_.get.deleteAcl(blobId, entity))
  def deleteAcl(bucket: String, entity: Acl.Entity): RIO[Storage, Boolean] =
    RIO.accessM(_.get.deleteAcl(bucket, entity))
  def deleteAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption]): RIO[Storage, Boolean] =
    RIO.accessM(_.get.deleteAcl(bucket, entity, options))
  def deleteDefaultAcl(bucket: String, entity: Acl.Entity): RIO[Storage, Boolean] =
    RIO.accessM(_.get.deleteDefaultAcl(bucket, entity))
  def deleteHmacKey(hmacKeyMetaData: HmacKey.HmacKeyMetadata, options: List[DeleteHmacKeyOption]): RIO[Storage, Unit] =
    RIO.accessM(_.get.deleteHmacKey(hmacKeyMetaData, options))
  def get(blobIds: List[BlobId]): RIO[Storage, List[Blob]] = RIO.accessM(_.get.get(blobIds))
  def get(blobId: BlobId): RIO[Storage, Option[Blob]]      = RIO.accessM(_.get.get(blobId))
  def get(blobId: BlobId, options: List[BlobGetOption]): RIO[Storage, Option[Blob]] =
    RIO.accessM(_.get.get(blobId, options))
  def get(blobIds: Iterable[BlobId]): RIO[Storage, List[Blob]]              = RIO.accessM(_.get.get(blobIds))
  def getAcl(blob: BlobId, entity: Acl.Entity): RIO[Storage, Option[Acl]]   = RIO.accessM(_.get.getAcl(blob, entity))
  def getAcl(bucket: String, entity: Acl.Entity): RIO[Storage, Option[Acl]] = RIO.accessM(_.get.getAcl(bucket, entity))
  def getAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption]): RIO[Storage, Option[Acl]] =
    RIO.accessM(_.get.getAcl(bucket, entity, options))
  def getDefaultAcl(bucket: String, entity: Acl.Entity): RIO[Storage, Option[Acl]] =
    RIO.accessM(_.get.getDefaultAcl(bucket, entity))
  def getHmacKey(accessId: String, options: List[GetHmacKeyOption]): RIO[Storage, HmacKey.HmacKeyMetadata] =
    RIO.accessM(_.get.getHmacKey(accessId, options))
  def getIamPolicy(bucket: String, options: List[BucketSourceOption]): RIO[Storage, com.google.cloud.Policy] =
    RIO.accessM(_.get.getIamPolicy(bucket, options))
  def getServiceAccount(projectId: String): RIO[Storage, ServiceAccount] =
    RIO.accessM(_.get.getServiceAccount(projectId))
  def list(options: List[BucketListOption]): RIO[Storage, Page[Bucket]] = RIO.accessM(_.get.list(options))
  def list(bucket: String, options: List[BlobListOption]): RIO[Storage, Page[Blob]] =
    RIO.accessM(_.get.list(bucket, options))
  def listAcls(blob: BlobId): RIO[Storage, List[Acl]]   = RIO.accessM(_.get.listAcls(blob))
  def listAcls(bucket: String): RIO[Storage, List[Acl]] = RIO.accessM(_.get.listAcls(bucket))
  def listAcls(bucket: String, options: List[BucketSourceOption]): RIO[Storage, List[Acl]] =
    RIO.accessM(_.get.listAcls(bucket, options))
  def listDefaultAcls(bucket: String): RIO[Storage, List[Acl]] = RIO.accessM(_.get.listDefaultAcls(bucket))
  def listHmacKeys(
    options: List[com.google.cloud.storage.Storage.ListHmacKeysOption]
  ): RIO[Storage, Page[HmacKeyMetadata]] = RIO.accessM(_.get.listHmacKeys(options))
  def lockRetentionPolicy(bucket: String, options: List[BucketTargetOption]): RIO[Storage, Bucket] =
    RIO.accessM(_.get.lockRetentionPolicy(bucket, options))
  def readAllBytes(blob: BlobId, options: List[BlobSourceOption]): RIO[Storage, Array[Byte]] =
    RIO.accessM(_.get.readAllBytes(blob, options))
  def readAllBytes(bucket: String, blob: String, options: List[BlobSourceOption]): RIO[Storage, Array[Byte]] =
    RIO.accessM(_.get.readAllBytes(bucket, blob, options))
  def reader(blobId: BlobId, options: List[BlobSourceOption]): RIO[Storage, com.google.cloud.ReadChannel] =
    RIO.accessM(_.get.reader(blobId, options))
  def reader(
    bucket: String,
    blob: String,
    options: List[BlobSourceOption]
  ): RIO[Storage, com.google.cloud.ReadChannel] = RIO.accessM(_.get.reader(bucket, blob, options))
  def setIamPolicy(
    bucket: String,
    policy: com.google.cloud.Policy,
    options: List[BucketSourceOption]
  ): RIO[Storage, com.google.cloud.Policy] = RIO.accessM(_.get.setIamPolicy(bucket, policy, options))
  def signUrl(blobInfo: BlobInfo, duration: Long, unit: TimeUnit, options: List[SignUrlOption]): RIO[Storage, URL] =
    RIO.accessM(_.get.signUrl(blobInfo, duration, unit, options))
  def testIamPermissions(
    bucket: String,
    permissions: List[String],
    options: List[BucketSourceOption]
  ): RIO[Storage, List[Boolean]]                                  = RIO.accessM(_.get.testIamPermissions(bucket, permissions, options))
  def update(blobInfos: List[BlobInfo]): RIO[Storage, List[Blob]] = RIO.accessM(_.get.update(blobInfos))
  def update(blobInfo: BlobInfo): RIO[Storage, Blob]              = RIO.accessM(_.get.update(blobInfo))
  def update(blobInfo: BlobInfo, options: List[BlobTargetOption]): RIO[Storage, Blob] =
    RIO.accessM(_.get.update(blobInfo, options))
  def update(bucketInfo: com.google.cloud.storage.BucketInfo, options: List[BucketTargetOption]): RIO[Storage, Bucket] =
    RIO.accessM(_.get.update(bucketInfo, options))
  def update(blobInfos: Iterable[BlobInfo]): RIO[Storage, List[Blob]] = RIO.accessM(_.get.update(blobInfos))
  def updateAcl(blobId: BlobId, acl: Acl): RIO[Storage, Acl]          = RIO.accessM(_.get.updateAcl(blobId, acl))
  def updateAcl(bucket: String, acl: Acl): RIO[Storage, Acl]          = RIO.accessM(_.get.updateAcl(bucket, acl))
  def updateAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): RIO[Storage, Acl] =
    RIO.accessM(_.get.updateAcl(bucket, acl, options))
  def updateDefaultAcl(bucket: String, acl: Acl): RIO[Storage, Acl] = RIO.accessM(_.get.updateDefaultAcl(bucket, acl))
  def updateHmacKeyState(
    hmacKeyMetaData: HmacKeyMetadata,
    state: HmacKeyState,
    options: List[UpdateHmacKeyOption]
  ): RIO[Storage, HmacKeyMetadata] = RIO.accessM(_.get.updateHmacKeyState(hmacKeyMetaData, state, options))
  def write(blobInfo: BlobInfo, options: List[BlobWriteOption]): RIO[Storage, com.google.cloud.WriteChannel] =
    RIO.accessM(_.get.write(blobInfo, options))
  def writer(signedURL: URL): RIO[Storage, com.google.cloud.WriteChannel] = RIO.accessM(_.get.writer(signedURL))
}
