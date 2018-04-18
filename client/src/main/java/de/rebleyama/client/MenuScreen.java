package de.rebleyama.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

import java.util.logging.*;


public class MenuScreen extends AbstractScreen {

	public MenuScreen(Game game) {
		super(game);
	}

	@Override
	public void show() {
	
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		BitmapFont font = new BitmapFont();
		font.setColor(Color.RED);
		SpriteBatch batch = new SpriteBatch();
		batch.begin();
		font.draw(batch, "Press to start", 200, 200);
		batch.end();

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			game.setScreen(new GameScreen(game));
		}
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