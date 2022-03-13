package com.platformer.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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

    public Play(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -0.81f), true);
        world.setContactListener(new MyContactListener());
        box2DDebugRenderer = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(150 / Game.PPM, 50 / Game.PPM);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50 / Game.PPM, 10 / Game.PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = B2DVars.BIT_GROUND;
        fixtureDef.filter.maskBits = B2DVars.BIT_BALL | B2DVars.BIT_BOX;
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData("ground");

        bodyDef.position.set(150 / Game.PPM, 200 / Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();
        box.setAsBox(5 / Game.PPM, 5 / Game.PPM);
        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = B2DVars.BIT_BOX;
        fixtureDef.filter.maskBits = B2DVars.BIT_GROUND;
        fixtureDef.shape = box;
        fixtureDef.density = 1;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef).setUserData("box");

        bodyDef.position.set(150 / Game.PPM, 250 / Game.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bodyDef);
        CircleShape ball = new CircleShape();
        ball.setRadius(7 / Game.PPM);
        fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = B2DVars.BIT_BALL;
        fixtureDef.filter.maskBits = B2DVars.BIT_GROUND;
        fixtureDef.shape = ball;
        fixtureDef.density = 1;
        fixtureDef.restitution = 1;
        body.createFixture(fixtureDef).setUserData("ball");
    }

    @Override
    public void handleUpdate() {

    }

    public void handleInput() {
        if (CustomInput.isDown(CustomInput.BUTTON1)) {
            System.out.println("z is down");
        }
        if (CustomInput.isPressed(CustomInput.BUTTON2)) {
            System.out.println("x is pressed");
        }
    }
    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);
    }

    @Override
    public void render() {
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
