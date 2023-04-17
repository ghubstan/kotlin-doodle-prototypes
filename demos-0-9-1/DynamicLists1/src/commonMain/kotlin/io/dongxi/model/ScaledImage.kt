package io.dongxi.model

import io.nacular.doodle.geometry.Rectangle

enum class ScaledImage(val canvasDestination: Rectangle) {

    SMALL_RING(Rectangle(5, 15, 30, 30)),

    SMALL_RING_STONE(Rectangle(10, 15, 30, 30)),

    LARGE_RING(Rectangle(5, 15, 190, 190)),

    LARGE_RING_STONE(Rectangle(5, 15, 35, 35)),

    SMALL_NECKLACE(Rectangle(5, 15, 30, 30)),

    SMALL_NECKLACE_PENDANT(Rectangle(10, 15, 30, 30)),

    LARGE_NECKLACE(Rectangle(5, 15, 190, 190)),

    LARGE_NECKLACE_PENDANT(Rectangle(5, 15, 43, 43))
}