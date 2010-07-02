package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.parser.pql.Parameter.REQUIREMENT;

/**
 * @author Jonas Jacobi
 */
public class AccessAOBuilder extends AbstractOperatorBuilder {

	private static final String SOURCE_NAME = "SOURCE";
	private final DirectParameter<String> sourceName;

	public AccessAOBuilder() {
		this.sourceName = new DirectParameter<String>(SOURCE_NAME,
				String.class, REQUIREMENT.MANDATORY);
		setParameters(sourceName);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 0);

		String sourceName = this.sourceName.getValue();
		return DataDictionary.getInstance().getView(sourceName);
	}
}
