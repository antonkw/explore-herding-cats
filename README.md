# herding-herding-cats

## day 1

### Eq
[eed3si9n.com/herding-cats/Eq](https://eed3si9n.com/herding-cats/Eq.html)

Nothing special here, just noticed that anonymous `given` works well. With Scala 3 we can finally omit names like:
```scala
given Eq[IdCard] with {
  def eqv(a: IdCard, b: IdCard): Boolean = ???
}
```
No need to write `given idCardEq: Eq[IdCard]` with clear understanding that idCardEq won't be used.

Finally, it's always quite tempting to put Eq into companion object:
```scala
case class IdCard(firstName: String, secondName: String)

object IdCard:
  given Eq[IdCard] = Eq.fromUniversalEquals
```
[Eq example](src/main/scala/io/github/antonkw/1_eq.sc)

### PartialOrder
```scala
trait PartialOrder[A] extends Eq[A]
```

Eugene didn't pay much attention to that type class on the page [PartialOrder.html](https://eed3si9n.com/herding-cats/PartialOrder.html)

[Pos.html](https://eed3si9n.com/herding-cats/Pos.html) page about partially ordered sets doesn't bring much information too.

I recommend taking a look at least at wiki page: [wiki/Partially_ordered_set](https://en.wikipedia.org/wiki/Partially_ordered_set)

PartialOrder is all about notion of partial ordered sets.

* **Greatest** element and **least** element: An element g in P is a greatest element if for every element a in P, a ≤ g. An element m in P is a least element if for every element a in P, a ≥ m. A poset can only have one greatest or least element.
* **Maximal** elements and **minimal** elements: An element g in P is a maximal element if there is no element a in P such that a > g. Similarly, an element m in P is a minimal element if there is no element a in P such that a < m. If a poset has a greatest element, it must be the unique maximal element, but otherwise there can be more than one maximal element, and similarly for least elements and minimal elements.

That's quite interesting how different ideas of greatest and maximal elements.

Naive implementation are:
```scala
def greatest[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Option[A] = 
  set.find(elem => set.forall(partialOrdering.tryCompare(_, elem).exists(_ <= 0)))

def maximals[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Set[A] =
  set.filter(elem => set.forall(partialOrdering.tryCompare(_, elem).map(_ <= 0).getOrElse(true)))

def least[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Option[A] =
  set.find(elem => set.forall(partialOrdering.tryCompare(_, elem).exists(_ >= 0)))

def minimals[A](set: Set[A])(using partialOrdering: PartialOrder[A]): Set[A] =
  set.filter(elem => set.forall(partialOrdering.tryCompare(_, elem).map(_ <= 0).getOrElse(true)))
```

I also combined values to case class.
```scala
case class PosetDescription[A](greatest: Option[A], least: Option[A], minimals: Set[A], maximals: Set[A])
```

[PartialOrder example](src/main/scala/io/github/antonkw/2_partial.sc)

It seemed that it is easy to abstract out sets itself to write something like:
```scala
def make[F[_]: Foldable, A](poset: F[A])(using partialOrdering: PartialOrder[A]) = PosetDescription(
  greatest = poset.find(el => poset.forall(_.tryCompare(el).exists(_ <= 0)))
...
```
In fact, we're interested in the properties of set itself instead of just provided API.

There is also interesting idea of bounds.
For a subset *A* of *P*, an element *x* in *P* is an upper bound of *A* if *a* ≤ *x*, for each element *a* in *A*. In particular, ***x* need not be in *A*** to be an upper bound of *A*. Similarly, an element *x* in *P* is a lower bound of *A* if *a* ≥ *x*, for each element *a* in *A*. A greatest element of *P* is an upper bound of *P* itself, and *a* least element is a lower bound of *P*.
Nevertheless, writing code to decouple strongly connected components is out of scope. Let's move forward.

### Order

```scala
trait Order[A] extends PartialOrder[A]
```

Unlike `PartialOrder` we can meet `Order[A]` everywhere.
We can't write just:
```scala
val persons = NonEmptySet.of(john, anton)
```

```scala
no implicit argument of type cats.kernel.Order[io.github.antonkw.IdCard] was found for parameter A of method of in object NonEmptySetImpl
```

Signature is following: `def of[A](a: A, as: A*)(implicit A: Order[A]): NonEmptySet[A]`

Hence, we need to implement order:
```scala
given Order[IdCard] = Order.from((id1, id2) => {
  val initial = id1.firstName compare id2.firstName
  if (initial == 0) id1.secondName compare id2.secondName
  else initial
})
```

[Order example](src/main/scala/io/github/antonkw/3_order.sc)

## day 2 - Functor

Functor extends Invariant. 
```scala
trait Functor[F[_]] extends Invariant[F]
```

That's a little bit tricky once invariant functor is typically explained as mix of usual (covariant) and contravariant functors.
Those functors are something like:
```scala
trait CovariantFunctor[A]:
  def map[B](f: A => B): CovariantFunctor[B]


trait ContravariantFunctor[A]:
  def contramap[B](f: B => A): ContravariantFunctor[B]
```

With HKT precise definitions are going to be:
```scala
trait CovariantFunctor[F[_]]:
  def map[A, B](fa: F[A])(f: A => B): F[B]


trait ContravariantFunctor[F[_]]:
  def contramap[A, B](fa: F[A])(f: B => A): F[B]
```

Let's return back to the notion of Invariant.
[Cats Invariant doc](https://typelevel.org/cats/typeclasses/invariant.html) and [Softwaremill Invarian note](https://blog.softwaremill.com/scala-cats-invariant-functor-be57d2e2fa91) provide nice examples.
Nevertheless, I want to attempt to bring something extremely down-to-earth.

Let we have some thin wrapper:
```scala
trait EqWrapper[T]:
  def eqv(valueToCompare: T): Boolean
  def get: T
```

String implementation:
```scala
class StringEqWrapper(private val value: String) extends EqWrapper[String] {
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
}
```

We can assume that in real world we define some non-trivial behaviour. It would be nice to have opportunity to derive new instances from old one using old ones as "back end".

Let's attempt to do it with usual `map`:
```scala
class StringEqWrapper(private val value: String) extends EqWrapper[String] { self =>
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
  def map[T](f: String => T): EqWrapper[T] = new EqWrapper[T] {
    override def eqv(valueToCompare: T): Boolean = self.eqv("dummy") // I need T => String here to convert value to familiar strings
    override def get: T = f(self.get) //map of basic covariant functor works well
  }
}
```
At that point we see that `String => T` helped to implement `get`. We just apply function to underlying string value and return result.

But we can't compare `T` with `String`.

Contravariant approach leads to opposite result.
```scala
class StringEqWrapper2(private val value: String) extends EqWrapper[String] { self =>
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
  def map[T](f: T => String): EqWrapper[T] = new EqWrapper[T] {
    override def eqv(valueToCompare: T): Boolean = self.eqv(f(valueToCompare)) // I know how to convert that T value to well-known String
    override def get: T = null.asInstanceOf[T] //I'm in trouble, I have String state but no idea how to return T value
  }
}
```
We can derive implement `eqv(valueToCompare: T)` to compare `T` with internal `String` state.

But `get` require something to convert internal `String` state to `T`.

```scala
class StringEqWrapper(private val value: String) extends EqWrapper[String] { self =>
  def eqv(valueToCompare: String): Boolean = valueToCompare == value
  def get: String = value
  def imap[T](f: String => T, g: T => String): EqWrapper[T] = new EqWrapper[T] {
    override def eqv(value: T): Boolean = self.eqv(g(value))
    override def get: T = f(self.get)
  }
}
```
Now we can derive new instance with `imap`:
```scala
val stringEqWrapper = new StringEqWrapper3("42")
val intEqWrapper: EqWrapper[Int] = stringEqWrapper.imap(_.toInt, _.toString)

intEqWrapper eqv 42 //true
```

[Typelevel Functor docs](https://typelevel.org/cats/api/cats/Functor.html) provides good description of API.

Explicitly I can denote that it is a right time to pay attention to [type lambdas](http://dotty.epfl.ch/docs/reference/new-types/type-lambdas-spec.html), there is also nice [rockthejvm post](https://blog.rockthejvm.com/scala-3-type-lambdas/) about it.
```scala
import cats.Functor
val listFuntor: Functor[List] = Functor[List]
listFuntor.as(List(1, 2, 3), "a") //List(a, a, a)

val listOfOptionFunctor: Functor[[α] =>> List[Option[α]]] = listFuntor.compose[Option] //Functor[λ[α => F[G[α]]]] in Scala2
listOfOptionFunctor.map(List(Some(1), None))("N" + _) //val res0: List[Option[String]] = List(Some(N1), None)
```
[Functor examples](src/main/scala/io/github/antonkw/4_functor.sc)