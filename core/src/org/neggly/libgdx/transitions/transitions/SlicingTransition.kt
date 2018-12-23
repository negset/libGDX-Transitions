package org.neggly.libgdx.transitions.transitions

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.utils.Array
import org.neggly.libgdx.transitions.ScreenTransition

class SlicingTransition(private val direction: Direction,
                        numSlices: Int,
                        private val interpolation: Interpolation,
                        override val duration: Float = 2f) : ScreenTransition
{
    private val slices = Array<Int>()

    enum class Direction
    {
        UP, DOWN, UP_DOWN
    }

    init
    {
        slices.clear()
        for (i in 0 until numSlices)
            slices.add(i)
        slices.shuffle()
    }

    override fun render(batch: Batch, currentScreenTexture: Texture, nextScreenTexture: Texture, percent: Float)
    {
        val applied = interpolation.apply(percent)
        val width = currentScreenTexture.width.toFloat()
        val height = currentScreenTexture.height.toFloat()
        val sliceWidth = (width / slices.size).toInt()

        batch.begin()
        batch.draw(currentScreenTexture, 0f, 0f, 0f, 0f, width, height,
                1f, 1f, 0f, 0, 0, width.toInt(), height.toInt(), false, true)
        for (i in 0 until slices.size)
        {
            val x = (i * sliceWidth).toFloat()

            val offsetY = height * (1 + slices.get(i) / slices.size.toFloat())
            val y = when (direction)
            {
                Direction.UP -> -offsetY + offsetY * applied
                Direction.DOWN -> offsetY - offsetY * applied
                Direction.UP_DOWN ->
                    if (i % 2 == 0) -offsetY + offsetY * applied
                    else offsetY - offsetY * applied
            }
            batch.draw(nextScreenTexture, x, y, 0f, 0f, sliceWidth.toFloat(), height,
                    1f, 1f, 0f, i * sliceWidth, 0, sliceWidth, height.toInt(), false, true)
        }
        batch.end()
    }

    override fun dispose()
    {
    }
}
