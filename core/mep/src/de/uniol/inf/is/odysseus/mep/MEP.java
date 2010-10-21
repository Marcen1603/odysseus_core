package de.uniol.inf.is.odysseus.mep;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.CeilFunction;
import de.uniol.inf.is.odysseus.mep.functions.CosinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.DivisionOperator;
import de.uniol.inf.is.odysseus.mep.functions.DoubleToLongFunction;
import de.uniol.inf.is.odysseus.mep.functions.EqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.FloorFunction;
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
import de.uniol.inf.is.odysseus.mep.functions.SinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.SmallerThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.ToNumberFunction;
import de.uniol.inf.is.odysseus.mep.functions.ToStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.UnaryMinusOperator;
import de.uniol.inf.is.odysseus.mep.impl.ExpressionBuilderVisitor;
import de.uniol.inf.is.odysseus.mep.impl.MEPImpl;
import de.uniol.inf.is.odysseus.mep.impl.ParseException;
import de.uniol.inf.is.odysseus.mep.impl.SimpleNode;

public class MEP {
	public static IExpression<?> parse(String expressionStr) throws de.uniol.inf.is.odysseus.mep.ParseException {
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

		registerFunction(new CeilFunction());
		registerFunction(new DoubleToLongFunction());
		registerFunction(new FloorFunction());
		registerFunction(new IfFunction());
		registerFunction(new SinusFunction());
		registerFunction(new CosinusFunction());
		registerFunction(new ToNumberFunction());
		registerFunction(new ToStringFunction());
	}

	public static void registerFunction(IFunction<?> function) {
		String symbol = function.getSymbol();
		if (functions.containsKey(symbol)) {
			throw new IllegalArgumentException(
					"multiple definition of function " + symbol);
		}
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

}
