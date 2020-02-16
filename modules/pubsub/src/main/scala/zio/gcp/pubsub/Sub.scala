package zio.gcp.pubsub

trait Sub[T] {
  def projectId: ProjectId
  def subscriptionId: SubscriptionId
  def topicId: TopicId
}
