package de.uniol.inf.is.odysseus.iql.qdl.types.source;

import de.uniol.inf.is.odysseus.iql.qdl.types.subscription.Subscribable;

public interface Source extends Subscribable {

	public String getName();

	String getAttribute(String attribute);
}
