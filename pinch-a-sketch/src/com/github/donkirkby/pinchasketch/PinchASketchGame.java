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
    private int radius = 128;
	
	@Override
	public void create() {		
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
	    int tempWidth = 512;
	    int tempHeight = 256;
		
        boolean keepAspectRatio = true;
        stage = new Stage(tempWidth, tempHeight, keepAspectRatio);
        Gdx.input.setInputProcessor(stage);

        /* We're going to draw to the FrameBuffer, which holds the contents
         * for the region, which is positioned by the Image, which is controlled
         * by the Stage.
         */
        frameBuffer = new FrameBuffer(Format.RGB565, tempWidth, tempHeight, false);
        region = new TextureRegion(frameBuffer.getColorBufferTexture());
        region.flip(false, true);
        image = new Image(region);
        stage.addActor(image);
        
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
            public void tap(InputEvent event, float x, float y,
                    int count, int button) {
                addCircle();
            }
        });
		
        
        shapes = new ShapeRenderer();
	}

	/**
	 * Just a simple demonstration of how to draw to the frameBuffer.
	 */
    private void addCircle() {
        frameBuffer.begin();
        shapes.begin(ShapeType.Circle);
        shapes.setColor(Color.RED);
        shapes.circle(128, 128, radius);
        shapes.end();
        frameBuffer.end();
        
        radius /= 2;
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
