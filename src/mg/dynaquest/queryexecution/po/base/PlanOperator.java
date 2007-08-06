package mg.dynaquest.queryexecution.po.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.4 $
 Log: $Log: PlanOperator.java,v $
 Log: Revision 1.4  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.3  2004/06/22 14:07:09  grawund
 Log: Open läuft nun auch für jeden Eingabeport in einem eigenen Thread.
 Log: Ein wird kein Zustand mehr zurückgeliefert, da bei Fehlern immer eine Exception
 Log: geworfen wird
 Log:
 Log: Revision 1.2  2002/01/31 16:14:17  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.HashMap;

import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;

import org.w3c.dom.Node;

public interface PlanOperator extends SimplePlanOperator, SupportsCloneMe {



    public abstract void setInputPO(int pos, PlanOperator input);

    public abstract PlanOperator getInputPO(int pos);  

    //public abstract void replaceInput(PlanOperator oldInput, PlanOperator newInput);
      
    public int getNumberOfInputs();
	//public String getInternalPOName();  
    
    public String initBaseValues(Node node);
    public boolean initInputs(HashMap idsToRefs);

}