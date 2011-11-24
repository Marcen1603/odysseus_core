package de.uniol.inf.is.odysseus.p2p;

import java.util.Date;

public interface IBid extends Comparable<IBid>{

	int getBidValue();
	
	Date getDate();

	String getPeerId();

}
