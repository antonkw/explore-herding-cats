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

object IdCard
  given Eq[IdCard] = Eq.fromUniversalEquals
```
[Eq spreadsheet](src/main/scala/io/github/antonkw/1_eq.sc)

### PartialOrder
```scala
def greatest[A](set: Set[A])(using partialOrdering: PartialOrdering[A]): Option[A] = 
  set.find(elem => set.forall(partialOrdering.tryCompare(_, elem).exists(_ <= 0)))

def maximals[A](set: Set[A])(using partialOrdering: PartialOrdering[A]): Set[A] =
  set.filter(elem => set.forall(partialOrdering.tryCompare(_, elem).map(_ <= 0).getOrElse(true)))

def least[A](set: Set[A])(using partialOrdering: PartialOrdering[A]): Option[A] =
  set.find(elem => set.forall(partialOrdering.tryCompare(_, elem).exists(_ >= 0)))

def minimals[A](set: Set[A])(using partialOrdering: PartialOrdering[A]): Set[A] =
  set.filter(elem => set.forall(partialOrdering.tryCompare(_, elem).map(_ >= 0).getOrElse(true)))
```

```scala
def make[F[_]: Foldable, A](poset: F[A])(using partialOrdering: PartialOrder[A]) = PosetDescription(
  greatest = poset.find(el => poset.forall(_.tryCompare(el).exists(_ <= 0)))
...
```