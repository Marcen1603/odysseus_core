package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.advertisements.operatorhelpers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import de.uniol.inf.is.odysseus.core.Subscription;
import de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.graph.GraphNode;

public class Tools<T extends Object> {
	/**
	 *  re-creates an int[]-array which was converted to a String via Arrays.toString()
	 */
	public static int[] fromStringToIntArray(String s) {
		// remove the brackets
		s= s.replace("[", "");
		s= s.replace("]", "");
		// split at the commas
		String[] elements = s.split(", ");
		int[] result = new int[elements.length];
		// parse
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(elements[i]);
		}
		return result;
	}
	
	/**
	 *  re-creates a String[]-array which was converted to a String via Arrays.toString()
	 */
	public static String[] fromStringToStringArray(String s) {
		// remove the brackets
		s= s.replace("[", "");
		s= s.replace("]", "");
		// split at the commas
		String[] elements = s.split(", ");
		String[] result = new String[elements.length];
		// parse
		for (int i = 0; i < result.length; i++) {
			result[i] = elements[i];
		}
		return result;
	}
	/**
	 * re-creates a Map<String,String>-object from its toString()-representation
	 */
	public static Map<String,String> fromStringToMap(String s) {
		Map<String,String> result = new HashMap<String,String>();
		if(s.equals("")) {
			return null;
		}
		s= s.replace("{", "");
		s= s.replace("}", "");
		String[] elements = s.split(", ");
		for(int i = 0; i < elements.length; i++) {
			String[] keyValuePair = elements[i].split("=");
			String key = keyValuePair[0];
			String value = keyValuePair[1];
			result.put(key, value);
		}
		return result;
	}

	public static List<GraphNode> removeDuplicates(List<GraphNode> l) {
		Set<GraphNode> s = new TreeSet<GraphNode>(new Comparator<GraphNode>() {
			@Override
			public int compare(GraphNode gn1, GraphNode gn2) {
				if(gn1.equals(gn2)) {
					return 0;
				} else {
					return -1;
				}

			}
		});
		List<GraphNode> result = new ArrayList<GraphNode>();
		s.addAll(l);
		Iterator<GraphNode> it = s.iterator();
		while(it.hasNext()) {
			result.add(it.next());
		}
		
		return result;
	}
	
	public static void collectGraphNodes(GraphNode currentOperator,
			Collection<GraphNode> list) {

		if (!list.contains(currentOperator)) {

			list.add(currentOperator);

			for (final Subscription<GraphNode> subscription : currentOperator
					.getSinkSubscriptions())
				if(!subscription.getTarget().isOld()) {
					Tools.collectGraphNodes(subscription.getTarget(), list);
				}

			for (final Subscription<GraphNode> subscription : currentOperator
					.getSubscribedToSource())
				Tools.collectGraphNodes(subscription.getTarget(), list);

		}

	}

}
