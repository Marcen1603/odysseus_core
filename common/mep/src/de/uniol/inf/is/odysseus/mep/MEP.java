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
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.IHasAlias;
import de.uniol.inf.is.odysseus.core.IHasSecondAlias;
import de.uniol.inf.is.odysseus.core.mep.IMepConstant;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.impl.ExpressionBuilderVisitor;
import de.uniol.inf.is.odysseus.mep.impl.MEPImpl;
import de.uniol.inf.is.odysseus.mep.impl.SimpleNode;
import de.uniol.inf.is.odysseus.mep.intern.Constant;

public class MEP implements IMepExpressionParser {

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
	
	public static <T> IMepConstant<T> createConstant(T value, SDFDatatype type){
		return new Constant<>(value, type);
	}

	@Override
	public IMepExpression<?> parse(String expressionStr) throws ParseException {
		return parse(expressionStr, (List<SDFSchema>) null);
	}

	@Override
	public IMepExpression<?> parse(String expressionStr, List<SDFSchema> schema)
			throws ParseException {

		MEPImpl impl = new MEPImpl(new StringReader(expressionStr));
		SimpleNode expressionNode;
		try {
			expressionNode = impl.Expression();
		} catch (Exception e) {
			throw new de.uniol.inf.is.odysseus.core.mep.ParseException(e);
		}
		ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(schema);
		IMepExpression<?> expression = (IMepExpression<?>) expressionNode.jjtAccept(
				builder, null);
		return ExpressionOptimizer.simplifyExpression(expression);
	}

	@Override
	public IMepExpression<?> parse(String expressionStr, SDFSchema schema)
			throws ParseException {

		MEPImpl impl = new MEPImpl(new StringReader(expressionStr));
		SimpleNode expressionNode;
		try {
			expressionNode = impl.Expression();
		} catch (Exception e) {
			throw new de.uniol.inf.is.odysseus.core.mep.ParseException(e);
		}
		ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(schema);
		IMepExpression<?> expression = (IMepExpression<?>) expressionNode.jjtAccept(
				builder, null);
		return ExpressionOptimizer.simplifyExpression(expression);
	}

