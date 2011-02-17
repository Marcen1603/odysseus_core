/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.pnapproach.id.physicalopertor.relational.join.helper;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.ISweepArea.Order;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.PosNeg;
import de.uniol.inf.is.odysseus.pnapproach.id.metadata.IDGenerator;

public class PNIDMetadataMergeFunction implements IPNMetadataMergeFunction<IPosNeg> {
	
	private int leftIDSize;
	private int rightIDSize;
	
	public PNIDMetadataMergeFunction(int leftIDSize, int rightIDSize){
		this.leftIDSize = leftIDSize;
		this.rightIDSize = rightIDSize;
	}
	
	public PNIDMetadataMergeFunction(
			PNIDMetadataMergeFunction pnidMetadataMergeFunction) {
		this.leftIDSize = pnidMetadataMergeFunction.leftIDSize;
		this.rightIDSize = pnidMetadataMergeFunction.rightIDSize;
		
	}

	@Override
	public IPosNeg mergeMetadata(IPosNeg left, IPosNeg right) {
		PosNeg pn = new PosNeg();
		if(left.getElementType().equals(right.getElementType()) && left.getElementType() == ElementType.POSITIVE){
			pn.setElementType(ElementType.POSITIVE);
			// a resulting positive element always gets the latest
			// start timestamp of both source elements
			pn.setTimestamp(PointInTime.max(left.getTimestamp(), right.getTimestamp()));
		}
		else if( !left.getElementType().equals(right.getElementType()) ){
			pn.setElementType(ElementType.NEGATIVE);

			// take the timestamp of the negative element
			pn.setTimestamp(left.getElementType() == ElementType.NEGATIVE ? left.getTimestamp() : right.getTimestamp());
		}
		else if(left.getElementType().equals(right.getElementType()) && left.getElementType() == ElementType.NEGATIVE){
			throw new RuntimeException("Join zweier negativer Elemente.\n left: " + left.toString() + "\n right: " + right.toString());
		}
		// create the new ID
		// it will be the ID from the left concatenated with the id from the right
		
		if(left.getID() != null && right.getID() != null){
			List<Long> newID = new ArrayList<Long>();
			for(Long l: left.getID()){
				newID.add(l);
			}
			for(Long r: right.getID()){
				newID.add(r);
			}
			pn.setID(newID);
		}
		return pn;
	}
	
	@Override
	public IPosNeg createNegativeResult(IPosNeg mdata, Order order){
		IPosNeg clone;
		clone = (IPosNeg)mdata.clone();
		/**
		 * Das Element stammt aus dem linken Eingabedatenstrom
		 */
		if(order == Order.LeftRight){
			for(int i = 0; i<this.rightIDSize; i++){
				clone.getID().add(IDGenerator.getWildcard());
			}
		}
		else{
			List<Long> newID = new ArrayList<Long>();
			// f�ge erst gen�gend wildcards vorne ein
			for(int i = 0; i<this.leftIDSize; i++){
				newID.add(IDGenerator.getWildcard());
			}
			for(int i = 0; i<this.rightIDSize; i++){
				newID.addAll(clone.getID());
			}
			clone.setID(newID);
		}
		return clone;
	}
	
	@Override
	public void init(){
	}
	
	@Override
	public PNIDMetadataMergeFunction clone() {
		return new PNIDMetadataMergeFunction(this);
	}

}
