package de.rebleyama.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.Align;


public class MenuScreen implements Screen {

	public Game game;
	Stage stage;
    TextButton startButton;
	TextButton exitButton;
	TextButton aboutButton;
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
		startButton = new TextButton("Start", skin);
		startButton.setPosition(350, 225);

		exitButton = new TextButton("Exit", skin);
		exitButton.setPosition(355, 150);

		aboutButton = new TextButton("About", skin);
		aboutButton.setPosition(350, 300);
		
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		startButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.log("Render Logger", "Switching to GameScreen");
				game.setScreen(new GameScreen(game));
			}
		});

		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});

		aboutButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event,, float x, float y) {
				Gdx.app.log("Render Logger", "Showing about dialogue");
				createAbout();
				aboutWindow.setVisible(!aboutWindow.isVisible());
                if (aboutWindow.isVisible()) {
                    aboutWindow.toFront();
                }
			}
		})

		stage.addActor(startButton);
		stage.addActor(exitButton);
		stage.addActor(aboutButton);
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

	private void createAbout() {
		//create window
		aboutWindow = new Window("About", skin);
		createXButton(aboutWindow);
       	
		//create Image and sets size
		Texture texture = new Texture(Gdx.files.internal("assets/textures/logo/titlebannerFullRes.jpg"));
		Image image1 = new Image(texture);
		image1.setSize(texture.getWidth()/3,texture.getHeight()/2);
       	
		//label with titel
		Label titel = new Label("Rebleyama Version 0.01", skin);
		titel.setAlignment(Align.center);
       	
		//labels with short description
		Label description = new Label("A simple strategic Open Source Game", skin);
		description.setAlignment(Align.center);
       	
		//label with disclaimer
		Label disclaimer = new Label("Rebleyama is a non-commercial product and was created \n  as part of a student project at the DHBW Stuttgart", skin);
		disclaimer.setAlignment(Align.center);
		disclaimer.setWrap(true);
    
		//label with copyright
		Label copyright = new Label("Copyright © 2018 Licensed under GNU General Public License v3.0", skin);
		copyright.setAlignment(Align.center);
       	
		//label with contributors
		Label contributors = new Label("Contributors:             Jan-Robin Aumann, Ivan Bogicevic, Paul Thore Flachbart, \n                                    Lennard Oswald Purucker, Alexander Tobias Marstaller, \n                                    Johanna Sommer, Thore Kruess , Alex Schaefer, Cathleen \n                                    Schmalfuss, Dorian Czichotzki, Daniel Rutz", skin);
		contributors.setAlignment(Align.left); 
		contributors.setWrap(true);
       	
		//label for the license
		Label license = new Label("License:                     https://www.gnu.org/licenses/gpl-3.0.en.html", skin);
		license.setAlignment(Align.left);
		license.addListener(new InputListener(){
				public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
					Gdx.net.openURI("https://www.gnu.org/licenses/gpl-3.0.en.html");
					return true;
				}   
			});
       	
		//label for github page
		Label github = new Label("Github-Project:         https://github.com/dhbw-stginf16a/Rebleyama", skin);
		github.setAlignment(Align.left);
		github.addListener(new InputListener(){
				public boolean touchDown(InputEvent event, float x, float y,int pointer, int button) {
					Gdx.net.openURI("https://github.com/dhbw-stginf16a/Rebleyama");
					return true;
				}   
			});
       	
		//label for libraries
		Label libraries = new Label("Used Libraries:         libGDX", skin);
		libraries.setAlignment(Align.left);
       	
		//creates Table, adds Labels and image and partly sets the size
		Table table = new Table();
		table.add(image1).height((float) (Gdx.graphics.getHeight() * 0.2)).width((float) (Gdx.graphics.getWidth() * 0.3));
		table.row();
		table.add(titel);
		table.row();
		table.add(description);
		table.row();
		table.add(disclaimer).width((float) (Gdx.graphics.getWidth() * 0.35));
		table.row();
		table.add(copyright).width((float) (Gdx.graphics.getWidth() * 0.4));
		table.row();
		table.add(contributors).width((float) (Gdx.graphics.getWidth() * 0.45));
		table.row();
		table.add(license).width((float) (Gdx.graphics.getWidth() * 0.45));
		table.row();
		table.add(github).width((float) (Gdx.graphics.getWidth() * 0.45));
		table.row();
		table.add(libraries).width((float) (Gdx.graphics.getWidth() * 0.45));
		table.row();
       	
		//fill inside of window with table
		aboutWindow.add(table);

		//set size of window
		aboutWindow.setSize((float) (Gdx.graphics.getWidth() * 0.5), (float) (Gdx.graphics.getHeight() * 0.65));

		//set position of window in the middle
		aboutWindow.setPosition((float) (Gdx.graphics.getWidth() / 2.0) - aboutWindow.getWidth() / 2, (float) (Gdx.graphics.getHeight() / 2.0) - aboutWindow.getHeight() / 2);
           
		//add aboutWindow to ui stage
		stage.addActor(aboutWindow);
       	
		//hides Window on start
		aboutWindow.setVisible(false);
    }

	private void createXButton(Window window) {
        //create button
        final TextButton buttonX = new TextButton("X", skin);
        //ad butto to top bar of window
        window.getTitleTable().add(buttonX).height(window.getPadTop());
        //setup event listener for X button
        buttonX.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                window.setVisible(false);
            }
        });
    }
}