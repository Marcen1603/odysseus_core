package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.carModel;

import de.uniol.inf.is.odysseus.scars.testdata.provider.extended.calcmodel.IGenericCalcModel;

public interface ICarModel {
	
	/**
	 * returns the underlying calculation model instance
	 * @return
	 */
	public IGenericCalcModel getCalcModel();
	
	/**
	 * returns the unique car id
	 * @return
	 */
	public int getId();
	
	/**
	 * sets the unique car id
	 * @param id
	 */
	public void setId(int id);
}
