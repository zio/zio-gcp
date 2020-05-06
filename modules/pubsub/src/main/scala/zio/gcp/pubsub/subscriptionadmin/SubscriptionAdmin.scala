package zio.gcp.pubsub.subscriptionadmin

import zio.Task
import java.util.concurrent.TimeUnit
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.cloud.pubsub.v1.stub.SubscriberStub
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings
import com.google.pubsub.v1.CreateSnapshotRequest
import com.google.pubsub.v1.Snapshot
import com.google.pubsub.v1.ProjectSnapshotName
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.PushConfig
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

object SubscriptionAdmin {

  trait Service {
    def awaitTermination(duration: Long, unit: TimeUnit): Task[Boolean]
    def close(): Task[Unit]
    def create(): Task[SubscriptionAdminClient]
    def create(stub: SubscriberStub): Task[SubscriptionAdminClient]
    def create(settings: SubscriptionAdminSettings): Task[SubscriptionAdminClient]
    def createSnapshot(request: CreateSnapshotRequest): Task[Snapshot]
    def createSnapshot(name: ProjectSnapshotName, subscription: ProjectSubscriptionName): Task[Snapshot]
    def createSubscription(
      name: ProjectSubscriptionName,
      topic: ProjectTopicName,
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
    def getIamPolicy(resource: String): Task[com.google.iam.v1.Policy]
    def getSettings(): Task[SubscriptionAdminSettings]
    def getStub(): Task[SubscriberStub]
    def getSubscription(request: GetSubscriptionRequest): Task[Subscription]
    def getSubscription(subscription: ProjectSubscriptionName): Task[Subscription]
    def getSubscription(subscription: String): Task[Subscription]
    def isShutdown(): Task[Boolean]
    def isTerminated(): Task[Boolean]
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
    def setIamPolicy(resource: String, policy: com.google.iam.v1.Policy): Task[com.google.iam.v1.Policy]
    def shutdown(): Task[Unit]
    def shutdownNow(): Task[Unit]
    def testIamPermissions(
      resource: String,
      permissions: List[String]
    ): Task[com.google.iam.v1.TestIamPermissionsResponse]
    def testIamPermissions(
      request: com.google.iam.v1.TestIamPermissionsRequest
    ): Task[com.google.iam.v1.TestIamPermissionsResponse]
    def updateSnapshot(request: UpdateSnapshotRequest): Task[Snapshot]
    def updateSubscription(request: UpdateSubscriptionRequest): Task[Subscription]
  }

}
