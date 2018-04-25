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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;


public class MenuScreen implements Screen {

	public Game game;
	Stage stage;
    TextButton button;
    TextButtonStyle textButtonStyle;
    BitmapFont font;
    Skin skin;
	TextureAtlas buttonAtlas;
	boolean created = false;

	public MenuScreen(Game game) {
		this.game = game;
	}

	@Override
	public void show() {
	}

	public void create() {
		Gdx.app.log("bla", "creating 2");

		//font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("assets/vis/skin/x2/uiskin.json"));
		button = new TextButton("Start", skin);
		button.setPosition(350, 225);
		
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		button.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("Render Logger", "Switching to GameScreen");
				game.setScreen(new GameScreen(game));
			}
		});

		stage.addActor(button);
	}

	@Override
	public void render(float delta) {
		if (!created) {
			Gdx.app.log("bla", "creating");
			create();
			created = true;			
		}

		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int height, int width) {
		// stage.getViewport().update(width, height, true);
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
		stage.dispose();
	}
}