package de.uniol.inf.is.odysseus.pnapproach.pn.physicaloperator.relational.join.helper;

import de.uniol.inf.is.odysseus.metadata.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.ElementType;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.IPosNeg;
import de.uniol.inf.is.odysseus.pnapproach.base.metadata.PosNeg;


public class PNMetaMergeFunction implements IMetadataMergeFunction<IPosNeg> {
	
//	private int leftIDSize;
//	private int rightIDSize;
	
	public PNMetaMergeFunction(int leftIDSize, int rightIDSize){
//		this.leftIDSize = leftIDSize;
//		this.rightIDSize = rightIDSize;
	}
	
	
	public PNMetaMergeFunction(PNMetaMergeFunction pnMetaMergeFunction) {
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

		return pn;
	}
	
	public void init(){
	}

	@Override
	public PNMetaMergeFunction clone()  {
		return new PNMetaMergeFunction(this);
	}
}
