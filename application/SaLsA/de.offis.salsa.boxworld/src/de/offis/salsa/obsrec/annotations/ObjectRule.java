package de.offis.salsa.obsrec.annotations;

import de.offis.salsa.obsrec.TrackedObject.Type;

public @interface ObjectRule {
	Type objectType();
	String name();
}
