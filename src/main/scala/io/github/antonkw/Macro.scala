package io.github.antonkw
import scala.quoted.*
object Macro {
  import scala.quoted.*

  inline def hello(): Unit = println("Hello, world!")

  // --

  inline def debugSingle(inline expr: Any): Unit = ${debugSingleImpl('expr)}

  private def debugSingleImpl(expr: Expr[Any])(using Quotes): Expr[Unit] =
    '{ println("Value of [" + ${Expr(expr.show)} + "] is " + $expr) }
}
