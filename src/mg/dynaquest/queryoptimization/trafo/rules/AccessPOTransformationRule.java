/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo.rules;

import org.apache.log4j.Logger;

import mg.dynaquest.queryexecution.caching.CacheManager;
import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.caching.CachingPO;
import mg.dynaquest.wrapper.WrapperPlanFactory;

/**
 * @author Marco Grawunder
 *
 */
public class AccessPOTransformationRule extends TransformationRule {

	static Logger logger = Logger.getLogger(AccessPOTransformationRule.class);
	
    /**
     * Wandelt einen Plan mit einem logischen Zugriffsoperator in einen
     * physischen Wrapper-Operator um  
     * @param schemaTransformationAccessPO
     * @return Wrapper bestehend aus physischen PlanOperatoren
     */
    private static PlanOperator transformToWrapperPO(AccessPO accessPO) {
    	PlanOperator wrapPlan;
    	logger.info("Übersetze Zugriff auf Quelle "+accessPO.getSource());
//    	System.out.println(accessPO.getSource()); 
        if (CacheManager.getInstance().isCachingEnabled() && accessPO instanceof SchemaTransformationAccessPO) {
        	logger.debug("Installiere Caching PO");
        	wrapPlan = new CachingPO((SchemaTransformationAccessPO)accessPO);
        } else {
        	wrapPlan = WrapperPlanFactory.getAccessPlan(accessPO);
        	if (wrapPlan == null){
        		logger.error("Keinen passenden Wrapper gefunden. Quelle "+accessPO.getSource()+" verwendbar?");
        	}
        }
        return wrapPlan;
    }

    @Override
    public PlanOperator transform(SupportsCloneMe logicalPO, int no) {
        return transformToWrapperPO((AccessPO)logicalPO);
    }

    @Override
    public int getNoOfTransformations() {
        return 1;
    }
}
