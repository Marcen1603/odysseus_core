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
package de.uniol.inf.is.odysseus.ontology.model;

import com.hp.hpl.jena.rdf.model.Resource;

import de.uniol.inf.is.odysseus.ontology.ontology.vocabulary.SSN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public enum SSNMeasurementProperty {
	Accuracy(SSN.Accuracy), DetectionLimit(SSN.DetectionLimit), Drift(SSN.Drift), Frequency(SSN.Frequency), Latency(SSN.Latency), MeasurementRange(SSN.MeasurementRange), Precision(SSN.Precision), ResponseTime(
            SSN.ResponseTime), Resolution(SSN.Resolution), Sensitivity(SSN.Sensitivity), Selectivity(SSN.Selectivity);

    private Resource resource;

    // Constructor

    SSNMeasurementProperty(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return resource;
    }
}
