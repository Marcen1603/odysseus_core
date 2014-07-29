package de.uniol.inf.is.odysseus.mining.weka.mapping;

import java.util.List;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;

public class WekaConverter {
	
	public static <M extends ITimeInterval> Instances convertToInstances(List<Tuple<M>> tuples, WekaAttributeResolver war) {
		Instances instances = new Instances(war.getSchema().getURI(), war.getWekaSchema(), 10);
		// wrap up elements
		for (Tuple<M> tuple : tuples) {
			Instance inst = convertToNominalInstance(tuple, war);
			instances.add(inst);
		}
		return instances;

	}

	public static <M extends ITimeInterval> Instance convertToNominalInstance(Tuple<M> tuple, WekaAttributeResolver war) {
		Instances instances = new Instances(war.getSchema().getURI(), war.getWekaSchema(), 1);		
		Instance inst = new DenseInstance(war.getWekaSchema().size());		
		for (int i = 0; i < tuple.size(); i++) {
			Attribute a = war.getWekaSchema().get(i);			
			if (a.isNominal() || a.isString()) {
				String val = tuple.getAttribute(i).toString();
				inst.setValue(a, val);
			} else if (tuple.getAttribute(i) instanceof Boolean) {
				boolean val = (boolean) tuple.getAttribute(i);
				inst.setValue(i, val ? 1.0 : 0.0);
			} else {
				double val = ((Number) tuple.getAttribute(i)).doubleValue();
				inst.setValue(i, val);
			}
		}
		inst.setDataset(instances);	
		return inst;
	}
	
}
