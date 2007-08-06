package mg.dynaquest.queryexecution.po.access;

import mg.dynaquest.queryexecution.event.ReadDoneEvent;
import mg.dynaquest.queryexecution.event.ReadInitEvent;
import mg.dynaquest.queryexecution.po.algebra.AccessPO;
import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;


/**
 * Diese Klasse dient als Oberklasse aller phyischen Planoperatoren, die keine Kinder haben, sondern direkt auf eine Datenquelle zugreifen. Sie stellen immer Blätter im Anfragebaum da
 */

public abstract class PhysicalAccessPO<T extends AccessPO> extends NAryPlanOperator{

	// Events für den Zugriff auf die Datenquelle
	/**
	 * @uml.property  name="readInitEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public ReadInitEvent readInitEvent = new ReadInitEvent(this, 0);
	/**
	 * @uml.property  name="readDoneEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	public ReadDoneEvent readDoneEvent = new ReadDoneEvent(this, 0);
	
	public PhysicalAccessPO() {
		super();
	}

	/**
	 * Erzeugen eines neues AccessPOs mit Hilfe eines existierenden AccessPOs,
	 * wird für die Unterklassen benötigt.
	 * 
	 * @param accessPO
	 */
	protected PhysicalAccessPO(PhysicalAccessPO accessPO2) {
		super(accessPO2);
    }
    
    public PhysicalAccessPO(AccessPO schemaTransformationAccessPO){
    	super(schemaTransformationAccessPO);
    }

	protected void readInit() {
		this.notifyPOEvent(readInitEvent);
	};

	protected void readDone() {
		this.notifyPOEvent(readDoneEvent);
	};
	
 
//	protected SDFConstantList getNextLocalInputValueSet() {
//		return ((SchemaTransformationAccessPO)getAccessPO()).getNextLocalInputValues();
//	}
//	
//	protected SDFCompareOperatorList getCurrentLocalCompareOperatorList(){
//		return ((SchemaTransformationAccessPO)getAccessPO()).getCurrentLocalCompareOperatorList();
//	}
//	
//	protected RelationalTuple transformToGlobalOutput(RelationalTuple tuple){
//		return ((SchemaTransformationAccessPO)getAccessPO()).transformToGlobal(tuple);
//	}

	public T getAccessPO(){
		return (T) getAlgebraPO();
	}

}