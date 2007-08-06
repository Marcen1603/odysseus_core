package mg.dynaquest.queryexecution.po.convert;

import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;

/**
 * Diese Klasse dient als abstrakte Schnittstelle für alle
 * Transformationsplanoperatoren. Author: $Author: grawund $ Date: $Date:
 * 2002/02/06 09:03:00 $ Version: $Revision: 1.2 $ Log: $Log: ConvertPO.java,v $
 * 2002/02/06 09:03:00 $ Version: $Revision: 1.2 $ Log: Revision 1.2  2004/09/16 08:57:13  grawund
 * 2002/02/06 09:03:00 $ Version: $Revision: 1.2 $ Log: Quellcode durch Eclipse formatiert
 * 2002/02/06 09:03:00 $ Version: $Revision: 1.2 $ Log:
 * Log: Revision 1.1 2002/02/06 09:03:00 grawund Log: [no comments] Log:
 */

abstract public class ConvertPO extends UnaryPlanOperator {
	public ConvertPO(ConvertPO po){
		super(po);
	}

	public ConvertPO() {
		super();
	}
}