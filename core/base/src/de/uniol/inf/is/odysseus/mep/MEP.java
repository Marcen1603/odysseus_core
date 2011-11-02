/** Copyright [2011] [The Odysseus Team]
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

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.mep.functions.AbsoluteFunction;
import de.uniol.inf.is.odysseus.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.CeilFunction;
import de.uniol.inf.is.odysseus.mep.functions.CosinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.DivisionOperator;
import de.uniol.inf.is.odysseus.mep.functions.DoubleToFloatFunction;
import de.uniol.inf.is.odysseus.mep.functions.DoubleToLongFunction;
import de.uniol.inf.is.odysseus.mep.functions.EqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.FloorFunction;
import de.uniol.inf.is.odysseus.mep.functions.GetAbsoluteValue;
import de.uniol.inf.is.odysseus.mep.functions.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.GreaterThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.IfFunction;
import de.uniol.inf.is.odysseus.mep.functions.MinusOperator;
import de.uniol.inf.is.odysseus.mep.functions.ModuloOperator;
import de.uniol.inf.is.odysseus.mep.functions.MultiplicationOperator;
import de.uniol.inf.is.odysseus.mep.functions.NotEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.OrOperator;
import de.uniol.inf.is.odysseus.mep.functions.PlusOperator;
import de.uniol.inf.is.odysseus.mep.functions.PowerOperator;
import de.uniol.inf.is.odysseus.mep.functions.RandomFunction;
import de.uniol.inf.is.odysseus.mep.functions.SinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.SmallerThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.SqrtValue;
import de.uniol.inf.is.odysseus.mep.functions.SquareValue;
import de.uniol.inf.is.odysseus.mep.functions.ToLongFunction;
import de.uniol.inf.is.odysseus.mep.functions.ToNumberFunction;
import de.uniol.inf.is.odysseus.mep.functions.ToStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.UnaryMinusOperator;
import de.uniol.inf.is.odysseus.mep.impl.ExpressionBuilderVisitor;
import de.uniol.inf.is.odysseus.mep.impl.MEPImpl;
import de.uniol.inf.is.odysseus.mep.impl.SimpleNode;

public class MEP {
	
	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(MEP.class);
		}
		return _logger;
	}
	
	public static IExpression<?> parse(String expressionStr)
			throws de.uniol.inf.is.odysseus.mep.ParseException {
		MEPImpl impl = new MEPImpl(new StringReader(expressionStr));
		SimpleNode expressionNode;
		try {
			expressionNode = impl.Expression();
		} catch (Exception e) {
			throw new de.uniol.inf.is.odysseus.mep.ParseException(e);
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

		registerFunction(new GetAbsoluteValue());
		registerFunction(new SquareValue());
		registerFunction(new SqrtValue());
	}

	public static void registerFunction(IFunction<?> function) {
		String symbol = function.getSymbol();
		if (functions.containsKey(symbol)) {
			throw new IllegalArgumentException(
					"multiple definition of function " + symbol);
		}
		//getLogger().debug("Register Function: " + function.getSymbol());
		functions.put(symbol, function);
	}

	public static void unregisterFunction(String symbol) {
		functions.remove(symbol);
	}

	public static boolean containsFunction(String symbol) {
		return functions.containsKey(symbol);
	}

	public static IFunction<?> getFunction(String symbol) {
		try {
			return functions.get(symbol).getClass().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

//	@Override
//	public List<IFunction<?>> getFunctions() {
//		List<IFunction<?>> funcs = new ArrayList<IFunction<?>>();
//		funcs.add(new AndOperator());
//		funcs.add(new OrOperator());
//
//		funcs.add(new EqualsOperator());
//		funcs.add(new NotEqualsOperator());
//
//		funcs.add(new GreaterThanOperator());
//		funcs.add(new SmallerThanOperator());
//		funcs.add(new GreaterEqualsOperator());
//		funcs.add(new SmallerEqualsOperator());
//
//		funcs.add(new PlusOperator());
//		funcs.add(new MinusOperator());
//
//		funcs.add(new MultiplicationOperator());
//		funcs.add(new DivisionOperator());
//		funcs.add(new ModuloOperator());
//
//		funcs.add(new PowerOperator());
//
//		funcs.add(new NotOperator());
//		funcs.add(new UnaryMinusOperator());
//
//		funcs.add(new AbsoluteFunction());
//		funcs.add(new CeilFunction());
//		funcs.add(new DoubleToLongFunction());
//		funcs.add(new FloorFunction());
//		funcs.add(new IfFunction());
//		funcs.add(new SinusFunction());
//		funcs.add(new CosinusFunction());
//		funcs.add(new ToNumberFunction());
//		funcs.add(new ToStringFunction());
//		funcs.add(new RandomFunction());
//
//		funcs.add(new MatrixInvert());
//		funcs.add(new MatrixAdd());
//		funcs.add(new MatrixSub());
//		funcs.add(new MatrixMult());
//		funcs.add(new MatrixTranspose());
//		funcs.add(new MatrixGetEntry());
//		funcs.add(new GetAbsoluteValue());
//		funcs.add(new SquareValue());
//		funcs.add(new SqrtValue());
//		
////		funcs.add(new DolToEur());
////		funcs.add(new Now());
////		funcs.add(new Distance());
////		funcs.add(new Polygon());
//		
//		return funcs;
//	}
	
	public void addFunctionProvider(IFunctionProvider provider){
		for(IFunction<?> f: provider.getFunctions()){
			String symbol = f.getSymbol();
			if (MEP.functions.containsKey(symbol)) {
				throw new IllegalArgumentException(
						"multiple definition of function " + symbol);
			}
			//System.out.println("################# FUNCTION ADDED: " + f.getSymbol());
//			getLogger().debug("Add Function Provider: " + f.getSymbol());
			functions.put(symbol, f);
		}
	}
	
	public void removeFunctionProvider(IFunctionProvider provider){
		// It's not allowed to have multiple implementations
		// of the same function (see addFunctionProvider).
		for(IFunction<?> f: provider.getFunctions()){
			getLogger().debug("Remove Function Provider: " + f.getSymbol());
			MEP.functions.remove(f.getSymbol());
		}
	}

}
