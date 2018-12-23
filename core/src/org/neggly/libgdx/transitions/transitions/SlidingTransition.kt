package org.neggly.libgdx.transitions.transitions

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import org.neggly.libgdx.transitions.ScreenTransition

class SlidingTransition(private val direction: Direction,
                        private val interpolation: Interpolation,
                        private val slideOut: Boolean,
                        override val duration: Float = 2f) : ScreenTransition
{
    enum class Direction
    {
        LEFT, RIGHT, UP, DOWN
    }

    override fun render(batch: Batch, currentScreenTexture: Texture, nextScreenTexture: Texture, percent: Float)
    {
        val applied = interpolation.apply(percent)
        val width = currentScreenTexture.width.toFloat()
        val height = currentScreenTexture.height.toFloat()
        var x = 0f
        var y = 0f

        when (direction)
        {
            Direction.LEFT ->
            {
                x = -width * applied
                if (!slideOut) x += width
            }
            Direction.RIGHT ->
            {
                x = width * applied
                if (!slideOut) x -= width
            }
            Direction.UP ->
            {
                y = height * applied
                if (!slideOut) y -= height
            }
            Direction.DOWN ->
            {
                y = -height * applied
                if (!slideOut) y += height
            }
        }
        val texBottom = if (slideOut) nextScreenTexture else currentScreenTexture
        val texTop = if (slideOut) currentScreenTexture else nextScreenTexture

        batch.begin()
        batch.draw(texBottom, 0f, 0f, 0f, 0f, width, height, 1f, 1f, 0f, 0, 0, width.toInt(), height.toInt(), false, true)
        batch.draw(texTop, x, y, 0f, 0f, nextScreenTexture.width.toFloat(), nextScreenTexture.height.toFloat(), 1f, 1f, 0f, 0, 0,
                nextScreenTexture.width, nextScreenTexture.height, false, true)
        batch.end()
    }

    override fun dispose()
    {
    }
}
