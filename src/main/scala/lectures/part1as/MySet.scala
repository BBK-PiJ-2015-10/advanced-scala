package lectures.part1as

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  override def apply(v1: A): Boolean = contains(v1)

  def contains(elem: A) : Boolean

  def +(elem: A): MySet[A]

  def ++(anotherSet: MySet[A]) : MySet[A]

  def map[B](f: A => B): MySet[B]

  def flatMap[B](f: A => MySet[B]) : MySet[B]

  def filter(predicate: A => Boolean) : MySet[A]

  def foreach(f: A => Unit) : Unit

  def -(elem: A) : MySet[A]

  def --(anotherSet: MySet[A]) : MySet[A]

  def &(anotherSet: MySet[A]) : MySet[A]

  def unary_!  :MySet[A]

}

class EmptySet[A] extends MySet[A] {

  override def contains(elem: A): Boolean = false

  override def +(elem: A): MySet[A] = new NonEmptySet[A](elem,this)

  override def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  override def map[B](f: A => B): MySet[B] = new EmptySet[B]

  override def flatMap[B](F: A => MySet[B]): MySet[B] = new EmptySet[B]

  override def filter(predicate: A => Boolean): MySet[A] = this

  override def foreach(f: A => Unit): Unit = ()

  override def -(elem: A): MySet[A] = this

  override def --(anotherSet: MySet[A]): MySet[A] = this

  override def &(anotherSet: MySet[A]): MySet[A] = this

  // set[1,2,3] => anything but 1,2,3
  def unary_! : MySet[A] = new PropertyBasedSet[A](_ => true)

}


/*
class AllInclusiveSet[A](head: A , tail: MySet[A]) extends MySet[A] {

  override def contains(elem: A): Boolean = true

  override def +(elem: A): MySet[A] = this

  override def ++(anotherSet: MySet[A]): MySet[A] = this

  // allinclusiveSet[Int] = all naturals
  // naturals.map(x => x % 3) ???
  override def map[B](f: A => B): MySet[B] = (tail map f) + f(head)

  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???

  //property-based set
  override def filter(predicate: A => Boolean): MySet[A] = ???

  override def foreach(f: A => Unit): Unit = ???

  override def -(elem: A): MySet[A] = ???

  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  override def unary_! : MySet[A] = new EmptySet[A]

}
*/

//all elements of type A which satisfy a property
// {x in A | property(x)}
class PropertyBasedSet[A](property: A => Boolean) extends MySet[A] {

  override def contains(elem: A): Boolean = property(elem)

  // { x in A | property(x) } + element = {x in A | property(x) || x == element}
  override def +(elem: A): MySet[A] =
    new PropertyBasedSet[A](x => property(x) || x == elem)

  override def ++(anotherSet: MySet[A]): MySet[A] =
    new PropertyBasedSet[A]( x => property(x) || anotherSet(x))

  // allinclusiveSet[Int] = all naturals
  // naturals.map(x => x % 3) ???
  override def map[B](f: A => B): MySet[B] = politelyFailed

  override def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFailed

  override def filter(predicate: A => Boolean): MySet[A] = new PropertyBasedSet[A](x => property(x) && predicate(x))

  override def foreach(f: A => Unit): Unit = politelyFailed

  override def -(elem: A): MySet[A] = filter(x => x != elem)

  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

  override def unary_! : MySet[A] = new PropertyBasedSet[A](x => !property(x))

  def politelyFailed = throw new IllegalArgumentException("Really deep rabbit hole!")

}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  override def contains(elem: A): Boolean = elem == head || tail.contains(elem)

  override def +(elem: A): MySet[A] = {
    if (this.contains(elem)) this
    else new NonEmptySet[A](elem,this)
  }

  override def ++(anotherSet: MySet[A]): MySet[A] = tail ++ anotherSet + head
  /*
  [1,2,3] ++ [4,5]
  [2,3] ++ [4,5] + 1
  [3] ++ [4,5] + 1 + 2
  [] ++ [4,5] + 1 +2 +3
  [4,5,1,2,3]
   */

  override def map[B](f: A => B): MySet[B] = (tail map f) + f(head)

  override def flatMap[B](f: A => MySet[B]): MySet[B] = (tail flatMap f) ++ f(head)

  override def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  override def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  override def -(elem: A): MySet[A] =
    if (head == elem) tail
    else tail - elem + head


  override def --(anotherSet: MySet[A]): MySet[A] = filter(x => !anotherSet(x))

  def unary_!  :MySet[A] = new PropertyBasedSet[A](x => !this.contains(x))

  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)

}

object MySet {
  /*
  val s = MySet(1,2,3) = buildSet(seq(1,2,3), [])
  = buildset(seq(2,3), [] + 1 )
  = buildset(seq(3), [1] + 2 )
  = buildset(seq(), [1,2] + 3 )
  = [1,2,3]
   */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valueSeq: Seq[A], acc: MySet[A]) : MySet[A] =
      if (valueSeq.isEmpty) acc
      else buildSet(valueSeq.tail,acc + valueSeq.head)
   buildSet(values.toSeq,new EmptySet[A])
  }

}

object MySetPlayground extends App {

  val s = MySet(1,2,3,4)

  s + 5 ++ MySet(-1,-2) + 3 flatMap  (x => MySet(x, x * 10)) filter (_ %2 == 0) foreach println

  val negative = !s  // all the naturals not equal to 1,2,3,4

  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ %2 == 0)

  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5
  println(negativeEven5(5))


}