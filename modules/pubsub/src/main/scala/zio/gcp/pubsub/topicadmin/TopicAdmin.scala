package zio.gcp.pubsub.topicadmin

import java.util.concurrent.TimeUnit
import zio.Task
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.cloud.pubsub.v1.stub.PublisherStub
import com.google.cloud.pubsub.v1.TopicAdminSettings
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.Topic
import com.google.pubsub.v1.DeleteTopicRequest
import com.google.pubsub.v1.GetTopicRequest
import com.google.pubsub.v1.ListTopicsRequest
import com.google.pubsub.v1.ProjectName
import com.google.pubsub.v1.ListTopicSubscriptionsRequest
import com.google.pubsub.v1.UpdateTopicRequest
import com.google.iam.v1.Policy
import com.google.iam.v1.SetIamPolicyRequest
import com.google.iam.v1.TestIamPermissionsResponse
import com.google.iam.v1.TestIamPermissionsRequest

object TopicAdmin {

  trait Service {
    def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean]
    def close: Task[Unit]
    def create: Task[TopicAdminClient]
    def create(stub: PublisherStub): Task[TopicAdminClient]
    def create(settings: TopicAdminSettings): Task[TopicAdminClient]
    def createTopic(name: ProjectTopicName): Task[Topic]
    def createTopic(name: String): Task[Topic]
    def createTopic(request: Topic): Task[Topic]
    def deleteTopic(request: DeleteTopicRequest): Task[Unit]
    def deleteTopic(topic: ProjectTopicName): Task[Unit]
    def deleteTopic(topic: String): Task[Unit]
    def getIamPolicy(request: com.google.iam.v1.GetIamPolicyRequest): Task[com.google.iam.v1.Policy]
    def getIamPolicy(resource: String): Task[com.google.iam.v1.Policy]
    def getSettings: Task[TopicAdminSettings]
    def getStub: Task[PublisherStub]
    def getTopic(request: GetTopicRequest): Task[Topic]
    def getTopic(topic: ProjectTopicName): Task[Topic]
    def getTopic(topic: String): Task[Topic]
    def isShutdown(): Task[Boolean]
    def listTopics(request: ListTopicsRequest): Task[TopicAdminClient.ListTopicsPagedResponse]
    def listTopics(project: ProjectName): Task[TopicAdminClient.ListTopicsPagedResponse]
    def listTopics(project: String): Task[TopicAdminClient.ListTopicsPagedResponse]
    def listTopicSubscriptions(
      request: ListTopicSubscriptionsRequest
    ): Task[TopicAdminClient.ListTopicSubscriptionsPagedResponse]
    def listTopicSubscriptions(topic: ProjectTopicName): Task[TopicAdminClient.ListTopicSubscriptionsPagedResponse]
    def listTopicSubscriptions(topic: String): Task[TopicAdminClient.ListTopicSubscriptionsPagedResponse]
    def setIamPolicy(request: SetIamPolicyRequest): Task[Policy]
    def setIamPolicy(resource: String, policy: Policy): Task[Policy]
    def shutdown(): Task[Unit]
    def shutdownNow(): Task[Unit]
    def testIamPermissions(
      resource: String,
      permissions: List[String]
    ): Task[TestIamPermissionsResponse]
    def testIamPermissions(
      request: TestIamPermissionsRequest
    ): Task[TestIamPermissionsResponse]
    def updateTopic(request: UpdateTopicRequest): Task[Topic]
  }

}
