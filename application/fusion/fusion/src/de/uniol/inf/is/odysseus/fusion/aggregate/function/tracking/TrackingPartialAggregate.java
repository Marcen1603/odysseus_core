package de.uniol.inf.is.odysseus.fusion.aggregate.function.tracking;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.core.collection.Tuple;


public class TrackingPartialAggregate<T> implements IPartialAggregate<Tuple<? extends IMetaAttribute>>, Iterable<Tuple<? extends IMetaAttribute>>{
	
	final Map<Integer, Tuple<? extends IMetaAttribute>> elems;
	
	public TrackingPartialAggregate(Tuple<? extends IMetaAttribute> elem) {
		elems = new HashMap<Integer,Tuple<? extends IMetaAttribute>>();
		addElem(elem);
	}
	
	public TrackingPartialAggregate(TrackingPartialAggregate<Tuple<? extends IMetaAttribute>> p) {
		this.elems = new HashMap<Integer,Tuple<? extends IMetaAttribute>>(p.elems);
	}

	public Map<Integer, Tuple<? extends IMetaAttribute>> getElems() {
		return elems;
	}
	
	public TrackingPartialAggregate<T> addElem(Tuple<? extends IMetaAttribute> elem) {
		boolean tracked = false;
		
		Geometry geometry1 =  (Geometry)elem.getAttribute(0);
		
		for (int i = 0; i < elems.size(); i++) {
			Geometry geometry2 =  (Geometry)  elems.get(i).getAttribute(0);
			
			if(geometry1.contains(geometry2.getCentroid())){
				tracked = true;
				//elems.get(i).setAttribute(3, i);
			}			
		}
		if (!tracked) {
			this.getElems().put(elems.size(), elem);
		}
		return this;
	}

	@Override
	public String toString() {
		return ""+elems;
	}
	
	@Override
	public ElementPartialAggregate<Tuple<? extends IMetaAttribute>> clone(){
		return new ElementPartialAggregate<Tuple<? extends IMetaAttribute>>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<Tuple<? extends IMetaAttribute>> iterator() {
		return ((Iterable<Tuple<? extends IMetaAttribute>>) elems).iterator();
	}

	public int size() {
		return elems.size();
	}
}
