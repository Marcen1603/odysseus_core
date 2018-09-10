/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.common;

/**
 * Helper class for variable position handling.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class VarHelper {
    /** The position. */
    private final int pos;
    /** The object to use. */
    private final int objectPosToUse;

    /**
     * Default constructor.
     * 
     * @param pos
     *            The position.
     * @param objectPosToUse
     *            The object to use.
     * @param probabilisticValue
     *            Flag to indicate a probabilistic value.
     */
    public VarHelper(final int pos, final int objectPosToUse) {
        super();
        this.pos = pos;
        this.objectPosToUse = objectPosToUse;
    }

    /**
     * Gets the value of the pos property.
     * 
     * @return the pos
     */
    public final int getPos() {
        return this.pos;
    }

    /**
     * Gets the value of the objectPosToUse property.
     * 
     * @return the objectPosToUse
     */
    public final int getObjectPosToUse() {
        return this.objectPosToUse;
    }

}
