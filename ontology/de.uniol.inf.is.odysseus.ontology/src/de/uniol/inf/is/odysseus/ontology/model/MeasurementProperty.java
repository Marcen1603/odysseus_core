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
import de.uniol.inf.is.odysseus.probabilistic.math.Interval;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MeasurementProperty {
    public static enum Property {
        Accurancy(SSN.Accuracy), DetectionLimit(SSN.DetectionLimit), Drift(SSN.Drift), Frequency(SSN.Frequency), Latency(SSN.Latency), MeasurementRange(SSN.MeasurementRange), Precision(SSN.Precision), ResponseTime(
                SSN.ResponseTime), Resolution(SSN.Resolution), Sensitivity(SSN.Sensitivity), Selectivity(SSN.Selectivity);

        private Resource resource;

        // Constructor
        Property(Resource resource) {
            this.resource = resource;
        }

        Resource getResource() {
            return resource;
        }
    }

    private final Property property;
    private final Interval interval;
    private String unit;

    /**
     * Class constructor.
     * 
     * @param property
     * @param interval
     */
    public MeasurementProperty(final Property property, final Interval interval) {
        super();
        this.property = property;
        this.interval = interval;
    }

    /**
     * @return the property
     */
    public Property getProperty() {
        return this.property;
    }

    /**
     * @return the interval
     */
    public Interval getInterval() {
        return this.interval;
    }

    /**
     * @return the unit
     */
    public String getUnit() {
        return this.unit;
    }

    /**
     * @param unit
     *            the unit to set
     */
    public void setUnit(final String unit) {
        this.unit = unit;
    }

}
