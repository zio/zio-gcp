package zio.gcp.pubsub.publisher

import java.util.concurrent.TimeUnit
import zio.Task
import com.google.pubsub.v1.TopicName
import com.google.cloud.pubsub.v1.Publisher.Builder
import com.google.pubsub.v1.PubsubMessage

object Publisher {

  trait Service {
    def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean]
    def getApiMaxRequestBytes: Task[Long]
    def getApiMaxRequestElementCount(): Task[Long]
    def getBatchSettings(): Task[com.google.api.gax.batching.BatchingSettings]
    def getTopicName(): Task[TopicName]
    def getTopicNameAsString(): Task[String]
    def newBuilder(topicName: String): Task[Builder]
    def newBuilder(topicName: TopicName): Task[Builder]
    def publish(message: PubsubMessage): Task[String]
    def publishAllOutstanding: Task[Unit]
    def resumePublish(key: String): Task[Unit]
    def shutdown: Task[Unit]
  }
}
