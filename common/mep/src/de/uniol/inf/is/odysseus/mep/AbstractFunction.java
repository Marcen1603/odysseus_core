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
package de.uniol.inf.is.odysseus.mep;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.Constant;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionVisitor;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public abstract class AbstractFunction<T> implements IFunction<T> {

	static final Logger LOG = LoggerFactory.getLogger(AbstractFunction.class);

	private static final long serialVersionUID = 3805396798229438499L;
	private IExpression<?>[] arguments;
	private Map<String, Serializable> additionalContent = new HashMap<String, Serializable>();
	private IMetaAttribute[] metaAttribute = new IMetaAttribute[1];
	private TimeUnit baseTimeUnit = TimeUnit.MILLISECONDS;
	private final String symbol;
	private final int arity;
	private final SDFDatatype[][] acceptedTypes;
	private final SDFDatatype[] acceptedTypes2;
	private final SDFDatatype returnType;
	private final boolean optimizeConstantParameter;
	
	public AbstractFunction(String symbol, int arity,
			SDFDatatype[][] acceptedTypes, SDFDatatype returnType) {
		this(symbol, arity, acceptedTypes, returnType, true);
		if (optimizeConstantParameter == true && arity == 0) {
			LOG.warn("This function will be precompiled and creates the same value in each run.");
		}
	}

//	public AbstractFunction(String symbol, int arity, SDFDatatype returnType) {
//		this(symbol, arity, null, null, returnType, true);
//		if (optimizeConstantParameter == true && arity == 0) {
//			LOG.warn("This function will be precompiled and creates the same value in each run.");
//		}
//	}

//	@Deprecated
//	public AbstractFunction(String symbol, int arity,
//			SDFDatatype[] acceptedTypes, SDFDatatype returnType) {
//		this(symbol, arity, null, acceptedTypes, returnType, true);
//		if (optimizeConstantParameter == true && arity == 0) {
//			LOG.warn("This function will be precompiled and creates the same value in each run.");
//		}
//	}

	public AbstractFunction(String symbol, int arity,
			SDFDatatype[][] acceptedTypes, SDFDatatype returnType,
			boolean optimizeConstantParameter) {
		this(symbol, arity, acceptedTypes, null, returnType,
				optimizeConstantParameter);
	}

	private AbstractFunction(String symbol, int arity,
			SDFDatatype[][] acceptedTypes, SDFDatatype[] acceptedTypes2,
			SDFDatatype returnType, boolean optimizeConstantParameter) {
		this.symbol = symbol;
		this.arity = arity;
		this.optimizeConstantParameter = optimizeConstantParameter;
		this.acceptedTypes2 = acceptedTypes2;

		if (acceptedTypes != null) {
			this.acceptedTypes = acceptedTypes;
			if (acceptedTypes.length != arity) {
				throw new IllegalArgumentException(
						"Error: arity and types do not fit for " + symbol + " "
								+ this.getClass());
				// System.err.println("Error: arity and types do not fit for "+symbol+" "+this.getClass());
			}
		} else {
			this.acceptedTypes = getAllTypes(arity);
		}

		if (returnType != null) {
			this.returnType = returnType;
		} else {
			this.returnType = determineReturnType();
		}
	}

	protected static SDFDatatype[][] getAllTypes(int arity){
		SDFDatatype[][] types = new SDFDatatype[arity][];
		for (int i = 0; i < arity; i++) {
			types[i] = SDFDatatype.getTypes().toArray(
					new SDFDatatype[0]);
		}
		return types;
	}
	
	protected SDFDatatype determineReturnType() {
		return SDFDatatype.OBJECT;
	}

	@Override
	final public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException(
					"negative argument index not allowed");
		}
		if (argPos > arity) {
			throw new IllegalArgumentException(symbol + " has only " + arity
					+ " argument(s).");
		}
		if (acceptedTypes2 != null) {
			return acceptedTypes2;
		} else {
			return acceptedTypes[argPos];
		}
	}

	@Override
	final public String getSymbol() {
		return symbol;
	}

	@Override
	final public int getArity() {
		return arity;
	}

	@Override
	final public SDFDatatype getReturnType() {
		return returnType;
	}	

	@Override
	final public SDFDatatype getReturnType(SDFDatatype inputType){
		if (inputType.isListValue()){
			return inputType.getSubType();
		}
		return null;
	}
	
	@Override
	final public boolean optimizeConstantParameter() {
		return optimizeConstantParameter;
	}

	@Override
	final public void setArguments(IExpression<?>... arguments) {
		if (arguments.length != getArity()) {
			throw new IllegalArgumentException(
					"illegal number of arguments for function " + getSymbol());
		}

		this.arguments = new IExpression<?>[getArity()];
		for (int i = 0; i < arguments.length; i++) {
			setArgument(i, arguments[i]);
		}
	}

	@Override
	public void setArgument(int argumentPosition, IExpression<?> argument) {
		this.arguments[argumentPosition] = argument;
	}

	@Override
	public IExpression<?>[] getArguments() {
		return arguments;
	}

	@Override
	public Map<String, Serializable> getAdditionalContents() {
		return additionalContent;
	}

	@Override
	public Serializable getAdditionalContent(String name) {
		return additionalContent.get(name);
	}

	@Override
	public IMetaAttribute[] getMetaAttributeContainer() {
		return metaAttribute;
	}
	
	@Override
	public IMetaAttribute getMetaAttribute() {
		return metaAttribute[0];
	}

	@SuppressWarnings("unchecked")
	final protected <S> S getInputValue(int argumentPos) {
		return (S) arguments[argumentPos].getValue();
	}

	final protected IMetaAttribute getInputMetadata(int argumentPos) {
		return ((Variable) arguments[argumentPos]).getMetadata();
	}

    final protected int getInputPosition(int argumentPos) {
        return ((Variable) arguments[argumentPos]).getPosition();
    }
	   
	final protected Double getNumericalInputValue(int argumentPos) {
		return ((Number) arguments[argumentPos].getValue()).doubleValue();
	}
	
	@Override
    public void propagateAdditionalContentReference(final Map<String, Serializable> additionalContent) {
        this.additionalContent = additionalContent;
        final IExpression<?>[] arguments = this.getArguments();
        for (final IExpression<?> arg : arguments) {
            if (arg instanceof AbstractFunction) {
                ((AbstractFunction<?>) arg).propagateAdditionalContentReference(additionalContent);
            }
        }
    }
    
	@Override
    public void propagateMetadataReference(final IMetaAttribute[] metaAttribute) {
        this.metaAttribute = metaAttribute;
        final IExpression<?>[] arguments = this.getArguments();
        for (final IExpression<?> arg : arguments) {
            if (arg instanceof AbstractFunction) {
                ((AbstractFunction<?>) arg).propagateMetadataReference(metaAttribute);
            }
        }
    }
    
	@Override
	public Object acceptVisitor(IExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public Set<Variable> getVariables() {
		Set<Variable> variables = new HashSet<Variable>();
		for (int i = 0; i < getArity(); ++i) {
			variables.addAll(getArguments()[i].getVariables());
		}
		return variables;
	}

	@Override
	public Variable getVariable(String name) {
		Set<Variable> variables = getVariables();
		for (Variable curVar : variables) {
			if (curVar.getIdentifier().equals(name)) {
				return curVar;
			}
		}
		return null;
	}

	@Override
	final public String toString() {

		String ret = _internalToString();
		if (ret != null) {
			return ret;
		}

		StringBuilder builder = new StringBuilder(getSymbol());
		builder.append('(');
		if (getArity() > 0) {
			if ((getArguments() != null) && (getArguments().length > 0)) {
				builder.append(getArguments()[0].toString());
				for (int i = 1; i < getArity(); ++i) {
					builder.append(", ");
					builder.append(getArguments()[i].toString());
				}
			}
		}
		builder.append(')');
		return builder.toString();
	}

	/**
	 * Use this method to overwrite the string representation of the
	 * operator/function WARNING: Do only overwrite if needed and if you know
	 * what you are doing ;-)
	 * 
	 * @return internal Representation
	 */
	protected String _internalToString() {
		return null;
	}

	@Override
	public IExpression<?> getArgument(int argumentPosition) {
		return this.arguments[argumentPosition];
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public Variable toVariable() {
		throw new RuntimeException("cannot convert IFunction to Variable");
	}

	@Override
	public IFunction<T> toFunction() {
		return this;
	}

	@Override
	public Constant<T> toConstant() {
		throw new RuntimeException("cannot convert IFunction to Constant");
	}

	@Override
	public void setBasetimeUnit(TimeUnit baseTimeUnit) {
		this.baseTimeUnit = baseTimeUnit;
	}

	public TimeUnit getBaseTimeUnit() {
		return baseTimeUnit;
	}

	@Override
	public int getReturnTypeCard() {
		return 1;
	}

	@Override
	public SDFDatatype getReturnType(int pos) {
		return getReturnType();
	}

	@Override
	public boolean determineTypeFromFirstInput() {
		return false;
	}

}
