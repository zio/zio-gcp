package zio.gcp.pubsub

import zio.{ Has, RIO }
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings
import java.util.concurrent.TimeUnit
import com.google.pubsub.v1.CreateSnapshotRequest
import com.google.pubsub.v1.Snapshot
import com.google.pubsub.v1.ProjectSnapshotName
import com.google.pubsub.v1.ProjectSubscriptionName
import com.google.pubsub.v1.TopicName
import com.google.pubsub.v1.PushConfig
import com.google.pubsub.v1.Subscription
import com.google.pubsub.v1.DeleteSnapshotRequest
import com.google.pubsub.v1.DeleteSubscriptionRequest
import com.google.cloud.pubsub.v1.stub.SubscriberStub
import com.google.pubsub.v1.GetSubscriptionRequest
import com.google.pubsub.v1.ListSnapshotsRequest
import com.google.cloud.pubsub.v1.SubscriptionAdminClient
import com.google.pubsub.v1.ProjectName
import com.google.pubsub.v1.ListSubscriptionsRequest
import com.google.pubsub.v1.ModifyPushConfigRequest
import com.google.pubsub.v1.SeekRequest
import com.google.pubsub.v1.UpdateSnapshotRequest
import com.google.pubsub.v1.SeekResponse
import com.google.pubsub.v1.UpdateSubscriptionRequest

package object subscriptionadmin {

  type SubscriptionAdmin = Has[SubscriptionAdmin.Service]

  def awaitTermination(duration: Long, unit: TimeUnit): RIO[SubscriptionAdmin, Boolean] =
    RIO.accessM(_.get.awaitTermination(duration, unit))

  def createSnapshot(request: CreateSnapshotRequest): RIO[SubscriptionAdmin, Snapshot] =
    RIO.accessM(_.get.createSnapshot(request))

  def createSnapshot(
    name: ProjectSnapshotName,
    subscription: ProjectSubscriptionName
  ): RIO[SubscriptionAdmin, Snapshot] = RIO.accessM(_.get.createSnapshot(name, subscription))

  def createSubscription(
    name: ProjectSubscriptionName,
    topic: TopicName,
    pushConfig: PushConfig,
    ackDeadlineSeconds: Int
  ): RIO[SubscriptionAdmin, Subscription] =
    RIO.accessM(_.get.createSubscription(name, topic, pushConfig, ackDeadlineSeconds))

  def createSubscription(request: Subscription): RIO[SubscriptionAdmin, Subscription] =
    RIO.accessM(_.get.createSubscription(request))

  def deleteSnapshot(request: DeleteSnapshotRequest): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.deleteSnapshot(request))

  def deleteSnapshot(snapshot: ProjectSnapshotName): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.deleteSnapshot(snapshot))

  def deleteSnapshot(snapshot: String): RIO[SubscriptionAdmin, Unit] = RIO.accessM(_.get.deleteSnapshot(snapshot))

  def deleteSubscription(request: DeleteSubscriptionRequest): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.deleteSubscription(request))

  def deleteSubscription(subscription: ProjectSubscriptionName): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.deleteSubscription(subscription))

  def deleteSubscription(subscription: String): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.deleteSubscription(subscription))

  def getIamPolicy(request: com.google.iam.v1.GetIamPolicyRequest): RIO[SubscriptionAdmin, com.google.iam.v1.Policy] =
    RIO.accessM(_.get.getIamPolicy(request))

  def getSettings: RIO[SubscriptionAdmin, SubscriptionAdminSettings] = RIO.accessM(_.get.getSettings)

  def getStub: RIO[SubscriptionAdmin, SubscriberStub] = RIO.accessM(_.get.getStub)

  def getSubscription(request: GetSubscriptionRequest): RIO[SubscriptionAdmin, Subscription] =
    RIO.accessM(_.get.getSubscription(request))

  def getSubscription(subscription: ProjectSubscriptionName): RIO[SubscriptionAdmin, Subscription] =
    RIO.accessM(_.get.getSubscription(subscription))

  def getSubscription(subscription: String): RIO[SubscriptionAdmin, Subscription] =
    RIO.accessM(_.get.getSubscription(subscription))

  def isShutdown: RIO[SubscriptionAdmin, Boolean] = RIO.accessM(_.get.isShutdown)

  def isTerminated: RIO[SubscriptionAdmin, Boolean] = RIO.accessM(_.get.isTerminated)

  def listSnapshots(
    request: ListSnapshotsRequest
  ): RIO[SubscriptionAdmin, SubscriptionAdminClient.ListSnapshotsPagedResponse] =
    RIO.accessM(_.get.listSnapshots(request))

  def listSnapshots(project: ProjectName): RIO[SubscriptionAdmin, SubscriptionAdminClient.ListSnapshotsPagedResponse] =
    RIO.accessM(_.get.listSnapshots(project))

  def listSnapshots(project: String): RIO[SubscriptionAdmin, SubscriptionAdminClient.ListSnapshotsPagedResponse] =
    RIO.accessM(_.get.listSnapshots(project))

  def listSubscriptions(
    request: ListSubscriptionsRequest
  ): RIO[SubscriptionAdmin, SubscriptionAdminClient.ListSubscriptionsPagedResponse] =
    RIO.accessM(_.get.listSubscriptions(request))

  def listSubscriptions(
    project: ProjectName
  ): RIO[SubscriptionAdmin, SubscriptionAdminClient.ListSubscriptionsPagedResponse] =
    RIO.accessM(_.get.listSubscriptions(project))

  def listSubscriptions(
    project: String
  ): RIO[SubscriptionAdmin, SubscriptionAdminClient.ListSubscriptionsPagedResponse] =
    RIO.accessM(_.get.listSubscriptions(project))

  def modifyPushConfig(request: ModifyPushConfigRequest): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.modifyPushConfig(request))

  def modifyPushConfig(subscription: ProjectSubscriptionName, pushConfig: PushConfig): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.modifyPushConfig(subscription, pushConfig))

  def modifyPushConfig(subscription: String, pushConfig: PushConfig): RIO[SubscriptionAdmin, Unit] =
    RIO.accessM(_.get.modifyPushConfig(subscription, pushConfig))

  def seek(request: SeekRequest): RIO[SubscriptionAdmin, SeekResponse] = RIO.accessM(_.get.seek(request))

  def setIamPolicy(request: com.google.iam.v1.SetIamPolicyRequest): RIO[SubscriptionAdmin, com.google.iam.v1.Policy] =
    RIO.accessM(_.get.setIamPolicy(request))

  def shutdown: RIO[SubscriptionAdmin, Unit] = RIO.accessM(_.get.shutdown)

  def shutdownNow: RIO[SubscriptionAdmin, Unit] = RIO.accessM(_.get.shutdownNow)

  def testIamPermissions(
    request: com.google.iam.v1.TestIamPermissionsRequest
  ): RIO[SubscriptionAdmin, com.google.iam.v1.TestIamPermissionsResponse] =
    RIO.accessM(_.get.testIamPermissions(request))

  def updateSnapshot(request: UpdateSnapshotRequest): RIO[SubscriptionAdmin, Snapshot] =
    RIO.accessM(_.get.updateSnapshot(request))

  def updateSubscription(request: UpdateSubscriptionRequest): RIO[SubscriptionAdmin, Subscription] =
    RIO.accessM(_.get.updateSubscription(request))

}
