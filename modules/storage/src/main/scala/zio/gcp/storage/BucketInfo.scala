package zio.gcp.storage

import com.google.cloud.storage.{ BucketInfo => GBucketInfo }
import zio.UIO
import com.google.cloud.storage.Acl
import scala.jdk.CollectionConverters._
import com.google.cloud.storage.Cors

final case class BucketInfo(private val bucketInfo: GBucketInfo) {
  def getAcl(): UIO[List[Acl]]        = UIO(bucketInfo.getAcl().asScala.toList)
  def getCors(): UIO[List[Cors]]      = UIO(bucketInfo.getCors().asScala.toList)
  def getCreateTime(): UIO[Long]      = UIO(bucketInfo.getCreateTime())
  def getDefaultAcl(): UIO[List[Acl]] = UIO(bucketInfo.getDefaultAcl().asScala.toList)
}
