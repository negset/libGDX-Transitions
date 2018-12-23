package org.neggly.libgdx.transitions.transitions

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import org.neggly.libgdx.transitions.ScreenTransition

class RotatingTransition(private val interpolation: Interpolation,
                         private val angle: Float,
                         private val scaling: TransitionScaling,
                         override val duration: Float = 2f) : ScreenTransition
{
    enum class TransitionScaling
    {
        IN, OUT
    }

    override fun render(batch: Batch, currentScreenTexture: Texture, nextScreenTexture: Texture, percent: Float)
    {
        val width = currentScreenTexture.width.toFloat()
        val height = currentScreenTexture.height.toFloat()

        val applied = interpolation.apply(percent)

        val bg: Texture
        val rotating: Texture
        val scale: Float

        when (scaling)
        {
            TransitionScaling.IN ->
            {
                scale = applied
                bg = currentScreenTexture
                rotating = nextScreenTexture
            }
            TransitionScaling.OUT ->
            {
                scale = 1f - applied
                bg = nextScreenTexture
                rotating = currentScreenTexture
            }
        }

        batch.begin()
        batch.draw(bg, 0f, 0f, width / 2, height / 2,
                width, height, 1f, 1f, 0f, 0, 0,
                width.toInt(), height.toInt(), false, true)
        batch.draw(rotating, 0f, 0f, width / 2, height / 2,
                width, height, scale, scale, applied * angle, 0, 0,
                width.toInt(), height.toInt(), false, true)
        batch.end()
    }

    override fun dispose()
    {
    }
}
