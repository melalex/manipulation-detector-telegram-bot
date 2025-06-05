package com.melalex.detector.error

case class AppException(error: AppError, cause: Throwable = None.orNull) extends RuntimeException(error.message, cause)

case class AppError(
    code: String,
    message: String
)
