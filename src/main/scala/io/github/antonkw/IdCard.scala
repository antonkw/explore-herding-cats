package io.github.antonkw

import cats.Eq
import cats.Order

case class IdCard(firstName: String, secondName: String)

object IdCard:
  given Eq[IdCard] = Eq.fromUniversalEquals
  given Order[IdCard] = Order.from((id1, id2) => {
    val initial = id1.firstName compare id2.firstName
    if (initial == 0) id1.secondName compare id2.secondName
    else initial
  })

