package analysis

import cfg._

class Interpreter(fun:Function) extends ValueAnalysis[Integer](fun) {
  override def run() = {
    populateSymbolTable()
    var currBB = fun.First

  }

  override def printResult() = {

  }
}
