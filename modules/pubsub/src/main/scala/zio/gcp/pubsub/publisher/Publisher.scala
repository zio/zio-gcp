package zio.gcp.pubsub.publisher

import java.util.concurrent.TimeUnit
import zio.Task
import com.google.pubsub.v1.TopicName
import com.google.pubsub.v1.PubsubMessage
import com.google.cloud.pubsub.v1.{ Publisher => Publisher_ }
import zio._
import com.google.api.gax.batching.BatchingSettings
import zio.interop.guava._
import com.google.api.core.ApiFutureToListenableFuture

object Publisher {

  trait Service {
    def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean]
    def getBatchSettings(): Task[com.google.api.gax.batching.BatchingSettings]
    def getTopicName(): Task[TopicName]
    def publish(message: PubsubMessage): Task[String]
    def publishAllOutstanding: Task[Unit]
    def resumePublish(key: String): Task[Unit]
  }

  def live(topicId: String): Layer[Throwable, Publisher] =
    ZLayer.fromManaged {
      val acquire = IO.effect(Publisher_.newBuilder(topicId).build())
      val release = (publisher: Publisher_) => IO.effect(publisher.shutdown).orDie

      Managed.make(acquire)(release).map { publisher =>
        new Service {

          def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean] =
            Task(publisher.awaitTermination(duration, unit))

          def getBatchSettings: Task[BatchingSettings] = Task(publisher.getBatchingSettings)

          def getTopicName: Task[TopicName] = Task(publisher.getTopicName())

          def publish(message: PubsubMessage): Task[String] =
            fromListenableFuture(
              UIO(
                new ApiFutureToListenableFuture[String](
                  publisher.publish(message)
                )
              )
            )

          def publishAllOutstanding: Task[Unit] = Task(publisher.publishAllOutstanding())

          def resumePublish(key: String): Task[Unit] = Task(publisher.resumePublish(key))

        }
      }
    }
}
