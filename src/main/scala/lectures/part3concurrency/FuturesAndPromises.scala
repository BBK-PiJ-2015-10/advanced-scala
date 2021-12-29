package lectures.part3concurrency

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FuturesAndPromises extends App {

  def calculateMeaningOfLife : Int = {
    Thread.sleep(2000)
    42
  }


  val aFuture = Future {
    calculateMeaningOfLife
  }

  //aFuture.wait(3000)
  println("Waiting on the future")

  aFuture.onComplete{
      case Success(dog) => println(s"The meaning of life is $dog")
      case Failure(exception) => println(s"Failed due to $exception")
  } //Some thread

  Thread.sleep(3000)



}
