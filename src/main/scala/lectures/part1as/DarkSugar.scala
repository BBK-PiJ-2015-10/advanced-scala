package lectures.part1as

import scala.util.Try

object DarkSugar extends App{

  def singleArgMethod(arg: Int) : String = s"$arg little ducks..."

  val description = singleArgMethod {
    42
  }

  val aTryInstance = Try {
    throw new RuntimeException
  }


  List(1,2,3).map { x =>
    x +1
  }

  trait Action {
    def act(x: Int): Int
  }

  val aFunkyInstance : Action = (x: Int) => x + 1

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala")
  })

  val aSweeterThread = new Thread(() => println("sweet, Scala"))

  abstract class AnAbstractType {

    def implemented: Int = 23

    def f(a: Int) : Unit

  }

  val anAbstractInstance : AnAbstractType = (a: Int) => println("sweet")

  val prependedList = 2 :: List(3,4)

  1 :: 2 :: 3 :: List(4,5)

  List(4,5).::(3).::(2).::(1)

  class MyStream[T] {
    def -->: (value: T): MyStream[T] = this
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]


  class TeenGirls(name: String) {
    def `and then said` (gossip: String) = println(s" $name said $gossip")
  }

  val lilly = new TeenGirls("Lilly")

  lilly `and then said` "Scala is so sweet!"

  class Composite[A,B]

  val composite : Composite[Int,String] = ???

  val myComposite: Int Composite String = ???

  class -->[A,B]

  val towards : Int --> String = ???

  val anArray = Array(1,2,3)

  anArray(2) = 7 //rewritten to anArray.updated(2,7)
  // used in mutable collections
  //remember apply() AND updated()!


  class Mutable {
    private var internalMember : Int = 0
    def member = internalMember
    def member_= (value: Int) : Unit = internalMember = value
  }

  val aMutableContainer = new Mutable

  aMutableContainer.member = 42




















}
