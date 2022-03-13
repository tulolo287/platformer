package com.platformer.handlers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class MyInputProcessor extends InputAdapter {

    public boolean keyDown(int k) {
        if (k == Input.Keys.Z) {
            CustomInput.setKey(CustomInput.BUTTON1, true);
        }
        if (k == Input.Keys.X) {
            CustomInput.setKey(CustomInput.BUTTON2, true);
        }
        return true;
    }

    public boolean keyUp(int k) {
        if (k == Input.Keys.Z) {
            CustomInput.setKey(CustomInput.BUTTON1, false);
        }
        if (k == Input.Keys.X) {
            CustomInput.setKey(CustomInput.BUTTON2, false);
        }
        return true;
    }
}
