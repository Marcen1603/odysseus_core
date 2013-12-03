package de.uniol.inf.is.odysseus.core.planmanagement;

import java.util.Comparator;

public class OperatorOwnerComparator implements Comparator<IOperatorOwner> {
	
	static private OperatorOwnerComparator instance = new OperatorOwnerComparator();
	
	public static OperatorOwnerComparator getInstance(){
		return instance;
	}
	
	@Override
	public int compare(IOperatorOwner arg0, IOperatorOwner arg1) {
		if (arg0.getID()<arg1.getID()){
			return -1;
		}
		if (arg0.getID()>arg1.getID()){
			return 1;
		}
		return 0;
	}

}
