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
package de.uniol.inf.is.odysseus.rcp.dashboard.canvas;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class Coordinate {
    public final double x;
    public final double y;
    public final double z;

    /**
     * Class constructor.
     *
     */
    public Coordinate(final double x, final double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    /**
     * Class constructor.
     *
     */
    public Coordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    /**
     * Class constructor.
     *
     */
    public Coordinate(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Class constructor.
     *
     */
    public Coordinate(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
