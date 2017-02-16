package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

public abstract class AbstractPreTransformationHandler implements IPreTransformationHandler {

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
