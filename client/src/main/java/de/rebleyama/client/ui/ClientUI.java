package de.rebleyama.client.ui;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import de.rebleyama.client.ui.TileColor;


public class ClientUI implements Disposable {
    //Global Variables from Client CLass
    private TiledMap tiledMap;

    //Global Variable for UI
    private Stage stage;
    private Skin skin;
    private Pixmap minipixmap;
    private Pixmap bigpixmap;
    private Window miniMapWindow;
    private Window mapWindow;
    private Window escMenuWindow;
    private Image map;
    private Image minimap;
    private Application gdxApp;
    private UIclacThread pixmalcalcer;
    private OrthographicCamera camera;


    //creation of array for Minimap Colors, last color is an error color
        private int[] minimapcolors = {Color.rgba8888(Color.DARK_GRAY), Color.rgba8888(Color.FOREST), Color.rgba8888(Color.LIGHT_GRAY), Color.rgba8888(Color.GRAY), Color.rgba8888(Color.BLUE), Color.rgba8888(Color.RED)};

    // create methods
    public ClientUI(TiledMap tiledmap, Application gdxApp,OrthographicCamera camera) {
        this.tiledMap = tiledmap;
        this.gdxApp = gdxApp;
        this.camera = camera;
        //Calls method that is responsible to create UI elements
        createUI();
    }

