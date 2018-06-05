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
package de.uniol.inf.is.odysseus.probabilistic.metadata;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * Probabilistic meta data for probabilistic relational tuple.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public interface IProbabilistic extends IMetaAttribute, IClone {
    /**
     * Gets the value of the tuple existence. The tuple existence describes the
     * probability that the given tuple exists in the real world.
     * 
     * @return The tuple existence
     */
    double getExistence();

    /**
     * Sets the value of the tuple existence property. The tuple existence
     * describes the probability that the given tuple exists in the real world.
     * 
     * @param existence
     *            The tuple existence
     */
    void setExistence(double existence);

    /**
     * 
     * {@inheritDoc}
     */
//    @Override
//    IProbabilistic clone();

}
