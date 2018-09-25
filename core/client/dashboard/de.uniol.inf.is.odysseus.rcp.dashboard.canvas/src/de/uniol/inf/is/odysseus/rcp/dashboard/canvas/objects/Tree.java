/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

    public void draw(final float trunkRadius, final float foliageRadius, final float height, final int slices, final int stacks) {
        final Cylinder trunk = new Cylinder();
        trunk.setDrawStyle(this.getDrawStyle());
        trunk.draw(trunkRadius, trunkRadius, height / 3, slices, stacks / 3);
        final Cylinder foliage = new Cylinder();
        foliage.setDrawStyle(this.getDrawStyle());
        foliage.draw(foliageRadius, 0, (2 * height) / 3, slices, (2 * stacks) / 3);
    }
}
