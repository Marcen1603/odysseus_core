/*
 * Created on 10.12.2004
 *
 */
package mg.dynaquest.queryoptimization.trafo;

import java.util.HashMap;
import java.util.List;

import mg.dynaquest.queryexecution.po.access.RMIDataAccessPO;
import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.TopPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryoptimization.trafo.rules.AccessPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.CollectorPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.DifferencePOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.IntersectionPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.JoinPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.ProjectPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.SelectPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.SortPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.TransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.UnionPOTransformationRule;
import mg.dynaquest.queryoptimization.trafo.rules.WindowPOTransformationRule;


/**
 * 
 * Diese Klasse dient dazu, einen übergebenen Plan aus logischen Operatoren
 * in einen physischen Plan zu übersetzen. Diese Klasse stellt dabei nur die 
 * Schnittstelle dar. Die Art der Plangenerierung (vollständig, heuristik etc). 
 * muss von einer Unterklasse implementiert werden
 * 
 * @author Marco Grawunder
 * 
 *
 */
abstract public class ProcessPlanTransform {    
    
    /**
	 * @uml.property  name="trafos"
	 * @uml.associationEnd  qualifier="className:java.lang.String mg.dynaquest.queryoptimization.trafo.rules.JoinPOTransformationRule"
	 */
    protected HashMap<String, TransformationRule> trafos = new HashMap<String, TransformationRule>();
    
    public ProcessPlanTransform() {
		trafos.put("mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO", new AccessPOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.AccessPO", new AccessPOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.DifferencePO", new DifferencePOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.IntersectionPO", new IntersectionPOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.JoinPO", new JoinPOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.ProjectPO", new ProjectPOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.SelectPO", new SelectPOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.SortPO", new SortPOTransformationRule());
		trafos.put("mg.dynaquest.queryexecution.po.algebra.UnionPO", new UnionPOTransformationRule());  
		trafos.put("mg.dynaquest.queryexecution.po.algebra.CollectorPO", new CollectorPOTransformationRule());
	}
    
    public abstract List<PlanOperator> transform(AlgebraPO logicalPlan);

    /**
     * @param logicalPlan
     * @return
     */
    protected TransformationRule getTrafoRule(SupportsCloneMe logicalPlan) {
        String className = logicalPlan.getClass().getName();
        return trafos.get(className);
    }
    
    
    public static void dumpPlan(AlgebraPO plan, String indent){
    	StringBuffer toDump = new StringBuffer();
    	dumpPlan(plan,indent,toDump);
    	System.out.println(toDump);
    }
    
    public static void dumpPlan(PlanOperator plan, String indent){
    	StringBuffer toDump = new StringBuffer();
    	dumpPlan(plan,indent,toDump);
    	System.out.println(toDump);
    }
    
    public static void dumpPlan(AlgebraPO plan, String indent, StringBuffer dumpedPlan){
        if (plan instanceof SchemaTransformationAccessPO){
        	dumpedPlan.append(((AccessPO)plan).getSource()+" --> ");
        }
    	dumpedPlan.append(plan.getPOName()+" "+plan.getOutElements()+"\n");
        for (int i=0;i<plan.getNumberOfInputs();i++){
            dumpedPlan.append(indent+"  +Child "+i+": ");
            if (plan.getInputPO(i) != null){
            	dumpPlan(plan.getInputPO(i), indent+"  ", dumpedPlan);
            }else{
            	dumpPlan(plan.getPhysInputPO(i), indent+"  ", dumpedPlan);
            }
        }
    }
    
    public static void dumpPlan(PlanOperator plan, String indent, StringBuffer dumpedPlan){
//    	if (plan == null){
//    		dumpedPlan.append(" ACHTUNG NULL --> FEHLER\n");
//    		return;
//    	}
        dumpedPlan.append(plan.getPOName()+"\n");
        for (int i=0;i<plan.getNumberOfInputs();i++){
            dumpedPlan.append(indent+" +Child "+i+": ");
            dumpPlan(plan.getInputPO(i), indent+"  ", dumpedPlan);
        }
    }   
    
    private static void dumpPlan(SupportsCloneMe po, String indent) {
    	if (po instanceof PlanOperator){
    		dumpPlan((PlanOperator)po, indent);
    	}else{
    		dumpPlan((AlgebraPO) po, indent);
    	}
		
	}
    
    
    static public SupportsCloneMe cloneTree(SupportsCloneMe toClone){
    	// Aktuellen Vaterknoten klonen
    	SupportsCloneMe nOp = toClone.cloneMe();
    	// Unterschiedliche Zugriffsmethoden bei PlanOperator und AlgebraPO
    	if (nOp instanceof PlanOperator){
    		PlanOperator po = (PlanOperator) nOp;
    		// Ein Planoperator kann keine AlgebraKnoten als Kinder haben
    		for (int i=0;i<po.getNumberOfInputs();i++){
    			po.setInputPO(i, (PlanOperator)cloneTree(((PlanOperator)toClone).getInputPO(i)));
    		}
    	}else{ // AlgebraOp
    		AlgebraPO po = (AlgebraPO) nOp;
    		for (int i=0;i<po.getNumberOfInputs();i++){
    			// Wenn die Kinder des Operators PlanOperatoren sind, dann
    			// andere Zuweisungs/Auslese-Methoden, als wenn sie AlgebraOperatoren sind
    			if (((AlgebraPO)toClone).getPhysInputPO(i) != null){
    				po.setPhysInputPO(i, (PlanOperator) cloneTree(((AlgebraPO)toClone).getPhysInputPO(i)));
    			}else{ // AlgebraPO
    				po.setInputPO(i, (AlgebraPO) cloneTree(((AlgebraPO)toClone).getInputPO(i))); 
    			}
    		}
    	}
    	return nOp;
    }
    
	private static void cloneAndDump(SupportsCloneMe po) {
		SupportsCloneMe cloned = cloneTree(po);
    	dumpPlan(po, "  ");
    	dumpPlan(cloned, "  ");
	}
    
	// TODO: Auslagern in JUnit-Test
    static public void testCloneTree(){
    	// Bäume aufbauen
    	// 1. Nur AccessPO
    	AccessPO access = new SchemaTransformationAccessPO();
    	access.setPOName("Access 1");
    	//cloneAndDump(access);

    	// 1b. physisch
    	RMIDataAccessPO access2 = new RMIDataAccessPO();
    	access2.setPOName("phyAccess2");
    	//cloneAndDump(access2);

    	
    	// 2. SelectPO über AccessPO
    	SelectPO sel = new SelectPO();
    	sel.setInputPO(access);
    	//cloneAndDump(sel);

    	// 3. Join mit physisisch rechts
    	JoinPO join = new JoinPO();
    	join.setLeftInput(sel);
    	join.setRightInput(access2);
    	//cloneAndDump(join);
    	
    	// 4. Top drüber
    	TopPO top = new TopPO();
    	top.setInputPO(join);
    	//dumpPlan(top,"");
    	cloneAndDump(top);
    	
    	
    } 

	public static void main(String[] args) {
		testCloneTree();
	}
}
