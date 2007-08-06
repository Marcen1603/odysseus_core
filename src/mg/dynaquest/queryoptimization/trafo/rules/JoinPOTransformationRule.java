/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.relational.DoublePipeJoinPO;
import mg.dynaquest.queryexecution.po.relational.HashJoinPO;
import mg.dynaquest.queryexecution.po.relational.HybridHashJoinPO;
import mg.dynaquest.queryexecution.po.relational.NestedLoopJoinPO;
import mg.dynaquest.queryexecution.po.relational.RelationalPhysicalJoinPO;
import mg.dynaquest.queryexecution.po.relational.RippleJoinPO;
import mg.dynaquest.queryexecution.po.relational.XJoinPO;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElementSet;

import org.apache.log4j.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Marco Grawunder
 *
 *	TEST
 *
 */
public class JoinPOTransformationRule extends TransformationRule{

    static Logger logger = Logger.getLogger(JoinPOTransformationRule.class);

    /**
     * @param joinPO
     * @return
     */
    @SuppressWarnings("unused")
	private static PlanOperator transformToSortMergeJoinUnsortedInput(JoinPO joinPO) {
    	throw new NotImplementedException();
    	// Problem hier: Es werden hinter einem Physischen PO neuer logische POs integriert ...
    	
//        PhysicalJoinPO newPlan = new SortMergeJoinPO(joinPO);
//        SortPO sortPlan1 = new SortPO(joinPO.getJoinPredicate().getLeftElement());
//        SortPO sortPlan2 = new SortPO(joinPO.getJoinPredicate().getRightElement());
//        sortPlan1.setPhysInputPO(joinPO.getPhysInputPO(0));
//        sortPlan2.setInputPO(joinPO.getPhysInputPO(0));
//
//        newPlan.setInputPO(0,(PlanOperator)sortPlan1);
//        newPlan.setInputPO(1,(PlanOperator)sortPlan2);
//
//        return newPlan;
    }

    /**
     * @param joinPO
     * @return
     * @throws TransformationNotApplicableExeception 
     */
    private static PlanOperator transformToSortMergeJoin(JoinPO joinPO) throws TransformationNotApplicableExeception {
    	SDFSchemaElementSet sorted1 = ((NAryPlanOperator)joinPO.getPhysInputPO(0)).getSorted();
        SDFSchemaElementSet sorted2 = ((NAryPlanOperator)joinPO.getPhysInputPO(1)).getSorted();
        if (sorted1!=null && sorted2!=null){
//	        if ((sorted1.contains(joinPO.getJoinPredicate().getLeftElement())) &&
//	        	(sorted2.contains(joinPO.getJoinPredicate().getRightElement()))){
//	        	return setJoinValues(joinPO, new SortMergeJoinPO());
//	        }
        }   
        throw new NotImplementedException();
      	//throw new TransformationNotApplicableExeception("Input Streams are not sorted or do not fit "+sorted1+" "+sorted2);
    }

    private static PlanOperator transformToDoublePipeJoin(JoinPO joinPO) {
    	return setJoinValues(joinPO, new DoublePipeJoinPO());
    }
    
    private static PlanOperator transformToHashJoin(JoinPO joinPO) {
    	return setJoinValues(joinPO, new HashJoinPO());
    }

    private static PlanOperator transformToHybridHashJoin(JoinPO joinPO) {
    	return setJoinValues(joinPO, new HybridHashJoinPO());
    }

    private static PlanOperator transformToNestedLoopJoin(JoinPO joinPO) {
    	return setJoinValues(joinPO, new NestedLoopJoinPO());
    }
    
    private static PlanOperator transformToRippleJoin(JoinPO joinPO) {
        return setJoinValues(joinPO, new RippleJoinPO());
    }
    
    private static PlanOperator transformToXJoin(JoinPO joinPO) {
        return setJoinValues(joinPO, new XJoinPO());
    }
    
	private static RelationalPhysicalJoinPO setJoinValues(JoinPO joinPO, RelationalPhysicalJoinPO newPlan) {
		newPlan.setInputPO(0, (PlanOperator)joinPO.getPhysInputPO(0));
        newPlan.setInputPO(1, (PlanOperator)joinPO.getPhysInputPO(1));
        newPlan.setJoinPO(joinPO);
        return newPlan;
	}


    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) 
    	throws TransformationNotApplicableExeception {
    	switch (no){
    	case 0:
    		return transformToSortMergeJoin((JoinPO) logicalPO);
    	case 1:
    		return transformToDoublePipeJoin((JoinPO) logicalPO);
    	case 2:
    		return transformToHashJoin((JoinPO) logicalPO);
    	case 3:
    		return transformToHybridHashJoin((JoinPO) logicalPO);
    	case 4:
    		return transformToNestedLoopJoin((JoinPO) logicalPO);
    	case 5:
    		return transformToRippleJoin((JoinPO) logicalPO);
    	case 6:
    		return transformToXJoin((JoinPO) logicalPO);
    	}
        return null;
    }

    @Override
    public int getNoOfTransformations() {
        // TODO Auto-generated method stub
        return 7;
    }
}
