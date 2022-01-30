package lectures.part4implicits

object OrganizingImplicits extends App {

  implicit val reserveOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)

  //implicit val normalOrdrding : Ordering[Int] = Ordering.fromLessThan(_ < _)

  println(List(1,4,5,3,2).sorted)

  // scala.Predef
  /*
  Implicit params
   - val/var
   - objet
   - accessor methos = defs no params
   */

  case class Person(name: String, age: Int)

  object Person {
  }

  object AlphabeticNameOrdering {
    implicit val alphabeticOrdering : Ordering[Person] = Ordering.fromLessThan( _.name < _.name)
  }

  object AgeOrdering {
    implicit val ageOrdering : Ordering[Person] = Ordering.fromLessThan((a,b) => a.age < b.age)
  }

  import AlphabeticNameOrdering.alphabeticOrdering

  val persons = List(
    Person("Steve",30),
    Person("Any",22),
    Person("John",66)
  )

  println(persons.sorted)

  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val byTotalPrice : Ordering[Purchase] = Ordering.fromLessThan((a,b) => a.nUnits * a.unitPrice < b.nUnits * b.unitPrice)
  }

  object PurchaseByCount {
    implicit val byCount : Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object PurchaseByPrice {
    implicit val byPrice : Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice )
  }

  val purchases =  List(
    Purchase(5,25),
    Purchase(3,99),
    Purchase(1,10)
  )

  println(purchases.sorted)

  /*
  Implicit scope:
  - normal scope = LOCAL SCOPE
  - imported scope
  - companions of all types involved in the method signature
   */


}
