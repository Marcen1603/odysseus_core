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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

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
import de.uniol.inf.is.odysseus.core.mep.IMepVariable;
import de.uniol.inf.is.odysseus.core.mep.ParseException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.impl.ExpressionBuilderVisitor;
import de.uniol.inf.is.odysseus.mep.impl.MEPImpl;
import de.uniol.inf.is.odysseus.mep.impl.SimpleNode;
import de.uniol.inf.is.odysseus.mep.intern.Constant;
import de.uniol.inf.is.odysseus.mep.intern.Variable;

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
	
	/**
	 * Creates a new variable
	 * 
	 * @param id   The name of the variable
	 * @param type The type of the variable
	 * @return The new variable
	 */
	public static <T> IMepVariable createVariable(String id, SDFDatatype type) {
		return new Variable(id, type);
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

	private static FunctionStore functionStore = FunctionStore.getInstance();

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
	}

	public static boolean containsFunction(String symbol) {
		return functionStore.containsSymbol(symbol);
	}

	public static IMepFunction<?> getFunction(FunctionSignature signature) {
		try {
			return functionStore.getFunction(signature).getClass().getDeclaredConstructor()
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
				return function.getClass().getDeclaredConstructor().newInstance();
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
				functions.add(function.getClass().getDeclaredConstructor().newInstance());
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
					MEP.registerFunction((IMepFunction<?>) classObject.getDeclaredConstructor()
							.newInstance());
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
