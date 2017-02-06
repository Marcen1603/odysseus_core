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
package de.uniol.inf.is.odysseus.mep.functions.object;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * Call on an object arbitrary methods by Java Reflection Api and return the
 * value of the method.
 * 
 * @author christoph Schröer
 * @deprecated
 */
public class InvokeFunction extends AbstractFunction<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4584480066358145342L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.OBJECT },
			{ SDFDatatype.STRING } };

	public InvokeFunction() {
		super("invoke", 2, accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {

		Object object = getInputValue(0);

		String methodNames = getInputValue(1);

		// Split by "."
		String[] multipleMethods = methodNames.split("\\.");

		Object returnObject = object;
		for (String methodName : multipleMethods) {

			Method methodObject;
			try {
				
				// Method of the class
				methodObject = returnObject.getClass().getMethod(methodName);
				
				// execute Method on returnObject (returnObject.methodObject)
				returnObject = methodObject.invoke(returnObject);

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}

		}
		return returnObject;
	}

}
