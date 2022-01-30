package lectures.part4implicits

object PimpMyLibrary extends App{

  //2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven : Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)

    def times(function: () => Unit) : Unit = {
      def timesAux(n: Int): Unit  =
        if (n <= 0) ()
        else {
          function()
          timesAux(n - 1)
        }
     timesAux(value)
    }

    def *[T](list: List[T]) = {
      def concatenate(n: Int): List[T] =
        if (n <=0) List()
        else concatenate(n - 1) ++ list
      concatenate(value)
    }

  }

  implicit class RichString(val value: String) extends AnyVal {
    def asInt : Int = Integer.valueOf(value)
    def encrypt(cypherDistance : Int) : String = value.map(c => (c + cypherDistance).asInstanceOf[Char])
  }


  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }


  new RichInt(42).sqrt

  println(42.isEven)

  //type enrichment = pimping

  1 to 10

  import scala.concurrent.duration._

  3.seconds

  println("3".asInt + 4)
  println("John".encrypt(2))

  3.times(() => println(s"Scala Rocks"))
  println(3 * List(1,2))

  implicit def stringToInt(string: String) : Int = Integer.valueOf(string)

  println("6" / 2)

  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  //dangerous zone

  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionValue = if (3) "OK" else "Something wrong"

  println(aConditionValue)

}
