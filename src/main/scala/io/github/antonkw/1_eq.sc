import cats._, cats.syntax.all._

println(s"1.some =!= 2.some is [${1.some =!= 2.some}]")
println(s"1.0 max 2.0 is [${1.0 max 2.0}]")

case class IdCard(firstName: String, secondName: String)
object IdCard {
  given Eq[IdCard] = Eq.fromUniversalEquals
}

//  manual implementation of Eq be like:
//  given Eq[IdCard] with {
//    def eqv(a: IdCard, b: IdCard): Boolean = true
//  }

val anton = IdCard(firstName = "Anton", secondName = "Kovalevsky")
val john = IdCard(firstName = "John", secondName = "Doe")

println(s"anton === john is [${anton === john}]")