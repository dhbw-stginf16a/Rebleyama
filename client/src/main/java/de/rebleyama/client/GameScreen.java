package de.rebleyama.client;

import org.w3c.dom.views.AbstractView;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import java.util.logging.*;
import de.rebleyama.client.utils.DataInfusedTileMap;
import de.rebleyama.client.utils.RebleyamaTmxFileLoader;
import de.rebleyama.client.ui.ClientUI;


public class GameScreen extends Game implements Screen, InputProcessor {
	private static final int TILESIZE = 40;
    private static final int MAPSIZE_TILES = 512;
    private static final int MAPSIZE_PIXELS = TILESIZE * MAPSIZE_TILES;
    private static final String COORDINATE_LOGGER = "Coordinate Logger";
	 private static final int VIRTUAL_WIDTH = 640;
    private static final int VIRTUAL_HEIGHT = 360;
    private static final float ASPECT_RATIO = 16f / 9f;

    //other resolutions: 1024x576, 1280x720, 1408x792, 1920x1080, 2560x1440, 3200x1800, 3840x2160

    private SpriteBatch batch;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;
    private DataInfusedTileMap tileMap;
    private Rectangle viewport;
    private Stage tiledMapStage;
    private FitViewport worldViewport;
    private OrthographicCamera tileCamera;

    //Global Vars for UI
    private ClientUI clientUI;
    private int postint;

	public Game game;

	public GameScreen(Game game, ClientUI clientUI, SpriteBatch batch, TiledMap tiledMap, DataInfusedTileMap  tileMap, TiledMapRenderer tiledMapRenderer, Rectangle viewport, Stage tiledMapStage, FitViewport worldViewport, OrthographicCamera tileCamera) {
		this.game = game;
		this.clientUI = clientUI;
		this.batch = batch;
		this.tiledMapRenderer = tiledMapRenderer;
		this.tiledMap = tiledMap;
		this.tileMap = tileMap;
		this.viewport = viewport;
		this.tiledMapStage = tiledMapStage;
		this.worldViewport = worldViewport;
		this.tileCamera = tileCamera;
	}

	public void create() {
        postint = 0;
        //load the map
        //also available: ../client/assets/custommaps/testMap.tmx
        tiledMap = new RebleyamaTmxFileLoader().load("../client/assets/custommaps/512x512TestMap.tmx");
        tileMap = new DataInfusedTileMap(tiledMap);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tileMap.getTiledMap());
        tiledMapStage = new TileMapStage(tileMap, worldViewport);
        tileCamera = ((OrthographicCamera) tiledMapStage.getCamera());

        batch = new SpriteBatch();
        worldViewport = new FitViewport(MAPSIZE_PIXELS, MAPSIZE_PIXELS, tileCamera);

        //create ui class
        clientUI = new ClientUI(tileMap, Gdx.app,tileCamera);

        //Creation of a Multiplexer which allows multi layer event handling (UI Layer and TiledMap Layer) (UI layer needs to be first ORDER IS IMPORTANT)
        InputMultiplexer inputMultiplexer = new InputMultiplexer(clientUI.getStage(),this, tiledMapStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        clientUI.startcalcThread();


    }

	@Override
	public void show() {
		//set camera positions to be at the middle of the map at startup
        tileCamera.position.x = MAPSIZE_PIXELS / 2;
        tileCamera.position.y = MAPSIZE_PIXELS / 2;
		Gdx.app.log("Render Logger", "logging from game screen");
	}



	@Override
	public void render(float delta) {
		//prevent tileCamera from going out of bounds
        stayInBounds();

        //handle Input
        handleKeyMovementInput();
        handleKeyZoomInput();
        handleMouseMovementInput();

        // update tileCamera
        tileCamera.update();

        // set viewport
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y, (int) viewport.width, (int) viewport.height);

