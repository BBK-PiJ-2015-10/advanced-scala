package lectures.part4implicits

  trait MyClassTemplate[T] {
    def action(value : T) : String
  }

  object MyClassTemplate {
    def apply[T](implicit instance: MyClassTemplate[T]) = instance
  }