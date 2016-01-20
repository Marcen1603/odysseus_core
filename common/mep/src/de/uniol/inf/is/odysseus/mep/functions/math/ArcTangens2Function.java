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
package de.uniol.inf.is.odysseus.mep.functions.math;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryNumberInputFunction;

/**
 * Returns the angle theta from the conversion of rectangular coordinates (x, y)
 * to polar coordinates (r, theta)
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class ArcTangens2Function extends
		AbstractBinaryNumberInputFunction<Double> {

	private static final long serialVersionUID = 1824826421970268927L;

	public ArcTangens2Function() {
		super("atan2", SDFDatatype.DOUBLE);
	}

	@Override
	public Double getValue() {
		Number a = getInputValue(0);
		Number b = getInputValue(1);
		if ((a == null) || (b == null)) {
			return null;
		}
		return Math.atan2(a.doubleValue(), b.doubleValue());
	}

}
