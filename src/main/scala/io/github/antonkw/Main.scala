package io.github.antonkw


import cats.implicits._

import scala.collection.immutable.HashSet

object Main extends App {

  import cats._, cats.syntax.all._

  println(s"1.some =!= 2.some is [${1.some =!= 2.some}]")
  println(s"1.0 max 2.0 is [${1.0 max 2.0}]")
  
  val anton = IdCard(firstName = "Anton", secondName = "Kovalevsky")
  val john = IdCard(firstName = "John", secondName = "Doe")


  anton === john
  println(s"anton === john is [${anton === john}]")


  def greatest[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Option[A] =
    set.find(elem => set.forall(partialOrdering.tryCompare(_, elem).exists(_ <= 0)))

  def maximals[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Set[A] =
    set.filter(elem => set.forall(partialOrdering.tryCompare(_, elem).map(_ <= 0).getOrElse(true)))

  def least[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Option[A] =
    set.find(elem => set.forall(partialOrdering.tryCompare(_, elem).exists(_ >= 0)))

  def minimals[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Set[A] =
    set.filter(elem => set.forall(partialOrdering.tryCompare(_, elem).map(_ >= 0).getOrElse(true)))


  case class PosetDescription[A](greatest: Option[A], least: Option[A], minimals: Set[A], maximals: Set[A])

  object PosetDescription {
    def make[A](poset: Set[A])(using partialOrdering: PartialOrder[A]): PosetDescription[A] = PosetDescription(
      greatest = poset.find(el => poset.forall(_.tryCompare(el).exists(_ <= 0))),
      least  = poset.find(el => poset.forall(_.tryCompare(el).exists(_ >= 0))),
      maximals = poset.filter(el => poset.forall(_.tryCompare(el).map(_ <= 0).getOrElse(true))),
      minimals = poset.filter(el => poset.forall(_.tryCompare(el).map(_ >= 0).getOrElse(true))),
    )
  }


  val gr = greatest(Set(3, 2, 1))
  import cats.data._


  val persons = NonEmptySet.of(john, anton)
  println(persons)
//  println(PosetDescription.make(Set(3, 2, 1)))

}