	private static Map<FunctionSignature, IMepFunction<?>> functions = new HashMap<FunctionSignature, IMepFunction<?>>();
	private static FunctionStore functionStore = FunctionStore.getInstance();
	// MG: Now done by Activator
	//	static {
//
//		getLogger().debug("Register Base Function");
//		
//		
//		/** BitVector Functions */
//		registerFunction(new BitAccessFunction());
//		registerFunction(new BitVectorToLong());
//		registerFunction(new BitSubsetFunction());
//
//		/** Boolean Functions */
//		registerFunction(new AndOperator());
//		registerFunction(new OrOperator());
//		registerFunction(new XorOperator());
//
//		registerFunction(new EqualsOperator());
//		registerFunction(new BooleanEqualsOperator());
//		registerFunction(new StringEqualsOperator());
//		registerFunction(new NotEqualsOperator());
//		registerFunction(new StringNotEqualsOperator());
//		
//		/** Bit functions */
//		registerFunction(new LeftShiftOperator());
//		registerFunction(new RightShiftOperator());
//		registerFunction(new BitInvertOperator());
//		registerFunction(new BitAndOperator());
//		registerFunction(new BitOrOperator());
//
//		/** Math Functions */
//		registerFunction(new GreaterThanOperator());
//		registerFunction(new SmallerThanOperator());
//		registerFunction(new GreaterEqualsOperator());
//		registerFunction(new SmallerEqualsOperator());
//
//		registerFunction(new PlusOperator());
//		registerFunction(new MinusOperator());
//
//		registerFunction(new MultiplicationOperator());
//		registerFunction(new DivisionOperator());
//		registerFunction(new ModuloOperator());
//
//		registerFunction(new PowerOperator());
//		registerFunction(new SqrtFunction());
//
//		registerFunction(new NotOperator());
//		registerFunction(new UnaryMinusOperator());
//
//		registerFunction(new AbsoluteFunction());
//		registerFunction(new CeilFunction());
//		registerFunction(new DoubleToLongFunction());
//		registerFunction(new DoubleToIntegerFunction());
//		registerFunction(new DoubleToFloatFunction());
//		registerFunction(new DoubleToShortFunction());
//		registerFunction(new DoubleToByteFunction());
//		registerFunction(new DoubleToBooleanFunction());
//		registerFunction(new DoubleToCharFunction());
//		registerFunction(new FloorFunction());
//		registerFunction(new IfFunction());
//		registerFunction(new SinusFunction());
//		registerFunction(new HyperbolicSinusFunction());
//		registerFunction(new ArcSinusFunction());
//		registerFunction(new CosinusFunction());
//		registerFunction(new HyperbolicCosinusFunction());
//		registerFunction(new ArcCosinusFunction());
//		registerFunction(new TangensFunction());
//		registerFunction(new HyperbolicTangensFunction());
//		registerFunction(new ArcTangensFunction());
//		registerFunction(new ArcTangens2Function());
//		registerFunction(new ToNumberFunction());
//		registerFunction(new ToBooleanFunction());
//		registerFunction(new ToByteFunction());
//		registerFunction(new ToShortFunction());
//		registerFunction(new ToFloatFunction());
//		registerFunction(new ToFloatIEEE754());
//		registerFunction(new ToFloatIEEE754_2());
//		registerFunction(new ToDoubleFunction());
//		registerFunction(new ToLongFunction());
//		registerFunction(new ToListFunction());
//		registerFunction(new ToIntegerFromNumberFunction());
//		registerFunction(new ToUnsignedInt16Function());
//		registerFunction(new ToCharFunction());
//		registerFunction(new ToStringFunction());
//		registerFunction(new ToHexFromStringFunction());
//		registerFunction(new ToHexFromDiscreteFunction());
//		registerFunction(new ToHexFromFloatingNumberFunction());
//		registerFunction(new ToBinaryFromStringFunction());
//		registerFunction(new ToBinaryFromNumberFunction());
//		registerFunction(new ToBinaryFromUnsignedInt16Function());
//		registerFunction(new ToBinaryFromFloatingNumberFunction());
//		registerFunction(new RandomFunction());
//		registerFunction(new RandomFunction2());
//		registerFunction(new RoundFunction());
//		registerFunction(new SignFunction());
//
//		registerFunction(new PIFunction());
//		registerFunction(new EFunction());
//		registerFunction(new NaNFunction());
//		registerFunction(new InfFunction());
//
//		registerFunction(new ExpFunction());
//		registerFunction(new LogFunction());
//		registerFunction(new Log10Function());
//
//		registerFunction(new ToRadians());
//		registerFunction(new ToDegrees());
//
//		registerFunction(new IsNullFunction());
//		registerFunction(new IsNaNFunction());
//		registerFunction(new AssureNumber());
//
//		/** String Functions */
//		registerFunction(new LikeFunction());
//		registerFunction(new StringContainsFunction());
//		registerFunction(new StringPlusOperator());
//		registerFunction(new StringMinusOperator());
//		registerFunction(new StringMultiplicationOperator());
//		registerFunction(new StringDivisionOperator());
//		registerFunction(new ConcatFunction());
//		registerFunction(new SubStringFunction());
//		registerFunction(new SubStringFunction2());
//		registerFunction(new StartsWithFunction());
//		registerFunction(new LengthFunction());
//		registerFunction(new UpperFunction());
//		registerFunction(new LowerFunction());
//
//		/** Date Functions */
//		registerFunction(new ToDateFromStringFunction());
//		registerFunction(new ToDateFromNumberFunction());
//		registerFunction(new DateToStringFunction());
//		registerFunction(new DateToLongFunction());
//
//		registerFunction(new DatePlusOperator());
//		registerFunction(new DatePlusNumberOperator());
//		registerFunction(new DateMinusOperator());
//		registerFunction(new DateMinusNumberOperator());
//
//        registerFunction(new MilliSecondFunction());
//		registerFunction(new SecondFunction());
//		registerFunction(new MinuteFunction());
//		registerFunction(new MinuteOfDayFunction());
//		registerFunction(new HourFunction());
//		registerFunction(new DayFunction());
//		registerFunction(new DayOfMonthFunction());
//		registerFunction(new WeekdayFunction());
//		registerFunction(new WeekFunction());
//		registerFunction(new MonthFunction());
//		registerFunction(new YearFunction());
//
//        registerFunction(new MilliSecondStringFunction());
//		registerFunction(new SecondStringFunction());
//		registerFunction(new MinuteStringFunction());
//		registerFunction(new HourStringFunction());
//		registerFunction(new DayStringFunction());
//		registerFunction(new DayOfMonthStringFunction());
//		registerFunction(new WeekdayStringFunction());
//		registerFunction(new WeekStringFunction());
//		registerFunction(new MonthStringFunction());
//		registerFunction(new YearStringFunction());
//
//        registerFunction(new MilliSecondsFunction());
//		registerFunction(new SecondsFunction());
//		registerFunction(new MinutesFunction());
//		registerFunction(new HoursFunction());
//		registerFunction(new DaysFunction());
//		registerFunction(new MonthsFunction());
//		registerFunction(new YearsFunction());
//		registerFunction(new BusinessDaysFunction());
//
//		registerFunction(new MilliTimeFunction());
//		registerFunction(new CurDateFunction());
//		registerFunction(new NanoTimeFunction());
//		registerFunction(new DateInMillis());
//
//		registerFunction(new SysDateFunction());
//		registerFunction(new StreamDateFunction());
//		registerFunction(new StreamDateFunction2());
//		registerFunction(new StreamTimeFunction());
//		registerFunction(new TimestampFunction());
//
//		registerFunction(new MD5Function());
//		registerFunction(new SHA1Function());
//		registerFunction(new SHA256Function());
//		registerFunction(new SHA244Function());
//		registerFunction(new SHA384Function());
//		registerFunction(new SHA512Function());
//		registerFunction(new DSAFunction());
//		registerFunction(new DSASignFunction());
//		registerFunction(new DSAVerifyFunction());
//		registerFunction(new RSAFunction());
//		registerFunction(new RSASignFunction());
//		registerFunction(new RSAVerifyFunction());
//
//		registerFunction(new UUIDFunction());
//		registerFunction(new CPULoadFunction());
//		registerFunction(new UptimeFunction());
//		registerFunction(new MemoryFunction());
//		registerFunction(new ReadFunction());
//		registerFunction(new EvalFunction());
//		registerFunction(new SMinFunction());
//		registerFunction(new SMaxFunction());
//
//		registerFunction(new SleepFunction());
//		registerFunction(new BurnFunction());
//
//		registerFunction(new StoredValueFunction());
//		registerFunction(new StoredLineFunction());
//
//		registerFunction(new SplitFunction());
//		registerFunction(new SplitFunctionOld());
//
//		// Array Functions
//		registerFunction(new ListElementAtFunction());
//
//		// List Functions
//		registerFunction(new ListContainsFunction());
//        registerFunction(new IsEmptyFunction());
//        registerFunction(new SizeFunction());
//        registerFunction(new IndexOfFunction());
//
//		registerFunction(new TupleAccessFunction());
//
//		registerFunction(new MatrixFunction());
//		registerFunction(new MatrixLine());
//	}

