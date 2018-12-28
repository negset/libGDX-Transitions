package org.neggly.libgdx.transitions.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.FitViewport
import org.neggly.libgdx.transitions.HEIGHT
import org.neggly.libgdx.transitions.TransitionTest
import org.neggly.libgdx.transitions.WIDTH

class FirstScreen(private val game: TransitionTest) : ScreenAdapter()
{
    private val tag = FirstScreen::class.java.simpleName

    private val stage = Stage(FitViewport(WIDTH, HEIGHT))
    private var paused = false

    private val texture = Texture("bg01.jpg")

    init
    {
        Gdx.input.inputProcessor = stage

        stage.addActor(Image(texture))
    }

    override fun render(delta: Float)
    {
        stage.draw()

        if (!paused)
        {
            stage.act(delta)
        }

        when
        {
            Gdx.input.isKeyJustPressed(Input.Keys.ENTER)
                    || Gdx.input.justTouched() ->
                if (!game.transitionRunning)
                    game.screen = SecondScreen(game)
                else
                    Gdx.app.debug(tag, "Cannot change screen while transition in progress")

            Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)
                    || Gdx.input.isKeyJustPressed(Input.Keys.BACK) ->
                if (!game.transitionRunning)
                    game.switchTransition()
                else
                    Gdx.app.debug(tag, "Cannot change transition while transition in progress")
        }
    }

    override fun resize(width: Int, height: Int)
    {
        stage.viewport.update(width, height)
    }

    override fun pause()
    {
        paused = true
    }

    override fun resume()
    {
        paused = false
    }

    override fun hide()
    {
        dispose()
    }

    override fun dispose()
    {
        stage.dispose()
        texture.dispose()
    }
}
