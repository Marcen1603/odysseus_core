package de.uniol.inf.is.odysseus.relational.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.3 $
 Log: $Log: RelationalTupleComparator.java,v $
 Log: Revision 1.3  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.2  2002/01/31 16:14:37  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.Comparator;
@SuppressWarnings("unchecked")
public class RelationalTupleComparator implements Comparator {

	public int compare(Object p0, Object p1) {
		return ((RelationalTuple<?>) p0).compareTo((RelationalTuple<?>) p1);
	}
}