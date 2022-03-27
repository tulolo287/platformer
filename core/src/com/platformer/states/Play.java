package com.platformer.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.Game;
import com.platformer.entities.Player;
import com.platformer.handlers.B2DVars;
import com.platformer.handlers.CustomInput;
import com.platformer.handlers.GameStateManager;
import com.platformer.handlers.MyContactListener;

public class Play extends GameState {

    private final BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private MyContactListener cl;

    private OrthographicCamera b2drCam;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tmr;

    private SpriteBatch sb;

    private int tileSize;

    private Player player;

    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0, -0.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        box2DDebugRenderer = new Box2DDebugRenderer();

        sb = new SpriteBatch();

        createPlayer();
        createTiles();


        b2drCam = new OrthographicCamera();
        b2drCam.setToOrtho(false, Game.V_WIDTH / Game.PPM, Game.V_HEIGHT / Game.PPM);

    }

    @Override
    public void handleUpdate() {

    }

    public void handleInput() {
        if (CustomInput.isPressed(CustomInput.BUTTON1)) {
            if (cl.isPlayerOnGround()) {
                player.getBody().applyForceToCenter(0, 500 / Game.PPM, true);
            }
        }
    }
    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);

        player.update(dt);
    }

    @Override
    public void render() {

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.setView(cam);
        tmr.render();

        box2DDebugRenderer.render(world, b2drCam.combined);

        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

    }

    public void createPlayer() {

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();


        bodyDef.position.set(150 / Game.PPM, 200 / Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(12 / Game.PPM, 12 / Game.PPM);
        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_RED;
        fixtureDef.shape = box;
        fixtureDef.density = 1;
        fixtureDef.restitution = 0;
        body.createFixture(fixtureDef).setUserData("box");

        box.setAsBox(12 / Game.PPM, 5 / Game.PPM, new Vector2(0, -10 / Game.PPM), 0);
        fixtureDef.shape = box;
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_RED;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData("foot");

        player = new Player(body);
    }

    public void createTiles() {

        tiledMap = new TmxMapLoader().load("maps/level1.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        tileSize = (int) tiledMap.getProperties().get("tilewidth");
        TiledMapTileLayer tiledMapTileLayer;

        tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("red");
        createLayer(tiledMapTileLayer, B2DVars.BIT_RED);
        tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("green");
        createLayer(tiledMapTileLayer, B2DVars.BIT_GREEN);
        tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("blue");
        createLayer(tiledMapTileLayer, B2DVars.BIT_BLUE);



    }

    public void createLayer(TiledMapTileLayer tiledMapTileLayer, short bits) {

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        for (int row = 0; row < tiledMapTileLayer.getHeight(); row++) {
            for (int col = 0; col < tiledMapTileLayer.getWidth(); col++) {
                TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(col, row);
                if (cell == null) continue;
                if (cell.getTile() == null) continue;

                bodyDef.type = BodyDef.BodyType.StaticBody;
                bodyDef.position.set(
                        (col + 0.5f) * tileSize / Game.PPM,
                        (row + 0.5f) * tileSize / Game.PPM
                );
                ChainShape chainShape = new ChainShape();
                Vector2[] v = new Vector2[3];
                v[0] = new Vector2(-tileSize / 2 / Game.PPM, -tileSize / 2 / Game.PPM);
                v[1] = new Vector2(-tileSize / 2 / Game.PPM, tileSize / 2 / Game.PPM);
                v[2] = new Vector2(tileSize / 2 / Game.PPM, tileSize / 2 / Game.PPM);
                chainShape.createChain(v);
                fixtureDef.friction = 0;
                fixtureDef.shape = chainShape;
                fixtureDef.filter.categoryBits = bits;
                fixtureDef.filter.maskBits = B2DVars.BIT_PLAYER;
                fixtureDef.isSensor = false;
                world.createBody(bodyDef).createFixture(fixtureDef);
            }
        }
    }

    @Override
    public void dispose() {

    }


}
