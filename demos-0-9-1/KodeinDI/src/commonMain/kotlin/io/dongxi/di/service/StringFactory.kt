package io.dongxi.di.service

class StringFactory(
    private val helloService: IHelloService,
    private val goodbyeService: IGoodbyeService
) {

    fun sayHello(): String {
        return helloService.sayHello()
    }

    fun sayGoodbye(): String {
        return goodbyeService.sayGoodbye()
    }
}