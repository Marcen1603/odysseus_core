package de.uniol.inf.is.odysseus.nlp.datastructure.annotations.model;

import java.io.File;

public interface ITrainableModel {
	public void train(String languageCode, File file, String charSet);
}
