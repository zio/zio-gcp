package zio.gcp.firestore

import com.google.api.gax.rpc._
import com.google.cloud.firestore.FirestoreException

sealed trait ZFirestoreException
object ZFirestoreException {
  final case class ZFAlreadyExistsException(cause: Throwable) extends ZFirestoreException
  final case class ZFCancelledException(cause: Throwable) extends ZFirestoreException
  final case class ZFUnknownException(cause: Throwable) extends ZFirestoreException
  final case class ZFInvalidArgumentException(cause: Throwable)
      extends ZFirestoreException
  final case class ZFDeadlineExceededException(cause: Throwable)
      extends ZFirestoreException
  final case class ZFNotFoundException(cause: Throwable) extends ZFirestoreException
  final case class ZFPermissionDeniedException(cause: Throwable)
      extends ZFirestoreException
  final case class ZFResourceExhaustedException(cause: Throwable)
      extends ZFirestoreException
  final case class ZFFailedPreconditionException(cause: Throwable)
      extends ZFirestoreException
  final case class ZFAbortedException(cause: Throwable) extends ZFirestoreException
  final case class ZFOutOfRangeException(cause: Throwable) extends ZFirestoreException
  final case class ZFUnimplementedException(cause: Throwable) extends ZFirestoreException
  final case class ZFInternalException(cause: Throwable) extends ZFirestoreException
  final case class ZFUnavailableException(cause: Throwable) extends ZFirestoreException
  final case class ZFDataLossException(cause: Throwable) extends ZFirestoreException
  final case class ZFUnauthenticatedException(cause: Throwable)
      extends ZFirestoreException
  final case class ZFFirestoreException(cause: Throwable) extends ZFirestoreException

  def handleRpcError(e: Throwable): ZFirestoreException =
    e match {
      case e: FirestoreException          => ZFFirestoreException(e)
      case e: AlreadyExistsException      => ZFAlreadyExistsException(e)
      case e: CancelledException          => ZFCancelledException(e)
      case e: UnknownException            => ZFUnknownException(e)
      case e: InvalidArgumentException    => ZFInvalidArgumentException(e)
      case e: DeadlineExceededException   => ZFDeadlineExceededException(e)
      case e: NotFoundException           => ZFNotFoundException(e)
      case e: PermissionDeniedException   => ZFPermissionDeniedException(e)
      case e: ResourceExhaustedException  => ZFResourceExhaustedException(e)
      case e: FailedPreconditionException => ZFFailedPreconditionException(e)
      case e: AbortedException            => ZFAbortedException(e)
      case e: OutOfRangeException         => ZFOutOfRangeException(e)
      case e: UnimplementedException      => ZFUnimplementedException(e)
      case e: InternalException           => ZFInternalException(e)
      case e: UnavailableException        => ZFUnavailableException(e)
      case e: DataLossException           => ZFDataLossException(e)
      case e: UnauthenticatedException    => ZFUnauthenticatedException(e)
    }
}
