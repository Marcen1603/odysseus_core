package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.elementcloning;

public abstract class AbstractElementCloningUpdater implements
		IElementCloningUpdater {

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
}
