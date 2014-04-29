package de.uniol.inf.is.odysseus.mining;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.classification.IClassifier;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;
import de.uniol.inf.is.odysseus.mining.frequentitem.IFrequentPatternMiner;

public class MiningAlgorithmRegistry<T extends ITimeInterval> {
	
	public static MiningAlgorithmRegistry<? extends ITimeInterval> instance = null;
	
	private List<IClassificationLearner<T>> classificationLearners = new ArrayList<>();
	private List<IClassifier<T>> classifiers = new ArrayList<>();
	private List<IClusterer<T>> clusterers = new ArrayList<>();
	private List<IFrequentPatternMiner<T>> frequentpatternminer = new ArrayList<>();
	
	private MiningAlgorithmRegistry(){
		
	}
	
	public static synchronized <T extends ITimeInterval> MiningAlgorithmRegistry<T> getInstance(){
		if(instance==null){
			instance = new MiningAlgorithmRegistry<T>();			
		}		
		@SuppressWarnings("unchecked")
		MiningAlgorithmRegistry<T> current = (MiningAlgorithmRegistry<T>) instance;
		return current;
	}
	
	
	public void addClassificationLearner(IClassificationLearner<T> learner){
		this.classificationLearners.add(learner);
	}
	
	public void addClassifier(IClassifier<T> classifier){
		this.classifiers.add(classifier);
	}
	
	public void addClusterer(IClusterer<T> clusterer){
		this.clusterers.add(clusterer);
	}
	
	public void addFrequentPatternMiner(IFrequentPatternMiner<T> fpm){
		this.frequentpatternminer.add(fpm);
	}
	
	public void removeClassificationLearner(IClassificationLearner<T> learner){		
		this.classificationLearners.remove(learner);
	}
	
	public void removeClassifier(IClassifier<T> classifier){
		this.classifiers.remove(classifier);
	}
	
	public void removeClusterer(IClusterer<T> clusterer){
		this.clusterers.remove(clusterer);
	}
	
	public void removeFrequentPatternMiner(IFrequentPatternMiner<T> fpm){
		this.frequentpatternminer.remove(fpm);
	}
	
	public IClassificationLearner<T> getClassificationLearner(String name){		
		for(IClassificationLearner<T> learner : this.classificationLearners){
			if(learner.getName().equalsIgnoreCase(name)){
				return learner;
			}
		}
		return null;
	}
	
	public IClassifier<T> getClassifier(String name){		
		for(IClassifier<T> classifier : this.classifiers){
			if(classifier.getName().equalsIgnoreCase(name)){
				return classifier;
			}
		}
		return null;
	}
	
	public IClusterer<T> getClusterer(String name){		
		for(IClusterer<T> clusterer : this.clusterers){
			if(clusterer.getName().equalsIgnoreCase(name)){
				return clusterer;
			}
		}
		return null;
	}

	public IFrequentPatternMiner<T> getFrequentPatternMiner(String name) {
		for(IFrequentPatternMiner<T> fpm : this.frequentpatternminer){
			if(fpm.getName().equalsIgnoreCase(name)){
				return fpm;
			}
		}
		return null;
	}

}
