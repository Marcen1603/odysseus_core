package mg.dynaquest.queryexecution.factory;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:13 $ 
 Version: $Revision: 1.4 $
 Log: $Log: POFactory.java,v $
 Log: Revision 1.4  2004/09/16 08:57:13  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.3  2004/07/28 11:29:47  grawund
 Log: POManager erste Version integriert
 Log:
 Log: Revision 1.2  2002/01/31 16:13:54  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import mg.dynaquest.queryexecution.action.FilterAction;
import mg.dynaquest.queryexecution.po.base.NAryPlanOperator;
import mg.dynaquest.sourceselection.action.compensate.CompensateAction;

// Die Fabrik dient dazu, einen Planoperator anhand des Namens zu
// erzeugen
public class POFactory {

	// Der Tagname enthält den kompletten Package-Namen
	public static NAryPlanOperator createPO(String tagname) {
		NAryPlanOperator tpo = null;
		Class tpo_class = null;
		// neues Object aus dem Namen erzeugen
		// ToDo
		try {
			tpo_class = Class.forName(tagname);
		} catch (ClassNotFoundException e) {
			// hier muesste jetzt eine PO not found exception
			//  geworfen werden ... ToDo
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		try {
			tpo = (NAryPlanOperator) tpo_class.newInstance();
		} catch (Exception e) {

			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return tpo;
	}
	
	// Enthält eine Instanz einer CompensateAction
	public static CompensateAction createCompensateAction(String tagname) {
		CompensateAction action = null;
		Class ca_class = null;
		// neues Object aus dem Namen erzeugen
		// ToDo
		try 
		{
			ca_class = Class.forName(tagname);
		} 
		catch (ClassNotFoundException e) 
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		try 
		{
			action = (CompensateAction)ca_class.newInstance();
		} 
		catch (Exception e) 
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return action;
	}
	
	
	// Erzeugt eine Instanz der FilterAction
	public static FilterAction createFilterAction(String tagname) {
		FilterAction action = null;
		Class fa_class = null;
		// neues Object aus dem Namen erzeugen
		// ToDo
		try 
		{
			fa_class = Class.forName(tagname);
		} 
		catch (ClassNotFoundException e) 
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		try 
		{
			action = (FilterAction)fa_class.newInstance();
		} 
		catch (Exception e) 
		{
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		return action;
	}
	

}