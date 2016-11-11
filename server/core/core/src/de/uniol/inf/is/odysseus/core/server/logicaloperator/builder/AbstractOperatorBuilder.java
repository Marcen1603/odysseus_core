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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * @author Jonas Jacobi
 */
public abstract class AbstractOperatorBuilder implements IOperatorBuilder {

	private static final long serialVersionUID = 5166465410911961680L;

	private static final String DEFAULT_DOC_TEXT = "No documentation available";
	private static final String DEFAULT_URL = "http://odysseus.informatik.uni-oldenburg.de:8090/pages/viewpage.action?pageId=4587829";

	private Set<IParameter<?>> parameters;
	private List<String> errors;
	private List<String> warnings;
	private int minPortCount;
	private int maxPortCount;
	private Map<Integer, InputOperatorItem> inputOperators;
	private ISession caller;
	private String[] categories;

	protected InputOperatorItem getInputOperatorItem(int i) {
		return inputOperators.get(i);
	}

	private IDataDictionary dataDictionary;
	private IServerExecutor executor;
	private Context context;
	private IMetaAttribute metaAttribute;

	private String name;
	private String doc;
	private String url;


	public AbstractOperatorBuilder(String name, int minPortCount,
			int maxPortCount) {
		this(name, minPortCount, maxPortCount, null, null, null);
	}

	public AbstractOperatorBuilder(String name, int minPortCount,
			int maxPortCount, String doc, String[] categories) {
		this(name, minPortCount, maxPortCount, doc, null, categories);
	}

	public AbstractOperatorBuilder(String name, int minPortCount,
			int maxPortCount, String doc, String url, String[] categories) {

		this.name = name;
		this.doc = doc;
		if (Strings.isNullOrEmpty(this.doc)) {
			this.doc = DEFAULT_DOC_TEXT;
		}
		this.url = url;
		if (Strings.isNullOrEmpty(this.url)) {
			this.url = DEFAULT_URL;
		}
		if (minPortCount > maxPortCount) {
			throw new IllegalArgumentException(
					"minimum number of ports may not be higher than maximum number");
		}
		this.minPortCount = minPortCount;
		this.maxPortCount = maxPortCount;
		this.parameters = new HashSet<IParameter<?>>();
		this.errors = new ArrayList<String>();
		this.warnings = new ArrayList<String>();
		this.inputOperators = new TreeMap<Integer, InputOperatorItem>();
		this.categories = categories;
	}

	public void addParameters(IParameter<?>... parameters) {
		for (IParameter<?> parameter : parameters) {
			if (this.parameters.contains(parameter)) {
				throw new IllegalArgumentException(
						"duplicate parameter definition: "
								+ parameter.getName());
			}
			this.parameters.add(parameter);
		}
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDoc() {
		return this.doc;
	}

	@Override
	public String getUrl() {
		return this.url;
	}

	protected void addError(String e) {
		this.errors.add(e);
	}

	protected void addErrors(List<String> errors) {
		this.errors.addAll(errors);
	}

	@Override
	public List<String> getErrors() {
		return this.errors;
	}

	@Override
	public List<String> getWarnings() {
		return this.warnings;
	}

	@Override
	public int getMaxInputOperatorCount() {
		return maxPortCount;
	}

	@Override
	public int getMinInputOperatorCount() {
		return minPortCount;
	}

	@Override
	public void setCaller(ISession caller) {
		this.caller = caller;
	}

	public ISession getCaller() {
		return caller;
	}

	@Override
	public void setDataDictionary(IDataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}

	public IDataDictionary getDataDictionary() {
		return dataDictionary;
	}

	@Override
	public void setServerExecutor(IServerExecutor executor) {
		this.executor = executor;
	}

	protected IServerExecutor getServerExecutor(){
		return executor;
	}

	@Override
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
		this.metaAttribute = metaAttribute;
	}

