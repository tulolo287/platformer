package com.platformer.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.platformer.handlers.GameStateManager;

public class Play extends GameState {

    private final BitmapFont font = new BitmapFont();
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    public Play(GameStateManager gsm) {
        super(gsm);
        world = new World(new Vector2(0, -9.81f), true);
        box2DDebugRenderer = new Box2DDebugRenderer();

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(50, 50);
        bodyDef.type = BodyDef.BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(50, 10);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef);
    }

    @Override
    public void handleUpdate() {

    }

    @Override
    public void update(float dt) {
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
