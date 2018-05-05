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
    void end()  {
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
        //local variables
        float transposX;
        float transposY;

        int minimapXMid;
        int minimapYMid;
        int recHalfX;
        int recHalfY;
        int recPositionMinusX;
        int recPositionPlusX;
        int recPositionMinusY;
        int recPositionPlusY;
        int recPosMinFullX;
        int recPosPlusFullX;
        int recPosMinFullY;
        int recPosPlusFullY;

        while (running) {
            //select correct display variant
            if ((camera.position.x / 40) < halfwindow + 1) {
                transposX = 0;
                minimapXMid = (int)(camera.position.x / 20);
            } else {
                transposX = (camera.position.x / 40) - halfwindow;
                minimapXMid = minimapXY/2;

            }


            if ((camera.position.y / 40) < halfwindow + 1) {
                transposY = 0;
                minimapYMid = (int)(camera.position.y / 20);
            } else {
                transposY = (camera.position.y / 40) - halfwindow;
                minimapYMid = minimapXY/2;

            }

            transposX = transposX * 2;
            transposY = transposY * 2;
            gdxApp.log("Test","X: "+minimapXMid+"| Y: "+minimapYMid);

            //anzahl an tiles für standard breite herausfinden


            //variables for position rectangle calculation
            //amount of view tiles
            recHalfX = (int)((camera.viewportWidth *  camera.zoom)/40);
            recHalfY = (int)((camera.viewportHeight *  camera.zoom)/40);

            //position of rect lines
            recPositionMinusX = minimapXMid-recHalfX;
            recPositionPlusX = minimapXMid+recHalfX;

            recPositionMinusY = minimapYMid-recHalfY;
            recPositionPlusY = minimapYMid+recHalfY;
            //postion + witdh
            recPosMinFullX = recPositionMinusX-lineWidth;
            recPosPlusFullX = recPositionPlusX+lineWidth;

            recPosMinFullY = recPositionMinusY-lineWidth;
            recPosPlusFullY = recPositionPlusY+lineWidth;




            //redraw pixmap
            for (int y = 0; y <= minimapXY; y++) {
                for (int x = 0; x <= minimapXY; x++) {

                    if      (
                            (((x <= recPositionMinusX && x >recPosMinFullX) || (x >= recPositionPlusX && x <recPosPlusFullX)) && y < recPosPlusFullY && y > recPosMinFullY)
                            ||
                            (((y <= recPositionMinusY && y >recPosMinFullY) || (y >= recPositionPlusY && y <recPosPlusFullY)) && x < recPosPlusFullX && x > recPosMinFullX)
                            )
                    {

                        minipixmap.drawPixel(x, minimapXY - y, Color.rgba8888(Color.BLACK));

                    }else {
                        minipixmap.drawPixel(x, minimapXY - y, bigpixmap.getPixel((int) transposX + x, 1024 - ((int) transposY + y)));
                    }


                }
            }



            //Send to gdx render
            gdxApp.postRunnable(() -> minimap.setDrawable(new TextureRegionDrawable(new TextureRegion(new Texture(minipixmap)))));




            /*

            //Possible to use this for less computing

            try {
                sleep(2);
            } catch (InterruptedException e) {
                Gdx.app.log("Minimap Calculator Thread", e.toString());
                this.interrupt();
            }
            */
            // TODO use xy from pixmap (create new pixmap with window xy -> translate xy postions -> change transPos with multiplyer[40])
            //TODO on each site problem (too much tiles on top area of minimap)

            //TODO add thread start/close to all minimap window visiblity settings (map + xbutton + m)


        }
    }

}