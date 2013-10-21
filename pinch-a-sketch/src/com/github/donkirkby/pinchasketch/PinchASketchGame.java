package com.github.donkirkby.pinchasketch;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

public class PinchASketchGame implements ApplicationListener {
    private Stage stage;
    private FrameBuffer frameBuffer;
    private TextureRegion region;
    private Image image;
    private ShapeRenderer shapes;
    private Vector2 lastPosition;
    private Vector2 tweezerOffset;
    private boolean isTweezerShown;

    @Override
    public void create() {		
        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        boolean keepAspectRatio = true;
        stage = new Stage(width, height, keepAspectRatio);
        Gdx.input.setInputProcessor(stage);

        /* We're going to draw to the FrameBuffer, which holds the contents
         * for the region, which is positioned by the Image, which is controlled
         * by the Stage.
         */
        frameBuffer = new FrameBuffer(Format.RGB565, width, height, false);
        region = new TextureRegion(frameBuffer.getColorBufferTexture());
        region.flip(false, true);
        image = new Image(region);
        stage.addActor(image);
        
        tweezerOffset = new Vector2(-50, 10);
        lastPosition = new Vector2();
        isTweezerShown = false;

        frameBuffer.begin();
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        frameBuffer.end();

        float halfTapSquareSize = 5;
        float tapCountInterval = 0.4f;
        float longPressDuration = 1.1f;
        float maxFlingDelay = 0.15f;
        image.addListener(new ActorGestureListener(
                halfTapSquareSize, 
                tapCountInterval, 
                longPressDuration, 
                maxFlingDelay) {

            @Override
            public void pan(
                    InputEvent event, 
                    float x, 
                    float y,
                    float deltaX, 
                    float deltaY) {
                addLine(x, y, deltaX, deltaY);
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y,
                    int pointer, int button) {
                isTweezerShown = false;
            }
        });


        shapes = new ShapeRenderer();
    }

    private void addLine(float x, float y, float deltaX, float deltaY) {
        frameBuffer.begin();
        shapes.begin(ShapeType.Line);
        shapes.setColor(Color.BLACK);
        shapes.line(
                x + tweezerOffset.x - deltaX, 
                y + tweezerOffset.y - deltaY, 
                x + tweezerOffset.x, 
                y + tweezerOffset.y);
        shapes.end();
        frameBuffer.end();
        lastPosition.set(x, y);
        isTweezerShown = true;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void render() {		
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
        if (isTweezerShown) {
            shapes.begin(ShapeType.Line);
            shapes.line(
                    lastPosition.x, 
                    lastPosition.y, 
                    lastPosition.x + tweezerOffset.x, 
                    lastPosition.y + tweezerOffset.y);
            shapes.end();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
