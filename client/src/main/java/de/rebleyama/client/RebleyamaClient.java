package de.rebleyama.client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import de.rebleyama.client.MenuScreen;


public class RebleyamaClient extends Game implements InputProcessor {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;

    //Global Vars for UI
    private ClientUI clientUI;

    @Override
    public void create() {

        batch = new SpriteBatch();

        //get the window size for the camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //initiate the camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.update();
        camera.position.x = 10240;
        camera.position.y = 10240;

        //load the map
        //also available: ../client/assets/custommaps/testMap.tmx
        tiledMap = new TmxMapLoader().load("../client/assets/custommaps/default.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        
        Stage stage = new TiledMapStage(tiledMap);
        //create ui class
        clientUI = new ClientUI(tiledMap);

		//set Screen to Menu
		this.setScreen(new MenuScreen(this));

        //Creation of a Multiplexer which allows multi layer event handling (UI Layer and TiledMap Layer) (UI layer needs to be first ORDER IS IMPORTANT)
        InputMultiplexer inputMultiplexer = new InputMultiplexer(clientUI.getStage(), this, stage);
		Gdx.input.setInputProcessor(inputMultiplexer);

    }

    //start render method

  
    @Override
    public void render() {
        super.render();
		// Gdx.app.log("Render Logger", "logging from Rebleyama Client");
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.LEFT)
            camera.translate(-400, 0);
        if (keycode == Input.Keys.RIGHT)
            camera.translate(400, 0);
        if (keycode == Input.Keys.UP)
            camera.translate(0, -400);
        if (keycode == Input.Keys.DOWN)
            camera.translate(0, 400);
        if (keycode == Input.Keys.NUM_1)
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
