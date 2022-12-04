package playground

import scala.concurrent.{Await, Future, Promise}
import scala.util.{Failure, Random, Success, Try}
import scala.concurrent.duration._


// important for futures
import scala.concurrent.ExecutionContext.Implicits.global

object ScalaPlayground extends App { // the extends app will automatically add main function for our object
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  def calculateMohsen: Int = {
    Thread.sleep(300)
    println("mohsen hererererererere")
    45
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates the  meaning of  life on ANOTHER thread
    calculateMohsen
  } // (global) which is passed by the compiler


  println(aFuture.value) // Option[Try[Int]]

  println("Waiting on the future")
  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"the meaning of life is $meaningOfLife")
    case Failure(e) => println(s"I have failed with $e")
  } // SOME thread

  Thread.sleep(3000)
}
