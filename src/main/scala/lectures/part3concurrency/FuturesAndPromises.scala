package lectures.part3concurrency

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._

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

  case class Profile(id: String, name: String) {
    def poke(anotherProfile : Profile) : Unit =
      println(s"${this.name} is poking ${anotherProfile.name}")
  }

  object SocialNetwork {

    val names = Map(
      "fb.id.1-zuck" -> "Mark",
      "fb.id.2-bill" -> "Bill",
      "fb.id.0-dummy" -> "Dummy"
    )

    val friends = Map(
      "fb.id.1-zuck" -> "fb.id.2-bill"
    )

    val random = new Random()

    def fetchProfile(id: String) : Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      Profile(id,names(id))
    }

    def fetchBestFriend(profile: Profile) : Future[Profile] = Future {
      Thread.sleep(random.nextInt(300))
      val bfId = friends(profile.id)
      Profile(bfId,names(bfId))
    }

  }

  val mark = SocialNetwork.fetchProfile("fb.id.1-zuck")

  mark.onComplete{
    case Success(markProfile) => {
      val bill = SocialNetwork.fetchBestFriend(markProfile)
      bill.onComplete{
        case Success(billProfile) => markProfile.poke(billProfile)
        case Failure(e) => e.printStackTrace()
      }
    }
    case Failure(e) => e.printStackTrace()
  }

  //Thread.sleep(2000)


  //functional composition of future
  //map. flatMap, filter

  val nameOnTheWall = mark.map(_.name)

  val markBestFriends = mark.flatMap(profile => SocialNetwork.fetchBestFriend(profile))

  val zucksBestFriendRestricted = markBestFriends.filter(_.name.startsWith("Z"))

  for {
    mark <- SocialNetwork.fetchProfile("fb.id.1-zuck")
    bill <- SocialNetwork.fetchBestFriend(mark)
  } mark.poke(bill)


  val aProfileNoMatterWhat  =
    SocialNetwork.fetchProfile("unknownId").recover{
      case e: Throwable => Profile("fb-id.0-dummyId","Forever alone")
    }

  val aFetchProfileNoMatterWhat =
    SocialNetwork.fetchProfile("unknownId").recoverWith{
      case e: Throwable => SocialNetwork.fetchProfile("fb-id.0-dummyId")
    }

  val fallBackResult =
    SocialNetwork.fetchProfile("unknownId").fallbackTo(SocialNetwork.fetchProfile("fb.id.0-dummy"))


  Thread.sleep(2000)


  
  case class User(name: String)
  case class Transaction(sender: String, receiver: String, amount: Double, status: String)

  object BankingApp {

    val name = "Rock the JVM Banking"

    def fetchUser(name: String) : Future[User] = Future {
      Thread.sleep(300)
      User(name)
    }

    def createTransaction(user: User, merchantName: String, amount: Double) : Future[Transaction] = Future {
      Thread.sleep(300)
      Transaction(user.name,merchantName,amount,"SUCCESS")
    }

    def purchase(username: String, item: String, merchantName: String, cost : Double) : String = {
      val transactionStatusFuture = fetchUser(username)
        .flatMap(user => createTransaction(user, merchantName, cost))
        .map(_.status)
      //This is to block
      Await.result(transactionStatusFuture, 2.seconds)
    }
  }

  println(BankingApp.purchase("Dog","iPhone 12","rock the jvm store",3000))

  val promise = Promise[Int]()
  val future = promise.future

  future.onComplete{
    case Success(r) => println(s"[Consumer] I've received ${r}")
  }

  val producer = new Thread(() => {
    println(s"[Producer] crunching data ...")
    Thread.sleep(500)
    // fullfilling the promise
    promise.success(42)
    println("[producer] done")
  })

  producer.start()

  Thread.sleep(1000)



  def fullfillImmediately[T](value: T) : Future[T] = Future.successful(value)

  def inSequence[A,B](first: Future[A],second: Future[B]) : Future[B] = first.flatMap(_ => second)

  def fistCompleted[A](operation1: Future[A], operation2: Future[A]) : Future[A] = {
    val promise = Promise[A]
    operation1.onComplete(promise.tryComplete)
    operation2.onComplete(promise.tryComplete)
    promise.future
  }

  def last[A](fa: Future[A], fb:Future[A]) : Future[A] = {
    val bothPromise = Promise[A]
    val lastPromise = Promise[A]

    def checkingAndComplete = (result: Try[A]) =>
      if(!bothPromise.tryComplete(result))
        lastPromise.complete(result)

    fa.onComplete(checkingAndComplete)
    fb.onComplete(checkingAndComplete)
    lastPromise.future
  }


  val fast = Future {
    Thread.sleep(100)
    42
  }

  val slow = Future {
    Thread.sleep(200)
    45
  }

  //fistCompleted(fast,slow).foreach(println)
  //last(fast,slow).foreach(println)

  //Thread.sleep(1000)

  def retryUntil[A](action: () => Future[A],condition: A => Boolean) : Future[A] =
    action()
      .filter(condition)
      .recoverWith {
        case _ => retryUntil(action, condition)
      }

  val random = new Random()
  val action = () => Future {
    Thread.sleep(100)
    val nextValue = random.nextInt(1000)
    println(s"Generated ${nextValue}")
    nextValue
  }

  retryUntil(action,(x:Int) => x < 10).foreach(result => println(s"Settled at ${result}"))

  Thread.sleep(10000)









}
