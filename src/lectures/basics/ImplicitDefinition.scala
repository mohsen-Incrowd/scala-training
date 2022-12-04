package lectures.basics

object ImplicitDefinition extends App{
/*
In Scala, the keyword "implicit" is used to indicate that a value or function can be automatically provided by the compiler,
 without the need for the programmer to explicitly specify it. This can be useful for defining default values for function arguments,
 or for implementing type conversion rules. For example, if a function expects an argument of type "Int",
 but the programmer provides a value of type "String", the compiler can automatically convert the string to an integer,
 if an implicit conversion rule has been defined for that specific conversion.
In this way, the "implicit" keyword in Scala allows for more concise and expressive code.
*/
// Define a simple function that takes a string argument
def printString(s: String): Unit = println(s)

  // Call the function with a string value
  printString("hello") // this will print "hello"

  // Call the function with an integer value
  // This will cause a compilation error, because the function expects a String, not an Int
  // printString(1)

  // To fix this error, we can define an implicit conversion rule that will automatically
  // convert the integer to a string when the function is called
  implicit def intToString(i: Int): String = i.toString

  // Now, when we call the function with an integer value, the compiler will automatically
  // apply the conversion rule, and the function will be called with a string argument
  printString(1) // this will print "1"
}
