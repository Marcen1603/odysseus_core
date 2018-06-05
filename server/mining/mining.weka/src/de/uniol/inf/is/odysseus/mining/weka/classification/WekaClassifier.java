package de.uniol.inf.is.odysseus.mining.weka.classification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.Utils;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;
import de.uniol.inf.is.odysseus.mining.weka.mapping.WekaAttributeResolver;
import de.uniol.inf.is.odysseus.mining.weka.mapping.WekaConverter;

public class WekaClassifier<M extends ITimeInterval> implements IClassifier<M> {

	private Classifier classifier;
	private WekaAttributeResolver war;
	private Map<SDFAttribute, List<String>> nominals;

	public WekaClassifier(Map<SDFAttribute, List<String>> nominals, Classifier classifier) {
		this.nominals = nominals;		
		this.classifier = classifier;
	}

	@Override
	public Object classify(Tuple<M> tuple) {		
		Instance t = WekaConverter.convertToNominalInstance(tuple, war);
		// last one will be the class-index
		t.dataset().setClassIndex(t.dataset().numAttributes() - 1);
		try {
			double classValue = classifier.classifyInstance(t);
			if(!Utils.isMissingValue(classValue)){
				return classValue;
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void init(SDFSchema schema, SDFAttribute classAttribute) {
		ArrayList<SDFAttribute> outattributes = new ArrayList<SDFAttribute>(schema.getAttributes());
		// we add the class attribute (this will be the last one...)
		outattributes.add(classAttribute);
		SDFSchema outschema = SDFSchemaFactory.createNewWithAttributes(outattributes, schema);
		this.war = new WekaAttributeResolver(outschema, nominals);

	}
	
	@Override
	public String toString() {	
		return "WekaClassifier: "+classifier.toString();
	}


}