	/**
	 * Register a MEP function instance
	 * 
	 * @param function
	 *            The function instance
	 */
	public static void registerFunction(IMepFunction<?> function) {

		try {
			registerFunctionWithName(function, function.getSymbol());
			if (function instanceof IHasAlias) {
				registerFunctionWithName(function,
						((IHasAlias) function).getAliasName());
			}
			if (function instanceof IHasSecondAlias) {
				registerFunctionWithName(function,
						((IHasSecondAlias) function).getSecondAliasName());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized static void registerFunctionWithName(
			IMepFunction<?> function, String symbol) {
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
		if (function.getClass().getAnnotation(Deprecated.class) != null){
			functionStore.setDeprecated(function);
		}
	}

	/**
	 * @deprecated Use unregisterFunction with parameter {@link IMepFunction}
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
	public static void unregisterFunction(IMepFunction<?> function) {
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
		} 
		// can be ignored
		//		else {
//			getLogger()
//					.warn("Tried to unregister function {}, which was not registered before",
//							symbol);
//		}
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
	public static IMepFunction<?> getFunction(String symbol) {
		try {
			return functionStore.getFunctions(symbol).get(0).getClass()
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static IMepFunction<?> getFunction(FunctionSignature signature) {
		try {
			return functionStore.getFunction(signature).getClass()
					.newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static boolean isDepreacted(IMepFunction<?> function) {
		try {
			boolean dep = functionStore.isDeprecated(function);
			return dep;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public static IMepFunction<?> getFunction(String symbol,
			List<SDFDatatype> parameter) {
		try {
			IMepFunction<?> function = functionStore
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

	public static List<IMepFunction<?>> getFunctions(String symbol) {
		List<IMepFunction<?>> functions = new ArrayList<IMepFunction<?>>();
		try {
			for (IMepFunction<?> function : functionStore.getFunctions(symbol)) {
				functions.add(function.getClass().newInstance());
			}
			return functions;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void addFunctionProvider(IFunctionProvider provider) {
		for (IMepFunction<?> f : provider.getFunctions()) {
			MEP.registerFunction(f);
		}
	}

	public void removeFunctionProvider(IFunctionProvider provider) {
		// It's not allowed to have multiple implementations
		// of the same function (see addFunctionProvider).
		for (IMepFunction<?> f : provider.getFunctions()) {
			getLogger().debug("Remove Function Provider: " + f.getSymbol());
			MEP.unregisterFunction(f);
		}
	}

	public static ImmutableSet<FunctionSignature> getFunctions() {
		return functionStore.getSignatures();
	}
	
	
	public static void searchBundleForMepFunctions(Bundle bundle) {
		Enumeration<URL> entries = bundle.findEntries("/bin/", "*.class", true);
		// collect logical operators and register parameters first
		// add logical operators afterwards, because they may need the newly
		// registered parameters
		if (entries == null) {
			entries = bundle.findEntries("/", "*.class", true);
			if (entries == null) {
				return;
			}
		}
		while (entries.hasMoreElements()) {
			URL curURL = entries.nextElement();
			Class<?> classObject = loadClass(bundle, curURL);
			if (IMepFunction.class.isAssignableFrom(classObject)
					&& !Modifier.isAbstract(classObject.getModifiers())) {
				try {
					MEP.registerFunction((IMepFunction<?>) classObject
							.newInstance());
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static Class<?> loadClass(Bundle bundle, URL curURL) {
		String file = curURL.getFile();
		int start = 1;
		String className = "";
		try {
			if (file.startsWith("/bin/")) {
				start = "/bin/".length();
			}
			// remove potential '/bin' and 'class' and change path to package
			// name
			className = file.substring(start, file.length() - 6).replace('/',
					'.');
			Class<?> classObject = bundle.loadClass(className);
			return classObject;
		} catch (Exception e) {

		}
		return null;
	}
	
	public static Class<?> getFunctionClass(FunctionSignature signature) {
		try {
			return functionStore.getFunction(signature).getClass();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
