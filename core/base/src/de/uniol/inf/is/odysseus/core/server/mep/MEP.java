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
package de.uniol.inf.is.odysseus.core.server.mep;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.server.mep.functions.AbsoluteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.CeilFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ContainsFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.CosinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DebsDateFormatParse;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DivisionOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DoubleToFloatFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DoubleToLongFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.EqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.FloorFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.GetAbsoluteValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.GreaterThanOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.IfFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.IsNullFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.LikeFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ModuloOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MultiplicationOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NotEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NotOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.OrOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.PlusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.PowerOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.RandomFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.RoundFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SmallerThanOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SqrtValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SquareValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToDegrees;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToLongFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToNumberFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToRadians;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToStringFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.UnaryMinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.impl.ExpressionBuilderVisitor;
import de.uniol.inf.is.odysseus.core.server.mep.impl.MEPImpl;
import de.uniol.inf.is.odysseus.core.server.mep.impl.SimpleNode;

public class MEP implements IExpressionParser {
	
	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(MEP.class);
		}
		return _logger;
	}
	
	static MEP instance = new MEP();
	
	public static MEP getInstance() {
		return instance;
	}
	
	@Override
	public IExpression<?> parse(String expressionStr)
			throws ParseException {
		MEPImpl impl = new MEPImpl(new StringReader(expressionStr));
		SimpleNode expressionNode;
		try {
			expressionNode = impl.Expression();
		} catch (Exception e) {
			throw new de.uniol.inf.is.odysseus.core.mep.ParseException(e);
		}
		ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor();
		IExpression<?> expression = (IExpression<?>) expressionNode.jjtAccept(
				builder, null);
		return ExpressionOptimizer.simplifyExpression(expression);
	}

	private static Map<String, IFunction<?>> functions = new HashMap<String, IFunction<?>>();
	static {
		getLogger().debug("Register Base Function");
		
		registerFunction(new AndOperator());
		registerFunction(new OrOperator());

		registerFunction(new EqualsOperator());
		registerFunction(new NotEqualsOperator());

		registerFunction(new GreaterThanOperator());
		registerFunction(new SmallerThanOperator());
		registerFunction(new GreaterEqualsOperator());
		registerFunction(new SmallerEqualsOperator());

		registerFunction(new PlusOperator());
		registerFunction(new MinusOperator());

		registerFunction(new MultiplicationOperator());
		registerFunction(new DivisionOperator());
		registerFunction(new ModuloOperator());

		registerFunction(new PowerOperator());

		registerFunction(new NotOperator());
		registerFunction(new UnaryMinusOperator());

		registerFunction(new AbsoluteFunction());
		registerFunction(new CeilFunction());
		registerFunction(new DoubleToLongFunction());
		registerFunction(new DoubleToFloatFunction());
		registerFunction(new FloorFunction());
		registerFunction(new IfFunction());
		registerFunction(new SinusFunction());
		registerFunction(new CosinusFunction());
		registerFunction(new ToNumberFunction());
		registerFunction(new ToLongFunction());
		registerFunction(new ToStringFunction());
		registerFunction(new RandomFunction());
		registerFunction(new RoundFunction());

		registerFunction(new GetAbsoluteValue());
		registerFunction(new SquareValue());
		registerFunction(new SqrtValue());
		
		registerFunction(new ToRadians());
		registerFunction(new ToDegrees());
		
		registerFunction(new LikeFunction());
		registerFunction(new ContainsFunction());
		
		registerFunction(new IsNullFunction());
		
		registerFunction(new DebsDateFormatParse());
		
	}

	public static void registerFunction(IFunction<?> function) {
		String symbol = function.getSymbol();
		if (functions.containsKey(symbol.toUpperCase())) {
			throw new IllegalArgumentException(
					"multiple definition of function " + symbol);
		}
		//getLogger().debug("Register Function: " + function.getSymbol());
		functions.put(symbol.toUpperCase(), function);
	}

	public static void unregisterFunction(String symbol) {
		functions.remove(symbol.toUpperCase());
	}

	public static boolean containsFunction(String symbol) {
		return functions.containsKey(symbol.toUpperCase());
	}

	public static IFunction<?> getFunction(String symbol) {
		try {
			return functions.get(symbol.toUpperCase()).getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}


	public void addFunctionProvider(IFunctionProvider provider){
		for(IFunction<?> f: provider.getFunctions()){
			String symbol = f.getSymbol();
			if (MEP.functions.containsKey(symbol.toUpperCase())) {
				throw new IllegalArgumentException(
						"multiple definition of function " + symbol);
			}
			//System.out.println("################# FUNCTION ADDED: " + f.getSymbol());
//			getLogger().debug("Add Function Provider: " + f.getSymbol());
			functions.put(symbol.toUpperCase(), f);
		}
	}
	
	public void removeFunctionProvider(IFunctionProvider provider){
		// It's not allowed to have multiple implementations
		// of the same function (see addFunctionProvider).
		for(IFunction<?> f: provider.getFunctions()){
			getLogger().debug("Remove Function Provider: " + f.getSymbol());
			MEP.functions.remove(f.getSymbol().toUpperCase());
		}
	}

	public static ImmutableSet<String> getFunctions() {
		return ImmutableSet.copyOf(functions.keySet());
	}
}
