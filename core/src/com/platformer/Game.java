package com.platformer;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.platformer.handlers.Content;
import com.platformer.handlers.GameStateManager;
import com.platformer.handlers.MyInputProcessor;
import com.platformer.handlers.CustomInput;

public class Game extends ApplicationAdapter {
	public static final String TITLE = "Platformer game";
	public static final int V_WIDTH = 480;
	public static final int V_HEIGHT = 320;
	public static final int SCALE = 2;

	public static final float PPM = 100;

	private SpriteBatch sb;
	private OrthographicCamera cam;
	private OrthographicCamera hudCamera;

	public static final float STEP = 1 / 60f;
	private float accum;

	private GameStateManager gsm;

	public static Content res;


	@Override
	public void create () {

		Gdx.input.setInputProcessor(new MyInputProcessor());

		res = new Content();
		res.loadTexture("images/bunny.png", "bunny");
		res.loadTexture("images/crystal.png", "crystal");
		res.loadTexture("images/hud.png", "hud");

		sb = new SpriteBatch();
		cam = new OrthographicCamera();
		cam.setToOrtho(false, V_WIDTH, V_HEIGHT);

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
			CustomInput.update();
		}

	}

	public SpriteBatch getBatch() {
		return sb;
	}

	public OrthographicCamera getCamera() {
		return cam;
	}

	public OrthographicCamera getHudCamera() {
		return hudCamera;
	}
	
	@Override
	public void dispose () {

	}
}
