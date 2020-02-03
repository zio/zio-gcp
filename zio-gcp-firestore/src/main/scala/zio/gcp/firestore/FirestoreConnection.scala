package zio.gcp.firestore

import com.google.cloud.firestore.{Firestore, FirestoreOptions}

trait FirestoreConnection {
  def db: Firestore = FirestoreOptions.getDefaultInstance.toBuilder.build.getService
}

object FirestoreConnection extends FirestoreConnection
