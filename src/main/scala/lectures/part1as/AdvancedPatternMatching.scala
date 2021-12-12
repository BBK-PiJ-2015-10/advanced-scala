package lectures.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)

  val description = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ =>
  }

  /*
  - constants
  - wildcards
  - case classes
  - tuples
  - special magic like above
  - special cases 1% like below
   */

  class Person(val name: String, val age: Int)

  //object Person {
    //def unapply(person: Person): Option[(String,Int)] = Some((person.name,person.age))
  //}

  object Person {

    def unapply(person: Person): Option[(String,Int)] = {
      if (person.age < 21) None
      else Some((person.name,person.age))
    }

    def unapply(age: Int) : Option[String] = {
      Some(if (age < 21) "minor" else "major")
    }

  }

  val bob = new Person("Bob",25)

  val greeting = bob match {
    case Person(n,s) => s"My name is $n and I am $s years old"
  }

  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }

  println(legalStatus)

  /*
  Exercise
   */

  /*
  object even {
    def unapply(arg: Int) : Option[Boolean] =
      if (arg % 2 == 0) Some(true)
      else None
  }

  object singleDigit {
    def unapply(arg: Int) : Option[Boolean] =
      if (arg > -10 && arg < 10) Some(true)
      else None
  }

   */

  object even {
    def unapply(arg: Int) : Boolean = {
      if (arg % 2 ==0) true
      else false
    }
  }

  object singleDigit {
    def unapply(arg: Int) : Boolean = {
      if (arg > -10 && arg < 10) true
      else false
    }

  }


  val n : Int  = 40
  
  val mathProperty = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }


  println(mathProperty)

  /*
  val mathProperty = n match {
    case singleDigit(_) => "single digit"
    case even(_) => "an event number"
    case _ => "no property"
  }

   */



  /*
  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "no property"
  }
   */


}
