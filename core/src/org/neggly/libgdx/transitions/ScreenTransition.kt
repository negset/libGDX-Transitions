package org.neggly.libgdx.transitions

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch

interface ScreenTransition
{
    val duration: Float

    fun render(batch: Batch, currentScreenTexture: Texture, nextScreenTexture: Texture, percent: Float)

    fun dispose()
}
