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
package de.uniol.inf.is.odysseus.probabilistic;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Datatype provider for probabilistic datatypes.
 * 
 * @author Christian Kuka <christian@kuka.cc>, Marco Grawunder
 */
public class ProbabilisticDatatypeProvider implements IDatatypeProvider {
    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public final List<SDFDatatype> getDatatypes() {
        final List<SDFDatatype> ret = new ArrayList<>();
        ret.add(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE);
        return ret;
    }

}
