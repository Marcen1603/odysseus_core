/**
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.recommendation.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * @author Cornelius Ludmann
 *
 */
@LogicalOperator(name = "EXTRACT_TEST_DATA", minInputPorts = 1, maxInputPorts = 1, category = { LogicalOperatorCategory.MINING }, doc = "This operator splits input data into learning and test data.")
public class SplitLearningTestDataAO extends AbstractLogicalOperator {

	/**
	 * The splitting strategy.
	 */
	private String strategy;

	/**
	 * Default constructor.
	 */
	public SplitLearningTestDataAO() {
		super();
	}

	public SplitLearningTestDataAO(final SplitLearningTestDataAO op) {
		super(op);
		this.strategy = op.strategy;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -943606943481375694L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
	 * #clone()
	 */
	@Override
	public AbstractLogicalOperator clone() {
		return new SplitLearningTestDataAO(this);
	}

	/**
	 * @return the strategy
	 */
	public String getStrategy() {
		return this.strategy;
	}

	/**
	 * @param strategy
	 *            the strategy to set
	 */
	@Parameter(name = "strategy", type = StringParameter.class, doc = "The strategy how the data should be splitted.", possibleValues = "getStrategyValues")
	public void setStrategy(final String strategy) {
		this.strategy = strategy;
	}

	public List<String> getStrategyValues() {
		final List<String> list = new ArrayList<String>();
		list.add("ITTT");
		list.add("Hold out");
		return list;
	}
}
