package lectures.part2afp

object Monads extends App{

  trait Attempt[+A] {

    def flatMap[B](f: A => Attempt[B]): Attempt[B]

  }

  object Attempt {
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f.apply(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /* left.identity unit.flatmap(f) = f(x) */

  val attempt = Attempt {
    throw new RuntimeException("My own monda")
  }

  //println(attempt)

  class Lazy[+A](value: => A) {
    //call by need
    private lazy val internalValue = value

    def use: A = value

    //def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(value)
    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)

  }

  object Lazy {
    def apply[A](value: =>A) : Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I don't feel like doing anything")
    42
  }

  //println(lazyInstance.use)

  val flatMaInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  val flatMaInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  flatMaInstance.use
  flatMaInstance2.use

  /*
  left-identity
  unit.flatMap(f) = f(v)
  Lazy(v).flaMap(f) = f(v)

  right-identity
  l.flatMap(unit) = l
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

  associativity
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)


   */
















  //left on 11:32
}
