package de.uniol.inf.is.odysseus.probabilistic.common;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public final class ProbabilisticDiscreteUtils {
	public static Object[][] getWorlds(Tuple<?> input, int[] probabilisticAttributePos) {
		Iterator<?>[] attributeIters = new Iterator<?>[probabilisticAttributePos.length];
		int worldNum = 1;
		for (int i = 0; i < probabilisticAttributePos.length; i++) {
			AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) input.getAttribute(probabilisticAttributePos[i]);
			worldNum *= attribute.getValues().size();
			attributeIters[i] = attribute.getValues().entrySet().iterator();
		}

		// Create all possible worlds
		Object[][] worlds = new Object[worldNum][probabilisticAttributePos.length];
		double instances = 1.0;
		for (int i = 0; i < probabilisticAttributePos.length; i++) {
			int world = 0;
			AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) input.getAttribute(probabilisticAttributePos[i]);
			int num = (int) (worlds.length / (attribute.getValues().size() * instances));
			while (num > 0) {
				Iterator<?> iter = attribute.getValues().entrySet().iterator();
				while (iter.hasNext()) {
					Entry<?, Double> entry = ((Map.Entry<?, Double>) iter.next());
					for (int j = 0; j < instances; j++) {
						worlds[world][i] = entry.getKey();
						world++;
					}
				}
				num--;
			}
			instances *= attribute.getValues().size();
		}
		return worlds;
	}

	private ProbabilisticDiscreteUtils() {
	}
}
