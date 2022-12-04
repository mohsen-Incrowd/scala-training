package lectures.oop

object Generic extends App {
  /*
  Lets say that we created a linkedlist for integers and now we want a linkedlist for string or whatever
  so we will have a problem to duplicate our code WELL here comes that [A] generic type means
  */
  class LinkedList[A]{
    //use type A
  }

  val linkedListOfIntegers = new LinkedList[Int]
  val linkedListOfStrings = new LinkedList[String]


  class MyList[+A] {
    // use the type A
    def add[B >: A](element: B): MyList[B] = ???
    /*
      A = Cat
      B = Animal
     */
  }

  class MyMap[Key, Value]

  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  // generic methods
  // note that ??? it means this still implemented and it will compaile but will throw error if someone call if
  object MyList {
    def empty[A]: MyList[A] = ???
  }
  val emptyListOfIntegers = MyList.empty[Int]

  // variance problem
  // (don't stress about it)
  class Animal
  class Cat extends Animal
  class Dog extends Animal

  // 1. yes, List[Cat] extends List[Animal] = COVARIANCE
  class CovariantList[+A]
  val animal: Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]
  // animalList.add(new Dog) ??? HARD QUESTION => we return a list of Animals

  // 2. NO = INVARIANCE
  class InvariantList[A]
  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  // 3. Hell, no! CONTRAVARIANCE opposite to covariance
  class Trainer[-A]
  val trainer: Trainer[Cat] = new Trainer[Animal]

  // bounded types
  // This means class cage only accepts type parameters A which are subtypes of Animal
  // and there are also upbound types which is [A >: Animal] this means that accepts also super type of animal (parent)
  class Cage[A <: Animal](animal: A)
  val cage = new Cage(new Dog)

  class Car
  // generic type needs proper bounded type
  //  val newCage = new Cage(new Car)


  // expand MyList to be generic

}