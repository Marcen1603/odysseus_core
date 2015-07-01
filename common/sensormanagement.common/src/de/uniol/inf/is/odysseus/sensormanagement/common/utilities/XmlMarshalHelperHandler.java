package de.uniol.inf.is.odysseus.sensormanagement.common.utilities;

import java.io.File;

public interface XmlMarshalHelperHandler {

	void onUnmarshalling(File xmlFile);
	void onUnmarshalling(String xmlString);
}
