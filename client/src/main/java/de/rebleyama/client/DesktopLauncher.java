package de.rebleyama.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

    private DesktopLauncher() {

    }

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Rebleyama";
        config.width = 1280;
        config.height = 720;


        new LwjglApplication(new RebleyamaClient(), config);
    }
}
