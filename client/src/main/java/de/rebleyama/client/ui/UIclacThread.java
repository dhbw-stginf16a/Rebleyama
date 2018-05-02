package de.rebleyama.client.ui;


import com.badlogic.gdx.Application;
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


    //values for map calaculation
    private int minimapXY = 512;
    private int halfwindow = minimapXY / 4;

    /**
     * @param camera     of the main stage
     * @param minipixmap pixmap of the minimap
     * @param bigpixmap  pixmap of the map
     * @param minimap    image of the minimap
     * @param gdxApp     Gdx Application from main client
     */
    public UIclacThread(OrthographicCamera camera, Pixmap minipixmap, Pixmap bigpixmap, Image minimap, Application gdxApp) {
        this.camera = camera;
        this.minipixmap = minipixmap;
        this.bigpixmap = bigpixmap;
        this.minimap = minimap;
        this.gdxApp = gdxApp;
    }

    /**
     * start thread
     */
    public void begin() {
        this.running = true;
        this.start();
    }

    /**
     * end thread
     */
    public void end()  {
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

        while (running) {
            //select correct display variant
            if ((camera.position.x / 40) < halfwindow + 1) {
                transposX = 0;
            } else {
                transposX = (camera.position.x / 40) - halfwindow;
            }


            if ((camera.position.y / 40) < halfwindow + 1) {
                transposY = 0;
            } else {
                transposY = (camera.position.y / 40) - halfwindow;
            }


            transposX = transposX * 2;
            transposY = transposY * 2;
            //redraw pixmap
            for (int y = 0; y <= minimapXY; y++) {
                for (int x = 0; x <= minimapXY; x++) {
                    minipixmap.drawPixel(x, minimapXY - y, bigpixmap.getPixel((int) transposX + x, 1024 - ((int) transposY + y)));

                }
            }


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

            //TODO show rectangle on minimaps + too much tiles on top area of minimap + add thread start/close to all minimap window visiblity settings (map + xbutton + m)


        }
    }

}