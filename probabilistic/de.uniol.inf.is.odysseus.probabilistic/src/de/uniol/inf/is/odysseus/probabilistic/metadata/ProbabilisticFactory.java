/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.metadata.AbstractMetadataUpdater;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class ProbabilisticFactory extends AbstractMetadataUpdater<IProbabilistic, Tuple<? extends IProbabilistic>> {

    int[] pos;

    public ProbabilisticFactory(final int pos) {
        this.pos = new int[] { pos };
    }

    public ProbabilisticFactory(final int[] pos) {
        this.pos = pos;
    }

    @Override
    public void updateMetadata(final Tuple<? extends IProbabilistic> inElem) {
        final IProbabilistic metadata = inElem.getMetadata();
        if (this.pos.length == 1) {
            for (int i = 0; i < inElem.size(); i++) {
                if (i != this.pos[0]) {
                    metadata.setProbability(i, (Double) inElem.getAttribute(this.pos[0]));
                }
                else {
                    metadata.setProbability(i, 1.0);
                }
            }
        }
        else {
            int index = 0;
            for (int i = 0; i < inElem.size(); i++) {
                if (i == this.pos[index]) {
                    metadata.setProbability(i, 1.0);
                    index++;
                }
                else {
                    metadata.setProbability(i, (Double) inElem.getAttribute(this.pos[index]));
                }
            }
        }
    }

}
