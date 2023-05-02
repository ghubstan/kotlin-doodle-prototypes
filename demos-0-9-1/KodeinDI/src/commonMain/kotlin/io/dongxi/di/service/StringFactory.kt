package io.dongxi.di.service

// See https://github.com/kosi-libs/Kodein#readme
// See https://www.baeldung.com/kotlin/kodein-dependency-injection

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