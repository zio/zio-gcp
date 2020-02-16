package zio.gcp.pubsub

trait Pub[T] {
  def projectId: ProjectId
  def topicId: TopicId
}
