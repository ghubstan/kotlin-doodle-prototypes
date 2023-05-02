package io.dongxi.di

import io.dongxi.di.controller.IControllerA
import io.dongxi.di.service.GoodbyeService
import io.dongxi.di.service.HelloService
import io.dongxi.di.service.IGoodbyeService
import io.dongxi.di.service.IHelloService
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

// See https://github.com/kosi-libs/Kodein#readme

// Modules:  https://www.baeldung.com/kotlin/kodein-dependency-injection#1-modules
//
// Group components by particular criteria — for example, all classes related
// to data persistence — and combine the blocks to build a resulting container.
//
// Note: as modules contain binding rules, target beans are re-created when
// the same module is used in multiple Kodein instances.


// Note:  Module's must have a name, or there will be run-time error.
val groupModule = DI.Module(name = "GroupModule") {
    bindSingleton<IHelloService> { HelloService() }
    bindSingleton<IGoodbyeService> { GoodbyeService() }
}

val di = DI {
    import(groupModule)
}

val controllerA: IControllerA by di.instance()
// assertThat(controllerA is ControllerA)