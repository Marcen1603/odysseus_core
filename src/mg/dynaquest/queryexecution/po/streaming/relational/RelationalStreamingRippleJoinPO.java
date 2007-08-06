package mg.dynaquest.queryexecution.po.streaming.relational;

import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleCorrelator;
import mg.dynaquest.queryexecution.po.streaming.base.StreamingRippleJoinPO;
import mg.dynaquest.queryexecution.po.streaming.object.SortedMapDynamicStreamBuffer;
import mg.dynaquest.queryexecution.po.streaming.object.StreamExchangeElement;
import mg.dynaquest.queryexecution.po.streaming.object.TimeInterval;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFComplexPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class RelationalStreamingRippleJoinPO extends StreamingRippleJoinPO {
	
	
	private RelationalTupleCorrelator compareAttrs = null;

	public RelationalStreamingRippleJoinPO(){
		super();
	}
	
	public RelationalStreamingRippleJoinPO(JoinPO algebraPO){
		super(algebraPO);
	}
	
	public RelationalStreamingRippleJoinPO(RelationalStreamingRippleJoinPO joinPO){
		super(joinPO);
	}
	
	protected RelationalTupleCorrelator getCompareAttrs() {
		if (compareAttrs  == null){
			compareAttrs = calculateCompareAttributes();
		}
		return compareAttrs;
	}

	private RelationalTupleCorrelator calculateCompareAttributes() {
		SDFAttributeList left = ((NAryPlanOperator)getLeftInput()).getOutElements();
		SDFAttributeList right = ((NAryPlanOperator)getRightInput()).getOutElements();
		RelationalTupleCorrelator ca = new RelationalTupleCorrelator(Math.max(left.size(), right.size()));
		
		SDFComplexPredicate jp = ((JoinPO)getAlgebraPO()).getJoinPredicate();
		
		// TODO: Umwandlung von komplexen Prädikaten in RelationalTupleCorrelator
//		ca.setPair(0, left.indexOf(jp.getLeftElement()), 
//					right.indexOf(jp.getRightElement()));
					
		
		
		return ca;
	}
	
	@Override
	protected StreamExchangeElement process_join(StreamExchangeElement elem1,
												 StreamExchangeElement elem2) {

		RelationalTuple r1 = (RelationalTuple) elem1.getCargo();
		RelationalTuple r2 = (RelationalTuple) elem2.getCargo();
		
		RelationalTuple joinR = r1.mergeRight(r2,getCompareAttrs());
			
		return new StreamExchangeElement<RelationalTuple>(joinR, TimeInterval.intersection(elem1.getValidity(),elem2.getValidity()));
	}

	@Override
	protected void setStreamBufferQueryPredicate(SortedMapDynamicStreamBuffer b1, 
												 SortedMapDynamicStreamBuffer b2) {
		throw new NotImplementedException();
//		RelationalTemporalJoinPredicate 
//		StreamBufferQueryPredicate pred = new StreamBufferQueryPredicate()

	}

	public SupportsCloneMe cloneMe() {
		return new RelationalStreamingRippleJoinPO(this);
	}

}