        // clear previous frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //start up map renderer
        tiledMapRenderer.render();
        tiledMapStage.act(Gdx.graphics.getDeltaTime());
        tiledMapRenderer.setView(tileCamera);
        tiledMapStage.draw();

        // UI Render Part
        clientUI.getStage().act(Gdx.graphics.getDeltaTime());
        clientUI.getStage().draw();

        //post-initialize
        if (postint == 0) {
            tileCamera.position.set(10240, 10240, 1);
            postint++;
        }
	}

	@Override
	public void resize(int height, int width) {
		clientUI.stageResize(width, height);

		// calculate new viewport
        float aspectRatio = (float) width / (float) height;
        float scale;
        Vector2 crop = new Vector2(0f, 0f);
        if (aspectRatio > ASPECT_RATIO) {
            scale = (float) height / (float) VIRTUAL_HEIGHT;
            crop.x = (width - VIRTUAL_WIDTH * scale) / 2f;
        } else if (aspectRatio < ASPECT_RATIO) {
            scale = (float) width / (float) VIRTUAL_WIDTH;
            crop.y = (height - VIRTUAL_HEIGHT * scale) / 2f;
        } else {
            scale = (float) width / (float) VIRTUAL_WIDTH;
        }

        float w = (float) VIRTUAL_WIDTH * scale;
        float h = (float) VIRTUAL_HEIGHT * scale;
		Gdx.app.log("error finding", "before viewport");
        viewport = new Rectangle(crop.x, crop.y, w, h);
		Gdx.app.log("error finding", "after viewport");

		if (tiledMapStage == null) {
			Gdx.app.log("error finding", "tiledMapStage is null");
		}

		if (tiledMapStage.getViewport() == null) {
			Gdx.app.log("error finding", "tiledMapStage is null");
		}

		tiledMapStage.getViewport().update((int) w, (int) h, true);
		Gdx.app.log("error finding", "after getViewport()");
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
		clientUI.endcalcThread();
        tiledMap.dispose();
        batch.dispose();
        clientUI.dispose();
	}

	/**
     * Stub method for recognizing keypress
     * This triggers when any key is pressed down
     *
     * @param keycode keycode of the key that was pressed down
     */
    @Override
    public boolean keyDown(int keycode) {
        //If ESC is pressed, show Menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            clientUI.uiKeypressed("escMenuWindow");
            return true;
        }
        //If m is pressed, show Map
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            clientUI.uiKeypressed("mapWindow");
            return true;
        }
        return false;
    }

    /**
     * Stub method for recognizing keypress
     * This triggers when any key is released
     * @param keycode keycode of the key that was released
     */
    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    /**
     * Stub method for recognizing keypress
     * This triggers when any key is typed (pressed and released)
     *
     * @param character character that was typed
     */
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

