package de.rebleyama.client;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import java.util.logging.*;

public class RebleyamaClient extends ApplicationAdapter implements InputProcessor{
	
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
	private TiledMap tiledMap;

	@Override
	public void create () {

		batch = new SpriteBatch();

		//get the window size for the camera
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		//initiate the camera
		camera = new OrthographicCamera();
        camera.setToOrtho(false,w,h);
		camera.update();
		
        //load the map
        //also available: ../client/assets/custommaps/testMap.tmx
        tiledMap = new TmxMapLoader().load("../client/assets/custommaps/default.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void render() {
        //prevent camera from going out of bounds
        stayInBounds();
        //handle Input
        handleMouseMovementInput();
        handleKeyMovementInput();
        handleKeyZoomInput();
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

	}

	@Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
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
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        
        if (((effectiveViewportWidth < 20480) || amount < 0) && ((effectiveViewportWidth > 480) || amount > 0)) {
            camera.zoom += (0.3*amount);
        }

        return false;
    }

	@Override
	public void dispose() {
		// dispose of all the native resources
    }
    
    private void handleMouseMovementInput() {
        float mousePositionX = Gdx.input.getX(); 
        float mousePositionY = Gdx.input.getY();
        float windowWidth = Gdx.graphics.getWidth();
        float windowHeight = Gdx.graphics.getHeight();

        if (mousePositionY <= windowHeight/20) {
            camera.translate(0,25*camera.zoom);
        }
        if (mousePositionY >= windowHeight - windowHeight/20) {
            camera.translate(0,-25*camera.zoom);
        }
        if (mousePositionX >= windowWidth - windowWidth/20) {
            camera.translate(25*camera.zoom,0);
        }
        if (mousePositionX <= windowWidth/20) {
            camera.translate(-25*camera.zoom,0);
        }
    }

    private void handleKeyMovementInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            camera.translate(-25,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            camera.translate(25,0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            camera.translate(0,-25);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            camera.translate(0,25);
        }
    }

    private void handleKeyZoomInput() {
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;

        if (Gdx.input.isKeyPressed(Input.Keys.EQUALS) && (effectiveViewportWidth > 480)) {
            camera.zoom -= 0.1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.MINUS) && (effectiveViewportWidth < 20480)) {
            camera.zoom += 0.1;
        }
    }

    private void stayInBounds() {
        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
		float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        if (camera.position.x < 0 + effectiveViewportWidth/2) {
            camera.position.x = 0 + effectiveViewportWidth/2;
        }
        if (camera.position.x > 20480 - effectiveViewportWidth/2) {
            camera.position.x = 20480 - effectiveViewportWidth/2;
        }
        if (camera.position.y < 0 + effectiveViewportHeight/2) {
            camera.position.y = 0 + effectiveViewportHeight/2;
        }
        if (camera.position.y > 20480 - effectiveViewportHeight/2) {
            camera.position.y = 20480 - effectiveViewportHeight/2;
        }
    }
}
