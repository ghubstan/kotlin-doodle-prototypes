package io.dongxi.di.service

// See https://github.com/kosi-libs/Kodein#readme
// See https://www.baeldung.com/kotlin/kodein-dependency-injection

interface IGoodbyeService {
    fun sayGoodbye(): String
}

class GoodbyeService : IGoodbyeService {
    override fun sayGoodbye(): String {
        return "Goodbye"
    }
}