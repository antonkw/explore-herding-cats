
type UnaryLambda = Any => Any
type BinaryLambda = Any => Any => Any
val I: UnaryLambda = a => a
val K: BinaryLambda = a => _ => a
val KI: BinaryLambda = _ => a => a
val CA = (f:BinaryLambda) => (a: Any) => (b: Any) => f(b)(a)

