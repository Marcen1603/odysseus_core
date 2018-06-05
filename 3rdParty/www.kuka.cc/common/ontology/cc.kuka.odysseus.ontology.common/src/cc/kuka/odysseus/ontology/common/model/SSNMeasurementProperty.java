/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.ontology.common.model;

import cc.kuka.odysseus.ontology.common.SSN;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public enum SSNMeasurementProperty {

	Accuracy(SSN.NS + "Accuracy"), //

	DetectionLimit(SSN.NS + "DetectionLimit"), //

	Drift(SSN.NS + "Drift"), //

	Frequency(SSN.NS + "Frequency"), //

	Latency(SSN.NS + "Latency"), //

	MeasurementRange(SSN.NS + "MeasurementRange"), //

	Precision(SSN.NS + "Precision"), //

	ResponseTime(SSN.NS + "ResponseTime"), //

	Resolution(SSN.NS + "Resolution"), //

	Sensitivity(SSN.NS + "Sensitivity"), //

	Selectivity(SSN.NS + "Selectivity"),//

	;

	private String uri;

	// Constructor

	SSNMeasurementProperty(final String uri) {
		this.uri = uri;
	}

	public String resource() {
		return this.uri;
	}
}
