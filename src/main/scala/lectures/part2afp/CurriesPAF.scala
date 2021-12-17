package lectures.part2afp

object CurriesPAF extends App {

  //curried functions
  val superAdder  : Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) //: Int => Int = y => 3 + y

  //println(add3(5))

  //println(superAdder(3)(5))

  // METHOD!
  def curriedAdder(x: Int)( y:Int) = x + y

  val add4 : Int => Int = curriedAdder(4)

  // lifting = ETA-Expansion
  def inc(x: Int) = x + 1

  List(1,2,3).map(inc)

  // Partial function applications
  val add5 = curriedAdder(5) _  // Do ETA expansion

  //EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x +y

  def simpleAddMethod(x: Int, y: Int) = x + y;

  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y

  val add7Saf = simpleAddFunction(7,_)

  println(add7Saf(2))

  val add7SafC = simpleAddFunction.curried(7)

  println(add7SafC(2))

  val add7SaM = simpleAddMethod(7, _)

  println(add7SaM(2))

  val add7Cum = curriedAddMethod(7) _  // PAF

  println(add7Cum(2))


  def concatenator(a: String, b: String, c: String) = a + b + c

  val insertName = concatenator("Hello, I'm ",_,"how are you")

  println(insertName("Doggy"))

  val fillInTheBlanks = concatenator("Hello ", _, _)

  println(fillInTheBlanks("Dog ", " Alexis"))

  //EXERCISES
  /*
  - Process a list of number
  - Return String representations with diff formats
  %4.2f, %8.6f

   */

  //Exercise 1

  val list = List(1,2,3,Math.E,Math.PI)

  val formatter = (x: List[Double], format: Double => String) => x.map(format)

  val formatter2 : (List[Double],Double => String) => List[String] =
    (x: List[Double], format: Double => String) => x.map(format)

  val f1 : Double => String = "%4.2f".format(_);

  val f2 : Double => String = "%8.6f".format(_);

  val f3 : Double => String = "%14.12f".format(_);

  val f1f = formatter(_,f1)

  val f1fa = f1f(list).foreach(println)

  val f2f = formatter(_,f2)

  val f2fa = f2f(list).foreach(println)

  val f3f = formatter(_,f3)

  val f3fa = f3f(list).foreach(println)


  def byName(n: => Int) : Int = n + 1

  def byFunction(f: () => Int) : Int = f() +1

  def method: Int = 42

  def parenMethod() : Int = 42

  val bn1 = byName(2)
  val bn2 = byName(method)
  val bn3 = byName(parenMethod())

  //val bn4 = byName(() => 2)
  // below works since I am calling the lambda at the end
  val bn42 = byName((() => 2)())

  //val bn5 = byName(parenMethod _)

  //val bf1 = byFunction(2)
  //val bf2 = byFunction(method)
  //val bf3 = byFunction(parenMethod())
  val bf32 = byFunction(parenMethod) // compiler does ETA
  val bf4 = byFunction(() => 2)
  val bf5 = byFunction(parenMethod _)

  //left on 5:24 of 34 minutes
  
}
