package com.platformer.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.Game;
import com.platformer.handlers.B2DVars;
import com.platformer.handlers.CustomInput;
import com.platformer.handlers.GameStateManager;
import com.platformer.handlers.MyContactListener;

public class Play extends GameState {

    private final BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;
    private Body playerBody;
    private MyContactListener cl;

    private OrthographicCamera camera;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer tmr;

    private TiledMapTileLayer tiledMapTileLayer;
    private int tileSize;

    public Play(GameStateManager gsm) {
        super(gsm);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Game.V_WIDTH, Game.V_HEIGHT);

        world = new World(new Vector2(0, -0.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        box2DDebugRenderer = new Box2DDebugRenderer();

        tiledMap = new TmxMapLoader().load("maps/level1.tmx");
        tmr = new OrthogonalTiledMapRenderer(tiledMap);
        tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("red");
        tileSize = tiledMapTileLayer.getTileWidth();


        BodyDef bodyDef = new BodyDef();
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();


        bodyDef.position.set(150 / Game.PPM, 200 / Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        playerBody = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(5 / Game.PPM, 5 / Game.PPM);
        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = B2DVars.BIT_BOX;
        fixtureDef.filter.maskBits = B2DVars.BIT_GROUND;
        fixtureDef.shape = box;
        fixtureDef.density = 1;
        fixtureDef.restitution = 0;
        playerBody.createFixture(fixtureDef).setUserData("box");

        box.setAsBox(2 / Game.PPM, 2 / Game.PPM, new Vector2(0, -5 / Game.PPM), 0);
        fixtureDef.shape = box;
        fixtureDef.filter.categoryBits = B2DVars.BIT_BOX;
        fixtureDef.filter.maskBits = B2DVars.BIT_GROUND;
        fixtureDef.isSensor = true;
        playerBody.createFixture(fixtureDef).setUserData("foot");

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
                fixtureDef.filter.categoryBits = B2DVars.BIT_GROUND;
                fixtureDef.filter.maskBits = B2DVars.BIT_BOX;
                fixtureDef.isSensor = false;
                world.createBody(bodyDef).createFixture(fixtureDef);
            }
        }
    }

    @Override
    public void handleUpdate() {

    }

    public void handleInput() {
        if (CustomInput.isPressed(CustomInput.BUTTON1)) {
            if (cl.isPlayerOnGround()) {
                playerBody.applyForceToCenter(0, 100 / Game.PPM, true);
            }
        }
    }
    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);
    }

    @Override
    public void render() {

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tmr.setView(camera);
        tmr.render();

        box2DDebugRenderer.render(world, camera.combined);
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        font.draw(spriteBatch, "Hello", 100, 100);
        spriteBatch.end();
    }

    @Override
    public void dispose() {

    }


}
