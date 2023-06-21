package com.dishdiscoverers.foodrecipe.dongguo.data

interface BookRepositoryInterface {
    fun getAll(): List<BookData>
    fun findByTitle(title: String): List<BookData>
}

data class BookData(
    val id: String,
    val title: String,
    val imagePath: String,
    val price: Int,
    val description: String
)

class BookRepositoryLocalList : BookRepositoryInterface {
    override fun getAll(): List<BookData> {
        return getBooks()
    }

    override fun findByTitle(title: String): List<BookData> {
        return getBooks().filter { it.title.lowercase().indexOf(title.lowercase()) >= 0 }
    }

    private fun getBooks(): List<BookData> {

        return listOf(
            BookData(
                "1",
                "head first kotlin",
                "https://kotlinlang.org/docs/images/head-first-kotlin.jpeg",
                1000,
                description = "Head First Kotlin is a complete introduction to coding in Kotlin. This hands-on book helps you learn the Kotlin language with a unique method that goes beyond syntax and how-to manuals and teaches you how to think like a great Kotlin developer.\n" +
                        "\n" +
                        "You'll learn everything from language fundamentals to collections, generics, lambdas, and higher-order functions. Along the way, you'll get to play with both object-oriented and functional programming.\n" +
                        "\n" +
                        "If you want to really understand Kotlin, this is the book for you."

            ),
            BookData(
                "2",
                "kotlin in action",
                "https://kotlinlang.org/docs/images/kotlin-in-action.png",
                1000,
                description = "Kotlin in Action teaches you to use the Kotlin language for production-quality applications. Written for experienced Java developers, this example-rich book goes further than most language books, covering interesting topics like building DSLs with natural language syntax.\n" +
                        "\n" +
                        "The book is written by Dmitry Jemerov and Svetlana Isakova, developers on the Kotlin team.\n" +
                        "\n" +
                        "Chapter 6, covering the Kotlin type system, and chapter 11, covering DSLs, are available as a free preview on the publisher web site"
            ),
            BookData(
                "3",
                "Atomic Kotlin",
                "https://kotlinlang.org/docs/images/atomic-kotlin.png",
                1000,
                description = "Atomic Kotlin is for both beginning and experienced programmers!\n" +
                        "\n" +
                        "From Bruce Eckel, author of the multi-award-winning Thinking in C++ and Thinking in Java, and Svetlana Isakova, Kotlin Developer Advocate at JetBrains, comes a book that breaks the language concepts into small, easy-to-digest \"atoms\", along with a free course consisting of exercises supported by hints and solutions directly inside IntelliJ IDEA!"
            ),
            BookData(
                "4",
                "joy of kotlin",
                "https://kotlinlang.org/docs/images/joy-of-kotlin.png",
                1000,
                description = "The Joy of Kotlin teaches you the right way to code in Kotlin.\n" +
                        "\n" +
                        "In this insight-rich book, you'll master the Kotlin language while exploring coding techniques that will make you a better developer no matter what language you use. Kotlin natively supports a functional style of programming, so seasoned author Pierre-Yves Saumont begins by reviewing the FP principles of immutability, referential transparency, and the separation between functions and effects.\n" +
                        "\n" +
                        "Then, you'll move deeper into using Kotlin in the real world, as you learn to handle errors and data properly, encapsulate shared state mutations, and work with laziness.\n" +
                        "\n" +
                        "This book will change the way you code — and give you back some of the joy you had when you first started."
            ),
            BookData(
                "5",
                "Programming Kotlin",
                "https://kotlinlang.org/docs/images/programming-kotlin.png",
                1000,
                description = "Programming Kotlin\n is written by Venkat Subramaniam.\n" +
                        "\n" +
                        "Programmers don't just use Kotlin, they love it. Even Google has adopted it as a first-class language for Android development.\n" +
                        "\n" +
                        "With Kotlin, you can intermix imperative, functional, and object-oriented styles of programming and benefit from the approach that's most suitable for the problem at hand.\n" +
                        "\n" +
                        "Learn to use the many features of this highly concise, fluent, elegant, and expressive statically typed language with easy-to-understand examples.\n" +
                        "\n" +
                        "Learn to write maintainable, high-performing JVM and Android applications, create DSLs, program asynchronously, and much more."
            ),
            BookData(
                "6",
                "Kotlin Programming: The Big Nerd Ranch Guide",
                "https://kotlinlang.org/docs/images/joy-of-kotlin.png",
                100,
                description = "Kotlin Programming: The Big Nerd Ranch Guide\n" +
                        "\n" +
                        "In this book you will learn to work effectively with the Kotlin language through carefully considered examples designed to teach you Kotlin's elegant style and features.\n" +
                        "\n" +
                        "Starting from first principles, you will work your way to advanced usage of Kotlin, empowering you to create programs that are more reliable with less code."
            ),
        )
    }
}
