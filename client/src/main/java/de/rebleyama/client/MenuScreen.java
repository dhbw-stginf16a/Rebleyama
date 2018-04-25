package de.rebleyama.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.files.FileHandle;


public class MenuScreen implements Screen {

	public Game game;
	Stage stage;
    TextButton button;
    TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
    TextureAtlas buttonAtlas;

	public MenuScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
		//idk
	}

	@Override
	public void render(float delta) {
		stage = new Stage();
        Gdx.input.setInputProcessor(stage);
		font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("assets/vis/skin/x2/uiskin.json"));
        button = new TextButton("Start", skin);
        stage.addActor(button);

		stage.draw();

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.log("Render Logger", "Switching to GameScreen");
			game.setScreen(new GameScreen(game));
		}
		
		// Gdx.app.log("Render Logger", "logging from menu screen");
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