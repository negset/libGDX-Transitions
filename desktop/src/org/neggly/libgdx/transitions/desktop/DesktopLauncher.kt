package org.neggly.libgdx.transitions.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import org.neggly.libgdx.transitions.TransitionTest

object DesktopLauncher
{
    @JvmStatic
    fun main(arg: Array<String>)
    {
        val config = LwjglApplicationConfiguration()
        config.apply {
            resizable = true
        }
        LwjglApplication(TransitionTest(), config)
    }
}
