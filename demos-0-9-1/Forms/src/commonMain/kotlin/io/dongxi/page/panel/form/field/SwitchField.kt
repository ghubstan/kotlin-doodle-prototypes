package io.dongxi.page.panel.form.field

import io.nacular.doodle.controls.buttons.Switch
import io.nacular.doodle.controls.form.Form
import io.nacular.doodle.controls.form.field
import io.nacular.doodle.controls.form.ifValid
import io.nacular.doodle.controls.text.Label
import io.nacular.doodle.core.container
import io.nacular.doodle.geometry.Size
import io.nacular.doodle.layout.constraints.constrain
import io.nacular.doodle.text.StyledText
import io.nacular.doodle.utils.Dimension
import io.nacular.doodle.utils.TextAlignment


fun switchField(text: StyledText) = field {
    container {
        focusable = false // ensure wrapping container isn't focusable
        this += Label(text).apply {
            fitText = setOf(Dimension.Height)
            textAlignment = TextAlignment.Start
        }
        this += Switch().apply {
            initial.ifValid { selected = it } // adopt initial value if present

            selectedChanged += { _, _, _ ->
                state = Form.Valid(selected) // update field as switch changes
            }

            size = Size(40, 25)
            state = Form.Valid(selected) // ensure field is valid at beginning
        }

        layout = constrain(children[0], children[1]) { label, switch ->
            switch.left eq parent.right - 10 - switch.width.readOnly
            switch.centerY eq parent.centerY

            label.left eq 10
            label.right eq switch.left - 10
            label.centerY eq switch.centerY
        }
    }
}



