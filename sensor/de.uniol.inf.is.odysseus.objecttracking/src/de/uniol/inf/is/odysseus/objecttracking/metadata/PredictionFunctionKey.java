package de.uniol.inf.is.odysseus.objecttracking.metadata;

/**
 * The default implementation of IPredictionFunctionKey<T>
 * @author Andre Bolles
 *
 * @param <T>
 */
public class PredictionFunctionKey<T> implements IPredictionFunctionKey<T> {

	T key;

	public PredictionFunctionKey(){
	}
	
	public PredictionFunctionKey(PredictionFunctionKey<T> old){
		this.key = old.key;
	}
	
	@Override
	public T getPredictionFunctionKey() {
		return key;
	}

	@Override
	public void setPredictionFunctionKey(T key) {
		this.key = key;
	}

	public PredictionFunctionKey<T> clone(){
		return new PredictionFunctionKey<T>(this);
	}
	
	public String toString(){
		return this.key.toString();
	}
}
