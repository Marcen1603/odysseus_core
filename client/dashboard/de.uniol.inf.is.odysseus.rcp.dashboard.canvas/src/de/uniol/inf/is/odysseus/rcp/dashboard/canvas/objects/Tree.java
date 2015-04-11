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

import org.lwjgl.util.glu.Cylinder;
import org.lwjgl.util.glu.Quadric;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Tree extends Quadric {

    public Tree() {
        super();
    }

    public void draw(float trunkRadius, float foliageRadius, float height, int slices, int stacks) {
        Cylinder trunk = new Cylinder();
        trunk.setDrawStyle(getDrawStyle());
        trunk.draw(trunkRadius, trunkRadius, height / 3, slices, stacks / 3);
        Cylinder foliage = new Cylinder();
        foliage.setDrawStyle(getDrawStyle());
        foliage.draw(foliageRadius, 0, 2 * height / 3, slices, 2 * stacks / 3);
    }
}
