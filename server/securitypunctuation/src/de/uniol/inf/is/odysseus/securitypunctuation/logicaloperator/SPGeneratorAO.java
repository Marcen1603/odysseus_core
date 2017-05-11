package de.uniol.inf.is.odysseus.securitypunctuation.logicaloperator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;

@LogicalOperator(name = "generatesp", minInputPorts = 0, maxInputPorts = 1, doc = "Generates SPs and adds them to the Datastream randomly", url = "-", category = {
		LogicalOperatorCategory.BASE })
public class SPGeneratorAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 0L;
	boolean secondStream;
	

	public SPGeneratorAO() {
		super();
	}


	public boolean isSecondStream() {
		return secondStream;
	}

	public void setSecondStream(boolean secondStream) {
		this.secondStream = secondStream;
	}

	public SPGeneratorAO(SPGeneratorAO spGeneratorAO) {
		super(spGeneratorAO);
	}

	@Override
	public AbstractLogicalOperator clone() {

		return new SPGeneratorAO(this);
	}

}