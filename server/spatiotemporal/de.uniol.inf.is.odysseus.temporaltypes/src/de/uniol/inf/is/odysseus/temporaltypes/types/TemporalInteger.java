package de.uniol.inf.is.odysseus.temporaltypes.types;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;

public class TemporalInteger implements IClone, Cloneable, Serializable {
	
	private static final long serialVersionUID = 6537783520942392777L;

	
	
	@Override
	public TemporalInteger clone() {
		return new TemporalInteger();
	}

}
