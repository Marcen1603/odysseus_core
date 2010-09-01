package de.uniol.inf.is.odysseus.util;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ISubscriber;
import de.uniol.inf.is.odysseus.base.ISubscription;

public class SimplePlanPrinter<T> {

	private List<T> visited = new ArrayList<T>();

	public void clear() {
		visited.clear();
	}

	public String createString(T root) {

		StringBuffer buff = new StringBuffer();
		buff = new StringBuffer();
		dumpPlan(root, 0, buff);
		return buff.toString();
	}

	private void dumpPlan(T object, int depth, StringBuffer builder) {
		builder.append(getIndent(depth));
		if (!contains(visited, object)) {
			visited.add(object);
			builder.append(object);
			builder.append('\n');
			if (object instanceof ISubscriber) {
				@SuppressWarnings("unchecked")
				ISubscriber<?, ISubscription<T>> objectSub = (ISubscriber<?, ISubscription<T>>) object;
				for (ISubscription<T> sub : objectSub.getSubscribedToSource()) {
					builder.append(getIndent(depth));
					builder.append(sub.getSinkInPort() + " <--- " + sub.getSourceOutPort());
					dumpPlan(sub.getTarget(), depth + 1, builder);
				}
			}
		} else {
			builder.append(object);
			builder.append('\n');
			builder.append(getIndent(depth) + "  [see above for following operators]\n");
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

	private String getIndent(int depth) {
		String str = "";
		while (depth > 0) {
			str = str + " ";
			depth--;
		}
		return str;
	}

}
