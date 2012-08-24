package de.offis.salsa.obsrec.ls;

import de.offis.salsa.obsrec.LmsListener;
import de.offis.salsa.obsrec.Objektwelt;

public abstract class AbstractLaserScanner implements LmsListener {
	protected Objektwelt world = null;
	
	public AbstractLaserScanner(Objektwelt world) {
		this.world = world;
	}
}
