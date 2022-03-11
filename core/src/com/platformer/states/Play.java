package com.platformer.states;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.platformer.handlers.GameStateManager;

public class Play extends GameState {

    private final BitmapFont font = new BitmapFont();

    public Play(GameStateManager gsm) {
        super(gsm);
    }

    @Override
    public void handleUpdate() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        font.draw(spriteBatch, "Hello", 100, 100);
        spriteBatch.end();
    }

    @Override
    public void dispose() {

    }


}
