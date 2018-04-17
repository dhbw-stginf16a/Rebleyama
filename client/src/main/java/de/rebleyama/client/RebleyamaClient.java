package de.rebleyama.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.util.logging.*;

public class RebleyamaClient extends Game implements InputProcessor{
	
	static SpriteBatch batch;
	static OrthographicCamera camera;
	static TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;

	@Override
	public void create () {
		setScreen(new MenuScreen(this));
		batch = new SpriteBatch();

		//get the window size for the camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		//initiate the camera
		camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
		camera.update();
		
        //load the map
        //also available: ../client/assets/custommaps/testMap.tmx
        tiledMap = new TmxMapLoader().load("../client/assets/custommaps/default.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		setScreen(new MenuScreen(this));
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
		
	}

	@Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.LEFT)
            camera.translate(-400,0);
        if(keycode == Input.Keys.RIGHT)
            camera.translate(400,0);
        if(keycode == Input.Keys.UP)
            camera.translate(0,-400);
        if(keycode == Input.Keys.DOWN)
            camera.translate(0,400);
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
}
