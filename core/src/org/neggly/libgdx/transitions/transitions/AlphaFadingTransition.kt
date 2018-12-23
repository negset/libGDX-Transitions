package org.neggly.libgdx.transitions.transitions

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import org.neggly.libgdx.transitions.ScreenTransition

class AlphaFadingTransition(override val duration: Float = 2f) : ScreenTransition
{
    override fun render(batch: Batch, currentScreenTexture: Texture, nextScreenTexture: Texture, percent: Float)
    {
        val width = currentScreenTexture.width
        val height = currentScreenTexture.height
        val applied = Interpolation.fade.apply(percent)

        batch.begin()
        batch.setColor(1f, 1f, 1f, 1f)
        batch.draw(currentScreenTexture, 0f, 0f, 0f, 0f, width.toFloat(), height.toFloat(),
                1f, 1f, 0f, 0, 0, width, height, false, true)
        batch.setColor(1f, 1f, 1f, applied)
        batch.draw(nextScreenTexture, 0f, 0f, 0f, 0f, width.toFloat(), height.toFloat(),
                1f, 1f, 0f, 0, 0, width, height, false, true)
        batch.end()
    }

    override fun dispose()
    {
    }
}
