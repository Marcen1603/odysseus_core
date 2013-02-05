/********************************************************************************** 
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateOptionParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.Option;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpressionParseException;

/**
 * @author Dennis Geesen
 * 
 */
@LogicalOperator(name = "CONVOLUTION", minInputPorts = 1, maxInputPorts = 1)
public class ConvolutionFilterAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -8972289160969278298L;
	private SDFExpression expression;
	private List<SDFAttribute> attributes;
	private int size = 10;
	private String function;
	private Map<String, Double> options = new HashMap<>();

	public ConvolutionFilterAO() {
		super();
	}

	/**
	 * @param convolutionFilterAO
	 */
	public ConvolutionFilterAO(ConvolutionFilterAO ao) {
		super(ao);
		this.expression = ao.expression;
		this.attributes = ao.attributes;
		this.size = ao.size;
	}

	public int getSize() {
		return size;
	}

	@Parameter(name = "size", type = IntegerParameter.class)
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ConvolutionFilterAO(this);
	}

	public SDFExpression getExpression() {
		return expression;
	}

	@Parameter(type = ResolvedSDFAttributeParameter.class, name = "ATTRIBUTES", isList = true)
	public void setAttributes(List<SDFAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<SDFAttribute> getAttributes() {
		return this.attributes;
	}

	@Parameter(type = StringParameter.class, name = "function")
	public void setFunction(String function) {
		this.function = function.trim();
	}

	private SDFExpression getGaussianFunction(double mean, double deviation) {
		// String function = "(1/(" + sigma +
		// "*sqrt(2*PI())))*E()^((-(n^2))/(2*(" + sigma + ")^2))";
		String function = "(1/(" + deviation + "*sqrt(2*PI())))*E()^((-1/2)*((x-" + mean + ")/" + deviation + ")^2)";
		SDFExpression gauss = new SDFExpression(function, MEP.getInstance());
		return gauss;
	}

	private SDFExpression getLogarithmicFunction(double mean, double deviation) {
		// log must be the natural log (ln) with base e!
		String function = "(1/(" + deviation + "*sqrt(2*PI())))*(1/x)*E()^((-1/2)*((log(x-" + mean + "))/" + deviation + ")^2)";
		SDFExpression gauss = new SDFExpression(function, MEP.getInstance());
		return gauss;
	}

	private SDFExpression getExponentialFunction(double alpha) {
		String function = alpha + "*E()^(-" + alpha + "*x)";
		SDFExpression gauss = new SDFExpression(function, MEP.getInstance());
		return gauss;
	}

	private SDFExpression getMeanFunction(double size) {
		String function = "1/" + size;
		SDFExpression gauss = new SDFExpression(function, MEP.getInstance());
		return gauss;
	}

	@Parameter(name = "options", isList = true, type = CreateOptionParameter.class, optional = true)
	public void setOptions(List<Option> ops) {
		for (Option option : ops) {
			options.put(option.getName().toLowerCase(), Double.parseDouble(option.getValue()));
		}
	}

	@Override
	public void initialize() {
		super.initialize();
		if (this.function.equalsIgnoreCase("GAUSSIAN")) {
			if (!options.containsKey("mean")) {
				options.put("mean", 0.0);
			}
			if (!options.containsKey("deviation")) {
				options.put("deviation", 1.0);
			}
			this.expression = getGaussianFunction(options.get("mean"), options.get("deviation"));
		} else if (this.function.equalsIgnoreCase("LOGARITHMIC")) {
			if (!options.containsKey("mean")) {
				options.put("mean", 0.0);
			}
			if (!options.containsKey("deviation")) {
				options.put("deviation", 1.0);
			}
			this.expression = getLogarithmicFunction(options.get("mean"), options.get("deviation"));
		} else if (this.function.equalsIgnoreCase("UNIFORM")) {
			this.expression = getMeanFunction((getSize() * 2) + 1);
		} else if (this.function.equalsIgnoreCase("EXPONENTIAL")) {
			if (!options.containsKey("alpha")) {
				options.put("alpha", 1.0);
			}		
			this.expression = getExponentialFunction(options.get("alpha"));
		} else {
			this.expression = new SDFExpression(this.function, MEP.getInstance());
		}

	}

	@Override
	public boolean isValid() {

		if (this.function.equalsIgnoreCase("GAUSSIAN")) {
			return true;
		} else if (this.function.equalsIgnoreCase("LOGARITHMIC")) {
			return true;
		} else if (this.function.equalsIgnoreCase("UNIFORM")) {
			return true;
		} else if (this.function.equalsIgnoreCase("EXPONENTIAL")) {
			return true;
		} else {
			try {
				new SDFExpression(this.function, MEP.getInstance());
			} catch (SDFExpressionParseException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}

}
