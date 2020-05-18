package zio.gcp.pubsub.topicadmin

import java.util.concurrent.TimeUnit
import zio._
import com.google.cloud.pubsub.v1.TopicAdminClient
import com.google.cloud.pubsub.v1.stub.PublisherStub
import com.google.cloud.pubsub.v1.TopicAdminSettings
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
import com.google.iam.v1.GetIamPolicyRequest
import com.google.pubsub.v1.Topic

object TopicAdmin {

  trait Service {
    def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean]
    def createTopic(name: String): Task[Topic]
    def createTopic(request: Topic): Task[Topic]
    def deleteTopic(request: DeleteTopicRequest): Task[Unit]
    def deleteTopic(topic: String): Task[Unit]
    def getIamPolicy(request: com.google.iam.v1.GetIamPolicyRequest): Task[com.google.iam.v1.Policy]
    def getSettings: Task[TopicAdminSettings]
    def getStub: Task[PublisherStub]
    def getTopic(request: GetTopicRequest): Task[Topic]
    def getTopic(topic: String): Task[Topic]
    def isShutdown: Task[Boolean]
    def listTopics(request: ListTopicsRequest): Task[TopicAdminClient.ListTopicsPagedResponse]
    def listTopics(project: ProjectName): Task[TopicAdminClient.ListTopicsPagedResponse]
    def listTopics(project: String): Task[TopicAdminClient.ListTopicsPagedResponse]
    def listTopicSubscriptions(
      request: ListTopicSubscriptionsRequest
    ): Task[TopicAdminClient.ListTopicSubscriptionsPagedResponse]
    def listTopicSubscriptions(topic: String): Task[TopicAdminClient.ListTopicSubscriptionsPagedResponse]
    def setIamPolicy(request: SetIamPolicyRequest): Task[Policy]
    def shutdown: Task[Unit]
    def shutdownNow: Task[Unit]
    def testIamPermissions(
      request: TestIamPermissionsRequest
    ): Task[TestIamPermissionsResponse]
    def updateTopic(request: UpdateTopicRequest): Task[Topic]
  }

  def live(topicAdminSettings: TopicAdminSettings): Layer[Throwable, TopicAdmin] =
    ZLayer.fromManaged {
      val acquire = IO.effect(TopicAdminClient.create(topicAdminSettings))
      val release = (topicAdminClient: TopicAdminClient) => IO.effect(topicAdminClient.close()).orDie

      Managed.make(acquire)(release).map { topicAdminClient =>
        new Service {
          def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean] =
            Task(topicAdminClient.awaitTermination(duration, unit))

          def createTopic(name: String): Task[Topic] = Task(topicAdminClient.createTopic(name))

          def createTopic(request: Topic): Task[Topic] = Task(topicAdminClient.createTopic(request))

          def deleteTopic(request: DeleteTopicRequest): Task[Unit] = Task(topicAdminClient.deleteTopic(request))

          def deleteTopic(topic: String): Task[Unit] = Task(topicAdminClient.deleteTopic(topic))

          def getIamPolicy(request: GetIamPolicyRequest): Task[Policy] = Task(topicAdminClient.getIamPolicy(request))

          def getSettings: Task[TopicAdminSettings] = Task(topicAdminClient.getSettings())

          def getStub: Task[PublisherStub] = Task(topicAdminClient.getStub())

          def getTopic(request: GetTopicRequest): Task[Topic] = Task(topicAdminClient.getTopic(request))

          def getTopic(topic: String): Task[Topic] = Task(topicAdminClient.getTopic(topic))

          def isShutdown: Task[Boolean] = Task(topicAdminClient.isShutdown())

          def listTopics(request: ListTopicsRequest): Task[TopicAdminClient.ListTopicsPagedResponse] =
            Task(topicAdminClient.listTopics(request))

          def listTopics(project: ProjectName): Task[TopicAdminClient.ListTopicsPagedResponse] =
            Task(topicAdminClient.listTopics(project))

          def listTopics(project: String): Task[TopicAdminClient.ListTopicsPagedResponse] =
            Task(topicAdminClient.listTopics(project))

          def listTopicSubscriptions(
            request: ListTopicSubscriptionsRequest
          ): Task[TopicAdminClient.ListTopicSubscriptionsPagedResponse] =
            Task(topicAdminClient.listTopicSubscriptions(request))

          def listTopicSubscriptions(topic: String): Task[TopicAdminClient.ListTopicSubscriptionsPagedResponse] =
            Task(topicAdminClient.listTopicSubscriptions(topic))

          def setIamPolicy(request: SetIamPolicyRequest): Task[Policy] = Task(topicAdminClient.setIamPolicy(request))

          def shutdown: Task[Unit] = Task(topicAdminClient.shutdown())

          def shutdownNow: Task[Unit] = Task(topicAdminClient.shutdownNow())

          def testIamPermissions(request: TestIamPermissionsRequest): Task[TestIamPermissionsResponse] =
            Task(topicAdminClient.testIamPermissions(request))

          def updateTopic(request: UpdateTopicRequest): Task[Topic] = Task(topicAdminClient.updateTopic(request))

        }
      }

    }

}
