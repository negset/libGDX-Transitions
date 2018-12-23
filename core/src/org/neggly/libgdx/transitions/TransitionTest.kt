package org.neggly.libgdx.transitions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.Logger
import org.neggly.libgdx.transitions.screens.FirstScreen
import org.neggly.libgdx.transitions.transitions.*

const val WIDTH = 640f
const val HEIGHT = 480f

class TransitionTest : MyGame(WIDTH, HEIGHT)
{
    private var transitions = mutableListOf<ScreenTransition>()
    private var index = 0

    override fun create()
    {
        super.create()

        Gdx.app.logLevel = Logger.DEBUG

        transition = ColorFadeTransition(Color.BLACK, Interpolation.exp10, 3f)

        transitions.run {
            add(AlphaFadingTransition())
            add(SlidingTransition(SlidingTransition.Direction.LEFT, Interpolation.linear, true))
            add(SlidingTransition(SlidingTransition.Direction.UP, Interpolation.bounce, false))
            add(SlicingTransition(SlicingTransition.Direction.UP_DOWN, 128, Interpolation.pow4))
            add(SlicingTransition(SlicingTransition.Direction.DOWN, 8, Interpolation.bounce))
            add(RotatingTransition(Interpolation.pow2Out, 720f, RotatingTransition.TransitionScaling.OUT))
            add(RotatingTransition(Interpolation.bounce, 360f, RotatingTransition.TransitionScaling.IN))
            add(ColorFadeTransition(Color.WHITE, Interpolation.sine))
        }

        screen = FirstScreen(this)
    }

    fun switchTransition()
    {
        if (++index >= transitions.size) index = 0
        transition = transitions[index]
    }
}
