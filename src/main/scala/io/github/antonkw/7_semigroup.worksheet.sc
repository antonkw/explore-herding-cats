
import cats._
import cats.implicits._

Semigroup[String].combineAllOption(List()) === none
Semigroup[String].combineAllOption(List("hello", "world")) === "helloworld".some

Semigroup[Int].combineN(2, 8) === 16

Semigroup[String].intercalate("|").combine("hello", "world") == "hello|world"
Semigroup[String].reverse.combine("hello", "world") === "worldhello"

Semigroup.maybeCombine("hello", none) === "hello"
Semigroup.maybeCombine(none, "world") === "world"
Semigroup.maybeCombine("hello".some, "world") === "helloworld"