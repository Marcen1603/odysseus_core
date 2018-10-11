/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.rcp.views.mepfunctions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;

import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.MEP;

public class MEPFunctionInfo {

	private static final String UNKNOWN_RETURN_TYPE_NAME = "<unknown>";

	private static final Logger LOG = LoggerFactory.getLogger(MEPFunctionInfo.class);

	private final String symbol;
	private final int arity;
	private final ImmutableList<String> argTypes;
	private final String resultType;
	private final boolean deprecated;

	public MEPFunctionInfo(String symbol, int arity, ImmutableList<String> argTypes, String resultType, boolean deprecated) {
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(symbol), "Symbol of Function must not be null!");
		// Preconditions.checkArgument(arity >= 0, "Arity of function must be zero or positive, instead of %s!", arity);
		// Preconditions.checkNotNull(argTypes, "List of argument types of function must not be null!");
		// Preconditions.checkArgument(!Strings.isNullOrEmpty(resultType), "ResultType of function must not be null or empty!");

		this.symbol = symbol;
		this.arity = arity;
		this.argTypes = argTypes;
		this.resultType = resultType;
		this.deprecated = deprecated;
	}

	public static MEPFunctionInfo fromMEPFunction( IMepFunction<?> function ) {
		// Preconditions.checkNotNull(function, "Function must not be null!");
		
		boolean dep = false;
		try {
			ImmutableList.Builder<String> builder = ImmutableList.builder();
			for( int i = 0; i < function.getArity(); i++ ) {
				builder.add( concat(function.getAcceptedTypes(i)));
			}
			
			dep = MEP.isDepreacted(function);
			
			return new MEPFunctionInfo(
				function.getSymbol(),
				function.getArity(),
				builder.build(),
				tryGetReturnType(function), dep);
		} catch( Throwable t ) {
			LOG.error("Exception during creating MEPFunctionInfo from MEPFunction {}", function, t);
			return new MEPFunctionInfo(
					function.getSymbol(),
					0,
					ImmutableList.<String>of("???"),
					"???",dep
					);
		}
	}

	private static String tryGetReturnType(IMepFunction<?> function) {
		try {
			return function.getReturnType().getQualName();
		} catch( Throwable t ) {
			LOG.debug("Could not determine return type of function {}", function.getSymbol());
			return UNKNOWN_RETURN_TYPE_NAME;
		}
	}

	public String getSymbol() {
		return symbol;
	}

	public int getArity() {
		return arity;
	}

	public ImmutableList<String> getArgTypes() {
		return argTypes;
	}

	public String getResultType() {
		return resultType;
	}
	
	public boolean isDeprecated(){
		return deprecated;
	}

	private static String concat(SDFDatatype[] types) {
		if (types == null || types.length == 0) {
			return "";
		}

		if (types.length == 1) {
			return types[0].getQualName();
		}

		StringBuilder sb = new StringBuilder();

		sb.append("[");
		for (int i = 0; i < types.length; i++) {
			sb.append(types[i].getQualName());
			if (i < types.length - 1) {
				sb.append(" | ");
			}
		}
		sb.append("]");

		return sb.toString();
	}
}
