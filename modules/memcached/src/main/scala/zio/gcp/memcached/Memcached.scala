package zio.gcp.memcached

import com.google.api.gax.paging.Page
import com.google.cloud.memcache.v1beta2._
import com.google.cloud.memcache.v1beta2.stub.CloudMemcacheStub
import com.google.protobuf.{ Empty, FieldMask }
import zio._

object Memcached {
  trait Service {
    def applyParameters(request: ApplyParametersRequest): Task[Instance]
    def applyParameters(name: String, nodeIds: java.util.List[String], applyAll: Boolean): Task[Instance]
    def applyParameters(name: InstanceName, nodeIds: java.util.List[String], applyAll: Boolean): Task[Instance]
    def createInstance(request: CreateInstanceRequest): Task[Instance]
    def createInstance(parent: String, instanceId: String, resource: Instance): Task[Instance]
    def createInstance(parent: LocationName, instanceId: String, resource: Instance): Task[Instance]
    def deleteInstance(request: DeleteInstanceRequest): Task[Empty]
    def deleteInstance(name: String): Task[Empty]
    def getInstance(request: GetInstanceRequest): Task[Instance]
    def getInstance(name: String): Task[Instance]
    def getInstance(name: InstanceName): Task[Instance]
    def getSettings: Task[CloudMemcacheSettings]
    def getStub: Task[CloudMemcacheStub]
    def isShutDown: Task[Boolean]
    def isTerminated: Task[Boolean]
    def listInstances(request: ListInstancesRequest): Task[Page[Instance]]
    def listInstances(string: String): Task[Page[Instance]]
    def listInstances(parent: LocationName): Task[Page[Instance]]
    def shutDown: Task[Unit]
    def shutDownNow: Task[Unit]
    def updateInstance(request: UpdateInstanceRequest): Task[Instance]
    def updateInstance(updateMask: FieldMask, resource: Instance): Task[Instance]
    def updateParameters(request: UpdateParametersRequest): Task[Instance]
    def updateParameters(name: String, updateMask: FieldMask, parameters: MemcacheParameters): Task[Instance]
    def updateParameters(name: InstanceName, updateMask: FieldMask, parameters: MemcacheParameters): Task[Instance]
  }

  def live: Layer[Throwable, Memcached] =
    ZLayer.fromManaged {

      val acquire = IO.effect(CloudMemcacheClient.create())
      val release = (cloudMemcacheClient: CloudMemcacheClient) => IO.effect(cloudMemcacheClient.close()).orDie

      Managed.make(acquire)(release).map { cloudMemcacheClient =>
        new Service {
          override def applyParameters(request: ApplyParametersRequest): Task[Instance] =
            Task(cloudMemcacheClient.applyParametersAsync(request).get())

          override def applyParameters(name: String, nodeIds: java.util.List[String], applyAll: Boolean)
            : Task[Instance] =
            Task(cloudMemcacheClient.applyParametersAsync(name, nodeIds, applyAll).get())

          override def applyParameters(name: InstanceName, nodeIds: java.util.List[String], applyAll: Boolean)
            : Task[Instance] =
            Task(cloudMemcacheClient.applyParametersAsync(name, nodeIds, applyAll).get())

          override def createInstance(request: CreateInstanceRequest): Task[Instance] =
            Task(cloudMemcacheClient.createInstanceAsync(request).get())

          override def createInstance(parent: String, instanceId: String, resource: Instance): Task[Instance] =
            Task(cloudMemcacheClient.createInstanceAsync(parent, instanceId, resource).get())

          override def createInstance(parent: LocationName, instanceId: String, resource: Instance): Task[Instance] =
            Task(cloudMemcacheClient.createInstanceAsync(parent, instanceId, resource).get())

          override def deleteInstance(request: DeleteInstanceRequest): Task[Empty] =
            Task(cloudMemcacheClient.deleteInstanceAsync(request).get())

          override def deleteInstance(name: String): Task[Empty] =
            Task(cloudMemcacheClient.deleteInstanceAsync(name).get())

          override def getInstance(request: GetInstanceRequest): Task[Instance] =
            Task(cloudMemcacheClient.getInstance(request))

          override def getInstance(name: String): Task[Instance] = Task(cloudMemcacheClient.getInstance(name))

          override def getInstance(name: InstanceName): Task[Instance] = Task(cloudMemcacheClient.getInstance(name))

          override def getSettings: Task[CloudMemcacheSettings] = Task(cloudMemcacheClient.getSettings)

          override def getStub: Task[CloudMemcacheStub] =
            Task(cloudMemcacheClient.getStub)

          override def isShutDown: Task[Boolean] = Task(cloudMemcacheClient.isShutdown)

          override def isTerminated: Task[Boolean] = Task(cloudMemcacheClient.isTerminated)

          override def listInstances(request: ListInstancesRequest): Task[Page[Instance]] =
            Task(cloudMemcacheClient.listInstances(request).getPage)

          override def listInstances(string: String): Task[Page[Instance]] =
            Task(cloudMemcacheClient.listInstances(string).getPage)

          override def listInstances(parent: LocationName): Task[Page[Instance]] =
            Task(cloudMemcacheClient.listInstances(parent).getPage)

          override def shutDown: Task[Unit] = Task(cloudMemcacheClient.shutdown())

          override def shutDownNow: Task[Unit] = Task(cloudMemcacheClient.shutdownNow())

          override def updateInstance(request: UpdateInstanceRequest): Task[Instance] =
            Task(cloudMemcacheClient.updateInstanceAsync(request).get())

          override def updateInstance(updateMask: FieldMask, resource: Instance): Task[Instance] =
            Task(cloudMemcacheClient.updateInstanceAsync(updateMask, resource).get())

          override def updateParameters(request: UpdateParametersRequest): Task[Instance] =
            Task(cloudMemcacheClient.updateParametersAsync(request).get())

          override def updateParameters(name: String, updateMask: FieldMask, parameters: MemcacheParameters)
            : Task[Instance] =
            Task(cloudMemcacheClient.updateParametersAsync(name, updateMask, parameters).get())

          override def updateParameters(name: InstanceName, updateMask: FieldMask, parameters: MemcacheParameters)
            : Task[Instance] =
            Task(cloudMemcacheClient.updateParametersAsync(name, updateMask, parameters).get())
        }
      }
    }
}
