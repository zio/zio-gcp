package zio.gcp

import zio.{ Has, RIO }
import com.google.cloud.redis.v1.GetInstanceRequest
import com.google.cloud.redis.v1.CloudRedisSettings
import com.google.cloud.redis.v1.Instance

package object redis {

  type Redis = Has[Redis.Service]

  def getInstance(request: GetInstanceRequest): RIO[Redis, Instance] = RIO.accessM(_.get.getInstance(request))
  def getSettings: RIO[Redis, CloudRedisSettings]                    = RIO.accessM(_.get.getSettings)
  def isShutDown: RIO[Redis, Boolean]                                = RIO.accessM(_.get.isShutDown)
  def isTerminated: RIO[Redis, Boolean]                              = RIO.accessM(_.get.isTerminated)

}
