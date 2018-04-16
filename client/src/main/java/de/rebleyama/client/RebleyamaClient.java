package de.rebleyama.client;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class RebleyamaClient extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private TiledMapRenderer tiledMapRenderer;
    private TiledMap tiledMap;

    //Global Variable of window for Minimap, map, esc menu, UI Skin, UI Stage and Pixmap for Minimap
    private Stage stage;
    private Skin skin;
    private Pixmap minipixmap;
    private Pixmap bigpixmap;
    private Window miniMapWindow;
    private Window mapWindow;
    private Window escMenuWindow;
    private Image map;
    private Image minimap;


    //creation of array for Minimap Colors, last color is an error color
    private int[] minimapcolors = {Color.rgba8888(Color.DARK_GRAY), Color.rgba8888(Color.FOREST), Color.rgba8888(Color.LIGHT_GRAY), Color.rgba8888(Color.GRAY), Color.rgba8888(Color.BLUE), Color.rgba8888(Color.RED)};

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

        //Calls method that is responsible to create UI elements
        createUI();
        //Creation of a Multiplexer which allows multi layer event handling (UI Layer and TiledMap Layer) (UI layer needs to be first ORDER IS IMPORTANT)
        InputMultiplexer im = new InputMultiplexer(stage, this);
        Gdx.input.setInputProcessor(im);

    }

    /**
     * Create Method for UI Elements
     */
    private void createUI() {
        //TODO NOTES
        // correcte farb werte von paul bekommen
        // daniels datenstrukturen einbauen
        // multithreading for render?

        // Minimap neu
        // keep aspect ratio maybe later


        //setup skin, stage, input
        skin = new Skin(Gdx.files.internal("assets/uiskin/skin/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        //Create UI Elements here
        createMinimap();
        createESCMenu();
        createMap();


    }

    /**
     * creates an window which opens/closes if ESC is pressed
     */
    private void createESCMenu() {
        //declare Window
        final TextButton buttonMiniMap = new TextButton("MiniMap", skin);
        final TextButton buttonMap = new TextButton("Map", skin);
        final TextButton buttonExit = new TextButton("Continue", skin);
        final TextButton change = new TextButton("ChangePixel_TEST", skin);


        escMenuWindow = new Window("Menu", skin);
        createXButton(escMenuWindow);
        escMenuWindow.row().fill().expandX();
        escMenuWindow.add(buttonMap);
        escMenuWindow.row();
        escMenuWindow.add(buttonMiniMap);
        escMenuWindow.row();
        escMenuWindow.add(change);
        escMenuWindow.row();
        escMenuWindow.add(buttonExit);
        escMenuWindow.pack();
        escMenuWindow.setPosition((float) (Gdx.graphics.getWidth() / 2.0) - escMenuWindow.getWidth() / 2, (float) (Gdx.graphics.getHeight() / 2.0) - escMenuWindow.getHeight() / 2);
        stage.addActor(escMenuWindow);
        escMenuWindow.setVisible(false);
        //create Listener
        buttonMiniMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                miniMapWindow.setVisible(!miniMapWindow.isVisible());
            }
        });

        buttonMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mapWindow.setVisible(!mapWindow.isVisible());
            }
        });
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escMenuWindow.setVisible(false);
            }
        });

        change.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //update pixmaps
                for (int x = 0; x < 100; x++) {
                    for (int y = 0; y < 100; y++) {
                        tilechanged(x, y, 6);
                    }
                }
                //update drawable inside windows
                minimap.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(minipixmap))));
                map.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(bigpixmap))));


            }
        });
    }

    /**
     * method to create X button for window
     *
     * @param window in which a X button shall be created
     */
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

    /**
     * function to change the pixel of pixmaps
     *
     * @param x         coordinate
     * @param y         coordinate
     * @param newtileid new tile type id
     */
    private void tilechanged(int x, int y, int newtileid) {
        //get color
        int tmpColor = minimapcolors[newtileid - 1];
        minipixmap.drawPixel(x, minipixmap.getWidth() - y, tmpColor);
        //create a rectangle of 2px x 2px
        bigpixmap.drawPixel(x, bigpixmap.getWidth() - y, tmpColor);

        bigpixmap.drawPixel(x + 1, bigpixmap.getWidth() - y, tmpColor);

        bigpixmap.drawPixel(x, bigpixmap.getWidth() - y + 1, tmpColor);

        bigpixmap.drawPixel(y + 1, bigpixmap.getWidth() - y + 1, tmpColor);

    }

    /**
     * Creates a windows with a minimap in it
     */
    private void createMinimap() {

        //TODO make constant size + chose correct sizes (200)
        // create window
        miniMapWindow = new Window("Minimap", skin);
        //set postion of window (-size)
        miniMapWindow.setPosition((float) (Gdx.graphics.getWidth() - 200), (float) (Gdx.graphics.getHeight() - 200));

        //set size of window
        miniMapWindow.setSize(200, 200);


        //allow the window to be resized
        miniMapWindow.setResizable(true);

        //call method that creates a pixmap of our tiledmap
        minipixmap = createPixmap(512, false);

        //create image
        minimap = new Image();
        minimap.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(minipixmap))));


        //fill inside of window with minimap
        miniMapWindow.add(minimap);
        //add minimap to ui stage
        createXButton(miniMapWindow);
        stage.addActor(miniMapWindow);
    }

    /**
     * create a window with a large map in it
     */
    private void createMap() {
        //create window
        mapWindow = new Window("Map", skin);
        //call method that creates a pixmap of our tiledmap
        bigpixmap = createPixmap(1024, true);
        //create image
        map = new Image();
        map.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(bigpixmap))));

        //fill inside of window with image
        mapWindow.add(map);
        //set size to 80% of resolution
        mapWindow.setSize((float) (Gdx.graphics.getWidth() * 0.8), (float) (Gdx.graphics.getHeight() * 0.8));

        //set postion of window in the middle
        mapWindow.setPosition((float) (Gdx.graphics.getWidth() / 2.0) - mapWindow.getWidth() / 2, (float) (Gdx.graphics.getHeight() / 2.0) - mapWindow.getHeight() / 2);

        //add minimap to ui stage
        stage.addActor(mapWindow);
        createXButton(mapWindow);
        mapWindow.setVisible(false);

    }


    /**
     * creates a pixmap of our tiledmap
     * A pixmap is similar to a bitmap or bufferedimage
     *
     * @param minimapXY xy height/width of pixmap
     * @param big       boolen if the big or small map shall be created
     */
    private Pixmap createPixmap(int minimapXY, boolean big) {
        //optional para for size, xy , pixmap (ref or value),
        //create Pixmap
        Pixmap tmppixmap = new Pixmap(minimapXY, minimapXY, Pixmap.Format.RGBA8888);
        //get our tiledMap layer
        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        //own x/y coordinates for pixmap manipulation
        int pX = 0;
        int pY = 1;
        //loop through all map (for each tile)
        for (int x = 0; x < layer.getWidth(); x++) {
            pY = 0;
            for (int y = 0; y < layer.getHeight(); y++) {
                //get current cell id
                int tmpid = layer.getCell(x, y).getTile().getId();
                //set color of pixmap pixel similar to the color of the tile on our tiledmap
                if (tmpid <= minimapcolors.length) {
                    int tmpColor = minimapcolors[tmpid - 1];
                    if (!big) {
                        tmppixmap.drawPixel(x, minimapXY - y, tmpColor);
                    } else {
                        //create a rectangle of 2px x 2px
                        tmppixmap.drawPixel(pX, minimapXY - pY, tmpColor);

                        tmppixmap.drawPixel(pX + 1, minimapXY - pY, tmpColor);

                        tmppixmap.drawPixel(pX, minimapXY - ++pY, tmpColor);

                        tmppixmap.drawPixel(pX + 1, minimapXY - pY, tmpColor);
                    }
                } else {
                    //Changer Logger/Error exception if uniform method is used
                    Gdx.app.log("Pixmap_creation", "ERROR - Color ID Unknown. ID: " + tmpid);
                }

                pY++;
            }
            pX += 2;
        }
        Gdx.app.log("test", pX + "   " + pY);
        //tmppixmap gets disposed by java
        return tmppixmap;
    }


    @Override
    public void render() {
        //prevent camera from going out of bounds
        stayInBounds();
        //handle Input
        handleKeyMovementInput();
        handleKeyZoomInput();
        handleMouseMovementInput();

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
        // MiniMap Test 1
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    /**
     * Resize event which triggers on size change of client window
     *
     * @param width  Width after resize has happened
     * @param height height after resize has happened
     */
    @Override
    public void resize(int width, int height) {
        //calls method to resize for Minimap window (can be used for other too)
        resizeWindow(width, height, miniMapWindow);
        resizeWindow(width, height, escMenuWindow);
        resizeWindow(width, height, mapWindow);

        //update viewport of stage
        stage.getViewport().update(width, height, true);


    }

    /**
     * resize ui Stage and positions of window relative to last position on new window size
     *
     * @param width  after resize
     * @param height after resize
     */
    private void resizeWindow(int width, int height, Window window) {

        //get old client window postion (-size of window for "correct" coordinate system)
        float oldheight = stage.getViewport().getScreenHeight() - window.getHeight();
        float oldwidth = stage.getViewport().getScreenWidth() - window.getWidth();
        //get old position of window
        float oldheightWindow = window.getY();
        float oldwidthWindow = window.getX();
        //calculate old relative position on screen
        float relativheight = oldheightWindow / oldheight;
        float relativwidth = oldwidthWindow / oldwidth;
        //resize Map Window to new resolution
        if (window == mapWindow) {
            window.setSize((float) (width * 0.8), (float) (height * 0.8));
        }
        //calculate new position after resize with new window size
        float newheightWindow = (height - window.getHeight()) * relativheight;
        float newwidthWindow = (width - window.getWidth()) * relativwidth;
        //set new position
        window.setPosition(newwidthWindow, newheightWindow);


    }

    /**
     * dispose of all the native resources we allocated in create()
     */
    @Override
    public void dispose() {
        tiledMap.dispose();
        batch.dispose();
        skin.dispose();
        stage.dispose();
        minipixmap.dispose();
        bigpixmap.dispose();
    }

    /**
     * Stub method for recognizing keypress
     * This triggers when any key is pressed down
     *
     * @param keycode keycode of the key that was pressed
     */
    @Override
    public boolean keyDown(int keycode) {
        //If ESC is pressed, show Menu
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            escMenuWindow.setVisible(!escMenuWindow.isVisible());
            return true;
        }
        //If m is pressed, show Map
        if (Gdx.input.isKeyPressed(Input.Keys.M)) {
            mapWindow.setVisible(!mapWindow.isVisible());
            return true;
        }


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