    /**
     * Create Method for UI Elements
     */
    private void createUI() {
        //setup skin, stage, input
        skin = new Skin(Gdx.files.internal("assets/uiskin/skin/uiskin.json"));
        stage = new Stage(new ScreenViewport());

        //create pixmaps
        bigpixmap = createPixmap();
        minipixmap = new Pixmap(512, 512, Pixmap.Format.RGBA8888);

        minipixmap.setColor(Color.RED);

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
        final TextButton exit = new TextButton("Exit", skin);


        escMenuWindow = new Window("Menu", skin);
        createXButton(escMenuWindow);
        escMenuWindow.row().fill().expandX();
        escMenuWindow.add(buttonMap);
        escMenuWindow.row().fill().expandX();
        escMenuWindow.add(buttonMiniMap);
        escMenuWindow.row().fill().expandX();
        escMenuWindow.add(change);
        escMenuWindow.row().fill().expandX();
        escMenuWindow.add(buttonExit);
        escMenuWindow.row().fill().expandX();
        escMenuWindow.add(exit);
        escMenuWindow.pack();
        escMenuWindow.setPosition((float) (Gdx.graphics.getWidth() / 2.0) - escMenuWindow.getWidth() / 2, (float) (Gdx.graphics.getHeight() / 2.0) - escMenuWindow.getHeight() / 2);
        stage.addActor(escMenuWindow);
        escMenuWindow.setVisible(false);

        //create Listener
        buttonMiniMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                miniMapWindow.setVisible(!miniMapWindow.isVisible());
                if (miniMapWindow.isVisible()) {
                    miniMapWindow.toFront();
                    startcalcThread();
                }else {
                    endcalcThread();
                }
            }
        });

        buttonMap.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mapWindow.setVisible(!mapWindow.isVisible());
                if (mapWindow.isVisible()) {
                    mapWindow.toFront();
                }
            }
        });
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escMenuWindow.setVisible(false);
            }
        });

        exit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gdxApp.exit();
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
     * Creates a windows with a minimap in it
     */
    private void createMinimap() {
        // create window
        miniMapWindow = new Window("Minimap", skin);

                //set postion of window (-size)
        miniMapWindow.setPosition((float) (Gdx.graphics.getWidth() - 200), (float) (Gdx.graphics.getHeight() - 200));

        //set size of window
        miniMapWindow.setSize(256, 256);
        //create image
        minimap = new Image();

        //add Xbuton in top bar of minimap with extras
        final TextButton buttonX = new TextButton("X", skin);
        //ad butto to top bar of window
        miniMapWindow.getTitleTable().add(buttonX).height(miniMapWindow.getPadTop());
        //setup event listener for X button
        buttonX.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                miniMapWindow.setVisible(false);

                //close render
                endcalcThread();

            }
        });

        //aspect ratio button
        final TextButton buttonScale = new TextButton("+", skin);
        //listener with logic inside
        buttonScale.addListener(new DragListener() {
            float oldY;
            float oldX;
            float oldWidth;
            float newwidth;


            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                oldY = miniMapWindow.getY();
                oldX = miniMapWindow.getX();
                oldWidth = miniMapWindow.getWidth();

                //logic for minimap resizing
                if ((x > 0) && (y > 0) && (oldWidth > 128)) {
                    if (oldWidth - x < 128) {
                        newwidth = 128;
                    } else {
                        newwidth = oldWidth - x;
                    }
                } else if ((x < 0) && (y < 0) && (oldWidth < 512)) {
                    if (oldWidth - x > 512) {
                        newwidth = 512;
                    } else {
                        newwidth = oldWidth - x;
                    }
                }
                miniMapWindow.setPosition(oldX + oldWidth - newwidth, oldY + oldWidth - newwidth);
                miniMapWindow.setSize(newwidth, newwidth);
            }


        });

        //stack actor over each other
        Stack stack = new Stack();
        Table overlay = new Table();
        stack.add(minimap);
        overlay.add(buttonScale).expand().bottom().left();
        stack.add(overlay);


        miniMapWindow.add(stack);
        //add minimap to ui stage
        stage.addActor(miniMapWindow);

    }

    /**
     * create a window with a large map in it
     */
    private void createMap() {
        //create window
        mapWindow = new Window("Map", skin);

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
     */
    private Pixmap createPixmap() {
        int minimapXY = 1024;
        int pX = 0;
        int pY;

        //create Pixmap
        Pixmap tmppixmap = new Pixmap(minimapXY, minimapXY, Pixmap.Format.RGBA8888);
        //get our tiledMap layer


        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        //loop through all map (for each tile)
        for (int x = 0; x < layer.getWidth(); x++) {
            pY = 0;
            for (int y = 0; y < layer.getHeight(); y++) {

                //get current cell id
                int tmpid = layer.getCell(x, y).getTile().getId();
                //set color of pixmap pixel similar to the color of the tile on our tiledmap

                if (tmpid <= minimapcolors.length) {
                    int tmpColor = minimapcolors[tmpid - 1];

                        //create a rectangle of 2px x 2px
                        tmppixmap.drawPixel(pX, minimapXY - pY, tmpColor);

                        tmppixmap.drawPixel(pX + 1, minimapXY - pY, tmpColor);

                        tmppixmap.drawPixel(pX, minimapXY - ++pY, tmpColor);

                        tmppixmap.drawPixel(pX + 1, minimapXY - pY, tmpColor);

                } else {
                    //Changer Logger/Error exception if uniform method is used
                    Gdx.app.log("Pixmap_creation", "ERROR - Color ID Unknown. ID: " + tmpid);
                }


                pY++;
            }
            pX += 2;
        }

        //tmppixmap gets disposed by java
        return tmppixmap;
    }

    /**
     * Selects correct TileType
     * @return int of Tile Color
     */
    private int tileColorSelect(){
        return 0;
    }


    /**
     * starts the thread which calculates the pixmap changes
     *
     */
    public void startcalcThread() {
        pixmalcalcer = new UIclacThread(camera, minipixmap, bigpixmap, minimap, gdxApp);
        pixmalcalcer.begin();

    }

    /**
     * end the thread which calculates the pixmap changes
     */
    public void endcalcThread() {
        pixmalcalcer.end();

    }


    // change methods

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
     * Resizes stage + UI elements
     *
     * @param width  of window after resize
     * @param height of window after resize
     */
    public void stageResize(int width, int height) {
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
     * Reacts on window relevant key presses
     *
     * @param windowName name of window that shall react
     */
    public void uiKeypressed(String windowName) {
        //select which window (please suggest better selector ways)
        if (windowName.equals("mapWindow")) {
            //set Window visible and to front
            mapWindow.setVisible(!mapWindow.isVisible());
            if (mapWindow.isVisible()) {
                mapWindow.toFront();
            }
        } else if (windowName.equals("escMenuWindow")) {
            //set Window visible and to front
            escMenuWindow.setVisible(!escMenuWindow.isVisible());
            if (escMenuWindow.isVisible()) {
                escMenuWindow.toFront();
            }
        }


    }

    /**
     * Disposes UI elemets
     */
    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();
        minipixmap.dispose();
        bigpixmap.dispose();


    }

    // getter/setter

    /**
     * Get Method for Stage Object
     *
     * @return Stage that the UI uses
     */
    public Stage getStage() {
        return stage;
    }


}