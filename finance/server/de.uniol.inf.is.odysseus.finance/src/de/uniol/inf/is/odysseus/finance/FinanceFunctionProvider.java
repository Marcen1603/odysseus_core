/**********************************************************************************
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.finance;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.finance.mep.GetSampleSizeFunction;
import de.uniol.inf.is.odysseus.finance.mep.ValueAtRiskFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;

/**
 * 
 * @author Christoph Schröer
 *
 */
public class FinanceFunctionProvider implements IFunctionProvider {
	private static final Logger LOG = LoggerFactory.getLogger(FinanceFunctionProvider.class);

	public FinanceFunctionProvider() {

	}

	@Override
	public List<IMepFunction<?>> getFunctions() {

		final List<IMepFunction<?>> functions = new ArrayList<>();

		functions.add(new ValueAtRiskFunction());
		functions.add(new GetSampleSizeFunction());
		FinanceFunctionProvider.LOG.info(String.format("Register functions: %s", functions));
		return functions;
	}

}
