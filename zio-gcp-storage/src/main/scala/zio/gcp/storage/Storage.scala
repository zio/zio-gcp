package zio.gcp.storage

import com.google.cloud.storage.BucketInfo
import zio.URIO

trait Storage {
  val storage: Storage.Service[Any]
}

object Storage {
  trait Service[R] {
    def create(bucketInfo: BucketInfo): URIO[R, BucketInfo]

  }
}
