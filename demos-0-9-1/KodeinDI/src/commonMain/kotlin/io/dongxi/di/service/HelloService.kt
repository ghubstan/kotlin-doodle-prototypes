package io.dongxi.di.service

// See https://github.com/kosi-libs/Kodein#readme
// See https://www.baeldung.com/kotlin/kodein-dependency-injection

interface IHelloService {
    fun sayHello(): String
}

class HelloService : IHelloService {
    override fun sayHello(): String {
        return "Hello"
    }
}