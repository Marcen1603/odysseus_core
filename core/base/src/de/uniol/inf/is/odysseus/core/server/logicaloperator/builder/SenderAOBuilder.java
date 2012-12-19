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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.HashMap;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.WrapperRegistry;

/**
 * Operator builder for the {@link SenderAO}
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SenderAOBuilder extends AbstractOperatorBuilder {
	/**
     * 
     */
	private static final long serialVersionUID = 7352271768755308949L;
	/** The sink name */
	private final StringParameter sinkName = new StringParameter("SINK", REQUIREMENT.MANDATORY);
	/** Name of the wrapper */
	private final StringParameter wrapper = new StringParameter("WRAPPER", REQUIREMENT.OPTIONAL);
	/** Name of the data handler */
	private final StringParameter dataHandler = new StringParameter("DATAHANDLER", REQUIREMENT.OPTIONAL);
	/** Name of the transport handler */
	private final StringParameter transportHandler = new StringParameter("TRANSPORT", REQUIREMENT.OPTIONAL);
	/** Name of the protocol handler */
	private final StringParameter protocolHandler = new StringParameter("PROTOCOL", REQUIREMENT.OPTIONAL);

	/** Options as key value map */
	private final ListParameter<Option> options = new ListParameter<Option>("OPTIONS", REQUIREMENT.OPTIONAL, new CreateOptionParameter("OPTION", REQUIREMENT.MANDATORY));

	/**
	 * Default constructor for the operator builder
	 */
	public SenderAOBuilder() {
		super("SENDER", 1, Integer.MAX_VALUE);
		// Add the supported parameters
		this.addParameters(this.sinkName, this.options, this.dataHandler, this.transportHandler, this.protocolHandler, this.wrapper);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder
	 * #cleanCopy()
	 */
	@Override
	public IOperatorBuilder cleanCopy() {
		return new SenderAOBuilder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.
	 * AbstractOperatorBuilder#internalValidation()
	 */
	@Override
	protected boolean internalValidation() {
		if (!this.wrapper.hasValue()) {
			this.addError(new IllegalArgumentException("to less information for the creation of sender. expecting wrapper."));
			return false;
		} else {
			if (!WrapperRegistry.containsWrapper(this.wrapper.getValue())) {
				this.addError(new IllegalArgumentException("Wrapper " + this.wrapper.getValue() + " is unknown"));
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.
	 * AbstractOperatorBuilder#createOperatorInternal()
	 */
	@Override
	protected ILogicalOperator createOperatorInternal() {
		final String sinkName = this.sinkName.getValue();
		if (this.getDataDictionary().containsSink(sinkName, getCaller())) {
			return this.getDataDictionary().getSinkInput(sinkName, getCaller());
		}
		final ILogicalOperator ao = this.createNewSenderAO(sinkName);
		return ao;
	}

	/**
	 * @return A new instance of {@link SenderAO} with the defined parameter
	 */
	private ILogicalOperator createNewSenderAO(final String sinkName) {
		final HashMap<String, String> optionsMap = new HashMap<String, String>();
		if (this.options.hasValue()) {
			for (final Option option : this.options.getValue()) {
				optionsMap.put(option.getName().toLowerCase(), option.getValue());
			}
		}
		final SenderAO ao = new SenderAO(sinkName, this.wrapper.getValue(), optionsMap);
		if (this.dataHandler.hasValue()) {
			ao.setDataHandler(this.dataHandler.getValue());
		} else {
			ao.setDataHandler(null);
		}
		if (this.transportHandler.hasValue()) {
			ao.setTransportHandler(this.transportHandler.getValue());
		}
		if (this.protocolHandler.hasValue()) {
			ao.setProtocolHandler(this.protocolHandler.getValue());
		}
		return ao;
	}

}
