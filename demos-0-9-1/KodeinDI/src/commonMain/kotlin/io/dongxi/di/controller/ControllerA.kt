package io.dongxi.di.controller

import io.dongxi.di.service.IGoodbyeService
import io.dongxi.di.service.IHelloService
import org.kodein.di.DI
import org.kodein.di.instance

interface IControllerA {

}

class ControllerA(private val di: DI) : IControllerA {
    private val helloService: IHelloService by di.instance()
    private val goodbyeService: IGoodbyeService by di.instance()
}