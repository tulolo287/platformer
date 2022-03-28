package com.platformer.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;

public class MyContactListener implements ContactListener {

    private int playerOnGround = 0;
    private Array<Body> crystalsToRemove;

    public MyContactListener() {
        super();
        crystalsToRemove = new Array<Body>();
    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround++;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround++;
        }

        if (fa.getUserData() != null && fa.getUserData().equals("crystal")) {
            crystalsToRemove.add(fa.getBody());
        }
        if (fb.getUserData() != null && fb.getUserData().equals("crystal")) {
            crystalsToRemove.add(fb.getBody());
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if (fa == null || fb == null) return;

        if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
            playerOnGround--;
        }
        if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
            playerOnGround--;
        }
    }

    public Array<Body> getCrystalsToRemove() {
        return crystalsToRemove;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean isPlayerOnGround() {return playerOnGround > 0;}
}
