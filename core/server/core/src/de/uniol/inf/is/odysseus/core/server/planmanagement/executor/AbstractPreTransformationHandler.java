package de.uniol.inf.is.odysseus.core.server.planmanagement.executor;

/**
 * This is a base class for all PreTransformation handler
 * @author Marco Grawunder
 *
 */
public abstract class AbstractPreTransformationHandler implements IPreTransformationHandler {

	@Override
	public String getName() {
		return this.getClass().getSimpleName();
	}

}
