package de.uniol.inf.is.odysseus.mining.weka.frequentitem;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.frequentitem.Pattern;
import weka.associations.AssociationRulesProducer;
import weka.associations.Associator;

public interface IWekaFPM<M extends ITimeInterval> extends Associator, AssociationRulesProducer{
	public List<Pattern<M>> getItemSets(M metadata);

}
