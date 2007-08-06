package mg.dynaquest.queryexecution.po.base;

import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.event.POException;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:12 $ Version:
 * $Revision: 1.2 $ Log: $Log: WritesErrorElements.java,v $
 * $Revision: 1.2 $ Log: Revision 1.2  2004/09/16 08:57:12  grawund
 * $Revision: 1.2 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.2 $ Log: Log: Revision 1.1
 * 2003/08/20 13:45:13 grawund Log: no message Log:
 */

public interface WritesErrorElements {
	public Object nextErrorElement() throws POException, TimeoutException;
}