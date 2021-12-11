package lectures.part1as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition : Boolean = false

  val aConditionVal = if (aCondition) 42 else 65

  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  val theUnit = println("Hello Scala")

  def aFunction(x: Int) : Int =  x + 1

  @tailrec def factorial(n: Int, accumulator: Int): Int  = {
    if (n <=0) accumulator
    else factorial(n-1,n * accumulator)
  }

  class Animal
  class Dog extends Animal

  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal) : Unit
  }

  class Crocodrile extends Animal with Carnivore {

    override def eat(a: Animal): Unit = println("Delicious!")

  }

  val aCroc = new Crocodrile

  aCroc.eat(aDog)

  aCroc eat aDog

  1.+(2)

  1 + 2

  val aCarnivor = new Carnivore {
    override def eat(a: Animal): Unit = println("roar")
  }

  abstract class MyList[+A]

  object MyList

  case class Person(name: String, age: Int)

  val throwsException : Nothing = throw new RuntimeException

  val aPotentialFailure : String = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught an exception"
  } finally {
    println("some logs")
  }


  val incrementer = new Function1[Int,Int] {
    override def apply(v1: Int): Int = v1 + 1
  }


  incrementer(1)

  val anonIncrementer = (x: Int) => x + 1


  List(1,2,3).map(anonIncrementer) //HOF

  val pairs =  for {
    num <- List(1,2,3)
    char <- List('a','b','c')
  } yield  num + '-' +char


  val aMap =  Map (
    "Dog" -> 789,
    "Cat" -> 555
  )

  val anOption = Some(2)

  val x = 2

  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob",22)

  val greeting = bob match {
    case Person(n, _) => s"Hello $n"
  }
























}
