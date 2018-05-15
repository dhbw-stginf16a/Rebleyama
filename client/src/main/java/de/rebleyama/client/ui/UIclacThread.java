package de.rebleyama.client.ui;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class UIclacThread extends Thread {

    private OrthographicCamera camera;
    private Pixmap minipixmap;
    private Pixmap bigpixmap;
    private Image minimap;
    private Application gdxApp;

    private Boolean running;

    //values for map calculation
    private int minimapXY = 512;
    private int halfwindow = minimapXY / 4;
    private int lineWidth;

    //pre calc values
    private float transposX;
    private float transposY;
    private int recPositionMinusX;
    private int recPositionPlusX;
    private int recPositionMinusY;
    private int recPositionPlusY;
    private int recPosMinFullX;
    private int recPosPlusFullX;
    private int recPosMinFullY;
    private int recPosPlusFullY;

    /**
     * @param camera     of the main stage
     * @param minipixmap pixmap of the minimap
     * @param bigpixmap  pixmap of the map
     * @param minimap    image of the minimap
     * @param gdxApp     Gdx Application from main client
     */
    UIclacThread(OrthographicCamera camera, Pixmap minipixmap, Pixmap bigpixmap, Image minimap, Application gdxApp) {
        this.camera = camera;
        this.minipixmap = minipixmap;
        this.bigpixmap = bigpixmap;
        this.minimap = minimap;
        this.gdxApp = gdxApp;

        //line Width of View-Rectangle
        this.lineWidth = 4;

    }

    /**
     * start thread
     */
    void begin() {
        this.running = true;
        this.start();
    }

    /**
     * end thread
     */
    void end() {
        this.running = false;

        this.interrupt();
        try {
            this.join();
        } catch (InterruptedException e) {
            gdxApp.log("Minimap Calculator Thread", e.toString());
            this.interrupt();
        }
    }


    @Override
    public void run() {
        while (running) {

            preCalc();

            //redraw pixmap
            for (int y = 0; y <= minimapXY; y++) {
                for (int x = 0; x <= minimapXY; x++) {
                    pixmapClac(x,y);
                }
            }


            //Send to gdx render
            gdxApp.postRunnable(() -> minimap.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(minipixmap)))));


            //This reduces CPU load
            try {
                //keep between 15 (ca. 60fps) and 27 (ca. 30 fps)
                sleep(20);
            } catch (InterruptedException e) {
                gdxApp.log("Minimap Calculator Thread", e.toString());
                this.interrupt();
            }


        }
    }

    /**
     * pre Calculates values for pixmap calculation (reduces cognitive complexity)
     */
    private void preCalc(){
        //select correct display variant for X
        int minimapXMid;
        if ((camera.position.x / 40) < halfwindow + 1) {
            transposX = 0;
            minimapXMid = (int) (camera.position.x / 20);
        } else if ((camera.position.x / 40) > minimapXY - halfwindow) {
            transposX = (float) (minimapXY - halfwindow * 2);
            minimapXMid = (int)((camera.position.x / 20) - minimapXY);
        } else {
            transposX = (camera.position.x / 40) - halfwindow;
            minimapXMid = minimapXY / 2;

        }


        //Y
        int minimapYMid;
        if ((camera.position.y / 40) < halfwindow + 1) {
            transposY = 0;
            minimapYMid = (int) (camera.position.y / 20);
        } else if ((camera.position.y / 40) > minimapXY - halfwindow) {
            transposY = (float) (minimapXY - halfwindow * 2);
            minimapYMid = (int)((camera.position.y / 20) - minimapXY);
        } else {
            transposY = (camera.position.y / 40) - halfwindow;
            minimapYMid = minimapXY / 2;

        }

        //transform to bigmap
        transposX = transposX * 2;
        transposY = transposY * 2;


        //variables for position rectangle calculation
        //amount of view tiles
        int recHalfX = (int) ((camera.viewportWidth * camera.zoom) / 40);
        int recHalfY = (int) ((camera.viewportHeight * camera.zoom) / 40);

        //position of rect lines
        recPositionMinusX = minimapXMid - recHalfX;
        recPositionPlusX = minimapXMid + recHalfX;

        recPositionMinusY = minimapYMid - recHalfY;
        recPositionPlusY = minimapYMid + recHalfY;
        //postion + witdh
        recPosMinFullX = recPositionMinusX - lineWidth;
        recPosPlusFullX = recPositionPlusX + lineWidth;

        recPosMinFullY = recPositionMinusY - lineWidth;
        recPosPlusFullY = recPositionPlusY + lineWidth;
    }

    /**
     * all needed pixmap calculations
     * @param x position in minimap
     * @param y position in minimap
     */
    private void pixmapClac(int x, int y){
        if ((((x <= recPositionMinusX && x > recPosMinFullX) || (x >= recPositionPlusX && x < recPosPlusFullX)) && y < recPosPlusFullY && y > recPosMinFullY)
                ||
                (((y <= recPositionMinusY && y > recPosMinFullY) || (y >= recPositionPlusY && y < recPosPlusFullY)) && x < recPosPlusFullX && x > recPosMinFullX)) {

            minipixmap.drawPixel(x, minimapXY - y, Color.rgba8888(Color.BLACK));

        } else {
            minipixmap.drawPixel(x, minimapXY - y, bigpixmap.getPixel((int) transposX + x, 1024 - ((int) transposY + y)));
        }
    }
}