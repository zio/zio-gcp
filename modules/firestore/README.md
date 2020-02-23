# zio-gcp-firestore

A ZIO-based interface to Cloud Firestore. Cloud Firestore is a fully-managed NoSQL document database for mobile, web, and server development.

## Quickstart

Add the following dependencies to your `build.sbt` file:
```scala
libraryDependencies ++= Seq(
  "dev.zio" %% "zio-gcp-firestore" % "<version>"
)
```

Use the provided `FirestoreDB` wrapper:
```scala
import zio.gcp.firestore._

val makeDocument = def create(
    documentPath: DocumentPath,
    userData: UserData
  ): ZIO[FirestoreDB, Exception, WriteResult] = {
    for {
      collectionReference <- collection(CollectionPath("collection-name"))
      writeResult         <- createDocument(collectionReference, documentPath, userData)
    } yield (writeResult)
  }

val result: Task[WriteResult] =
  makeDocument.provideManaged(FirestoreDB.Live.open())
```

## Getting help

Join us on the [ZIO Discord server](https://discord.gg/2ccFBr4).

## Legal

Copyright 2020 Mikail Khan and the zio-gcp contributors. All rights reserved.