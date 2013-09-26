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
package de.uniol.inf.is.odysseus.wrapper.base.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "BINDING", category={LogicalOperatorCategory.SINK})
public class SinkAO extends AbstractLogicalOperator {

	/**
     * 
     */
	private static final long serialVersionUID = -2328459291978470344L;
	@SuppressWarnings("unused")
	private static Logger LOG = LoggerFactory.getLogger(SinkAO.class);
	private final Map<String, String> options = new HashMap<String, String>();
	private String adapter;

	/**
     * 
     */
	public SinkAO() {
		super();
	}

	/**
	 * @param ao
	 */
	public SinkAO(final SinkAO ao) {
		super(ao);
		this.adapter = ao.adapter;
		this.options.putAll(ao.options);
	}


	@Parameter(name = "OPTIONS", optional = true, type = StringParameter.class, isList = true)
	public void setOptions(final List<String> optionsList) {
		for (final String item : optionsList) {
			final String[] option = item.split(":");
			if (option.length == 2) {
				this.options.put(option[0],
						item.substring(option[0].length() + 1));
			} else {
				this.options.put(option[0], "");
			}
		}
	}

	@Parameter(name = "ADAPTER", type = StringParameter.class, isList = false)
	public void setAdapter(final String adapterName) {
		this.adapter = adapterName;
	}

	public String getAdapter() {
		return this.adapter;
	}

	public List<String> getOptions() {
		final List<String> optionList = new ArrayList<String>();
		for (final String key : this.options.keySet()) {
			optionList.add(key + ":" + this.options.get(key));
		}
		return optionList;
	}

	public Map<String, String> getOptionsMap() {
		return this.options;
	}

	@Override
	public SinkAO clone() {
		return new SinkAO(this);
	}

}
