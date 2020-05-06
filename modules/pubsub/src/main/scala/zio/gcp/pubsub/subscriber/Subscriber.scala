package zio.gcp.pubsub.subscriber

import zio.Task
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.api.core.ApiService

object Subscriber {
  trait Service {
    def doStart: Task[Unit]
    def doStop: Task[Unit]
    def getFlowControlSettings: Task[com.google.api.gax.batching.FlowControlSettings]
    def getSubscriptionNameString: Task[String]
    def newBuilder(
      subscription: ProjectSubscriptionName,
      receiver: MessageReceiver
    ): Task[com.google.cloud.pubsub.v1.Subscriber.Builder]
    def startAsync: Task[ApiService]
  }
}
