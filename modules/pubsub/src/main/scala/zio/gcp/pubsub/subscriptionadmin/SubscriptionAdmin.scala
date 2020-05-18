package zio.gcp.pubsub.subscriptionadmin

import zio._
import java.util.concurrent.TimeUnit
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.stub.SubscriberStub
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings
import com.google.pubsub.v1.CreateSnapshotRequest
import com.google.pubsub.v1.Snapshot
import com.google.pubsub.v1.ProjectSnapshotName
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.Subscription
import com.google.pubsub.v1.DeleteSnapshotRequest
import com.google.pubsub.v1.DeleteSubscriptionRequest
import com.google.pubsub.v1.GetSubscriptionRequest
import com.google.pubsub.v1.ListSnapshotsRequest
import com.google.pubsub.v1.ProjectName
import com.google.pubsub.v1.ListSubscriptionsRequest
import com.google.pubsub.v1.ModifyPushConfigRequest
import com.google.pubsub.v1.SeekResponse
import com.google.pubsub.v1.SeekRequest
import com.google.pubsub.v1.UpdateSnapshotRequest
import com.google.pubsub.v1.UpdateSubscriptionRequest
import com.google.iam.v1.{ Policy, SetIamPolicyRequest }
import com.google.iam.v1.{ TestIamPermissionsRequest, TestIamPermissionsResponse }
import com.google.iam.v1.{ GetIamPolicyRequest, Policy }
import com.google.iam.v1.Policy
import com.google.iam.v1.TestIamPermissionsResponse
import com.google.pubsub.v1.TopicName
import com.google.pubsub.v1.PushConfig

object SubscriptionAdmin {

