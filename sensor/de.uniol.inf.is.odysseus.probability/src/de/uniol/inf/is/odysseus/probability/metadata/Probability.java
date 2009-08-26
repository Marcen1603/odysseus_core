package de.uniol.inf.is.odysseus.probability.metadata;

public class Probability implements IProbability{

	private double[][] cov;
	
	public Probability(){
		cov = null;
	}
	
	public Probability(double[][] sigma){
		cov = new double[sigma.length][sigma[0].length];
		for(int i =0; i<sigma.length; i++){
			for(int u = 0; u<sigma[i].length; u++){
				cov[u][i] = sigma[u][i];
			}
		}
	}
	
	public double[][] getCovariance(){
		return cov;
	}
	
	public void setCovariance(double[][] sigma){
		this.cov = sigma;
	}
	
	public IProbability clone(){
		return new Probability(this.cov);
	}
	
	public String toString(){
		if(this.cov == null || this.cov.length == 0){
			return "Cov: empty";
		}
		
		String bf = "[";
		for(int i = 0; i<this.cov.length; i++){
			bf += "[";
			for(int u = 0; u<this.cov[i].length; u++){
				bf += this.cov[i][u] + " ";
			}
			bf += "];";
		}
		
		return bf;
	}
}
