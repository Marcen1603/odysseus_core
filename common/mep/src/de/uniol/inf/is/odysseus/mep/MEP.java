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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.functions.EvalFunction;
import de.uniol.inf.is.odysseus.mep.functions.IfFunction;
import de.uniol.inf.is.odysseus.mep.functions.IsNaNFunction;
import de.uniol.inf.is.odysseus.mep.functions.IsNullFunction;
import de.uniol.inf.is.odysseus.mep.functions.RandomFunction;
import de.uniol.inf.is.odysseus.mep.functions.RandomFunction2;
import de.uniol.inf.is.odysseus.mep.functions.SMaxFunction;
import de.uniol.inf.is.odysseus.mep.functions.SMinFunction;
import de.uniol.inf.is.odysseus.mep.functions.SleepFunction;
import de.uniol.inf.is.odysseus.mep.functions.SplittFunction;
import de.uniol.inf.is.odysseus.mep.functions.StoredLineFunction;
import de.uniol.inf.is.odysseus.mep.functions.StoredValueFunction;
import de.uniol.inf.is.odysseus.mep.functions.UUIDFunction;
import de.uniol.inf.is.odysseus.mep.functions.array.ListArrayFunction;
import de.uniol.inf.is.odysseus.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.OrOperator;
import de.uniol.inf.is.odysseus.mep.functions.bool.XorOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.EqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.EqualsOperator2;
import de.uniol.inf.is.odysseus.mep.functions.compare.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.GreaterThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.LikeFunction;
import de.uniol.inf.is.odysseus.mep.functions.compare.NotEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.NotEqualsOperator2;
import de.uniol.inf.is.odysseus.mep.functions.compare.NotEqualsStringOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.NotEqualsStringOperator2;
import de.uniol.inf.is.odysseus.mep.functions.compare.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.mep.functions.compare.SmallerThanOperator;
import de.uniol.inf.is.odysseus.mep.functions.crypt.DSAFunction;
import de.uniol.inf.is.odysseus.mep.functions.crypt.DSASignFunction;
import de.uniol.inf.is.odysseus.mep.functions.crypt.DSAVerifyFunction;
import de.uniol.inf.is.odysseus.mep.functions.crypt.MD5Function;
import de.uniol.inf.is.odysseus.mep.functions.crypt.RSAFunction;
import de.uniol.inf.is.odysseus.mep.functions.crypt.RSASignFunction;
import de.uniol.inf.is.odysseus.mep.functions.crypt.RSAVerifyFunction;
import de.uniol.inf.is.odysseus.mep.functions.crypt.SHA1Function;
import de.uniol.inf.is.odysseus.mep.functions.crypt.SHA244Function;
import de.uniol.inf.is.odysseus.mep.functions.crypt.SHA256Function;
import de.uniol.inf.is.odysseus.mep.functions.crypt.SHA384Function;
import de.uniol.inf.is.odysseus.mep.functions.crypt.SHA512Function;
import de.uniol.inf.is.odysseus.mep.functions.math.AbsoluteFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.ArcCosinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.ArcSinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.ArcTangens2Function;
import de.uniol.inf.is.odysseus.mep.functions.math.ArcTangensFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.CeilFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.CosinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.DivisionOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.EFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.ExpFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.FloorFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.HyperbolicCosinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.HyperbolicSinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.HyperbolicTangensFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.Log10Function;
import de.uniol.inf.is.odysseus.mep.functions.math.LogFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.MinusOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.ModuloOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.MultiplicationOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.PIFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.PlusOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.PowerOperator;
import de.uniol.inf.is.odysseus.mep.functions.math.RoundFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.SignFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.SinusFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.SqrtFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.TangensFunction;
import de.uniol.inf.is.odysseus.mep.functions.math.ToDegrees;
import de.uniol.inf.is.odysseus.mep.functions.math.ToRadians;
import de.uniol.inf.is.odysseus.mep.functions.math.UnaryMinusOperator;
import de.uniol.inf.is.odysseus.mep.functions.string.ConcatFunction;
import de.uniol.inf.is.odysseus.mep.functions.string.ContainsFunction;
import de.uniol.inf.is.odysseus.mep.functions.string.LengthFunction;
import de.uniol.inf.is.odysseus.mep.functions.string.LowerFunction;
import de.uniol.inf.is.odysseus.mep.functions.string.StringDivisionOperator;
import de.uniol.inf.is.odysseus.mep.functions.string.StringMinusOperator;
import de.uniol.inf.is.odysseus.mep.functions.string.StringMultiplicationOperator;
import de.uniol.inf.is.odysseus.mep.functions.string.StringPlusOperator;
import de.uniol.inf.is.odysseus.mep.functions.string.SubStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.string.SubStringFunction2;
import de.uniol.inf.is.odysseus.mep.functions.string.UpperFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.BusinessDaysFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.CurDateFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.DateMinusNumberOperator;
import de.uniol.inf.is.odysseus.mep.functions.time.DateMinusOperator;
import de.uniol.inf.is.odysseus.mep.functions.time.DatePlusNumberOperator;
import de.uniol.inf.is.odysseus.mep.functions.time.DatePlusOperator;
import de.uniol.inf.is.odysseus.mep.functions.time.DayFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.DayOfMonthFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.DayOfMonthStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.DayStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.DaysFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.HourFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.HourStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.HoursFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MilliTimeFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MinuteFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MinuteOfDayFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MinuteStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MinutesFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MonthFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MonthStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.MonthsFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.NanoTimeFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.SecondFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.SecondStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.SecondsFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.StreamDateFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.StreamDateFunction2;
import de.uniol.inf.is.odysseus.mep.functions.time.StreamTimeFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.SysDateFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.TimestampFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.WeekFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.WeekStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.WeekdayFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.WeekdayStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.YearFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.YearStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.time.YearsFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DateToLongFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DateToStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DoubleToBooleanFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DoubleToByteFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DoubleToFloatFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DoubleToIntegerFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DoubleToLongFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.DoubleToShortFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToBooleanFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToByteFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToDateFromNumberFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToDateFromStringFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToDoubleFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToFloatFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToIntegerFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToLongFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToNumberFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToShortFunction;
import de.uniol.inf.is.odysseus.mep.functions.transform.ToStringFunction;
import de.uniol.inf.is.odysseus.mep.impl.ExpressionBuilderVisitor;
import de.uniol.inf.is.odysseus.mep.impl.MEPImpl;
import de.uniol.inf.is.odysseus.mep.impl.SimpleNode;

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
	public IExpression<?> parse(String expressionStr) throws ParseException {
		return parse(expressionStr, (List<SDFSchema>) null);
	}

	@Override
	public IExpression<?> parse(String expressionStr, List<SDFSchema> schema)
			throws ParseException {

		MEPImpl impl = new MEPImpl(new StringReader(expressionStr));
		SimpleNode expressionNode;
		try {
			expressionNode = impl.Expression();
		} catch (Exception e) {
			throw new de.uniol.inf.is.odysseus.core.mep.ParseException(e);
		}
		ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(schema);
		IExpression<?> expression = (IExpression<?>) expressionNode.jjtAccept(
				builder, null);
		return ExpressionOptimizer.simplifyExpression(expression);
	}

	@Override
	public IExpression<?> parse(String expressionStr, SDFSchema schema)
			throws ParseException {

		MEPImpl impl = new MEPImpl(new StringReader(expressionStr));
		SimpleNode expressionNode;
		try {
			expressionNode = impl.Expression();
		} catch (Exception e) {
			throw new de.uniol.inf.is.odysseus.core.mep.ParseException(e);
		}
		ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(schema);
		IExpression<?> expression = (IExpression<?>) expressionNode.jjtAccept(
				builder, null);
		return ExpressionOptimizer.simplifyExpression(expression);
	}

	private static Map<FunctionSignature, IFunction<?>> functions = new HashMap<FunctionSignature, IFunction<?>>();
	private static FunctionStore functionStore = FunctionStore.getInstance();
	static {

		getLogger().debug("Register Base Function");
		/** Boolean Functions */
		registerFunction(new AndOperator());
		registerFunction(new OrOperator());
		registerFunction(new XorOperator());

		registerFunction(new EqualsOperator());
		registerFunction(new EqualsOperator2());
		registerFunction(new NotEqualsOperator());
		registerFunction(new NotEqualsOperator2());
		registerFunction(new NotEqualsStringOperator());
		registerFunction(new NotEqualsStringOperator2());

		/** Math Functions */
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
		registerFunction(new SqrtFunction());

		registerFunction(new NotOperator());
		registerFunction(new UnaryMinusOperator());

		registerFunction(new AbsoluteFunction());
		registerFunction(new CeilFunction());
		registerFunction(new DoubleToLongFunction());
		registerFunction(new DoubleToIntegerFunction());
		registerFunction(new DoubleToFloatFunction());
		registerFunction(new DoubleToShortFunction());
		registerFunction(new DoubleToByteFunction());
		registerFunction(new DoubleToBooleanFunction());
		registerFunction(new FloorFunction());
		registerFunction(new IfFunction());
		registerFunction(new SinusFunction());
		registerFunction(new HyperbolicSinusFunction());
		registerFunction(new ArcSinusFunction());
		registerFunction(new CosinusFunction());
		registerFunction(new HyperbolicCosinusFunction());
		registerFunction(new ArcCosinusFunction());
		registerFunction(new TangensFunction());
		registerFunction(new HyperbolicTangensFunction());
		registerFunction(new ArcTangensFunction());
		registerFunction(new ArcTangens2Function());
		registerFunction(new ToNumberFunction());
		registerFunction(new ToBooleanFunction());
		registerFunction(new ToByteFunction());
		registerFunction(new ToShortFunction());
		registerFunction(new ToFloatFunction());
		registerFunction(new ToDoubleFunction());
		registerFunction(new ToLongFunction());
		registerFunction(new ToIntegerFunction());
		registerFunction(new ToStringFunction());
		registerFunction(new RandomFunction());
		registerFunction(new RandomFunction2());
		registerFunction(new RoundFunction());
		registerFunction(new SignFunction());

		registerFunction(new PIFunction());
		registerFunction(new EFunction());

		registerFunction(new ExpFunction());
		registerFunction(new LogFunction());
	    registerFunction(new Log10Function());


		registerFunction(new ToRadians());
		registerFunction(new ToDegrees());

		registerFunction(new IsNullFunction());
		registerFunction(new IsNaNFunction());
		registerFunction(new AssureNumber());

		/** String Functions */
		registerFunction(new LikeFunction());
		registerFunction(new ContainsFunction());
		registerFunction(new StringPlusOperator());
		registerFunction(new StringMinusOperator());
		registerFunction(new StringMultiplicationOperator());
		registerFunction(new StringDivisionOperator());
		registerFunction(new ConcatFunction());
		registerFunction(new SubStringFunction());
		registerFunction(new SubStringFunction2());
		registerFunction(new LengthFunction());
		registerFunction(new UpperFunction());
		registerFunction(new LowerFunction());

		/** Date Functions */
		registerFunction(new ToDateFromStringFunction());
		registerFunction(new ToDateFromNumberFunction());
		registerFunction(new DateToStringFunction());
		registerFunction(new DateToLongFunction());

		registerFunction(new DatePlusOperator());
		registerFunction(new DatePlusNumberOperator());
		registerFunction(new DateMinusOperator());
		registerFunction(new DateMinusNumberOperator());

		registerFunction(new SecondFunction());
		registerFunction(new MinuteFunction());
		registerFunction(new MinuteOfDayFunction());
		registerFunction(new HourFunction());
		registerFunction(new DayFunction());
		registerFunction(new DayOfMonthFunction());
		registerFunction(new WeekdayFunction());
		registerFunction(new WeekFunction());
		registerFunction(new MonthFunction());
		registerFunction(new YearFunction());

		registerFunction(new SecondStringFunction());
		registerFunction(new MinuteStringFunction());
		registerFunction(new HourStringFunction());
		registerFunction(new DayStringFunction());
		registerFunction(new DayOfMonthStringFunction());
		registerFunction(new WeekdayStringFunction());
		registerFunction(new WeekStringFunction());
		registerFunction(new MonthStringFunction());
		registerFunction(new YearStringFunction());

		registerFunction(new SecondsFunction());
		registerFunction(new MinutesFunction());
		registerFunction(new HoursFunction());
		registerFunction(new DaysFunction());
		registerFunction(new MonthsFunction());
		registerFunction(new YearsFunction());
		registerFunction(new BusinessDaysFunction());

		registerFunction(new MilliTimeFunction());
		registerFunction(new CurDateFunction());
		registerFunction(new NanoTimeFunction());

		registerFunction(new SysDateFunction());
		registerFunction(new StreamDateFunction());
		registerFunction(new StreamDateFunction2());
		registerFunction(new StreamTimeFunction());
		registerFunction(new TimestampFunction());

		registerFunction(new MD5Function());
		registerFunction(new SHA1Function());
		registerFunction(new SHA256Function());
		registerFunction(new SHA244Function());
		registerFunction(new SHA384Function());
		registerFunction(new SHA512Function());
		registerFunction(new DSAFunction());
		registerFunction(new DSASignFunction());
		registerFunction(new DSAVerifyFunction());
		registerFunction(new RSAFunction());
		registerFunction(new RSASignFunction());
		registerFunction(new RSAVerifyFunction());

		registerFunction(new UUIDFunction());
		registerFunction(new EvalFunction());
		registerFunction(new SMinFunction());
		registerFunction(new SMaxFunction());

		registerFunction(new SleepFunction());

		registerFunction(new StoredValueFunction());
		registerFunction(new StoredLineFunction());

		registerFunction(new SplittFunction());
		
		// Array Functions
		registerFunction(new ListArrayFunction());
	}

	/**
	 * Register a MEP function instance
	 * 
	 * @param function
	 *            The function instance
	 */
	public static void registerFunction(IFunction<?> function) {

		try {
			registerFunctionWithName(function, function.getSymbol());
			if (function instanceof IHasAlias) {
				registerFunctionWithName(function,
						((IHasAlias) function).getAliasName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized static void registerFunctionWithName(IFunction<?> function,
			String symbol) {
		List<SDFDatatype[]> parameters = new ArrayList<SDFDatatype[]>();
		int arity = function.getArity();
		for (int i = 0; i < arity; i++) {
			if (function.getAcceptedTypes(i) != null) {
				parameters.add(function.getAcceptedTypes(i));
			}
		}
		FunctionSignature signature = new FunctionSignature(symbol, parameters);
		if (functionStore.containsSignature(signature)) {
			throw new IllegalArgumentException(
					"multiple definition of function " + symbol);
		}
		// getLogger().debug("Register Function: " + function.getSymbol());
		functionStore.put(signature, function);
	}

	/**
	 * @deprecated Use unregisterFunction with parameter {@link IFunction}
	 * @param symbol
	 */
	@Deprecated
	public static void unregisterFunction(String symbol) {
		functions.remove(symbol.toUpperCase());
	}

	/**
	 * Unregister a MEP function instance
	 * 
	 * @param function
	 *            The function instance
	 */
	public static void unregisterFunction(IFunction<?> function) {
		String symbol = function.getSymbol();
		List<SDFDatatype[]> parameters = new ArrayList<SDFDatatype[]>();
		int arity = function.getArity();
		for (int i = 0; i < arity; i++) {
			if (function.getAcceptedTypes(i) != null) {
				parameters.add(function.getAcceptedTypes(i).clone());
			}
		}
		FunctionSignature signature = new FunctionSignature(symbol, parameters);
		if (functionStore.containsSignature(signature)) {
			functionStore.remove(signature);
		} else {
			getLogger()
					.warn("Tried to unregister function {}, which was not registered before",
							symbol);
		}
	}

	public static boolean containsFunction(String symbol) {
		return functionStore.containsSymbol(symbol);
	}

	/**
	 * @deprecated Use getFunction with SDFDatatype parameter
	 * @param symbol
	 * @return
	 */
	@Deprecated
	public static IFunction<?> getFunction(String symbol) {
		try {
			return functionStore.getFunctions(symbol).get(0).getClass()
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static IFunction<?> getFunction(FunctionSignature signature) {
		try {
			return functionStore.getFunction(signature).getClass()
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static IFunction<?> getFunction(String symbol,
			List<SDFDatatype> parameter) {
		try {
			IFunction<?> function = functionStore
					.getFunction(symbol, parameter);
			if (function != null) {
				return function.getClass().newInstance();
			}

			getLogger().debug(
					"No such function: " + symbol + " for parameter "
							+ parameter);
			return null;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static List<IFunction<?>> getFunctions(String symbol) {
		List<IFunction<?>> functions = new ArrayList<IFunction<?>>();
		try {
			for (IFunction<?> function : functionStore.getFunctions(symbol)) {
				functions.add(function.getClass().newInstance());
			}
			return functions;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addFunctionProvider(IFunctionProvider provider) {
		for (IFunction<?> f : provider.getFunctions()) {
			MEP.registerFunction(f);
		}
	}

	public void removeFunctionProvider(IFunctionProvider provider) {
		// It's not allowed to have multiple implementations
		// of the same function (see addFunctionProvider).
		for (IFunction<?> f : provider.getFunctions()) {
			getLogger().debug("Remove Function Provider: " + f.getSymbol());
			MEP.unregisterFunction(f);
		}
	}

	public static ImmutableSet<FunctionSignature> getFunctions() {
		return functionStore.getSignatures();
	}
}
