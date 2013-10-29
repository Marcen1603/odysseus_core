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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.IExpressionParser;
import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.functions.EvalFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.IfFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.IsNaNFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.IsNullFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.RandomFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.RandomFunction2;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SMaxFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SMinFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SleepFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.UUIDFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.bool.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.bool.NotOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.bool.OrOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.EqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.EqualsOperator2;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.GreaterThanOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.LikeFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.NotEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.NotEqualsOperator2;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.NotEqualsStringOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.NotEqualsStringOperator2;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.SmallerThanOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.crypt.MD5Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.crypt.SHA1Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.crypt.SHA256Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.AbsoluteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.ArcCosinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.ArcSinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.ArcTangens2Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.ArcTangensFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.CeilFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.CosinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.DivisionOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.ExpFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.FloorFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.GetAbsoluteValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.HyperbolicCosinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.HyperbolicSinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.HyperbolicTangensFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.LogFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.LowerFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.MinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.ModuloOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.MultiplicationOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.PIFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.PlusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.PowerOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.RoundFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.SinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.SqrtFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.SqrtValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.SquareValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.TangensFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.ToRadians;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.UnaryMinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.math.UpperFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.string.ConcatFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.string.ContainsFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.string.LengthFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.string.StringPlusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.string.SubStringFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.CurDateFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.DayFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.DebsDateFormatParse;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.HourFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.MinuteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.MonthFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.NanoTimeFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.NowFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.SecondFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.ToDateFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.WeekFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.time.YearFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.DoubleToByteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.DoubleToFloatFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.DoubleToIntegerFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.DoubleToLongFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.DoubleToShortFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.EFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToByteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToDegrees;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToDoubleFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToFloatFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToIntegerFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToLongFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToNumberFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToShortFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.transform.ToStringFunction;
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
    public IExpression<?> parse(String expressionStr, SDFSchema schema) throws ParseException {
    	
        MEPImpl impl = new MEPImpl(new StringReader(expressionStr));
        SimpleNode expressionNode;
        try {
            expressionNode = impl.Expression();
        }
        catch (Exception e) {
            throw new de.uniol.inf.is.odysseus.core.mep.ParseException(e);
        }
        ExpressionBuilderVisitor builder = new ExpressionBuilderVisitor(schema);
        IExpression<?> expression = (IExpression<?>) expressionNode.jjtAccept(builder, null);
        return ExpressionOptimizer.simplifyExpression(expression);
    }

    private static Map<FunctionSignature, IFunction<?>> functions     = new HashMap<FunctionSignature, IFunction<?>>();
    private static FunctionStore                        functionStore = FunctionStore.getInstance();
    static {
        getLogger().debug("Register Base Function");
        /** Boolean Functions */
        registerFunction(new AndOperator());
        registerFunction(new OrOperator());

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

        registerFunction(new PIFunction());
        registerFunction(new EFunction());
        
        registerFunction(new ExpFunction());
        registerFunction(new LogFunction());
        
        registerFunction(new GetAbsoluteValue());
        registerFunction(new SquareValue());
        registerFunction(new SqrtValue());

        registerFunction(new ToRadians());
        registerFunction(new ToDegrees());

        registerFunction(new IsNullFunction());
        registerFunction(new IsNaNFunction());
        registerFunction(new AssureNumber());

        /** String Functions */
        registerFunction(new LikeFunction());
        registerFunction(new ContainsFunction());
        registerFunction(new StringPlusOperator());
        registerFunction(new ConcatFunction());
        registerFunction(new SubStringFunction());
        registerFunction(new LengthFunction());
        registerFunction(new UpperFunction());
        registerFunction(new LowerFunction());
        
        /** Date Functions */
        registerFunction(new DebsDateFormatParse());
        registerFunction(new ToDateFunction());      
        
        registerFunction(new SecondFunction());
        registerFunction(new MinuteFunction());
        registerFunction(new HourFunction());
        registerFunction(new DayFunction());
        registerFunction(new WeekFunction());
        registerFunction(new MonthFunction());
        registerFunction(new YearFunction());
        registerFunction(new NowFunction());
        registerFunction(new CurDateFunction());
        registerFunction(new NanoTimeFunction());
        
        registerFunction(new MD5Function());
        registerFunction(new SHA1Function());
        registerFunction(new SHA256Function());
        
        registerFunction(new UUIDFunction());
        registerFunction(new EvalFunction());
        registerFunction(new SMinFunction());
        registerFunction(new SMaxFunction());
        
        registerFunction(new SleepFunction());
    }

    /**
     * Register a MEP function instance
     * 
     * @param function
     *            The function instance
     */
    public static void registerFunction(IFunction<?> function) {
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
            throw new IllegalArgumentException("multiple definition of function " + symbol);
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
        }
        else {
        	getLogger().warn("Tried to unregister function {}, which was not registered before", symbol);
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
            return functionStore.getFunctions(symbol).get(0).getClass().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static IFunction<?> getFunction(FunctionSignature signature) {
        try {
            return functionStore.getFunction(signature).getClass().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static IFunction<?> getFunction(String symbol, List<SDFDatatype> parameter) {
        try {
            IFunction<?> function = functionStore.getFunction(symbol, parameter);
            if (function != null) {
                return function.getClass().newInstance();
            }
            else {
                getLogger().debug("No such function: " + symbol + " for parameter " + parameter);
                return null;
            }
        }
        catch (Exception e) {
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
        }
        catch (Exception e) {
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
