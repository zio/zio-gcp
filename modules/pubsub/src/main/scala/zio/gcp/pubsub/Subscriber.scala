package zio.gcp.pubsub

import zio.URIO
import com.google.api.gax.batching.FlowControlSettings
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.cloud.pubsub.v1.{ Subscriber => GSubscriber }
import zio.Task
import com.google.api.gax.rpc.TransportChannelProvider
import com.google.api.gax.core.CredentialsProvider
import com.google.api.gax.core.ExecutorProvider
import com.google.api.gax.rpc.HeaderProvider
import org.threeten.bp.Duration

trait Subscriber {
  val subscriber: Subscriber.Service[Any, Any]
}

object Subscriber {
  trait Service[R, T] {
    def start(): URIO[R, Unit]
    def stop(): URIO[R, Unit]
    def getControlFlowSettings(): URIO[R, FlowControlSettings]
    def getSubscriptionNameString(): URIO[R, String]
    def makeSubscriber(
      subscription: ProjectSubscriptionName,
      receiver: MessageReceiver
    ): Task[GSubscriber.Builder]
    def makeSubscriber(
      subscription: String,
      receiver: MessageReceiver
    ): Task[GSubscriber.Builder]
    def setChannelProvider(
      builder: GSubscriber.Builder,
      channelProvider: TransportChannelProvider
    ): URIO[R, GSubscriber.Builder]
    def setCredentialsProvider(
      builder: GSubscriber.Builder,
      credentialsProviders: CredentialsProvider
    ): URIO[R, GSubscriber.Builder]
    def setEndPoint(
      builder: GSubscriber.Builder,
      endPoint: String
    ): URIO[R, GSubscriber.Builder]
    def setExecutorProvider(
      builder: GSubscriber.Builder,
      executorProvider: ExecutorProvider
    ): URIO[R, GSubscriber.Builder]
    def setFlowControlSettings(
      builder: GSubscriber.Builder,
      flowControlSettings: FlowControlSettings
    ): URIO[R, GSubscriber.Builder]
    def setHeaderProvider(
      builder: GSubscriber.Builder,
      headerProvider: HeaderProvider
    ): URIO[R, GSubscriber.Builder]
    def setMaxAckExtensionPeriod(
      builder: GSubscriber.Builder,
      maxAckExtensionPeriod: Duration
    ): URIO[R, GSubscriber.Builder]
    def setParallelPullCount(
      builder: GSubscriber.Builder,
      parallelPullCount: Int
    ): URIO[R, GSubscriber.Builder]
    def setSystemExecutorProvider(
      builder: GSubscriber.Builder,
      executorProvider: ExecutorProvider
    ): URIO[R, GSubscriber.Builder]
    def startAsync(): URIO[R, Unit]
  }
}
