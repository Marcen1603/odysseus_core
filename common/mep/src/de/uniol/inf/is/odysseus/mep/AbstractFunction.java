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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpressionVisitor;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;
import de.uniol.inf.is.odysseus.mep.optimizer.BooleanExpressionOptimizer;

public abstract class AbstractFunction<T> extends AbstractExpression<T> implements IMepFunction<T> {

	static final Logger LOG = LoggerFactory.getLogger(AbstractFunction.class);

	private static final long serialVersionUID = 3805396798229438499L;
	private IMepExpression<?>[] arguments;
	private TimeUnit baseTimeUnit = TimeUnit.MILLISECONDS;
	private final String symbol;
	private final int arity;
	private final SDFDatatype[][] acceptedTypes;
	private final SDFDatatype returnType;
	private final boolean optimizeConstantParameter;
	private final int timeComplexity;
	private final int spaceComplexity;

	private List<ISession> sessions;

	protected static final Map<String, Object> keyValueStore = new HashMap<String, Object>();
	// Replaced with general Key Value Store concept
	//	protected static final Map<String, Map<String, Object>> namedkeyValueStore = new HashMap<String, Map<String,Object>>();


	public AbstractFunction(String symbol, int arity, SDFDatatype[][] acceptedTypes, SDFDatatype returnType) {
		this(symbol, arity, acceptedTypes, returnType, true);
		if (optimizeConstantParameter == true && arity == 0) {
			LOG.warn("This function will be precompiled and creates the same value in each run.");
		}
	}

	public AbstractFunction(String symbol, int arity, SDFDatatype[][] acceptedTypes, SDFDatatype returnType,
			boolean optimizeConstantParameter) {
		this(symbol, arity, acceptedTypes, returnType, optimizeConstantParameter, 9, 9);
	}

	public AbstractFunction(String symbol, int arity, SDFDatatype[][] acceptedTypes, SDFDatatype returnType,
			int timeComplexity, int spaceComplexity) {
		this(symbol, arity, acceptedTypes, returnType, true, timeComplexity, spaceComplexity);
	}

