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
package de.uniol.inf.is.odysseus.args.marshaller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.args.ArgsException;

/**
 * @author Jonas Jacobi
 */
public class NumberMarshaller<T extends Number> implements
		IParameterMarshaller {
	T value;
	private Class<T> type;

	public NumberMarshaller(Class<T> type) {
		this.type = type;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void parse(ListIterator<String> args) throws ArgsException {
		if (!args.hasNext()) {
			throw new ArgsException("missing parameter");
		}

		String value = args.next();
		try {
			Method method = type.getMethod("valueOf", String.class);
			this.value = (T) method.invoke(null, value);
		} catch (InvocationTargetException e) {
			throw new ArgsException("illegal format for " + type.getSimpleName() + " parameter");
		} catch (Exception e) {
		}
	}
}
