package de.rebleyama.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

public class MenuScreen implements Screen {

	public Game game;

	public MenuScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		//idk
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		BitmapFont font = new BitmapFont();
		font.setColor(Color.RED);
		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		font.draw(batch, "Press to start", 200, 200);
		batch.end();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.log("Render Logger", "Switching to GameScreen");
			game.setScreen(new GameScreen(game));
		}
		Gdx.app.log("Render Logger", "logging from menu screen");
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
	
	}
}