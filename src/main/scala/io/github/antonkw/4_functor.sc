trait EqWrapper[T]:
  def eqv(valueToCompare: T): Boolean
  def get: T

class StringEqWrapper(private val value: String) extends EqWrapper[String] { self =>
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
}

println(s"new StringEqWrapper(\"42\").eqv(\"42\") = ${new StringEqWrapper("42").eqv("42")}")

class StringEqWrapper1(private val value: String) extends EqWrapper[String] { self =>
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
  def map[T](f: String => T): EqWrapper[T] = new EqWrapper[T] {
    override def eqv(valueToCompare: T): Boolean = self.eqv("dummy") // I need T => String here to convert value to familiar strings
    override def get: T = f(self.get) //map of basic covariant functor works well
  }
}

class StringEqWrapper2(private val value: String) extends EqWrapper[String] { self =>
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
  def map[T](f: T => String): EqWrapper[T] = new EqWrapper[T] {
    override def eqv(valueToCompare: T): Boolean = self.eqv(f(valueToCompare)) // I know how to convert that T value to well-known String
    override def get: T = null.asInstanceOf[T] //I'm in trouble, I have String state but no idea how to return T value
  }
}

class StringEqWrapper3(private val value: String) extends EqWrapper[String] { self =>
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
  def imap[T](f: String => T, g: T => String): EqWrapper[T] = new EqWrapper[T] {
    override def eqv(value: T): Boolean = self.eqv(g(value))
    override def get: T = f(self.get)
  }
}

val stringEqWrapper = new StringEqWrapper3("42")
val intEqWrapper: EqWrapper[Int] = stringEqWrapper.imap(_.toInt, _.toString)

println(s"intEqWrapper eqv 42: ${intEqWrapper eqv 42}")

import cats.Functor
val listFuntor: Functor[List] = Functor[List]
listFuntor.as(List(1, 2, 3), "a") //List(a, a, a)

val listOfOptionFunctor: Functor[[α] =>> List[Option[α]]] = listFuntor.compose[Option] //Functor[λ[α => F[G[α]]]] in Scala2
listOfOptionFunctor.map(List(Some(1), None))("N" + _) //val res0: List[Option[String]] = List(Some(N1), None)

listFuntor.fproduct(List('a', 'b', 'c'))(_.toInt) //val res2: List[(Char, Int)] = List((a,97), (b,98), (c,99))