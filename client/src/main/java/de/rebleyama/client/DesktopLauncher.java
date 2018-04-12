package de.rebleyama.client;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.rebleyama.client.RebleyamaClient;

public class DesktopLauncher {

	private DesktopLauncher() {
		
	}

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Rebleyama";
		config.width = 1280;
		config.height = 720;

		/*Fullscreen config
		config.width = LwjglApplicationConfiguration.getDesktopDisplayMode().width;
        config.height = LwjglApplicationConfiguration.getDesktopDisplayMode().height;
        config.fullscreen = true;
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
		 */

		new LwjglApplication(new RebleyamaClient(), config);
	}
}
