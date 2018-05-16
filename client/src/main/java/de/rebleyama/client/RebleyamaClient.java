package de.rebleyama.client;

import com.badlogic.gdx.*;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import de.rebleyama.client.utils.DataInfusedTileMap;
import de.rebleyama.client.utils.RebleyamaTmxFileLoader;
import de.rebleyama.client.ui.ClientUI;


public class RebleyamaClient extends Game implements InputProcessor {

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



    @Override
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

		//set Screen to Menu
		this.setScreen(new MenuScreen(this, clientUI, batch, tiledMap, tileMap, tiledMapRenderer, viewport, tiledMapStage, worldViewport, tileCamera));

        //Creation of a Multiplexer which allows multi layer event handling (UI Layer and TiledMap Layer) (UI layer needs to be first ORDER IS IMPORTANT)
        InputMultiplexer inputMultiplexer = new InputMultiplexer(clientUI.getStage(),this, tiledMapStage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        clientUI.startcalcThread();
    }

    //start render method
    @Override
    public void render() {
        super.render();
    }

    //start event methods

    
    /**
     * Stub method for recognizing keypress
     * This triggers when any key is pressed down
     *
     * @param keycode keycode of the key that was pressed down
     */
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    /**
     * Stub method for recognizing keypress
     * This triggers when any key is released
     *
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
     *
     * @param amount amount that the scrollwheel was moved
     */
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    /**
     * dispose of all the native resources we allocated in create()
     */
    @Override
    public void dispose() {
    }
}
