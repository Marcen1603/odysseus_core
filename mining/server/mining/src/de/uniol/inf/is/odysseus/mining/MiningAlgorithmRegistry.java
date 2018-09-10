package de.uniol.inf.is.odysseus.mining;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.mining.classification.IClassificationLearner;
import de.uniol.inf.is.odysseus.mining.clustering.IClusterer;
import de.uniol.inf.is.odysseus.mining.frequentitem.IFrequentPatternMiner;

public class MiningAlgorithmRegistry<T extends ITimeInterval> {
	
	public static MiningAlgorithmRegistry<? extends ITimeInterval> instance = null;
	
	private List<IClassificationLearner<T>> classificationLearners = new ArrayList<>();	
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
	
	
	public void addClusterer(IClusterer<T> clusterer){
		this.clusterers.add(clusterer);
	}
	
	public void addFrequentPatternMiner(IFrequentPatternMiner<T> fpm){
		this.frequentpatternminer.add(fpm);
	}
	
	public void removeClassificationLearner(IClassificationLearner<T> learner){		
		this.classificationLearners.remove(learner);
	}
		
	public void removeClusterer(IClusterer<T> clusterer){
		this.clusterers.remove(clusterer);
	}
	
	public void removeFrequentPatternMiner(IFrequentPatternMiner<T> fpm){
		this.frequentpatternminer.remove(fpm);
	}
	
	public IClassificationLearner<T> createClassificationLearner(String name){		
		for(IClassificationLearner<T> learner : this.classificationLearners){
			if(learner.getName().equalsIgnoreCase(name)){
				return learner.createInstance();
			}
		}
		return null;
	}
	
	public List<String> getClassificationLearnerNames() {
		List<String> list = new ArrayList<>();
		for(IClassificationLearner<T> learner : this.classificationLearners){
			list.add(learner.getName());
		}
		Collections.sort(list);
		return list;
	}

	public List<String> getClassificationLearnerAlgorithms() {
		List<String> list = new ArrayList<>();
		for(IClassificationLearner<T> learner : this.classificationLearners){
			list.addAll(learner.getAlgorithmNames());
		}
		Collections.sort(list);
		return list;
	}



	
	public IClusterer<T> createClusterer(String name){		
		for(IClusterer<T> clusterer : this.clusterers){
			if(clusterer.getName().equalsIgnoreCase(name)){
				return clusterer.createInstance();								
			}
		}
		return null;
	}
	
	public List<String> getClustererNames() {
		List<String> list = new ArrayList<>();
		for(IClusterer<T> learner : this.clusterers){
			list.add(learner.getName());
		}
		Collections.sort(list);
		return list;
	}

	public List<String> getClustererAlgorithms() {
		List<String> list = new ArrayList<>();
		for(IClusterer<T> learner : this.clusterers){
			list.addAll(learner.getAlgorithmNames());
		}
		Collections.sort(list);
		return list;
	}

	public IFrequentPatternMiner<T> getFrequentPatternMiner(String name) {
		for(IFrequentPatternMiner<T> fpm : this.frequentpatternminer){
			if(fpm.getName().equalsIgnoreCase(name)){
				return fpm;
			}
		}
		return null;
	}
	
	public List<String> getFrequentPatternMinerNames() {
		List<String> list = new ArrayList<>();
		for(IFrequentPatternMiner<T> learner : this.frequentpatternminer){
			list.add(learner.getName());
		}
		Collections.sort(list);
		return list;
	}

	public List<String> getFrequentPatternMinerAlgorithms() {
		List<String> list = new ArrayList<>();
		for(IFrequentPatternMiner<T> learner : this.frequentpatternminer){
			list.addAll(learner.getAlgorithmNames());
		}
		Collections.sort(list);
		return list;
	}

	

}
