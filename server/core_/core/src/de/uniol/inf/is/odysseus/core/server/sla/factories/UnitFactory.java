/*******************************************************************************
 * Copyright 2012 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.sla.factories;

import de.uniol.inf.is.odysseus.core.server.sla.unit.RatioUnit;
import de.uniol.inf.is.odysseus.core.server.sla.unit.ThroughputUnit;
import de.uniol.inf.is.odysseus.core.server.sla.unit.TimeUnit;

public class UnitFactory {
	
	public Enum<?> buildUnit(String unitID) {
		Enum<?> unit = UnitFactory.getEnumValue(RatioUnit.class, unitID);
		if (unit == null) {
			unit = UnitFactory.getEnumValue(ThroughputUnit.class, unitID);
		} 
		if (unit == null) {
			unit = UnitFactory.getEnumValue(TimeUnit.class, unitID);
		}
		if (unit == null) {
			throw new RuntimeException("not a valid unit: " + unitID);
		}
		return unit;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Enum<?> getEnumValue(Class enumType, String id) {
		try {
			return Enum.valueOf(enumType, id);
		} catch (Exception e) {
			return null;
		}
	}
	
}
