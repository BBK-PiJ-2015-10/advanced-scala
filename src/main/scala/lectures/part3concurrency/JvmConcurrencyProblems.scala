package lectures.part3concurrency

object JvmConcurrencyProblems {

  def runInParallel() : Unit = {

    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()
    println(x)

  }


  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int) : Unit = {
    bankAccount.amount -= price
  }

  def buySafe(bankAccount: BankAccount, thing: String, price: Int) : Unit = {
    bankAccount.synchronized {
      bankAccount.amount -= price
    }
  }

  def demoBankingProblem() : Unit = {

    (1 to 100000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buySafe(account,"shoes",3000))
      val thread2 = new Thread(() => buySafe(account,"iPhone",4000))
      thread1.start()
      thread2.start()
      //thread3.start()
      //thread4.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"AHA. I just broke the bank: ${account.amount}")
    }

  }

  def starter(x: Int): Unit = {
    val ct= new Thread(() => println(s"Hello from thread ${x+1}"))
    ct.start()
    ct.join()
    println(s"Hello from thread ${x}")
  }

  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread = {
    new Thread(() => {
      if (i<maxThreads) {
        val newThread = inceptionThreads(50,i+1)
        newThread.start()
        newThread.join()
      }
      println(s"Hello from thread $i")
    })
  }

  def minMaxX() : Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x +=1))
    threads.foreach(_.start())
  }

  def demoSleepFallacy() : Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })
    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    awesomeThread.join()
    println(message)
  }



  def main(args: Array[String]): Unit = {

    //inceptionThreads(50).start()

    demoSleepFallacy()

    //left on 22

  }


}
