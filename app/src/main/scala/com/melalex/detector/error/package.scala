package com.melalex.detector

package object error {

  type AppEither[T] = Either[AppException, T]
}
