package zio.gcp.pubsub.subscriber

import zio._
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.cloud.pubsub.v1.MessageReceiver
import com.google.api.core.ApiService
import com.google.api.gax.batching.FlowControlSettings
import com.google.cloud.pubsub.v1.{ Subscriber => Subscriber_ }

object Subscriber {
  trait Service {
    def getFlowControlSettings: Task[FlowControlSettings]
    def getSubscriptionNameString: Task[String]
    def startAsync: Task[ApiService]
  }

  def live(subscription: ProjectSubscriptionName, receiver: MessageReceiver): Layer[Throwable, Subscriber] =
    ZLayer.fromManaged {
      val acquire = IO.effect(Subscriber_.newBuilder(subscription, receiver).build())
      val release = (subscriber: Subscriber_) => IO.effect(subscriber.stopAsync()).orDie

      Managed.make(acquire)(release).map { subscriber =>
        new Service {
          override def getFlowControlSettings: Task[FlowControlSettings] = Task(subscriber.getFlowControlSettings())

          override def getSubscriptionNameString: Task[String] = Task(subscriber.getSubscriptionNameString())

          override def startAsync: Task[ApiService] = Task(subscriber.startAsync())
        }
      }

    }
}
