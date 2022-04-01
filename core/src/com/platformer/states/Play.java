package com.platformer.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
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
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.platformer.Game;
import com.platformer.entities.Crystal;
import com.platformer.entities.HUD;
import com.platformer.entities.Player;
import com.platformer.handlers.B2DVars;
import com.platformer.handlers.CustomInput;
import com.platformer.handlers.GameStateManager;
import com.platformer.handlers.MyContactListener;

public class Play extends GameState {

    private boolean debug = true;

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
    private Array<Crystal> crystals;

    private HUD hud;

    public Play(GameStateManager gsm) {
        super(gsm);

        world = new World(new Vector2(0, -0.81f), true);
        cl = new MyContactListener();
        world.setContactListener(cl);
        box2DDebugRenderer = new Box2DDebugRenderer();

        sb = new SpriteBatch();

        createPlayer();
        createTiles();
        createCrystals();


        b2drCam = new OrthographicCamera();
        b2drCam.setToOrtho(false, Game.V_WIDTH / Game.PPM, Game.V_HEIGHT / Game.PPM);

        hud = new HUD(player);

    }

    private void createCrystals() {
        crystals = new Array<Crystal>();
        MapLayer layer = tiledMap.getLayers().get("crystals");

        BodyDef bodyDef = new BodyDef();
        FixtureDef fixtureDef = new FixtureDef();

        for (MapObject mo : layer.getObjects()) {
            bodyDef.type = BodyDef.BodyType.StaticBody;

            float x = (float) mo.getProperties().get("x") / B2DVars.PPM;
            float y = (float) mo.getProperties().get("y") / B2DVars.PPM;

            bodyDef.position.set(x, y);

            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(8f / B2DVars.PPM);

            fixtureDef.shape = circleShape;
            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = B2DVars.BIT_CRYSTAL;
            fixtureDef.filter.maskBits = B2DVars.BIT_PLAYER;

            Body body = world.createBody(bodyDef);
            body.createFixture(fixtureDef).setUserData("crystal");
            circleShape.dispose();

            Crystal c = new Crystal(body);
            crystals.add(c);

            body.setUserData(c);
        }
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

        if (CustomInput.isPressed(CustomInput.BUTTON2)) {
            switchBlocks();
        }

        if (CustomInput.isPressed()) {
            //System.out.println(CustomInput.x);
            if (CustomInput.x > 0 && CustomInput.x < 250 && CustomInput.y > 1200 && CustomInput.y < 1500) {
                //switchBlocks();
              //  player.getBody().applyLinearImpulse(new Vector2(12, 32), new V);
                player.getBody().setLinearVelocity(-1f, 0f);
                player.flipX = true;
                CustomInput.isReleased();


            } else if (CustomInput.x > 450 && CustomInput.x < 700 && CustomInput.y > 1200 && CustomInput.y < 1500) {
                //player.getBody().applyForceToCenter(500 / Game.PPM, 0, true);
                player.getBody().setLinearVelocity(1f, 0f);
                player.flipX = false;
                CustomInput.isReleased();

            } else if (CustomInput.x > 2200 && CustomInput.x < 2500 && CustomInput.y > 1200 && CustomInput.y < 1500) {
            //player.getBody().applyForceToCenter(500 / Game.PPM, 0, true);
                if (cl.isPlayerOnGround()) {
                    player.getBody().applyForceToCenter(0, 1500 / Game.PPM, true);
                    //CustomInput.isReleased();
                }

            }

        }
    }

    private void switchBlocks() {
        Filter filter = player.getBody().getFixtureList().first().getFilterData();
        short bits = filter.maskBits;

        if (((bits & B2DVars.BIT_RED) != 0)) {
            bits &= ~B2DVars.BIT_RED;
            bits |= B2DVars.BIT_GREEN;
        }
        else if (((bits & B2DVars.BIT_GREEN) != 0)) {
            bits &= ~B2DVars.BIT_GREEN;
            bits |= B2DVars.BIT_BLUE;
        }
        else if (((bits & B2DVars.BIT_BLUE) != 0)) {
            bits &= ~B2DVars.BIT_BLUE;
            bits |= B2DVars.BIT_RED;
        }

        filter.maskBits = bits;
        player.getBody().getFixtureList().first().setFilterData(filter);

        filter = player.getBody().getFixtureList().get(1).getFilterData();
        bits &= ~B2DVars.BIT_CRYSTAL;
        filter.maskBits = bits;
        player.getBody().getFixtureList().get(1).setFilterData(filter);
    }

    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);

        Array<Body> bodies = cl.getCrystalsToRemove();
        for (int i = 0; i < bodies.size; i++) {
            Body b = bodies.get(i);
            crystals.removeValue((Crystal) b.getUserData(), true);
            world.destroyBody(b);
            player.collectCrystal();
        }
        bodies.clear();

        player.update(dt);

        for (int i = 0; i < crystals.size; i++) {
            crystals.get(i).update(dt);
        }
    }

    @Override
    public void render() {

        Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        cam.position.set(player.getPosition().x * B2DVars.PPM + Game.V_WIDTH / 4, Game.V_HEIGHT / 2, 0);
        cam.update();

        sb.setProjectionMatrix(cam.combined);
        player.render(sb);

        tmr.setView(cam);
        tmr.render();

        if (debug) {
            box2DDebugRenderer.render(world, b2drCam.combined);
        }

        for (int i = 0; i < crystals.size; i++) {
                crystals.get(i).render(sb);
            }

        sb.setProjectionMatrix(hudCamera.combined);
        hud.render(sb);

    }

    public void createPlayer() {

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();


        bodyDef.position.set(150 / Game.PPM, 200 / Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(15 / Game.PPM, 25 / Game.PPM, new Vector2(-25 / Game.PPM, -10 / Game.PPM), 0);
        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = B2DVars.BIT_PLAYER;
        fixtureDef.filter.maskBits = B2DVars.BIT_RED | B2DVars.BIT_CRYSTAL;
        fixtureDef.shape = box;
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;
        body.createFixture(fixtureDef).setUserData("player");
        box.dispose();

        box.setAsBox(15 / Game.PPM, 5 / Game.PPM, new Vector2(-25 / Game.PPM, -35 / Game.PPM), 0);
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
                chainShape.dispose();
            }
        }
    }

    @Override
    public void dispose() {

    }


}
