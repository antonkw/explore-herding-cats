import cats._
import cats.implicits._

Applicative[Option].pure(1) === 1.some
Applicative[Vector].pure(1) === Vector(1)

Applicative[Option].replicateA(3, 1.some) === List(1, 1, 1).some

Applicative[Option].unit === ().some

Applicative[List].compose[Vector].compose[Option].pure(3) === List(Vector(3.some))

Applicative[List].whenA(true)(List(1, 2, 3)) === List((), (), ())