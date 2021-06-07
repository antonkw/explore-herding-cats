import cats.implicits._
import cats._

Apply[Option].map2("hello ".some, "world".some)(_ + _) === "hello world".some
Apply[Option].map2(none[String], "world".some)(_ + _) === none[String]

val composeTwoOptions: (Option[String], Option[Int]) => Option[String] = Apply[Option].ap2(((s: String, i: Int) => s + i).some)
composeTwoOptions.apply("hi".some, 1.some) === "hi1".some
composeTwoOptions.apply("hi".some, none[Int]) === none[String]

"hello".some *> "world".some === "world".some
"hello".some <* "world".some === "hello".some
none[String] *> "world".some === none[String]
none[String] <* "world".some === none[String]