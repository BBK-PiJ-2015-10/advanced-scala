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

  implicit object UserSerializer extends HtmlSerializer[User] {
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

  object HtmlSerializer {
    def serialize[T](value: T)(implicit serializer: HtmlSerializer[T]) : String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HtmlSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HtmlSerializer[Int] {
    override def serialize(value: Int): String = s"<div style: color=blue>${value}</div>"
  }

  //println(HtmlSerializer.serialize(42))
  //println(HtmlSerializer.serialize(john))

  // access to the entire type class interface
  //println(HtmlSerializer[User].serialize(john))

  val anotherJohn = User("John",45,"anotherJohn@dog.com")

  //part 3

  implicit class HTMLEnrichment[T](value: T){
    def toHTML(implicit serializer: HtmlSerializer[T]) : String = serializer.serialize(value)
  }

  println(john.toHTML)

  println(2.toHTML)

  println(john.toHTML(PartialUserSerializer))

  /*
  - type class itself
  -- type class instances
  -- conversion with implicit classes
   */

  def htmlBoilerplate[T](content: T)(implicit serializer: HtmlSerializer[T]) : String  =
    s"<html><body> ${content.toHTML(serializer)} </body></html>"

  def htmlSugar[T : HtmlSerializer](content: T) : String = {
    val serializer = implicitly[HtmlSerializer[T]]
    s"<html><body> ${content.toHTML(serializer)} </body></html>"
  }


  //implicitly

  case class Permissions(mask: String)

  implicit val defaultPermissions = Permissions("0744")

  val standardPerms = implicitly[Permissions]









}
