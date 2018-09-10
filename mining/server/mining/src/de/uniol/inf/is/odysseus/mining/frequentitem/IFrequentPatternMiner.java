package de.uniol.inf.is.odysseus.mining.frequentitem;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface IFrequentPatternMiner<M extends ITimeInterval> {

		
		/**
		 * Initializes the classification learning algorithm
		 * @param inputschema The schema of the tuples (including the classAttribute)
		 */
		public void init(SDFSchema inputschema, int support);
		
		/**
		 * Creates a classifier by using given tuples
		 * 
		 * @param tuples The training set 	
		 * @return A classifier
		 */
		public List<Pattern<M>> createFrequentSets(List<List<Tuple<M>>> tuples, M metadata);
		
		/** 
		 * This allows to set some options that can be used by the implementing class
		 * @param options
		 */
		public void setOptions(Map<String, String> options);
		
		/**
		 * A system wide unique name of the classifier
		 * @return the unique name
		 */
		public String getName();
		
		/**
		 * A list of possible algorithms used by this learner
		 * @return a list of names
		 */
		public List<String> getAlgorithmNames();
}
