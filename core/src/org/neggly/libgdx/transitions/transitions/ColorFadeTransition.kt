package org.neggly.libgdx.transitions.transitions

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.math.Interpolation
import org.neggly.libgdx.transitions.ScreenTransition

class ColorFadeTransition(color: Color,
                          private val interpolation: Interpolation,
                          override val duration: Float = 2f) : ScreenTransition
{
    private val color = Color(Color.WHITE)
    private val texture: Texture

    init
    {
        val pixmap = Pixmap(1, 1, Format.RGBA8888).apply {
            setColor(color)
            fill()
        }
        texture = Texture(pixmap)
        pixmap.dispose()
    }

    override fun render(batch: Batch, currentScreenTexture: Texture, nextScreenTexture: Texture, percent: Float)
    {
        val applied = interpolation.apply(percent)
        val width = currentScreenTexture.width.toFloat()
        val height = currentScreenTexture.height.toFloat()

        batch.begin()

        var fade = applied * 2

        if (fade > 1.0f)
        {
            fade = 1.0f - (applied * 2 - 1.0f)
            color.a = 1.0f - fade
            batch.color = color
            batch.draw(nextScreenTexture, 0f, 0f, width / 2, height / 2,
                    width, height, 1f, 1f, 0f, 0, 0, width.toInt(), height.toInt(),
                    false, true)
        }
        else
        {
            color.a = 1.0f - fade
            batch.color = color
            batch.draw(currentScreenTexture, 0f, 0f, width / 2, height / 2,
                    width, height, 1f, 1f, 0f, 0, 0, width.toInt(), height.toInt(),
                    false, true)
        }

        color.a = fade
        batch.color = color
        batch.draw(texture, 0f, 0f, width, height)
        batch.end()
        batch.color = Color.WHITE
    }

    override fun dispose()
    {
        texture.dispose()
    }
}
