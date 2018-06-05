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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas.objects;

import org.lwjgl.opengl.GL11;

import de.uniol.inf.is.odysseus.rcp.dashboard.canvas.Coordinate;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Particle {

    /**
     * Class constructor.
     *
     */
    public Particle() {
        // TODO Auto-generated constructor stub
    }

    public void draw(final Coordinate coordinate) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0.0f, 0.0f);
        GL11.glVertex3d(coordinate.x, coordinate.y - 2, coordinate.z);
        GL11.glTexCoord2f(1.0f, 0.0f);
        GL11.glVertex3d(coordinate.x + 2, coordinate.y - 2, coordinate.z);
        GL11.glTexCoord2f(1.0f, 1.0f);
        GL11.glVertex3d(coordinate.x + 2, coordinate.y, coordinate.z);
        GL11.glTexCoord2f(0.0f, 1.0f);
        GL11.glVertex3d(coordinate.x, coordinate.y, coordinate.z);
        GL11.glEnd();
    }
}
