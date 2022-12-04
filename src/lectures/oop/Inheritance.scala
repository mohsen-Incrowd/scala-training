package lectures.oop

object Inheritance extends App {

  // Normal inheritance
  class Car{
    def startEngine = println("triiiivvv")
  }
  // bmw is a subclass of car and car is a superclass of bmw
  class BMW extends Car
  val mycar = new BMW
  mycar.startEngine

  // single class inheritance
  // you can extend only one class
  // sealed means that this class can be only inherited only in this file
  sealed class Animal {
    val creatureType = "wild"
    def eat = println("nomnom")
    final def sound = println("boom boom") // the final prevent the method to be overrided by inherited classes
  }

  class Cat extends Animal {
    def crunch = {
      eat
      println("crunch crunch")
    }
  }

  val cat = new Cat
  cat.crunch


  // constructors
  class Person(name: String, age: Int) {
    // this is a constructor in scala
    def this(name: String) = this(name,0)
  }
  class Adult(name: String, age: Int, idCard: String) extends Person(name)

  // overriding
  class Dog(override val creatureType: String) extends Animal {
    //    override val creatureType = "domestic" // can override in the body or directly in the constructor arguments
    override def eat = {
      super.eat // this will call the eat method from the inherited class which in this case is Animal
      println("crunch, crunch")
    }
//    override def sound = println("DSAdsa") cann't be done because sound method is final member
  }
  val dog = new Dog("K9")
  dog.eat
  println(dog.creatureType)
  dog.sound


  // type substitution (broad: polymorphism)
  // this is ploymorphism
  val unknownAnimal: Animal = new Dog("K9")
  unknownAnimal.eat

  // overRIDING vs overLOADING
  // overriding : means supplying different implementation in derived classes
  // overloading : means supplying multiple methods with different signatures but with the same
  // name in the same class

  // super

  // preventing overrides
  // 1 - use final on member
  // 2 - use final on the entire class // we can add final on the class level so it cann't be extended
  // 3 - seal the class = extend classes in THIS FILE, prevent extension in other files
}