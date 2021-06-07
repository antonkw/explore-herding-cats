package io.github.antonkw


import cats.implicits._
import cats._

import scala.collection.immutable.HashSet

object Main extends App {

  inline val pi = 3.141592653589793
  inline val pie = "🥧"

  val pi2 = pi + pi // val pi2 = 6.283185307179586
  val pie2 = pie + pie // val pie2 = "🥧🥧"

  println(pi2)


  import Macro._
  import scala.quoted.*
  def a = "3" + "2"
  val x = 0
  val y = 1
//  val inspected: Any = inspect(a)
  Macro.debugSingle(x + y)
  
  Apply[Option].map2("hello ".some, "world".some)(_ + _) === "hello world".some
  Apply[Option].map2(none[String], "world".some)(_ + _) === none[String]
}
