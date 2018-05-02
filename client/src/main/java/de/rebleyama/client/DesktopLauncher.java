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
		config.width = 800;
		config.height = 480;
		config.forceExit = false;  // for freeing all resources on close
		new LwjglApplication(new RebleyamaClient(), config);
	}
}
