package zio.gcp.storage

import zio._

trait Storage {
  val storage: Storage.Service[Any]
}

object Storage {
  trait Service[R] {
    def create(bucketInfo: BucketInfo): URIO[R, BucketInfo]
  }
}
