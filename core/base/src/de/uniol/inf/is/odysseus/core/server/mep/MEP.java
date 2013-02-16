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
import de.uniol.inf.is.odysseus.core.server.mep.functions.AbsoluteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.AndOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ArcCosinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ArcSinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ArcTangens2Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ArcTangensFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.CeilFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ConcatFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ContainsFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.CosinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.CurDateFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DayFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DebsDateFormatParse;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DebsIntensityCalc;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DebsIntensityCalc2;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DebsIntensityCalc_Numeric;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DivisionOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DoubleToByteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DoubleToFloatFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DoubleToLongFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.DoubleToShortFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.EFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.EqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.EqualsOperator2;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ExpFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.FloorFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.GetAbsoluteValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.GreaterEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.GreaterThanOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.HourFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.HyperbolicCosinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.HyperbolicSinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.HyperbolicTangensFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.IfFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.IsNullFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.LengthFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.LikeFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.LogFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.LowerFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MD5Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MinuteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ModuloOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MonthFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.MultiplicationOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NanoTimeFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NotEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NotEqualsOperator2;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NotOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.NowFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.OrOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.PIFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.PlusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.PowerOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.RandomFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.RoundFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SHA1Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SHA256Function;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SMaxFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SMinFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SecondFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SinusFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SmallerEqualsOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SmallerThanOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SqrtFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SqrtValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SquareValue;
import de.uniol.inf.is.odysseus.core.server.mep.functions.SubStringFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.TangensFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToByteFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToDegrees;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToFloatFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToLongFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToNumberFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToRadians;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToShortFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.ToStringFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.UUIDFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.UnaryMinusOperator;
import de.uniol.inf.is.odysseus.core.server.mep.functions.UpperFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.WeekFunction;
import de.uniol.inf.is.odysseus.core.server.mep.functions.YearFunction;
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
    private static FunctionStore                        functionStore = new FunctionStore();
    static {
        getLogger().debug("Register Base Function");
        /** Boolean Functions */
        registerFunction(new AndOperator());
        registerFunction(new OrOperator());

        registerFunction(new EqualsOperator());
        registerFunction(new EqualsOperator2());
        registerFunction(new NotEqualsOperator());
        registerFunction(new NotEqualsOperator2());
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
        registerFunction(new ToLongFunction());
        registerFunction(new ToStringFunction());
        registerFunction(new RandomFunction());
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
        
        /** String Functions */
        registerFunction(new LikeFunction());
        registerFunction(new ContainsFunction());
        
        registerFunction(new ConcatFunction());
        registerFunction(new SubStringFunction());
        registerFunction(new LengthFunction());
        registerFunction(new UpperFunction());
        registerFunction(new LowerFunction());
        
        /** Date Functions */
        registerFunction(new DebsDateFormatParse());
                
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

        registerFunction(new SMinFunction());
        registerFunction(new SMaxFunction());

        // TODO: Move to other bundle!!
        registerFunction(new DebsIntensityCalc());
        registerFunction(new DebsIntensityCalc_Numeric());
        registerFunction(new DebsIntensityCalc2());
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
            parameters.add(function.getAcceptedTypes(i));
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
            parameters.add(function.getAcceptedTypes(i));
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
