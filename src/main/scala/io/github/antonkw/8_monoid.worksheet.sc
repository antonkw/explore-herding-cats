
import cats._
import cats.implicits._

Monoid.isEmpty(0)
Monoid.combineAll(List[Int]()) === 0

List(2, 10).foldLeft(Monoid[Int].empty)(Monoid[Int].combine) === 12