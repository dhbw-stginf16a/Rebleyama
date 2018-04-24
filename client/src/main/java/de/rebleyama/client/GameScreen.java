package de.rebleyama.client;

import org.w3c.dom.views.AbstractView;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ApplicationAdapter;
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

public class GameScreen implements Screen {
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;

	public Game game;

	public GameScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
	
	}

	@Override
	public void render(float delta) {
		//clear background
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl20.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		RebleyamaClient.batch.setProjectionMatrix(RebleyamaClient.camera.combined);

		//update the camera
        RebleyamaClient.camera.update();
		
		//start up map renderer
		RebleyamaClient.tiledMapRenderer.setView(RebleyamaClient.camera);
		RebleyamaClient.tiledMapRenderer.render();
		
		Gdx.app.log("Render Logger", "logging from game screen");
	}

	@Override
	public void resize(int height, int width) {
	
	}

	@Override
	public void pause() {
	
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
	
	}

	@Override
	public void dispose() {
		// dispose of all the native resources
	}
}