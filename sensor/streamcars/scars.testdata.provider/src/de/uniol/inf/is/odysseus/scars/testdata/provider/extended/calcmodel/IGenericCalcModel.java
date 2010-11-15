package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel;

import java.util.Map;

public interface IGenericCalcModel {

	public void calculateAll();
	
	public void init(Map<String, Object> params);
	
	public void setDelay(int delay);
	
}
