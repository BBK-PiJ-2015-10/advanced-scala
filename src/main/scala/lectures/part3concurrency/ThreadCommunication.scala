package lectures.part3concurrency

import scala.collection.{mutable}
import scala.util.Random

object ThreadCommunication extends App {

  class SimpleContainer {
    private var value : Int = 0

    def isEmpty : Boolean = value == 0

    def get: Int = {
      val result = value
      value = 0
      result
    }

    def set(newValue : Int) = value = newValue

  }

  def naiveProdCons() : Unit = {
    val simpleContainer = new SimpleContainer()

    val consumer = new Thread(() => {
      println("[Consumer] waiting")
      while (simpleContainer.isEmpty){
        println("Actively waiting")
      }
      println("[Consumer] I have consumed "+simpleContainer.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced, after long work, the value "+value)
      simpleContainer.set(value)
    })

    consumer.start()
    producer.start()

  }

  def smarterProdCons() : Unit = {

    val simpleContainer = new SimpleContainer()

    val consumer = new Thread(() => {
      simpleContainer.synchronized {
        if (simpleContainer.isEmpty){
          println("[Consumer] waiting")
          simpleContainer.wait()
        }
        println("[Consumer] I have consumed "+simpleContainer.get)
        simpleContainer.notify()
      }
    })

    val producer = new Thread(() => {
      println("[producer] computing")
      Thread.sleep(500)
      simpleContainer.synchronized {
        if (!simpleContainer.isEmpty){
          simpleContainer.wait()
        }
        val value = 42
        println("[producer] I have produced, after long work, the value "+value)
        simpleContainer.set(value)
        simpleContainer.notify()
      }})

    consumer.start()
    producer.start()

  }

  def prodConsLargeBuffer() : Unit = {

    val buffer : mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread( () => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty){
            println("[Consumer] buffer empty")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println("[Consumer] consumed " +x)
          if (buffer.sizeIs == capacity-1){
            buffer.notify()
          }
          //buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread( () => {
      val random = new Random()
      var i = 0
      while (true) {
        buffer.synchronized {
          if (buffer.sizeIs == capacity){
            println("[Producer] buffer is full")
            buffer.wait()
          }
          buffer.enqueue(i)
          println("[Producer] added item : " +i)
          if (buffer.sizeIs == 1){
            buffer.notify()
          }
          i +=1
        }
        Thread.sleep(random.nextInt(250))
      }


    }

    )

    consumer.start()
    producer.start()

    //left on 6.53

  }

  def prodMultipleProdCons(): Unit = {

    val buffer: mutable.Queue[String] = new mutable.Queue[String]()
    val maxCapacity = 25

    Range.inclusive(1,3).foreach{ id =>
      consumerLauncher(id,buffer, maxCapacity).start()
      producerLauncher(id,buffer,maxCapacity).start()
    }

  }

  def consumerLauncher(id: Int, buffer: mutable.Queue[String], maxCapacity: Int) : Thread = {
    val random = new Random()
    val consumer = new Thread(() => {
      while (true) {
        buffer synchronized {
          while (buffer.isEmpty){
            println(s"[Consumer : $id ] is waiting!")
            buffer.wait()
          }
          val consumed = buffer.dequeue();
          println("[Consumer :" +id +" ] consumed : " +consumed)
          if (buffer.sizeIs == (maxCapacity - 1)){
            buffer.notify()
          }
        }
        Thread.sleep(random.nextInt(500))
      }
    })
    consumer
  }

  def producerLauncher(id: Int, buffer: mutable.Queue[String], maxCapacity: Int) : Thread = {
    val random = new Random()
    var itemCount : Int = 0
    val producer = new Thread(() => {
      while (true) {
        buffer.synchronized {
          while (buffer.sizeIs == maxCapacity){
            println(s"[Producer : $id ] waiting, buffer is full!")
            buffer.wait()
          }
          val item = itemCount.toString + " : " + id
          buffer.enqueue(item)
          println("[Producer " + id + " ]  produced : " +item)
          itemCount += 1
          if (buffer.sizeIs==1){
            buffer.notify()
          }
        }
        Thread.sleep(random.nextInt(500))
      }
    }
    )
    producer
  }







  /*
  producer -> [? ? ?] -> consumer
   */


  //naiveProdCons()

  //smarterProdCons()

  //prodConsLargeBuffer()

  //prodMultipleProdCons()

  //left on minute 13

}
