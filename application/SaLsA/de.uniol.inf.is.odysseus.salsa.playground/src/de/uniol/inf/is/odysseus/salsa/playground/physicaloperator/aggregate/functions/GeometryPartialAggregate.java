package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator.aggregate.functions;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ElementPartialAggregate;
import de.uniol.inf.is.odysseus.physicaloperator.aggregate.functions.ListPartialAggregate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class GeometryPartialAggregate<T> implements IPartialAggregate<T>, Iterable<T> {

	final List<T> notMElems;
	final List<T> elems;
	protected volatile static Logger LOGGER = LoggerFactory.getLogger(GeometryPartialAggregate.class);
	
	public GeometryPartialAggregate(T elem) {
		this.elems = new LinkedList<T>();
		this.elems.add(elem);
		
		this.notMElems = new LinkedList<T>();
		this.notMElems.add(elem);
		//LOGGER.debug("Add first Element");
	}
	
	public GeometryPartialAggregate(GeometryPartialAggregate<T> p) {
		//LOGGER.debug("Add first ElementLIST");
		this.elems = new LinkedList<T>(p.elems);
		this.notMElems =  new LinkedList<T>(p.elems);
	}

	public List<T> getElems() {
		return elems;
	}
	
	public GeometryPartialAggregate<T> addElem(T elem) {
		boolean merged = false;
		 Geometry geometry = (Geometry)((RelationalTuple)elem).getAttribute(0);
		 Geometry geometry_buffer = geometry.buffer(300);
		 
		for(int i = 0; i< elems.size(); i++){
			
			RelationalTuple tuple = (RelationalTuple)elems.get(i);
			Geometry geometry_element = (Geometry)tuple.getAttribute(0);

			if(geometry_element.crosses(geometry_buffer)){
				merged = true;
				tuple.setAttribute(0,geometry_element.union(geometry).convexHull());
				elems.add((T)tuple);
			}	
		}
		if(!merged){
			this.elems.add(elem);
		}
		
		return this;
	}

//	Testing
//	public GeometryPartialAggregate<T> addElem(T elem) {
//		boolean merged = false;
//		 Geometry geometry = (Geometry)((RelationalTuple)elem).getAttribute(0);
//		// Geometry geometry_buffer = geometry.buffer(100);
//		 
//		for(int i = 0; i< notMElems.size(); i++){
//			
//			RelationalTuple tuple2 = (RelationalTuple)notMElems.get(i);
//			Geometry geometry_element = (Geometry)tuple2.getAttribute(0);
//		
//			if(geometry_element.distance(geometry) < 200){
//				merged = true;
//				tuple2.setAttribute(0,geometry_element.union(geometry).convexHull());
//				notMElems.set(i, (T)tuple2);
//				elems.add((T)tuple2);
//			}
//			
//			if(geometry_element.crosses(geometry)){
//				merged = true;
//				tuple2.setAttribute(0,geometry_element.union(geometry).convexHull());
//				notMElems.set(i, (T)tuple2);
//				elems.add((T)tuple2);
//			}	
//		}
//		if(!merged){
//			this.notMElems.add(elem);
//		}
//		
//		return this;
//	}

	
	@Override
	public String toString() {
		return ""+elems;
	}
	
	@Override
	public ElementPartialAggregate<T> clone(){
		return new ElementPartialAggregate<T>(this);
	}

	@Override
	public Iterator<T> iterator() {
		return elems.iterator();
	}

	public int size() {
		return elems.size();
	}
	
}
