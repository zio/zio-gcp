package zio.gcp.pubsub

import zio.{ Has, RIO }
import com.google.api.gax.batching.FlowControlSettings
import com.google.api.core.ApiService

package object subscriber {

  type Subscriber = Has[Subscriber.Service]

  def getFlowControlSettings: RIO[Subscriber, FlowControlSettings] = RIO.accessM(_.get.getFlowControlSettings)

  def getSubscriptionNameString: RIO[Subscriber, String] = RIO.accessM(_.get.getSubscriptionNameString)

  def startAsync: RIO[Subscriber, ApiService] = RIO.accessM(_.get.startAsync)
}
