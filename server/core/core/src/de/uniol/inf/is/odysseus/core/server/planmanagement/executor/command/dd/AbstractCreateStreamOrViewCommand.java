package de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionaryWritable;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IAccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.AbstractExecutorCommand;
import de.uniol.inf.is.odysseus.core.util.FindSourcesLogicalVisitor;
import de.uniol.inf.is.odysseus.core.util.GenericGraphWalker;
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

	protected void checkAccessAONames(IDataDictionaryWritable dd) {

		FindSourcesLogicalVisitor<ILogicalOperator> visitor = new FindSourcesLogicalVisitor<ILogicalOperator>();
		GenericGraphWalker<ILogicalOperator> walker = new GenericGraphWalker<ILogicalOperator>();
		walker.prefixWalk(rootAO, visitor);

		List<ILogicalOperator> accessAO = visitor.getResult();

		for (ILogicalOperator op : accessAO) {
			if (op instanceof IAccessAO) {
				IAccessAO operator = (IAccessAO) op;
				IAccessAO other = dd.getAccessAO(operator.getAccessAOName(), getCaller());
				if (other != null && !other.isSemanticallyEqual(operator)) {
					throw new TransformationException(
							"Duplicate AccessOperator with name " + operator.getAccessAOName() + "!");
				}
			}
		}

	}

	@Override
	public String toString() {
		return "Create VIEW " + name;
	}

	public String getName() {
		return name;
	}

}