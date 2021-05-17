import cats._
import cats.syntax.all._
import cats.data._
import io.github.antonkw.IdCard

//given Order[IdCard] = Order.from((id1, id2) => {
//  val initial = id1.firstName compare id2.firstName
//  if (initial == 0) id1.secondName compare id2.secondName
//  else initial
//})

val anton = IdCard(firstName = "Anton", secondName = "Kovalevsky")
val john = IdCard(firstName = "John", secondName = "Doe")

val persons = NonEmptySet.of(john, anton)
println(s"Persons: ${persons.map(_.firstName).mkString_(", ")}")


