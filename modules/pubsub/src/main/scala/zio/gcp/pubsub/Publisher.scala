package zio.gcp.pubsub

import zio.RIO
import com.google.cloud.pubsub.v1.{Publisher => GPublisher}
import zio.Task
import com.google.pubsub.v1.PubsubMessage
import zio.URIO
import zio.duration.Duration
import com.google.api.gax.batching.BatchingSettings
import com.google.pubsub.v1.TopicName
import com.google.api.gax.rpc.TransportChannelProvider
import com.google.api.gax.core.CredentialsProvider
import com.google.api.gax.core.ExecutorProvider
import com.google.api.gax.rpc.HeaderProvider
import com.google.api.gax.retrying.RetrySettings

trait Publisher {
  val publisher: Publisher.Service[Any, Any]
}

object Publisher {
  trait Service[R, T] {
    def awaitTermination(duration: Duration): Task[Boolean]
    def getApiMaxRequestBytes(): URIO[R, Long]
    def getApiMaxRequestElementCount(): URIO[R, Long]
    def getBatchingSettings: URIO[R, BatchingSettings]
    def getTopicName: URIO[R, TopicName]
    def getTopicNameString: URIO[R, TopicId]
    def makePublisher(topicName: TopicName): RIO[R, GPublisher]
    def makePublisher(topicId: TopicId): RIO[R, GPublisher]
    def publish(pubSubMessage: PubsubMessage): Task[String]
    def publishAllOutstanding(): URIO[R, Unit]
    def resumePublish(key: String): URIO[R, Unit]
    def shutdown(): URIO[R, Unit]
    def setBatchSettings(
      builder: GPublisher.Builder,
      batchingSettings: BatchingSettings
    ): URIO[R, GPublisher.Builder]
    def setChannelProvider(
      builder: GPublisher.Builder,
      channelProvider: TransportChannelProvider
    ): URIO[R, GPublisher.Builder]
    def setCredentialsProvider(
      builder: GPublisher.Builder,
      credentialsProviders: CredentialsProvider
    ): URIO[R, GPublisher.Builder]
    def setEnableMessageOrdering(
      builder: GPublisher.Builder,
      enableMessageOrdering: Boolean
    ): URIO[R, GPublisher.Builder]
    def setEndPoint(
      builder: GPublisher.Builder,
      endPoint: String
    ): URIO[R, GPublisher.Builder]
    def setExecutorProvider(
      builder: GPublisher.Builder,
      executorProvider: ExecutorProvider
    ): URIO[R, GPublisher.Builder]
    def setHeaderProvider(
      builder: GPublisher.Builder,
      headerProvider: HeaderProvider
    ): URIO[R, GPublisher.Builder]
    def setRetrySettings(
      builder: GPublisher.Builder,
      retrySettings: RetrySettings
    ): URIO[R, GPublisher.Builder]
  }
}
