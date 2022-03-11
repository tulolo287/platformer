package com.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.platformer.handlers.GameStateManager;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Platformer game";
	public static final int V_WIDTH = 800;
	public static final int V_HEIGHT = 400;
	public static final int SCALE = 2;

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;

	public static final float STEP = 1 / 60f;
	private float accum;

	private GameStateManager gsm;

	public SpriteBatch getBatch() {
		return batch;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public OrthographicCamera getHudCamera() {
		return hudCamera;
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);

		hudCamera = new OrthographicCamera();
		hudCamera.setToOrtho(false, V_WIDTH, V_HEIGHT);

		gsm = new GameStateManager(this);
	}

	@Override
	public void render () {
		accum += Gdx.graphics.getDeltaTime();
		while (accum >= STEP) {
			accum -= STEP;
			gsm.update(STEP);
			gsm.render();
		}

	}
	
	@Override
	public void dispose () {

	}
}
