package lectures.part3concurrency

import java.util.concurrent.Executors

object Intro extends App {

  //println("This is a dog")

  // JVM threads

  val aRunnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }

  val aThread = new Thread(aRunnable)


  //block until thread is done running
  //aThread.join()

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))

  //threadHello.start()
  //threadGoodbye.start()

  //aThread.start()

  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool"))

  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  pool.shutdown()
  //pool.execute(() => println("should not appear"))

  //pool.shutdownNow()

  //println(pool.isShutdown)




}
