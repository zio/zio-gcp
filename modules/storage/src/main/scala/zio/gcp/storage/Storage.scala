package zio.gcp.storage

import zio._
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

object Storage {
  trait Service {
    def batch(): Task[StorageBatch]
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
    def create(blobInfo: BlobInfo, options: List[BlobTargetOption]): Task[Blob]
    def createAcl(blob: Blob, acl: Acl): Task[Acl]
    def createAcl(bucket: String, acl: Acl): Task[Acl]
    def createAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): Task[Acl]
    def createHmacKey(serviceAccount: ServiceAccount, options: List[CreateHmacKeyOption]): Task[HmacKey]
    def delete(blobIds: List[BlobId]): Task[List[Boolean]]
    def delete(blobId: BlobId): Task[Boolean]
    def delete(blobId: BlobId, options: List[BlobSourceOption]): Task[Boolean]
    def delete(blobIds: Iterable[BlobId]): Task[List[Boolean]]
    def delete(bucket: String, options: List[BucketSourceOption]): Task[Boolean]
    def delete(bucket: String, blob: String, options: List[BucketSourceOption]): Task[Boolean]
    def deleteAcl(blob: BlobId, entity: Acl.Entity): Task[Boolean]
    def deleteAcl(bucket: String, entity: Acl.Entity): Task[Boolean]
    def deleteAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption]): Task[Boolean]
    def deleteDefaultAcl(bucket: String, entity: Acl.Entity): Task[Boolean]
    def deleteHmacKey(hmacKeyMetaData: HmacKey.HmacKeyMetadata, options: List[DeleteHmacKeyOption]): Task[Unit]
    def get(blobIds: List[BlobId]): Task[List[Blob]]
    def get(blobId: BlobId): Task[Option[Blob]]
    def get(blobId: BlobId, options: List[BlobGetOption]): Task[Option[Blob]]
    def get(blobIds: Iterable[BlobId]): Task[List[Blob]]
    def getAcl(blob: BlobId, entity: Acl.Entity): Task[Option[Acl]]
    def getAcl(bucket: String, entity: Acl.Entity): Task[Option[Acl]]
    def getAcl(bucket: String, entity: Acl.Entity, options: List[BucketSourceOption]): Task[Option[Acl]]
    def getDefaultAcl(bucket: String, entity: Acl.Entity): Task[Option[Acl]]
    def getHmacKey(accessId: String, options: List[GetHmacKeyOption]): Task[HmacKey.HmacKeyMetadata]
    def getIamPolicy(bucket: String, options: List[BucketSourceOption]): Task[com.google.cloud.Policy]
    def getServiceAccount(projectId: String): Task[ServiceAccount]
    def list(options: List[BucketListOption]): Task[Page[Bucket]]
    def list(bucket: String, options: List[BlobListOption]): Task[Page[Blob]]
    def listAcls(blob: BlobId): Task[List[Acl]]
    def listAcls(bucket: String): Task[List[Acl]]
    def listAcls(bucket: String, options: List[BucketSourceOption]): Task[List[Acl]]
    def listDefaultAcls(bucket: String): Task[List[Acl]]
    def listHmacKeys(options: List[com.google.cloud.storage.Storage.ListHmacKeysOption]): Task[Page[HmacKeyMetadata]]
    def lockRetentionPolicy(bucket: String, options: List[BucketTargetOption]): Task[Bucket]
    def readAllBytes(blob: BlobId, options: List[BlobSourceOption]): Task[Array[Byte]]
    def readAllBytes(bucket: String, blob: String, options: List[BlobSourceOption]): Task[Array[Byte]]
    def reader(blob: BlobId, options: List[BlobSourceOption]): Task[com.google.cloud.ReadChannel]
    def reader(bucket: String, blob: String, options: List[BlobSourceOption]): Task[com.google.cloud.ReadChannel]
    def setIamPolicy(
      bucket: String,
      policy: com.google.cloud.Policy,
      options: List[BucketSourceOption]
    ): Task[com.google.cloud.Policy]
    def signUrl(blobInfo: BlobInfo, duration: Long, unit: TimeUnit, options: List[SignUrlOption]): Task[URL]
    def testIamPermissions(
      bucket: String,
      permissions: List[String],
      options: List[BucketSourceOption]
    ): Task[List[Boolean]]
    def update(blobInfos: List[BlobInfo]): Task[List[Blob]]
    def update(blobInfo: BlobInfo): Task[Blob]
    def update(blobInfo: BlobInfo, options: List[BlobTargetOption]): Task[Blob]
    def update(bucketInfo: com.google.cloud.storage.BucketInfo, options: List[BucketTargetOption]): Task[Bucket]
    def update(blobInfos: Iterable[BlobInfo]): Task[List[Blob]]
    def updateAcl(blobId: BlobId, acl: Acl): Task[Acl]
    def updateAcl(bucket: String, acl: Acl): Task[Acl]
    def updateAcl(bucket: String, acl: Acl, options: List[BucketSourceOption]): Task[Acl]
    def updateDefaultAcl(bucket: String, acl: Acl): Task[Acl]
    def updateHmacKeyState(
      hmacKeyMetaData: HmacKeyMetadata,
      state: HmacKeyState,
      options: List[UpdateHmacKeyOption]
    ): Task[HmacKeyMetadata]
    def write(blobInfo: BlobInfo, options: List[BlobWriteOption]): Task[com.google.cloud.WriteChannel]
    def writer(signedURL: URL): Task[com.google.cloud.WriteChannel]
  }
}
