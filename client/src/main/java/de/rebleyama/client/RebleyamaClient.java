package de.rebleyama.client;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class RebleyamaClient extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;

    //Global Vars for UI
    private ClientUI clientUI;

    //start create method
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

        //create ui class
        clientUI = new ClientUI(tiledMap, Gdx.app);

        //Creation of a Multiplexer which allows multi layer event handling (UI Layer and TiledMap Layer) (UI layer needs to be first ORDER IS IMPORTANT)
        InputMultiplexer im = new InputMultiplexer(clientUI.getStage(), this);
        Gdx.input.setInputProcessor(im);

    }

    //start render method
    @Override
    public void render() {
        //prevent camera from going out of bounds
        stayInBounds();
        //handle Input
        handleKeyMovementInput();
        handleKeyZoomInput();
        handleMouseMovementInput();

        //clear background
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        //update the camera
        camera.update();

        //start up map renderer
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();

        // UI Render Part
        clientUI.getStage().act(Gdx.graphics.getDeltaTime());
        clientUI.getStage().draw();
    }

    //start event methods

    /**
     * Resize event which triggers on size change of client window
     *
     * @param width  Width after resize has happened
     * @param height height after resize has happened
     */
    @Override
    public void resize(int width, int height) {
        clientUI.stageResize(width, height);
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
Gdx.app.exit();
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
     * This triggers when the mouse is moved
     */
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
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
     * This triggers when screen is touched
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
     * Stub method for recognizing keypress
     * This triggers when the scrollwheel is scrolled
     *
     * @param amount amount that the scrollwheel was moved
     */
    @Override
    public boolean scrolled(int amount) {
        /*calculate the effective area of the map that is shown on screen (only calculating width
        because it will always be larger than height as soon as we enforce 16:9 aspect ration)*/
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;

        /*check if the area shown on screen is still within the minimum (480 px -> 12 fields) and the
        maximum (20480 px -> 512 fields) bounds. If it is not, camera is not zoomed unless the zoom amount
        is going to change the effective area shown away from the bound
        */
        if (((effectiveViewportWidth < 20480) || amount < 0) && ((effectiveViewportWidth > 480) || amount > 0)) {
            camera.zoom += (0.3 * amount);
            return true;
        }

        return false;
    }

    /**
     * dispose of all the native resources we allocated in create()
     */
    @Override
    public void dispose() {
        tiledMap.dispose();
        batch.dispose();
        clientUI.dispose();
    }

    /**
     * Allows us to call app exit from every other class more easily
     */
    public void exitApp(){
        Gdx.app.exit();
    }
    //start own input handling methods

    /**
     * Handles Key Input for Movement
     */
    private void handleKeyMovementInput() {
        //if key is pressed, move camera accordingly
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-25, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(25, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0, -25);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0, 25);
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

        //if mouse is in a 5% range of any of the edges of the window, move the camera accordingly
        if (mousePositionY <= windowHeight / 20) {
            camera.translate(0, 25 * camera.zoom);
        }
        if (mousePositionY >= windowHeight - windowHeight / 20) {
            camera.translate(0, -25 * camera.zoom);
        }
        if (mousePositionX >= windowWidth - windowWidth / 20) {
            camera.translate(25 * camera.zoom, 0);
        }
        if (mousePositionX <= windowWidth / 20) {
            camera.translate(-25 * camera.zoom, 0);
        }
    }

    /**
     * Handles Key Input for Zoom
     */
    private void handleKeyZoomInput() {
        /*calculate the effective area of the map that is shown on screen (only calculating width
        because it will always be larger than height as soon as we enforce 16:9 aspect ration)*/
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;

        /*check if the area shown on screen is still within the minimum (480 px -> 12 fields) and the
        maximum (20480 px -> 512 fields) bounds. If it is not, camera is not zoomed unless the zoom amount
        is going to change the effective area shown away from the bound
        */
        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) && (effectiveViewportWidth > 480)) {
            camera.zoom -= 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS) && (effectiveViewportWidth < 20480)) {
            camera.zoom += 0.1;
        }
    }

    /**
     * Handles Camera to stay in bounds
     */
    private void stayInBounds() {
        //calculate the effective area of the map that is shown on screen
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        /*Check if camera position is at any coordinate out of the boundaries of our 512x512 tile map.
        If it is, set it to the corresponding edge. Since this method is called in the render() method,
        this is checked for every frame.
        */
        if (camera.position.x < 0 + effectiveViewportWidth / 2) {
            camera.position.x = 0 + effectiveViewportWidth / 2;
        }
        if (camera.position.x > 20480 - effectiveViewportWidth / 2) {
            camera.position.x = 20480 - effectiveViewportWidth / 2;
        }
        if (camera.position.y < 0 + effectiveViewportHeight / 2) {
            camera.position.y = 0 + effectiveViewportHeight / 2;
        }
        if (camera.position.y > 20480 - effectiveViewportHeight / 2) {
            camera.position.y = 20480 - effectiveViewportHeight / 2;
        }
    }
}
