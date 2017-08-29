package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;

public interface ITrainableModel {
	public void train(String languageCode, File file, String charSet, OptionMap trainOptions);
	public void store(File file) throws FileNotFoundException, IOException;
}
