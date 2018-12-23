package org.neggly.libgdx.transitions

import com.badlogic.gdx.ApplicationListener
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.utils.viewport.FitViewport

open class MyGame(private val width: Float, private val height: Float) : ApplicationListener
{
    private lateinit var batch: SpriteBatch
    private val viewport = FitViewport(width, height)

    var screen: Screen?
        get() = currentScreen
        set(value)
        {
            if (transitionRunning)
            {
                Gdx.app.debug(MyGame::class.java.simpleName,
                        "Cannot change screen while transition in progress")
                value?.dispose()
                return
            }

            value?.show()
            value?.resize(Gdx.graphics.width, Gdx.graphics.height)

            if (currentScreen == null)
                currentScreen = value
            else
            {
                if (transition == null)
                {
                    currentScreen?.hide()
                    currentScreen = value
                }
                else
                {
                    nextScreen = value
                    currentScreen?.pause()
                    nextScreen?.pause()
                    transitionElapsed = 0f
                    transitionRunning = true
                }
            }
        }
    private var currentScreen: Screen? = null
    private var nextScreen: Screen? = null

    private lateinit var currentScreenFBO: FrameBuffer
    private lateinit var nextScreenFBO: FrameBuffer

    var transition: ScreenTransition? = null
        set(value)
        {
            if (transitionRunning)
            {
                Gdx.app.debug(MyGame::class.java.simpleName,
                        "Cannot change transition while transition in progress")
                value?.dispose()
                return
            }

            transition?.dispose()
            field = value
            transition?.run { transitionDuration = duration }
        }
    private var transitionDuration = 0f
    private var transitionElapsed = 0f
    var transitionRunning = false
        private set

    override fun create()
    {
        batch = SpriteBatch()
        viewport.camera.translate(width / 2, height / 2, 0f)

        currentScreenFBO = FrameBuffer(Pixmap.Format.RGBA8888,
                width.toInt(), height.toInt(), false)
        nextScreenFBO = FrameBuffer(Pixmap.Format.RGBA8888,
                width.toInt(), height.toInt(), false)
    }

    override fun render()
    {
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        val delta = Gdx.graphics.deltaTime

        // no other screen
        if (nextScreen == null)
            currentScreen?.render(delta)
        else
        {
            // transition is active and time limit reached
            if (transitionRunning && transitionElapsed >= transitionDuration)
            {
                currentScreen?.hide()
                currentScreen = nextScreen
                currentScreen?.resume()
                currentScreen?.render(delta)

                transitionRunning = false
                nextScreen = null
            }
            // transition is active
            else
            {
                if (transition != null)
                {
                    currentScreenFBO.begin()
                    currentScreen?.render(delta)
                    currentScreenFBO.end()

                    nextScreenFBO.begin()
                    nextScreen?.render(delta)
                    nextScreenFBO.end()

                    // restore viewport modified by FrameBuffer.begin()
                    viewport.update(Gdx.graphics.width, Gdx.graphics.height)

                    viewport.camera.update()
                    batch.projectionMatrix = viewport.camera.combined

                    val percent = transitionElapsed / transitionDuration
                    transition?.render(batch,
                            currentScreenFBO.colorBufferTexture,
                            nextScreenFBO.colorBufferTexture,
                            percent)

                    transitionElapsed += delta
                }
            }
        }
    }

    override fun pause()
    {
        currentScreen?.pause()
        nextScreen?.pause()
    }

    override fun resume()
    {
        currentScreen?.resume()
        nextScreen?.resume()
    }

    override fun resize(width: Int, height: Int)
    {
        currentScreen?.resize(width, height)
        nextScreen?.resize(width, height)

        viewport.update(width, height)
    }

    override fun dispose()
    {
        batch.dispose()

        currentScreen?.hide()
        nextScreen?.hide()

        currentScreenFBO.dispose()
        nextScreenFBO.dispose()

        transition?.dispose()
    }
}
