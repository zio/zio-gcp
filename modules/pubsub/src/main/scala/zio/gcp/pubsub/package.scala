package zio.gcp

package object pubsub {
  final case class ProjectId(value: String)      extends AnyVal
  final case class SubscriptionId(value: String) extends AnyVal
  final case class TopicId(value: String)        extends AnyVal

  trait Sub[T] {
    def projectId: ProjectId
    def subscriptionId: SubscriptionId
    def topicId: TopicId
  }

  trait Pub[T] {
    def projectId: ProjectId
    def topicId: TopicId
  }
}
