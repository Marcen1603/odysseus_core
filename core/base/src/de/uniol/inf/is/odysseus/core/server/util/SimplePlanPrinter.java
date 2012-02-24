/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.core.server.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.ISubscriber;
import de.uniol.inf.is.odysseus.core.ISubscription;

public class SimplePlanPrinter<T> {

	private List<T> visited = new ArrayList<T>();

	public void clear() {
		visited.clear();
	}

	public String createString(T root) {

		StringBuffer buff = new StringBuffer();
		dumpPlan(root, 0, buff);
		return buff.toString();
	}

	private void dumpPlan(T object, int depth, StringBuffer builder) {
		
		if (!contains(visited, object)) {
			visited.add(object);
			builder.append(getObjectName(object));
			builder.append('\n');
			if (object instanceof ISubscriber) {
				@SuppressWarnings("unchecked")
				ISubscriber<?, ISubscription<T>> objectSub = (ISubscriber<?, ISubscription<T>>) object;
				for (ISubscription<T> sub : objectSub.getSubscribedToSource()) {		
					builder.append(getIndent(depth));
					builder.append(sub.getSinkInPort() + " <- " + sub.getSourceOutPort()+" ");
					dumpPlan(sub.getTarget(), depth + 1, builder);
				}
			}
		} else {
			builder.append(getObjectName(object));
			builder.append('\n');
			builder.append(getIndent(depth+1) + "[see above for following operators]\n");
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

	private String getObjectName(Object object){
		String name ="";
		name = object.getClass().getSimpleName()+" ("+object.hashCode()+")";
		return name;
	}
	
	private String getIndent(int depth) {
		String str = "";
		while (depth > 0) {
			str = str + "       ";
			depth--;
		}
		return str;
	}

}
