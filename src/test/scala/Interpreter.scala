import org.scalatest._

import cfg._
import analysis._

class InterpreterSpec extends FlatSpec with Matchers {
  def performParseTest(filename: String) = {
    val fun = Parser.parse(filename)
    val reference = scala.io.Source.fromFile(filename).mkString
  }

  "Interpreter" should "compute the result 10" in {
    val fun = new Function("foo")

    val bb_start = new BasicBlock("start")
    val bb_A = new BasicBlock("BB_A")
    val bb_B = new BasicBlock("BB_B")
    val bb_C = new BasicBlock("BB_C")

    fun.First = bb_start

    bb_start.Instrs = B(Const(1), bb_A, bb_A) :: Nil

    var phi1 = PHI("phi1", (Const(0), bb_start) :: (Undef("inc1"), bb_A) :: Nil)
    val cmp1 = BinOp("cmp1", SLT(), phi1, Const(10))

    val add1 = BinOp("inc1", ADD(), phi1, Const(1))
    phi1.Ops = phi1.Ops.head :: (add1, bb_B) :: Nil

    bb_A.Instrs = phi1 :: cmp1 :: B(cmp1, bb_B, bb_C) :: Nil
    bb_B.Instrs = add1 :: B(Const(1), bb_A, bb_A) :: Nil
    bb_C.Instrs = RET(phi1) :: Nil

    val interpreter = new Interpreter(fun)

    interpreter.run
    interpreter.getResult should include ("""__RES__ -> Some(10)""")
  }

  // it should "throw NoSuchElementException if an empty stack is popped" in {
  //   val emptyStack = new Stack[Int]
  //   a [NoSuchElementException] should be thrownBy {
  //     emptyStack.pop()
  //   }
  // }
}
