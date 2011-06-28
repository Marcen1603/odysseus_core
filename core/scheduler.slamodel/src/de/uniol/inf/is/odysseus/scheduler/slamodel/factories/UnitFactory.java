package de.uniol.inf.is.odysseus.scheduler.slamodel.factories;

import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.RatioUnit;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.ThroughputUnit;
import de.uniol.inf.is.odysseus.scheduler.slamodel.unit.TimeUnit;

public class UnitFactory {
	
	public Enum<?> buildUnit(String unitID) {
		Enum<?> unit = this.getEnumValue(RatioUnit.class, unitID);
		if (unit == null) {
			unit = this.getEnumValue(ThroughputUnit.class, unitID);
		} 
		if (unit == null) {
			unit = this.getEnumValue(TimeUnit.class, unitID);
		}
		if (unit == null) {
			throw new RuntimeException("not a valid unit: " + unitID);
		}
		return unit;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Enum<?> getEnumValue(Class enumType, String id) {
		try {
			return Enum.valueOf(enumType, id);
		} catch (Exception e) {
			return null;
		}
	}
	
}
