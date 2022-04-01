package com.platformer.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.platformer.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.platformer.handlers.B2DVars;

public class HUD {

    private Player player;
    private TextureRegion[] blocks;
    private ShapeRenderer sh;

    public HUD(Player player) {
        this.player = player;
        sh = new ShapeRenderer();
        sh.setAutoShapeType(true);

       /* Texture texture = Game.res.getTexture("hud");
        TextureRegion[] sprites = TextureRegion.split(texture, 100, 50)[0];
        setAnimation(sprites, 1 / 12f);*/


        /*blocks = new TextureRegion[3];
        for (int i = 0; i < blocks.length; i++) {
            blocks[i] = new TextureRegion(texture, 32 + i * 16, 0, 16, 16);
        }*/
    }

    public void render(SpriteBatch sb) {
        short bits = player.getBody().getFixtureList().first().getFilterData().maskBits;


        sh.begin(ShapeRenderer.ShapeType.Line);
        sh.setColor(1, 0, 0, 0.5f);
        sh.rect(0,0, 250, 250);
        sh.setColor(0, 1, 0, 0.5f);
        sh.rect(450,0, 250, 250);
        sh.setColor(0, 0, 1, 0.5f);
        sh.rect(2200,0, 250, 250);
      /*  if ((bits & B2DVars.BIT_RED) != 0) {
            sb.draw(blocks[0], 40, 200);
        }
        if ((bits & B2DVars.BIT_GREEN) != 0) {
            sb.draw(blocks[1], 40, 200);
        }
        if ((bits & B2DVars.BIT_BLUE) != 0) {
            sb.draw(blocks[2], 40, 200);
        }*/
        sh.end();

    }
}
