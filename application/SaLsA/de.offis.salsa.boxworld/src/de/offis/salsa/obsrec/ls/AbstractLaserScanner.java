package de.offis.salsa.obsrec.ls;

import de.offis.salsa.obsrec.LmsListener;
import de.offis.salsa.obsrec.Objectworld;

public abstract class AbstractLaserScanner implements LmsListener {
	protected Objectworld world = null;
	
	public AbstractLaserScanner(Objectworld world) {
		this.world = world;
	}
}
