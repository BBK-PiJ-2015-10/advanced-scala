package lectures.part2afp

object PartialFunctions extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int,Int] === Int => Int

  class FunctionNotApplicationException extends RuntimeException

  val aFuzzyFunction = (x: Int) =>
    if (x == 1) 42
    else if (x == 2) 56
    else if (x == 5) 999
    else throw new FunctionNotApplicationException

  val aNicerFuzzyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 56
    case 5 => 999
  }
  // {1,2,5} => Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
    case 5 => 990
  } //partial function value

  println(aPartialFunction(2))

  //println(aPartialFunction(256))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  //lift
  val lifted = aPartialFunction.lift  // Int => Option[Int]

  println(lifted(2))
  println(lifted(98))

  val pfChain = aPartialFunction.orElse[Int,Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  //partial functions extend normal functions

  val aTotalFunction : Int => Int = {
    case 1 =>99
  }

  //HOF accept partialFunctions as well
  val aMappedList =  List(1,2,3).map {
    case 1 => 42
    case 2 => 78
    case 3 => 1000
  }

  println(aMappedList)

  /*
  Note: PartialFunction can only have only ONE parameter type
   */

  /*
    - construct a PF instance myself
    - dumb chatbot as PF
   */

  val aManualFussyFunction = new PartialFunction[Int,Int] {

    override def isDefinedAt(x: Int): Boolean = x == 1 || x ==2 || x ==5

    override def apply(v1: Int): Int = v1 match {
      case 1 => 42
      case 2 => 65
      case 5 => 999
    }

  }

  val chatbot : PartialFunction[String,String] = {
    case "hello" => "Hi, my name is HAL9000"
    case "goodbye" => "You chatty dog"
    case "call mom" => "stop crying"
  }


  //scala.io.Source.stdin.getLines().foreach(line => println("chatbot said: "+chatbot(line)))

  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)

  
  //left on minute 8:20 out of 22:54


}
