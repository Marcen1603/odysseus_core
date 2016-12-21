package de.uniol.inf.is.odysseus.datarate;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IDatarate extends IMetaAttribute {

	void setDatarate(String key, double datarate );
	void setDatarates(Map<String, Double> datarates);

	double getDatarate(String key);
	Map<String, Double> getDatarates();


}
