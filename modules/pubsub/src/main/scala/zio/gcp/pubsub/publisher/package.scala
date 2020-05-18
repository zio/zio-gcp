package zio.gcp.pubsub

import zio.{ Has, RIO }
import java.util.concurrent.TimeUnit
import com.google.pubsub.v1.TopicName
import com.google.pubsub.v1.PubsubMessage
import com.google.api.gax.batching.BatchingSettings

package object publisher {

  type Publisher = Has[Publisher.Service]

  def awaitTermination(duration: Long, unit: TimeUnit): RIO[Publisher, Boolean] =
    RIO.accessM(_.get.awaitTermination(duration, unit))

  def getBatchSettings: RIO[Publisher, BatchingSettings] =
    RIO.accessM(_.get.getBatchSettings)

  def getTopicName: RIO[Publisher, TopicName] = RIO.accessM(_.get.getTopicName)

  def publish(message: PubsubMessage): RIO[Publisher, String] = RIO.accessM(_.get.publish(message))

  def publishAllOutstanding: RIO[Publisher, Unit] = RIO.accessM(_.get.publishAllOutstanding)

  def resumePublish(key: String): RIO[Publisher, Unit] = RIO.accessM(_.get.resumePublish(key))

}
