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
package de.uniol.inf.is.odysseus.core.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.IOwnedOperator;

public class SimplePlanPrinter<T> {

	private List<T> visited = new ArrayList<T>();
	private final boolean printDetails;

	public SimplePlanPrinter() {
		this(false);
	}

	public SimplePlanPrinter(boolean printDetails) {
		this.printDetails = printDetails;
	}

	public void clear() {
		visited.clear();
	}

	public String createString(T root) {

		StringBuffer buff = new StringBuffer();
		dumpPlan(root, 0, buff);
		return buff.toString();
	}

	@SuppressWarnings("unchecked")
	private void dumpPlan(T object, int depth, StringBuffer builder) {

		if (!contains(visited, object)) {
			visited.add(object);
			builder.append(getObjectName(object, printDetails));
			builder.append('\n');
			if (object instanceof ISubscriber) {
				ISubscriber<?, ISubscription<?,?>> objectSub = (ISubscriber<?, ISubscription<?,?>>) object;
				for (ISubscription<?,?> sub : objectSub.getSubscribedToSource()) {
					builder.append(getIndent(depth));
					builder.append(sub.getSinkInPort() + " <- " + sub.getSourceOutPort() + " ");
					dumpPlan((T)sub.getSource(), depth + 1, builder);
				}
			}
		} else {
			builder.append(getObjectName(object,printDetails));
			builder.append('\n');
			builder.append(getIndent(depth + 1) + "[see above for following operators]\n");
		}
	}

	private boolean contains(List<T> list, T op) {
		for (Object other : list) {
			if (other == op) {
				return true;
			}
		}
		return false;
	}

	private static String getObjectName(Object object, boolean withDetails) {
		StringBuffer name = new StringBuffer();
		name.append(object.getClass().getSimpleName() + " (" + object.hashCode() + ")");
		if (object instanceof IOwnedOperator) {
			name.append(" Owner: " + ((IOwnedOperator) object).getOwnerIDs());
		}
		if (withDetails) {
			if (object instanceof ILogicalOperator) {
				name.append(" " + ((ILogicalOperator) object).getOutputSchema());
				name.append(" " + ((ILogicalOperator) object).getParameterInfos());
			}
		}
		return name.toString();
	}

	private static String getIndent(int depth) {
		String str = "";
		while (depth > 0) {
			str = str + "       ";
			depth--;
		}
		return str;
	}

}