/**
 * Stub method for recognizing keypress
 * This triggers when screen is touched, also the method that handles mouse input
 */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //If left mouse button is clicked
        if (button == Input.Buttons.LEFT) {
            onLeftMouseDown(screenX, screenY);
        }
        return false;
    }

    /**
     * Stub method for recognizing keypress
     * This triggers when screen is released
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    /**
     * Stub method for recognizing keypress
     * This triggers when the finger is dragged across the screen
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    /**
     * Stub method for recognizing mouse movement
     * This triggers when the mouse is moved
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    /**
     * Stub method for recognizing keypress
     * This triggers when the scrollwheel is scrolled
     * @param amount amount that the scrollwheel was moved
     */
    @Override
    public boolean scrolled(int amount) {
        /*check if the area shown on screen is still within the minimum (480 px -> 12 fields) and the
        maximum (20480 px -> 512 fields) bounds. If it is not, camera is not zoomed unless the zoom amount
        is going to change the effective area shown away from the bound
        */
        if ((((tileCamera.viewportWidth * (tileCamera.zoom + 0.3 * amount)) < MAPSIZE_PIXELS) || amount < 0)
                && (((tileCamera.viewportWidth * (tileCamera.zoom + 0.3 * amount)) > 480) || amount > 0)) {
            tileCamera.zoom += (0.3 * amount);
            return true;
        }

        return false;
    }

	    /**
     * Handles Key Input for Movement
     */
    private void handleKeyMovementInput() {
        //if key is pressed, move tileCamera accordingly
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            tileCamera.translate(-25, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            tileCamera.translate(25, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            tileCamera.translate(0, -25);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            tileCamera.translate(0, 25);
        }
    }

    /**
     * Handles Mouse Input for Movement
     */
    private void handleMouseMovementInput() {
        float mousePositionX = Gdx.input.getX();
        float mousePositionY = Gdx.input.getY();
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();

        //if mouse is in a 5% range of any of the edges of the window, move the tileCamera accordingly
        if (mousePositionY <= windowHeight / 20) {
            tileCamera.translate(0, 25 * tileCamera.zoom);
        }
        if (mousePositionY >= windowHeight - windowHeight / 20) {
            tileCamera.translate(0, -25 * tileCamera.zoom);
        }
        if (mousePositionX >= windowWidth - windowWidth / 20) {
            tileCamera.translate(25 * tileCamera.zoom, 0);
        }
        if (mousePositionX <= windowWidth / 20) {
            tileCamera.translate(-25 * tileCamera.zoom, 0);
        }
    }

    /**
     * Handles Key Input for Zoom
     */
    private void handleKeyZoomInput() {
        /*check if the area shown on screen is still within the minimum (480 px -> 12 fields) and the
        maximum (20480 px -> 512 fields) bounds. If it is not, tileCamera is not zoomed unless the zoom amount
        is going to change the effective area shown away from the bound
        */
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS)
                && ((tileCamera.viewportWidth * (tileCamera.zoom + 0.1)) > 480)) {
            tileCamera.zoom -= 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS)
                && ((tileCamera.viewportWidth * (tileCamera.zoom + 0.1)) < MAPSIZE_PIXELS)) {
            tileCamera.zoom += 0.1;
        }
    }

    /**
     * Handles Camera to stay in bounds
     */
    private void stayInBounds() {
        //calculate the effective area of the map that is shown on screen
        float effectiveViewportWidth = tileCamera.viewportWidth * tileCamera.zoom;
        float effectiveViewportHeight = tileCamera.viewportHeight * tileCamera.zoom;

        /*Check if tileCamera position is at any coordinate out of the boundaries of our 512x512 tile map.
        If it is, set it to the corresponding edge. Since this method is called in the render() method,
        this is checked for every frame.
        */
        if (tileCamera.position.x <= 0 + effectiveViewportWidth / 2) {
            tileCamera.position.x = 0 + effectiveViewportWidth / 2;
        }
        if (tileCamera.position.x >= MAPSIZE_PIXELS - effectiveViewportWidth / 2) {
            tileCamera.position.x = MAPSIZE_PIXELS - effectiveViewportWidth / 2;
        }
        if (tileCamera.position.y <= 0 + effectiveViewportHeight / 2) {
            tileCamera.position.y = 0 + effectiveViewportHeight / 2;
        }
        if (tileCamera.position.y >= MAPSIZE_PIXELS - effectiveViewportHeight / 2) {
            tileCamera.position.y = MAPSIZE_PIXELS - effectiveViewportHeight / 2;
        }
    }

    private void onLeftMouseDown(int mousePositionX, int mousePositionY) {
        Gdx.app.log(COORDINATE_LOGGER, "tileCamera Tile (X): " + tileCamera.position.x / 40);
        Gdx.app.log(COORDINATE_LOGGER, "Mouse (X): " + mousePositionX);
        Gdx.app.log(COORDINATE_LOGGER, "tileCamera Tile (Y): " + tileCamera.position.y / 40);
        Gdx.app.log(COORDINATE_LOGGER, "Mouse (Y): " + mousePositionY);
    }
}