package lectures.part4implicits

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div> "
  }

  val john = User("John",32,"yasserpo@dog.com")

  println(john.toHtml)

  object HTMLSerializablePM {

    /*
    def serializaToHtml(value: Any) = value match {
      case User(n,a,e) =>
      case java.util.Date =>
      case _ =>
    }
    */

  }

  trait HtmlSerializer[T] {
    def serialize(value: T) : String
  }

  object UserSerializer extends HtmlSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div> "
  }

  object PartialUserSerializer extends HtmlSerializer[User] {
    def serialize(user: User): String = s"<div> ${user.name}</div>"
  }

  println(UserSerializer.serialize(john))

  import java.util.Date

  object DateSerializer extends HtmlSerializer[Date] {
    override def serialize(date: Date): String = s"<div> ${date.toString}</div>"
  }

  //TYPE CLASS
  trait MyClassTemplate[T] {
    def action(value : T) : String
  }

  /*
  - Equal
   */

  trait Equal[T] {
    def apply(first: T, second: T) : Boolean
  }

  object NameEquality extends Equal[User] {
    override def apply(first: User, second: User): Boolean = first.name == second.name
  }

  object FullEquality extends Equal[User] {
    override def apply(first: User, second: User): Boolean = first.name == second.name && first.email == second.email
  }




}
