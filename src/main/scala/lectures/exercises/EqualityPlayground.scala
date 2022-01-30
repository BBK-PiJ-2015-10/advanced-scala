package lectures.exercises

import lectures.exercises.EqualityPlayground.FullEquality
import lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App {

  trait Equal[T] {
    def apply(first: T, second: T) : Boolean
  }

  object Equal {
    def apply[T](a: T, b:T)(implicit equalizer: Equal[T]) = equalizer.apply(a,b)
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(first: User, second: User): Boolean = first.name == second.name
  }

  object FullEquality extends Equal[User] {
    override def apply(first: User, second: User): Boolean = first.name == second.name && first.email == second.email
  }

  implicit class TypeSafeEqual[T](value: T){
    def ===(anotherValue: T)(implicit equalizer: Equal[T]) = equalizer.apply(value,anotherValue)
    def !==(anotherValue: T)(implicit equalizer: Equal[T]) = !equalizer.apply(value,anotherValue)
  }


  val john = User("John",32,"yasserpo@dog.com")

  val anotherJohn = User("John",45,"anotherJohn@dog.com")

  println(Equal(john,anotherJohn))

  println(john === anotherJohn)


  //println(john === anotherJohn (FullEquality))






}