  trait Service {
    def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean]
    def createSnapshot(request: CreateSnapshotRequest): Task[Snapshot]
    def createSnapshot(name: ProjectSnapshotName, subscription: ProjectSubscriptionName): Task[Snapshot]
    def createSubscription(
      name: ProjectSubscriptionName,
      topic: TopicName,
      pushConfig: PushConfig,
      ackDeadlineSeconds: Int
    ): Task[Subscription]
    def createSubscription(request: Subscription): Task[Subscription]
    def deleteSnapshot(request: DeleteSnapshotRequest): Task[Unit]
    def deleteSnapshot(snapshot: ProjectSnapshotName): Task[Unit]
    def deleteSnapshot(snapshot: String): Task[Unit]
    def deleteSubscription(request: DeleteSubscriptionRequest): Task[Unit]
    def deleteSubscription(subscription: ProjectSubscriptionName): Task[Unit]
    def deleteSubscription(subscription: String): Task[Unit]
    def getIamPolicy(request: com.google.iam.v1.GetIamPolicyRequest): Task[com.google.iam.v1.Policy]
    def getSettings: Task[SubscriptionAdminSettings]
    def getStub: Task[SubscriberStub]
    def getSubscription(request: GetSubscriptionRequest): Task[Subscription]
    def getSubscription(subscription: ProjectSubscriptionName): Task[Subscription]
    def getSubscription(subscription: String): Task[Subscription]
    def isShutdown: Task[Boolean]
    def isTerminated: Task[Boolean]
    def listSnapshots(request: ListSnapshotsRequest): Task[SubscriptionAdminClient.ListSnapshotsPagedResponse]
    def listSnapshots(project: ProjectName): Task[SubscriptionAdminClient.ListSnapshotsPagedResponse]
    def listSnapshots(project: String): Task[SubscriptionAdminClient.ListSnapshotsPagedResponse]
    def listSubscriptions(
      request: ListSubscriptionsRequest
    ): Task[SubscriptionAdminClient.ListSubscriptionsPagedResponse]
    def listSubscriptions(project: ProjectName): Task[SubscriptionAdminClient.ListSubscriptionsPagedResponse]
    def listSubscriptions(project: String): Task[SubscriptionAdminClient.ListSubscriptionsPagedResponse]
    def modifyPushConfig(request: ModifyPushConfigRequest): Task[Unit]
    def modifyPushConfig(subscription: ProjectSubscriptionName, pushConfig: PushConfig): Task[Unit]
    def modifyPushConfig(subscription: String, pushConfig: PushConfig): Task[Unit]
    def seek(request: SeekRequest): Task[SeekResponse]
    def setIamPolicy(request: com.google.iam.v1.SetIamPolicyRequest): Task[com.google.iam.v1.Policy]
    def shutdown: Task[Unit]
    def shutdownNow: Task[Unit]
    def testIamPermissions(
      request: com.google.iam.v1.TestIamPermissionsRequest
    ): Task[com.google.iam.v1.TestIamPermissionsResponse]
    def updateSnapshot(request: UpdateSnapshotRequest): Task[Snapshot]
    def updateSubscription(request: UpdateSubscriptionRequest): Task[Subscription]
  }

  def live(settings: SubscriptionAdminSettings): Layer[Throwable, SubscriptionAdmin] =
    ZLayer.fromManaged {

      val acquire = IO.effect(SubscriptionAdminClient.create(settings))
      val release =
        (subscriptionAdminClient: SubscriptionAdminClient) => IO.effect(subscriptionAdminClient.close()).orDie

      Managed.make(acquire)(release).map { subscriptionAdminClient =>
        new Service {
          def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean] =
            Task(subscriptionAdminClient.awaitTermination(duration, unit))

          def createSnapshot(request: CreateSnapshotRequest): Task[Snapshot] =
            Task(subscriptionAdminClient.createSnapshot(request))

          def createSnapshot(name: ProjectSnapshotName, subscription: ProjectSubscriptionName): Task[Snapshot] =
            Task(subscriptionAdminClient.createSnapshot(name, subscription))

          def createSubscription(
            name: ProjectSubscriptionName,
            topic: TopicName,
            pushConfig: PushConfig,
            ackDeadlineSeconds: Int
          ): Task[Subscription] =
            Task(subscriptionAdminClient.createSubscription(name, topic, pushConfig, ackDeadlineSeconds))

          def createSubscription(request: Subscription): Task[Subscription] =
            Task(subscriptionAdminClient.createSubscription(request))

          def deleteSnapshot(request: DeleteSnapshotRequest): Task[Unit] =
            Task(subscriptionAdminClient.deleteSnapshot(request))

          def deleteSnapshot(snapshot: ProjectSnapshotName): Task[Unit] =
            Task(subscriptionAdminClient.deleteSnapshot(snapshot))

          def deleteSnapshot(snapshot: String): Task[Unit] = Task(subscriptionAdminClient.deleteSnapshot(snapshot))

          def deleteSubscription(request: DeleteSubscriptionRequest): Task[Unit] =
            Task(subscriptionAdminClient.deleteSubscription(request))

          def deleteSubscription(subscription: ProjectSubscriptionName): Task[Unit] =
            Task(subscriptionAdminClient.deleteSubscription(subscription))

          def deleteSubscription(subscription: String): Task[Unit] =
            Task(subscriptionAdminClient.deleteSubscription(subscription))

          def getIamPolicy(request: GetIamPolicyRequest): Task[Policy] =
            Task(subscriptionAdminClient.getIamPolicy(request))

          def getSettings: Task[SubscriptionAdminSettings] = Task(subscriptionAdminClient.getSettings())

          def getStub: Task[SubscriberStub] = Task(subscriptionAdminClient.getStub())

          def getSubscription(request: GetSubscriptionRequest): Task[Subscription] =
            Task(subscriptionAdminClient.getSubscription(request))

          def getSubscription(subscription: ProjectSubscriptionName): Task[Subscription] =
            Task(subscriptionAdminClient.getSubscription(subscription))

          def getSubscription(subscription: String): Task[Subscription] =
            Task(subscriptionAdminClient.getSubscription(subscription))

          def isShutdown: Task[Boolean] = Task(subscriptionAdminClient.isShutdown())

          def isTerminated: Task[Boolean] = Task(subscriptionAdminClient.isTerminated())

          def listSnapshots(request: ListSnapshotsRequest): Task[SubscriptionAdminClient.ListSnapshotsPagedResponse] =
            Task(subscriptionAdminClient.listSnapshots(request))

          def listSnapshots(project: ProjectName): Task[SubscriptionAdminClient.ListSnapshotsPagedResponse] =
            Task(subscriptionAdminClient.listSnapshots(project))

          def listSnapshots(project: String): Task[SubscriptionAdminClient.ListSnapshotsPagedResponse] =
            Task(subscriptionAdminClient.listSnapshots(project))

          def listSubscriptions(
            request: ListSubscriptionsRequest
          ): Task[SubscriptionAdminClient.ListSubscriptionsPagedResponse] =
            Task(subscriptionAdminClient.listSubscriptions(request))

          def listSubscriptions(project: ProjectName): Task[SubscriptionAdminClient.ListSubscriptionsPagedResponse] =
            Task(subscriptionAdminClient.listSubscriptions(project))

          def listSubscriptions(project: String): Task[SubscriptionAdminClient.ListSubscriptionsPagedResponse] =
            Task(subscriptionAdminClient.listSubscriptions(project))

          def modifyPushConfig(request: ModifyPushConfigRequest): Task[Unit] =
            Task(subscriptionAdminClient.modifyPushConfig(request))

          def modifyPushConfig(subscription: ProjectSubscriptionName, pushConfig: PushConfig): Task[Unit] =
            Task(subscriptionAdminClient.modifyPushConfig(subscription, pushConfig))

          def modifyPushConfig(subscription: String, pushConfig: PushConfig): Task[Unit] =
            Task(subscriptionAdminClient.modifyPushConfig(subscription, pushConfig))

          def seek(request: SeekRequest): Task[SeekResponse] = Task(subscriptionAdminClient.seek(request))

          def setIamPolicy(request: SetIamPolicyRequest): Task[Policy] =
            Task(subscriptionAdminClient.setIamPolicy(request))

          def shutdown: Task[Unit] = Task(subscriptionAdminClient.shutdown())

          def shutdownNow: Task[Unit] = Task(subscriptionAdminClient.shutdownNow())

          def testIamPermissions(request: TestIamPermissionsRequest): Task[TestIamPermissionsResponse] =
            Task(subscriptionAdminClient.testIamPermissions(request))

          def updateSnapshot(request: UpdateSnapshotRequest): Task[Snapshot] =
            Task(subscriptionAdminClient.updateSnapshot(request))

          def updateSubscription(request: UpdateSubscriptionRequest): Task[Subscription] =
            Task(subscriptionAdminClient.updateSubscription(request))

        }
      }

    }

}
