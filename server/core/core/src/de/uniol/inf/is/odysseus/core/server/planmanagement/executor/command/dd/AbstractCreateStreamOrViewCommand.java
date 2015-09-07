package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public abstract class AbstractCreateStreamOrViewCommand extends AbstractExecutorCommand {

	private static final long serialVersionUID = -8736984957038042363L;

	protected ILogicalOperator rootAO;
	protected String name;

	public AbstractCreateStreamOrViewCommand(String name, ILogicalOperator rootAO, ISession caller) {
		super(caller);
		this.rootAO = rootAO;
		this.name = name;
		if (!rootAO.isSourceOperator()) {
			throw new TransformationException("Can only create a view/stream on sources not on sinks");
		}
		if (name.contains("-")) {
			throw new TransformationException("'-' is not allowed in view/stream names");
		}
	}

	@Override
	public String toString() {
		return "Create VIEW " + name;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}