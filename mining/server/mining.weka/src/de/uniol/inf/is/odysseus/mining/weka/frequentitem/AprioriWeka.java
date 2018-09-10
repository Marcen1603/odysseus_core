package de.uniol.inf.is.odysseus.mining.weka.frequentitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import weka.associations.Apriori;
import weka.associations.AprioriItemSet;
import weka.core.FastVector;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.frequentitem.Pattern;

@SuppressWarnings("deprecation")
public class AprioriWeka<M extends ITimeInterval> extends Apriori implements IWekaFPM<M> {

	private static final long serialVersionUID = -4037361368589354531L;

	@SuppressWarnings({ "unchecked" })
	@Override
	public List<Pattern<M>> getItemSets(M metadata) {
		List<Pattern<M>> list = new ArrayList<>();
		Iterator<FastVector<AprioriItemSet>> iter = m_Ls.iterator();
		while (iter.hasNext()) {
			FastVector<AprioriItemSet> sets = iter.next();
			for (AprioriItemSet set : sets) {
				List<Tuple<M>> tuples = new ArrayList<>();
				for (int item : set.items()) {
					Tuple<M> t = new Tuple<M>(1, false);
					t.setAttribute(0, item);
					t.setMetadata(metadata);
					tuples.add(t);
				}
				Pattern<M> p = new Pattern<>(tuples, set.counter());
				list.add(p);
			}
		}
		return list;
	}

}
