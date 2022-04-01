package com.platformer.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.platformer.handlers.Animation;
import com.platformer.handlers.B2DVars;

public class B2DSprite {
    protected Body body;
    protected Animation animation;
    protected float width;
    protected float height;
    public boolean flipX;

    public B2DSprite(Body body) {
        this.body = body;
        this.animation = new Animation();
    }

    public void setAnimation(TextureRegion[] reg, float delay) {
        animation.setFrames(reg, delay);
        width = reg[0].getRegionWidth();
        height = reg[0].getRegionHeight();
        flipX = true;
    }

    public void update(float dt) {
        animation.update(dt);
    }
//body.getPosition().x * B2DVars.PPM - width / 2, body.getPosition().y * B2DVars.PPM - height / 2, -123f, height
    public void render(SpriteBatch sb) {
        sb.begin();
        sb.draw(animation.getFrame(),
                flipX ? body.getPosition().x+width : body.getPosition().x, body.getPosition().y, flipX ? -width : width, height);
        sb.end();
    }

    public Body getBody() {
        return body;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Vector2 getPosition() {
        return body.getPosition();
    }
}
