package io.dongxi.di.service


interface IHelloService {
    fun sayHello(): String
}

class HelloService : IHelloService {
    override fun sayHello(): String {
        return "Hello"
    }
}