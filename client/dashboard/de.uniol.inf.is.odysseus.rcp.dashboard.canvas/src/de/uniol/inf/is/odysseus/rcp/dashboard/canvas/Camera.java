/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
@SuppressWarnings("unused")
public class Camera {
    /** The position vector (the location of the camera). */
    private Vector3f position;
    /** The direction vector (the point to look at). */
    private final Vector3f direction;
    /** The up vector (the rotation of the camera around its origin). */
    private final Vector3f up;

    private float horizontalAngle;
    private float verticalAngle;

    /**
     * Class constructor.
     *
     */
    public Camera() {
        this.direction = new Vector3f();
        this.position = new Vector3f();
        this.up = new Vector3f();
        this.horizontalAngle = 0.0f;
        this.verticalAngle = 0.0f;
    }

    public void onKeyEvent() {
        final boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP) || Keyboard.isKeyDown(Keyboard.KEY_W);
        final boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN) || Keyboard.isKeyDown(Keyboard.KEY_S);
        final boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_A);
        final boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || Keyboard.isKeyDown(Keyboard.KEY_D);

    }

    /**
     * @return the position
     */
    public Vector3f getPosition() {
        return this.position;
    }

    /**
     * @param position
     *            the position to set
     */
    public void setPosition(final Vector3f position) {
        this.position = position;
    }

    /**
     * @param verticalAngle
     *            the verticalAngle to set
     */
    public void setVerticalAngle(final float verticalAngle) {
        this.verticalAngle = verticalAngle;
    }

    /**
     * @return the verticalAngle
     */
    public float getVerticalAngle() {
        return this.verticalAngle;
    }

    /**
     * @param horizontalAngle
     *            the horizontalAngle to set
     */
    public void setHorizontalAngle(final float horizontalAngle) {
        this.horizontalAngle = horizontalAngle;
    }

    /**
     * @return the horizontalAngle
     */
    public float getHorizontalAngle() {
        return this.horizontalAngle;
    }
}
