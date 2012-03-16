/** Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

package de.uniol.inf.is.odysseus.mining.clustering.feature;

import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen
 * Created at: 06.09.2011
 */
public class RelationalTIClusteringFeature<M extends ITimeInterval> extends ClusteringFeature<Tuple<M>>{
	
	public RelationalTIClusteringFeature(Tuple<M> tuple){
		createRelationalTuple(tuple);
	}
	
	private void createRelationalTuple(Tuple<M> tuple){
		Tuple<M> linearSum = new Tuple<M>(tuple.size());
		Tuple<M> squareSum = new Tuple<M>(tuple.size());		
		for(int i=0;i<tuple.size();i++){
			if(tuple.getAttribute(i) instanceof Number){
				linearSum.setAttribute(i, 0);
				squareSum.setAttribute(i, 0);			
			}
			if(tuple.getAttribute(i) instanceof String){
				linearSum.setAttribute(i, "");
				squareSum.setAttribute(i, "");
			}
		}
		this.setLinearSum(linearSum);
		this.setSquareSum(squareSum);
		super.setNumberOfPoints(1);		
		for(int i=0;i<tuple.size();i++){						
			addToSum(i, tuple, this.getLinearSum(), false);
			addToSum(i, tuple, this.getSquareSum(), true);
		}
	}
	
	@Override
	public void add(Tuple<M> tuple){
		Tuple<M> linearSum = super.getLinearSum();
		Tuple<M> squareSum = super.getSquareSum();
		if(linearSum.size()!=tuple.size() || squareSum.size() != tuple.size()){
			throw new RuntimeException("Number of attributes are not equal");
		}
		for(int i=0;i<tuple.size();i++){			
			addToSum(i, tuple, this.getLinearSum(), false);
			addToSum(i, tuple, this.getSquareSum(), true);
		}		
		super.setNumberOfPoints(getNumberOfPoints()+1);
	}

	
	@Override
	public ClusteringFeature<Tuple<M>> merge(ClusteringFeature<Tuple<M>> other) {
		for(int i=0;i<other.getLinearSum().size();i++){
			this.addToSum(i, other.getLinearSum(), this.getLinearSum(), false);
			this.addToSum(i, other.getSquareSum(), this.getSquareSum(), false);
		}
		int oldCount = super.getNumberOfPoints();
		this.setNumberOfPoints(oldCount+other.getNumberOfPoints());
		return this;
	}	
	
	private void addToSum(int pos, Tuple<M> tuple, Tuple<M> sumTuple, boolean square){
		Object value = tuple.getAttribute(pos);
		Object newvalue = value;
		if(value instanceof Double){
			newvalue = addValue(sumTuple, pos, (Double)value, square);			
		}else if(value instanceof Integer){			
			newvalue = addValue(sumTuple, pos, (Integer)value, square);
		}else if(value instanceof Float){			
			newvalue = addValue(sumTuple, pos, (Float)value, square);
		}else if(value instanceof Long){			
			newvalue = addValue(sumTuple, pos, (Long)value, square);
		}else if(value instanceof String){
			newvalue = addValue(sumTuple, pos, (String)value, square);
		}
		sumTuple.setAttribute(pos, newvalue);
	}	
	
	@SuppressWarnings("static-method")
    private Object addValue(Tuple<M> tuple, int pos, Double value, boolean square){
		Double old = tuple.getAttribute(pos);
		if(square){
			value = value*value;
		}
		return old+value;
	}
	
	@SuppressWarnings("static-method")
    private Object addValue(Tuple<M> tuple, int pos, Integer value, boolean square){
		Integer old = tuple.getAttribute(pos);
		if(square){
			value = value*value;
		}
		return old+value;
	}
	
	@SuppressWarnings("static-method")
    private Object addValue(Tuple<M> tuple, int pos, Float value, boolean square){
		Float old = tuple.getAttribute(pos);
		if(square){
			value = value*value;
		}
		return old+value;
	}
	
	@SuppressWarnings("static-method")
    private Object addValue(Tuple<M> tuple, int pos, Long value, boolean square){
		Long old = tuple.getAttribute(pos);
		if(square){
			value = value*value;
		}
		return old+value;
	}
	
	@SuppressWarnings("static-method")
    private Object addValue(Tuple<M> tuple, int pos, String value, boolean square){
		String old = tuple.getAttribute(pos);				
		return old+value;
	}
	

}
