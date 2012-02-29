package de.uniol.inf.is.odysseus.fusion.tracking.aggregate.function;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import com.vividsolutions.jts.geom.Geometry;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;


public class TrackingPartialAggregate<T> implements IPartialAggregate<RelationalTuple<? extends IMetaAttribute>>, Iterable<RelationalTuple<? extends IMetaAttribute>>{
	
	final Map<Integer, RelationalTuple<? extends IMetaAttribute>> elems;
	
	public TrackingPartialAggregate(RelationalTuple<? extends IMetaAttribute> elem) {
		elems = new HashMap<Integer,RelationalTuple<? extends IMetaAttribute>>();
		addElem(elem);
	}
	
	public TrackingPartialAggregate(TrackingPartialAggregate<RelationalTuple<? extends IMetaAttribute>> p) {
		this.elems = new HashMap<Integer,RelationalTuple<? extends IMetaAttribute>>(p.elems);
	}

	public Map<Integer, RelationalTuple<? extends IMetaAttribute>> getElems() {
		return elems;
	}
	
	public TrackingPartialAggregate<T> addElem(RelationalTuple<? extends IMetaAttribute> elem) {
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
	public ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>> clone(){
		return new ElementPartialAggregate<RelationalTuple<? extends IMetaAttribute>>(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<RelationalTuple<? extends IMetaAttribute>> iterator() {
		return ((Iterable<RelationalTuple<? extends IMetaAttribute>>) elems).iterator();
	}

	public int size() {
		return elems.size();
	}
}
