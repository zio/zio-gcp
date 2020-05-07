package zio.gcp.redis

import com.google.cloud.redis.v1.GetInstanceRequest
import zio._
import com.google.cloud.redis.v1.CloudRedisSettings
import com.google.cloud.redis.v1.Instance
import com.google.cloud.redis.v1.CloudRedisClient

object Redis {
  trait Service {
    def getInstance(request: GetInstanceRequest): Task[Instance]
    def getSettings: Task[CloudRedisSettings]
    def isShutDown: Task[Boolean]
    def isTerminated: Task[Boolean]
  }

  def live: Layer[Throwable, Redis] =
    ZLayer.fromManaged {

      val acquire = IO.effect(CloudRedisClient.create())
      val release = (cloudRedisClient: CloudRedisClient) => IO.effect(cloudRedisClient.close()).orDie

      Managed.make(acquire)(release).map { cloudRedisClient =>
        new Service {
          def getInstance(request: GetInstanceRequest): Task[Instance] = Task(cloudRedisClient.getInstance(request))

          def getSettings: Task[CloudRedisSettings] = Task(cloudRedisClient.getSettings())

          def isShutDown: Task[Boolean] = Task(cloudRedisClient.isShutdown())

          def isTerminated: Task[Boolean] = Task(cloudRedisClient.isTerminated())

        }
      }
    }

}