	public IMetaAttribute getMetaAttribute() {
		return metaAttribute;
	}

	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	@Override
	public boolean validate() {
		this.errors.clear();
		boolean isValid = true;

		// check input operators
		int inputOperatorCount = this.inputOperators.size();
		if (inputOperatorCount < minPortCount) {
			isValid = false;
			this.errors.add("need at least " + minPortCount + " input operators");
		}
		// > maxPortCount cannot occur, as this is checked in setInputOperator
		// TODO do input operators have to be contiguous with regard to
		// inputPorts
		// e.g. is input port 0, 2, 3 set, but 1 not set allowed?

		List<SDFSchema> inputSchema = new LinkedList<>();
		for (InputOperatorItem opItem : inputOperators.values()) {
			inputSchema.add(opItem.operator.getOutputSchema(opItem.outputPort));
		}
		DirectAttributeResolver attributeResolver = new DirectAttributeResolver(
				inputSchema);

		// check parameters
		for (IParameter<?> parameter : getParameters()) {

			parameter.setAttributeResolver(attributeResolver);
			parameter.setDataDictionary(getDataDictionary());
			parameter.setServerExecutor(getServerExecutor());
			parameter.setContext(getContext());
			parameter.setCaller(getCaller());
			if (!parameter.validate()) {
				isValid = false;
				this.errors.addAll(parameter.getErrors());
			}
			if (parameter.hasValue() && parameter.isDeprecated()) {
				this.warnings.add(this.getName()+": Parameter "
						+ parameter.getName() + " is deprecated!");
			}
		}

		if (isValid) {
			return internalValidation();
		}
		return isValid;
	}

	abstract protected boolean internalValidation();

	@Override
	public Set<IParameter<?>> getParameters() {
		return Collections.unmodifiableSet(this.parameters);
	}

	protected void checkInputSize(List<ILogicalOperator> inputOps,
			int inputPortCount) {
		if (inputOps.size() != inputPortCount) {
			throw new IllegalArgumentException("operator expects "
					+ inputPortCount + " inputs, but got " + inputOps.size());
		}
	}

	@Override
	final public ILogicalOperator createOperator() {
		// String newline = System.getProperty("line.separator");

		if (!validate()) {
			// StringBuffer messages = new StringBuffer();
			// for (Exception e2:getErrors()){
			// messages.append(e2.getMessage()).append(newline);
			// }
			// System.err.println("Validation Error "+messages.toString());
			throw new RuntimeException("Validation Error ");
		}

		ILogicalOperator op = createOperatorInternal();
		op.setMetadata(metaAttribute);
		for (Map.Entry<Integer, InputOperatorItem> curEntry : this.inputOperators
				.entrySet()) {
			InputOperatorItem curInputOperatorItem = curEntry.getValue();
			ILogicalOperator curInputOperator = curInputOperatorItem.operator;
			// Check if curInputOperator is a source
			if (curInputOperator.isSourceOperator()) {
				op.subscribeToSource(
						curInputOperator,
						curEntry.getKey(),
						curInputOperatorItem.outputPort,
						curInputOperator
								.getOutputSchema(curInputOperatorItem.outputPort));
			}else{
				throw new TransformationException(curInputOperator+" is not a source! Cannot connect to sink "+op);
			}
		}
		insertParameterInfos(op);
		return op;
	}

	final void insertParameterInfos(ILogicalOperator op) {
		// set all parameters as infos
		// Caution: Used in PQL-Generator to get parameter values
		for (IParameter<?> p : this.parameters) {
			if (p.hasValue()) {
				op.addParameterInfo(p.getName().toUpperCase(), p.getPQLString());
			}
		}
	}

	abstract protected ILogicalOperator createOperatorInternal();

	@Override
	public void setInputOperator(int inputPort, ILogicalOperator operator,
			int outputPort) {
		if (this.maxPortCount <= inputPort) {
			throw new IllegalArgumentException("illegal input port: "
					+ inputPort + " for operator " + name
					+ ". Tried to connect operator " + operator.getName());
		}
		if (operator != null) {
			this.inputOperators.put(inputPort, new InputOperatorItem(operator,
					outputPort));
		} else {
			this.inputOperators.remove(inputPort);
		}
	}

	public ILogicalOperator getInputOperator(int inputPort) {
		return this.inputOperators.get(inputPort).operator;
	}

	public void clearInputOperators() {
		this.inputOperators.clear();
	}

	public boolean hasInputOperator(int inputPort) {
		return this.inputOperators.containsKey(inputPort);
	}

	public int getInputOperatorCount() {
		return this.inputOperators.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder
	 * #getCategories()
	 */
	@Override
	public String[] getCategories() {
		return this.categories;
	}

}
