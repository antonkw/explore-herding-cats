import cats._
import cats.syntax.all._

case class PosetDescription[A](greatest: Option[A], least: Option[A], minimals: Set[A], maximals: Set[A])

object PosetDescription {
  def make[A](poset: Set[A])(using partialOrdering: PartialOrder[A]): PosetDescription[A] = PosetDescription(
    greatest = poset.find(el => poset.forall(_.tryCompare(el).exists(_ <= 0))),
    least  = poset.find(el => poset.forall(_.tryCompare(el).exists(_ >= 0))),
    maximals = poset.filter(el => poset.forall(_.tryCompare(el).map(_ >= 0).getOrElse(true))),
    minimals = poset.filter(el => poset.forall(_.tryCompare(el).map(_ <= 0).getOrElse(true))),
  )
}

println(PosetDescription.make(Set(1, 2, 3))) // todo complex example