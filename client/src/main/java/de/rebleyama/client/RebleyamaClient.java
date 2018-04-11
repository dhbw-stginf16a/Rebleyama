package de.rebleyama.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.util.logging.*;

public class RebleyamaClient extends ApplicationAdapter implements InputProcessor{
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;

	@Override
	public void create () {

		batch = new SpriteBatch();

		//get the window size for the camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		//initiate the camera
		camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
		camera.update();
        camera.position.x = 10240;
        camera.position.y = 10240;


        //load the map
        //also available: ../client/assets/custommaps/testMap.tmx
        tiledMap = new TmxMapLoader().load("../client/assets/custommaps/default.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {

        //handle Input
        handleInput();
		//clear background
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);

		//update the camera
        camera.update();
		
		//start up map renderer

        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

	}

	@Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-250,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(250,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,250);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0,-250);
        if(keycode == Input.Keys.EQUALS)
            camera.zoom -= 0.02;
        if(keycode == Input.Keys.MINUS)
            camera.zoom += 0.02;
        if(keycode == Input.Keys.NUM_1)
            tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

	@Override
	public void dispose() {
		// dispose of all the native resources
    }
    
    private void handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.zoom += 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            camera.zoom -= 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            camera.translate(-25,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            camera.translate(25,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0,-25);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0,25);
        }
    }
}
