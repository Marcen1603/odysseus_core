/*
 * Created on 01.02.2005
 *
 */
package mg.dynaquest.queryexecution.po.base;




/**
 * @author Marco Grawunder
 *
 */
public abstract class PhysicalSelectJoinPO extends NAryPlanOperator{
	
	public PhysicalSelectJoinPO() 
	{
		 super();
	}
	
    /**
     * @param joinPO
     */
    public PhysicalSelectJoinPO(PhysicalSelectJoinPO joinPO) {
        super(joinPO);
    }


}
