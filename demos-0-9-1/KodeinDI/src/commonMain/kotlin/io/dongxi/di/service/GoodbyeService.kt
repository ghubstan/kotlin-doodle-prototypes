package io.dongxi.di.service


interface IGoodbyeService {
    fun sayGoodbye(): String
}

class GoodbyeService : IGoodbyeService {
    override fun sayGoodbye(): String {
        return "Goodbye"
    }
}