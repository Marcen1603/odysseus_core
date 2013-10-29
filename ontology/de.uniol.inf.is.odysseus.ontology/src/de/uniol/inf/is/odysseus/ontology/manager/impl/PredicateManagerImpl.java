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
package de.uniol.inf.is.odysseus.ontology.manager.impl;

import com.hp.hpl.jena.ontology.OntModel;

import de.uniol.inf.is.odysseus.ontology.model.SensingDevice;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */

public class PredicateManagerImpl {
    private final OntModel aBox;

    /**
     * Class constructor.
     * 
     */
    public PredicateManagerImpl(final OntModel aBox) {
        this.aBox = aBox;
    }

    public void getSurvivalRangePredicates() {

    }


    public void getConditionPredicates(final SensingDevice sensingDevice) {

    }

    @SuppressWarnings("unused")
    private OntModel getABox() {
        return this.aBox;
    }
}
