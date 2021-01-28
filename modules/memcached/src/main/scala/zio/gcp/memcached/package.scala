package zio.gcp

import com.google.api.gax.paging.Page
import com.google.cloud.memcache.v1beta2._
import com.google.cloud.memcache.v1beta2.stub.CloudMemcacheStub
import com.google.protobuf.{ Empty, FieldMask }
import zio.{ Has, RIO }

package object memcached {

  type Memcached = Has[Memcached.Service]

  def applyParameters(request: ApplyParametersRequest): RIO[Memcached, Instance] =
    RIO.accessM(_.get.applyParameters(request))

  def applyParameters(name: String, nodeIds: java.util.List[String], applyAll: Boolean): RIO[Memcached, Instance] =
    RIO.accessM(_.get.applyParameters(name, nodeIds, applyAll))

  def applyParameters(
    name: InstanceName,
    nodeIds: java.util.List[String],
    applyAll: Boolean
  ): RIO[Memcached, Instance] = RIO.accessM(_.get.applyParameters(name, nodeIds, applyAll))

  def createInstance(request: CreateInstanceRequest): RIO[Memcached, Instance] =
    RIO.accessM(_.get.createInstance(request))

  def createInstance(parent: String, instanceId: String, resource: Instance): RIO[Memcached, Instance] =
    RIO.accessM(_.get.createInstance(parent, instanceId, resource))

  def createInstance(parent: LocationName, instanceId: String, resource: Instance): RIO[Memcached, Instance] =
    RIO.accessM(_.get.createInstance(parent, instanceId, resource))

  def deleteInstance(request: DeleteInstanceRequest): RIO[Memcached, Empty] =
    RIO.accessM(_.get.deleteInstance(request))

  def deleteInstance(name: String): RIO[Memcached, Empty] = RIO.accessM(_.get.deleteInstance(name))

  def getInstance(request: GetInstanceRequest): RIO[Memcached, Instance] = RIO.accessM(_.get.getInstance(request))

  def getInstance(name: String): RIO[Memcached, Instance] = RIO.accessM(_.get.getInstance(name))

  def getInstance(name: InstanceName): RIO[Memcached, Instance] = RIO.accessM(_.get.getInstance(name))

  def getSettings: RIO[Memcached, CloudMemcacheSettings] = RIO.accessM(_.get.getSettings)

  def getStub: RIO[Memcached, CloudMemcacheStub] = RIO.accessM(_.get.getStub)

  def isShutDown: RIO[Memcached, Boolean] = RIO.accessM(_.get.isShutDown)

  def isTerminated: RIO[Memcached, Boolean] = RIO.accessM(_.get.isTerminated)

  def listInstances(request: ListInstancesRequest): RIO[Memcached, Page[Instance]] =
    RIO.accessM(_.get.listInstances(request))

  def listInstances(string: String): RIO[Memcached, Page[Instance]] =
    RIO.accessM(_.get.listInstances(string))

  def listInstances(parent: LocationName): RIO[Memcached, Page[Instance]] =
    RIO.accessM(_.get.listInstances(parent))

  def shutDown: RIO[Memcached, Unit] = RIO.accessM(_.get.shutDown)

  def shutDownNow: RIO[Memcached, Unit] = RIO.accessM(_.get.shutDownNow)

  def updateInstance(request: UpdateInstanceRequest): RIO[Memcached, Instance] =
    RIO.accessM(_.get.updateInstance(request))

  def updateInstance(updateMask: FieldMask, resource: Instance): RIO[Memcached, Instance] =
    RIO.accessM(_.get.updateInstance(updateMask, resource))

  def updateParameters(request: UpdateParametersRequest): RIO[Memcached, Instance] =
    RIO.accessM(_.get.updateParameters(request))

  def updateParameters(name: String, updateMask: FieldMask, parameters: MemcacheParameters): RIO[Memcached, Instance] =
    RIO.accessM(_.get.updateParameters(name, updateMask, parameters))

  def updateParameters(
    name: InstanceName,
    updateMask: FieldMask,
    parameters: MemcacheParameters
  ): RIO[Memcached, Instance] =
    RIO.accessM(_.get.updateParameters(name, updateMask, parameters))
}
