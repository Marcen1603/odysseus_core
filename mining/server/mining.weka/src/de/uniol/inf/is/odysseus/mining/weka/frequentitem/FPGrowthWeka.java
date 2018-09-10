package de.uniol.inf.is.odysseus.mining.weka.frequentitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import weka.associations.BinaryItem;
import weka.associations.FPGrowth;
import weka.core.Capabilities;
import weka.core.Capabilities.Capability;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.frequentitem.Pattern;

public class FPGrowthWeka<M extends ITimeInterval> extends FPGrowth implements IWekaFPM<M> {

	private static final long serialVersionUID = -8396514245108926400L;

	public FPGrowthWeka(){
		super();
		super.m_findAllRulesForSupportLevel = true;
	}
	
	@Override
	public Capabilities getCapabilities() {
	    Capabilities result = super.getCapabilities();
	    result.enable(Capability.STRING_ATTRIBUTES);
	    return result;
	  }
	
	@Override
	public List<Pattern<M>> getItemSets(M metadata){
		List<Pattern<M>> list = new ArrayList<>();
		Iterator<FrequentBinaryItemSet> iter = m_largeItemSets.iterator();
		while(iter.hasNext()){
			FrequentBinaryItemSet set = iter.next();
			List<Tuple<M>> tuples = new ArrayList<>();
			for(BinaryItem item : set.getItems()){
				Tuple<M> t = new Tuple<M>(2, false);
				t.setAttribute(0, item.getAttribute().name());
				t.setAttribute(1, item.getFrequency());
				t.setMetadata(metadata);
				tuples.add(t);
			}
			Pattern<M> p = new Pattern<>(tuples, set.getSupport());
			list.add(p);
		}
		return list;
	}
}
