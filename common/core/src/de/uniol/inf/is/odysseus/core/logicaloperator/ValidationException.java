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
package de.uniol.inf.is.odysseus.core.logicaloperator;

import java.util.ArrayList;
import java.util.List;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = -1602573291200567065L;
	private final ArrayList<String> exceptions;

	public ValidationException(String operator, List<String> e) {
		super(getString(operator, e));
		this.exceptions = new ArrayList<String>(e);
	}

	public ArrayList<String> getExceptions() {
		return exceptions;
	}

//	private static void appendCause(StringBuilder builder, Throwable e) {
//		if (e != null && e.getCause() != null) {
//			builder.append("\nCaused by:").append(e.getCause());
//			appendCause(builder, e.getCause());
//		}
//	}

	private static String getString(String operator, List<String> exceptions) {
		final StringBuilder builder = new StringBuilder();
		builder.append("validation error in ").append(operator).append(":");
		for (final String e : exceptions) {
			builder.append('\n');
			builder.append(e);
		}
		return builder.toString();
	}
}
