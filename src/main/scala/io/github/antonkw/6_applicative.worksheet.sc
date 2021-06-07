import cats._
import cats.implicits._

Applicative[Option].pure(1) === 1.some
Applicative[Vector].pure(1) === Vector(1)
"a"