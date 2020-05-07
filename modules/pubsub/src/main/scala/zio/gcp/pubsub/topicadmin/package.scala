package zio.gcp.pubsub

import zio.Has

package object topicadmin {

  type TopicAdmin = Has[TopicAdmin.Service]

}
