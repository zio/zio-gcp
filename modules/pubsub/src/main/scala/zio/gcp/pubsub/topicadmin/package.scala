package zio.gcp.pubsub

import zio.{ Has, RIO }
import java.util.concurrent.TimeUnit
import com.google.pubsub.v1.UpdateTopicRequest
import com.google.pubsub.v1.Topic
import com.google.pubsub.v1.DeleteTopicRequest
import com.google.cloud.pubsub.v1.TopicAdminSettings
import com.google.cloud.pubsub.v1.stub.PublisherStub
import com.google.pubsub.v1.GetTopicRequest
import com.google.pubsub.v1.ListTopicsRequest
import com.google.pubsub.v1.ProjectName
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.pubsub.v1.ListTopicSubscriptionsRequest
import com.google.iam.v1.SetIamPolicyRequest
import com.google.iam.v1.Policy
import com.google.iam.v1.TestIamPermissionsRequest
import com.google.iam.v1.TestIamPermissionsResponse

package object topicadmin {

  type TopicAdmin = Has[TopicAdmin.Service]

  def awaitTermination(duration: Long, unit: TimeUnit): RIO[TopicAdmin, Boolean] =
    RIO.accessM(_.get.awaitTermination(duration, unit))

  def createTopic(name: String): RIO[TopicAdmin, Topic] = RIO.accessM(_.get.createTopic(name))

  def createTopic(request: Topic): RIO[TopicAdmin, Topic] = RIO.accessM(_.get.createTopic(request))

  def deleteTopic(request: DeleteTopicRequest): RIO[TopicAdmin, Unit] = RIO.accessM(_.get.deleteTopic(request))

  def deleteTopic(topic: String): RIO[TopicAdmin, Unit] = RIO.accessM(_.get.deleteTopic(topic))

  def getIamPolicy(request: com.google.iam.v1.GetIamPolicyRequest): RIO[TopicAdmin, com.google.iam.v1.Policy] =
    RIO.accessM(_.get.getIamPolicy(request))

  def getSettings: RIO[TopicAdmin, TopicAdminSettings] = RIO.accessM(_.get.getSettings)

  def getStub: RIO[TopicAdmin, PublisherStub] = RIO.accessM(_.get.getStub)

  def getTopic(request: GetTopicRequest): RIO[TopicAdmin, Topic] = RIO.accessM(_.get.getTopic(request))

  def getTopic(topic: String): RIO[TopicAdmin, Topic] = RIO.accessM(_.get.getTopic(topic))

  def isShutdown: RIO[TopicAdmin, Boolean] = RIO.accessM(_.get.isShutdown)

  def listTopics(request: ListTopicsRequest): RIO[TopicAdmin, TopicAdminClient.ListTopicsPagedResponse] =
    RIO.accessM(_.get.listTopics(request))

  def listTopics(project: ProjectName): RIO[TopicAdmin, TopicAdminClient.ListTopicsPagedResponse] =
    RIO.accessM(_.get.listTopics(project))

  def listTopics(project: String): RIO[TopicAdmin, TopicAdminClient.ListTopicsPagedResponse] =
    RIO.accessM(_.get.listTopics(project))

  def listTopicSubscriptions(
    request: ListTopicSubscriptionsRequest
  ): RIO[TopicAdmin, TopicAdminClient.ListTopicSubscriptionsPagedResponse] =
    RIO.accessM(_.get.listTopicSubscriptions(request))

  def listTopicSubscriptions(topic: String): RIO[TopicAdmin, TopicAdminClient.ListTopicSubscriptionsPagedResponse] =
    RIO.accessM(_.get.listTopicSubscriptions(topic))

  def setIamPolicy(request: SetIamPolicyRequest): RIO[TopicAdmin, Policy] = RIO.accessM(_.get.setIamPolicy(request))

  def shutdown: RIO[TopicAdmin, Unit] = RIO.accessM(_.get.shutdown)

  def shutdownNow: RIO[TopicAdmin, Unit] = RIO.accessM(_.get.shutdownNow)

  def testIamPermissions(
    request: TestIamPermissionsRequest
  ): RIO[TopicAdmin, TestIamPermissionsResponse] = RIO.accessM(_.get.testIamPermissions(request))

  def updateTopic(request: UpdateTopicRequest): RIO[TopicAdmin, Topic] = RIO.accessM(_.get.updateTopic(request))

}
