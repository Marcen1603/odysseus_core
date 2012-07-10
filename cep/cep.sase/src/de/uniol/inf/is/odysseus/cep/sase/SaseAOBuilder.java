/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.cep.sase;

import java.util.List;

import de.uniol.inf.is.odysseus.cep.PatternDetectAO;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

public class SaseAOBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -4928010950616650135L;

	private final StringParameter query = new StringParameter("QUERY",
			REQUIREMENT.MANDATORY);
	
	private final BooleanParameter oneMatchPerInstance = new BooleanParameter("OneMatchPerInstance",
			REQUIREMENT.OPTIONAL);
	
	private final IntegerParameter heartbeatrate = new IntegerParameter("heartbeatrate", REQUIREMENT.OPTIONAL);


	public SaseAOBuilder() {
		super("SASE", 1, Integer.MAX_VALUE);
		addParameters(query, oneMatchPerInstance, heartbeatrate);
	}

	@Override
	public IOperatorBuilder cleanCopy() {
		return new SaseAOBuilder();
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		SaseBuilder parser = new SaseBuilder();
		ILogicalOperator ret = null;
		List<ILogicalQuery> op = parser.parse(query.getValue(), getCaller(),
				getDataDictionary(), false);
		// I know there is only one operator
		ret = op.get(0).getLogicalPlan();
		if (oneMatchPerInstance.hasValue()){
			((PatternDetectAO<?>)ret).setOneMatchPerInstance(oneMatchPerInstance.getValue());
		}
		
		if (heartbeatrate.hasValue()){
			((PatternDetectAO<?>)ret).setHeartbeatRate(heartbeatrate.getValue());
		}

		return ret;
	}

}