	public AbstractFunction(String symbol, int arity, SDFDatatype[][] acceptedTypes, SDFDatatype returnType,
			boolean optimizeConstantParameter, int timeComplexity, int spaceComplexity) {
		this.symbol = symbol;
		this.arity = arity;
		this.optimizeConstantParameter = optimizeConstantParameter;
		this.timeComplexity = timeComplexity;
		this.spaceComplexity = spaceComplexity;

		if (acceptedTypes != null) {
			this.acceptedTypes = acceptedTypes;
			if (acceptedTypes.length != arity) {
				throw new IllegalArgumentException(
						"Error: arity and types do not fit for " + symbol + " " + this.getClass());
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

	protected AbstractFunction(AbstractFunction<T> other) {
		// Remark: Arguments cannot be copied here! Must be done
		// external, else Variables are not correctly cloned

		this.baseTimeUnit = other.baseTimeUnit;
		this.symbol = other.symbol;
		this.arity = other.arity;
		this.acceptedTypes = other.acceptedTypes;
		this.returnType = other.returnType;
		this.optimizeConstantParameter = other.optimizeConstantParameter;
		this.timeComplexity = other.timeComplexity;
		this.spaceComplexity = other.spaceComplexity;
		if (other.sessions != null) {
			this.sessions = new ArrayList<>(other.sessions);
		}
	}

	protected static SDFDatatype[][] getAllTypes(int arity) {
		SDFDatatype[][] types = new SDFDatatype[arity][];
		for (int i = 0; i < arity; i++) {
			types[i] = SDFDatatype.getTypes().toArray(new SDFDatatype[0]);
		}
		return types;
	}

	protected SDFDatatype determineReturnType() {
		return SDFDatatype.OBJECT;
	}

	@Override
	final public SDFDatatype[] getAcceptedTypes(int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > arity) {
			throw new IllegalArgumentException(symbol + " has only " + arity + " argument(s).");
		}
		return Arrays.copyOf(acceptedTypes[argPos], acceptedTypes[argPos].length);
	}

	@Override
	final public String getSymbol() {
		return symbol;
	}

	@Override
	final public int getArity() {
		return arity;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	final public int getTimeComplexity() {
		int complexity = this.timeComplexity;
		for (int i = 0; i < getArity(); ++i) {
			if (getArguments()[i].isFunction()) {
				complexity += getArguments()[i].toFunction().getTimeComplexity();
			}
		}
		return complexity;
	}

	/**
	 *
	 * {@inheritDoc}
	 */
	@Override
	final public int getSpaceComplexity() {
		int complexity = this.spaceComplexity;
		for (int i = 0; i < getArity(); ++i) {
			if (getArguments()[i].isFunction()) {
				complexity += getArguments()[i].toFunction().getSpaceComplexity();
			}
		}
		return complexity;
	}

	@Override
	final public SDFDatatype getReturnType() {
		if (determineTypeFromInput()) {
			if (arguments != null) {
				return determineType(arguments);
			}
		} else {
			// Must be called for cases where the subtype need argument
			// determination
			if (arguments != null) {
				initSubTypes(arguments);
			}
		}
		return returnType;
	}

	@Override
	final public boolean optimizeConstantParameter() {
		return optimizeConstantParameter;
	}

	@Override
	final public void setArguments(IMepExpression<?>... arguments) {
		if (arguments.length != getArity()) {
			throw new IllegalArgumentException("illegal number of arguments for function " + getSymbol());
		}

		this.arguments = new IMepExpression<?>[getArity()];
		for (int i = 0; i < arguments.length; i++) {
			setArgument(i, arguments[i]);
		}
	}

	@Override
	public void setArgument(int argumentPosition, IMepExpression<?> argument) {
		this.arguments[argumentPosition] = argument;
	}

	@Override
	public IMepExpression<?>[] getArguments() {
		return arguments;
	}

	@Override
	public List<ISession> getSessions() {
		return sessions;
	}

	@Override
	public void setSessions(List<ISession> sessions) {
		this.sessions = sessions;
	}

	@SuppressWarnings("unchecked")
	final protected <S> S getInputValue(int argumentPos) {
		return (S) arguments[argumentPos].getValue();
	}

	final protected int getInputPosition(int argumentPos) {
		return ((IMepVariable) arguments[argumentPos]).getPosition();
	}

	final protected Double getNumericalInputValue(int argumentPos) {
		try {
			if (arguments[argumentPos].getValue() == null)
				return null;
			double val = ((Number) arguments[argumentPos].getValue()).doubleValue();
			return val;
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("Input \"" + arguments[argumentPos].getValue() + "\" is not a number!");
		}
	}

	@Override
	public void propagateSessionReference(final List<ISession> sessions) {
		this.sessions = sessions;
		final IMepExpression<?>[] arguments = this.getArguments();
		for (final IMepExpression<?> arg : arguments) {
			if (arg instanceof AbstractFunction) {
				((AbstractFunction<?>) arg).propagateSessionReference(sessions);
			}
		}
	}

	@Override
	public Object acceptVisitor(IMepExpressionVisitor visitor, Object data) {
		return visitor.visit(this, data);
	}

	@Override
	public Set<IMepVariable> getVariables() {
		Set<IMepVariable> variables = new HashSet<IMepVariable>();
		for (int i = 0; i < getArity(); ++i) {
			variables.addAll(getArguments()[i].getVariables());
		}
		return variables;
	}

	@Override
	public IMepVariable getVariable(String name) {
		Set<IMepVariable> variables = getVariables();
		for (IMepVariable curVar : variables) {
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
	public IMepExpression<?> getArgument(int argumentPosition) {
		return this.arguments[argumentPosition];
	}


	@Override
	public boolean isFunction() {
		return true;
	}

	@Override
	public IMepFunction<T> toFunction() {
		return this;
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
	public boolean determineTypeFromInput() {
		return false;
	}

	@Override
	public SDFDatatype determineType(IMepExpression<?>[] args) {
		throw new RuntimeException("Function " + this + " must implement determineType function");
	}

	private void initSubTypes(IMepExpression<?>[] args) {
		// Must be called to init subtypes that are potentially created when
		// calling determine subtype
		for (IMepExpression<?> arg : args) {
			arg.getReturnType();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public IMepExpression<T> clone(Map<IMepVariable, IMepVariable> vars) {
		// Remark: stateful functions must override this method!
		try {
			AbstractFunction<T> newFunction = null;
			try {
				Constructor<?> cons = getClass().getConstructor(this.getClass());
				newFunction = (AbstractFunction<T>) cons.newInstance(this);
			} catch (NoSuchMethodException | SecurityException | IllegalArgumentException
					| InvocationTargetException e) {
				if (LOG.isTraceEnabled()) {
					LOG.trace("No CC for " + this.getClass());
				}
			}
			// Fallback when calling copy constructor fails
			if (newFunction == null) {
				newFunction = getClass().newInstance();
				newFunction.baseTimeUnit = this.baseTimeUnit;
				if (this.sessions != null) {
					newFunction.sessions = new ArrayList<>(this.sessions);
				}
			}
			// Important: Arguments cannot be set in constructor else there is
			// no chance, that each occurrence of the same variable is the
			// same Java instance in this expression
			newFunction.arguments = new IMepExpression<?>[this.arguments.length];
			for (int i = 0; i < arguments.length; i++) {
				newFunction.arguments[i] = arguments[i].clone(vars);
			}

			return newFunction;
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean cnfEquals(IMepExpression<?> otherExpression) {
		IMepExpression<?> cnf1 = BooleanExpressionOptimizer
				.sortByStringExpressions(BooleanExpressionOptimizer.toConjunctiveNormalForm(this));
		IMepExpression<?> cnf2 = BooleanExpressionOptimizer
				.sortByStringExpressions(BooleanExpressionOptimizer.toConjunctiveNormalForm(otherExpression));
		return cnf1.toString().equals(cnf2.toString());
	}

	@Override
	public boolean isAndPredicate() {
		return isFunction() && this instanceof AndOperator;
	}

	@Override
	public boolean isOrPredicate() {
		return isFunction() && this instanceof OrOperator;
	}

	@Override
	public boolean isNotPredicate() {
		return isFunction() && this instanceof NotOperator;
	}

	@Override
	public boolean isContainedIn(IMepExpression<?> otherExpression) {
		if (!(otherExpression instanceof IMepFunction)) {
			return false;
		}
		IMepFunction<?> otherFunction = (IMepFunction<?>) otherExpression;

		if (this.isAndPredicate()) {
			// TODO: FIX IS CONTAINED IN

			// Z.B. ist a in b enthalten, falls a= M && N und b = M oder b=N ist
			if (!otherFunction.isAndPredicate()) {
				// @SuppressWarnings("rawtypes")
				//
				// List<IFunction> spred =
				// BooleanExpressionOptimizer.toConjunctiveNormalForm(otherExpression);
				//
				// for (IFunction<?> p : spred) {
				// if (p.isContainedIn(otherFunction)) {
				// return true;
				// }
				// }
				// return false;
			}
			return false;
		}
		// OR
		// TODO: Aus dem alten OR-Predicate extrahieren
		if (this.isOrPredicate()) {
			return false;
		}
		// NOT
		// TODO: Geht das besser?
		if (this.isNotPredicate()) {
			if (otherFunction.isNotPredicate()) {
				return false;
			}
			return false;
		}
		// BASIS-Pr���dikat
		// Unterschiedliche Anzahl Attribute
		if (this.getArguments().length != otherFunction.getArguments().length) {
			return false;
		}
		IMepExpression<?> ex1 = this;
		IMepExpression<?> ex2 = otherFunction;
		if (ex1.getReturnType().equals(ex2.getReturnType()) && ex1.isFunction()) {
			IMepFunction<?> if1 = (IMepFunction<?>) ex1;
			IMepFunction<?> if2 = (IMepFunction<?>) ex2;
			if (if1.getArity() != 2) {
				return false;
			}

			IMepExpression<?> firstArgument1 = if1.getArgument(0);
			IMepExpression<?> secondArgument1 = if1.getArgument(1);
			IMepExpression<?> firstArgument2 = if2.getArgument(0);
			IMepExpression<?> secondArgument2 = if2.getArgument(1);

			String symbol1 = if1.getSymbol();
			String symbol2 = if2.getSymbol();

			// TODO: Optimize the following ...
			// Funktionen enthalten nur ein Attribut und ansonsten
			// Konstanten
			if (getArguments().length == 1) {

				// gleiches Attribut auf der linken Seite, Konstante auf der
				// rechten
				if (firstArgument1.isVariable() && secondArgument1.isConstant() && firstArgument2.isVariable()
						&& secondArgument2.isConstant()
						&& firstArgument1.toVariable().equals(firstArgument2.toVariable())) {
					Double c1;
					Double c2;
					try {
						c1 = Double.parseDouble(secondArgument1.toString());
						c2 = Double.parseDouble(secondArgument2.toString());
					} catch (Exception e) {
						return false;
					}

					// Funktion kleiner-als
					if (symbol1.equals("<") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 <= c2) {
						return true;
					}
					// Funktion kleiner-gleich-als
					if (symbol1.equals("<=")
							&& ((symbol2.equals("<=") && c1 <= c2) || (symbol2.equals("<") && c1 < c2))) {
						return true;
					}
					// Funktion ist-gleich oder ist-nicht-gleich
					if (((symbol1.equals("==") && symbol2.equals("=="))
							|| (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
						return true;
					}
					// Funktion gr������er-als
					if (symbol1.equals(">") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 >= c2) {
						return true;
					}
					// Funktion gr������er-gleich-als
					if (symbol1.equals(">=")
							&& ((symbol2.equals(">=") && c1 >= c2) || (symbol2.equals(">") && c1 > c2))) {
						return true;
					}
					// gleiches Attribut auf der rechten Seite, Konstante
					// auf der linken Seite
				} else if (secondArgument1.isVariable() && firstArgument1.isConstant() && secondArgument2.isVariable()
						&& firstArgument2.isConstant()
						&& secondArgument1.toVariable().equals(secondArgument2.toVariable())) {

					Double c1;
					Double c2;
					try {
						c1 = Double.parseDouble(firstArgument1.toString());
						c2 = Double.parseDouble(firstArgument2.toString());
					} catch (Exception e) {
						return false;
					}

					// Funktion kleiner-als
					if (symbol1.equals("<") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 >= c2) {
						return true;
					}
					// Funktion kleiner-gleich-als
					if (symbol1.equals("<=")
							&& ((symbol2.equals("<=") && c1 >= c2) || (symbol2.equals("<") && c1 > c2))) {
						return true;
					}
					// Funktion ist-gleich oder ist-nicht-gleich
					if (((symbol1.equals("==") && symbol2.equals("=="))
							|| (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
						return true;
					}
					// Funktion gr������er-als
					if (symbol1.equals(">") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 <= c2) {
						return true;
					}
					// Funktion gr������er-gleich-als
					if (symbol1.equals(">=")
							&& ((symbol2.equals(">=") && c1 <= c2) || (symbol2.equals(">") && c1 < c2))) {
						return true;
					}
					// Attribut bei F1 links, bei F2 rechts
				} else if (firstArgument1.isVariable() && secondArgument1.isConstant() && secondArgument2.isVariable()
						&& firstArgument2.isConstant()
						&& firstArgument1.toVariable().equals(secondArgument2.toVariable())) {
					Double c1;
					Double c2;
					try {
						c1 = Double.parseDouble(secondArgument1.toString());
						c2 = Double.parseDouble(firstArgument2.toString());
					} catch (Exception e) {
						return false;
					}

					// F1 kleiner-als, F2 gr������er-als oder
					// gr������er-gleich-als
					if (symbol1.equals("<") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 <= c2) {
						return true;
					}
					// F1 kleiner-gleich-als, F2 gr������er-als oder
					// gr������er-gleich-als
					if (symbol1.equals("<=") && (symbol2.equals(">") && c1 < c2)
							|| (symbol2.equals(">=")) && c1 <= c2) {
						return true;
					}
					// Funktion ist-gleich oder ist-nicht-gleich
					if (((symbol1.equals("==") && symbol2.equals("=="))
							|| (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
						return true;
					}
					// F1 gr������er-als, F2 kleiner-als oder
					// kleiner-gleich-als
					if (symbol1.equals(">") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 >= c2) {
						return true;
					}
					// F1 gr������er-gleich-als, F2 kleiner-als oder
					// kleiner-gleich-als
					if (symbol1.equals(">=") && (symbol2.equals("<") && c1 > c2)
							|| (symbol2.equals("<=")) && c1 >= c2) {
						return true;
					}

					// Attribut bei F1 rechts, bei F2 links
				} else if (secondArgument1.isVariable() && firstArgument1.isConstant() && firstArgument2.isVariable()
						&& secondArgument2.isConstant()
						&& secondArgument1.toVariable().equals(firstArgument2.toVariable())) {

					Double c1;
					Double c2;
					try {
						c1 = Double.parseDouble(firstArgument1.toString());
						c2 = Double.parseDouble(secondArgument2.toString());
					} catch (Exception e) {
						return false;
					}

					// F1 kleiner-als, F2 gr������er-als oder
					// gr������er-gleich-als
					if (symbol1.equals("<") && (symbol2.equals(">") || symbol2.equals(">=")) && c1 >= c2) {
						return true;
					}

					// F1 kleiner-gleich-als, F2 gr������er-als oder
					// gr������er-gleich-als
					if (symbol1.equals("<=") && (symbol2.equals(">") && c1 > c2)
							|| (symbol2.equals(">=")) && c1 >= c2) {
						return true;
					}

					// Funktion ist-gleich oder ist-nicht-gleich
					if (((symbol1.equals("==") && symbol2.equals("=="))
							|| (symbol1.equals("!=") && symbol2.equals("!="))) && c1.compareTo(c2) == 0) {
						return true;
					}

					// F1 gr������er-als, F2 kleiner-als oder
					// kleiner-gleich-als
					if (symbol1.equals(">") && (symbol2.equals("<") || symbol2.equals("<=")) && c1 <= c2) {
						return true;
					}

					// F1 gr������er-gleich-als, F2 kleiner-als oder
					// kleiner-gleich-als
					if (symbol1.equals(">=") && (symbol2.equals("<") && c1 < c2)
							|| (symbol2.equals("<=")) && c1 <= c2) {
						return true;
					}
				}
				// Funktionen sind Vergleiche zwischen zwei Attributen
			} else if (getArguments().length == 2 && firstArgument1.isVariable() && secondArgument1.isVariable()
					&& firstArgument2.isVariable() && secondArgument2.isVariable()) {
				IMepVariable v11 = firstArgument1.toVariable();
				IMepVariable v12 = secondArgument1.toVariable();
				IMepVariable v21 = firstArgument2.toVariable();
				IMepVariable v22 = secondArgument2.toVariable();

				// Attribute sind links und rechts gleich
				if (v11.equals(v21) && v12.equals(v22)) {
					if ((symbol1.equals("<") && symbol2.equals("<")) || (symbol1.equals("<=") && symbol2.equals("<="))
							|| (symbol1.equals("==") && symbol2.equals("=="))
							|| (symbol1.equals("!=") && symbol2.equals("!="))
							|| (symbol1.equals(">=") && symbol2.equals(">="))
							|| (symbol1.equals(">") && symbol2.equals(">"))) {
						return true;
					}
				}
				// linkes Attribut von F1 ist gleich rechtem Attribut von F2
				// und umgekehrt
				if (v11.equals(v22) && v12.equals(v21)) {
					if ((symbol1.equals("<") && symbol2.equals(">")) || (symbol1.equals("<=") && symbol2.equals(">="))
							|| (symbol1.equals("==") && symbol2.equals("=="))
							|| (symbol1.equals("!=") && symbol2.equals("!="))
							|| (symbol1.equals(">=") && symbol2.equals("<="))
							|| (symbol1.equals(">") && symbol2.equals("<"))) {
						return true;
					}
				}

				return false;
			}
		}
		return false;

	}



}
