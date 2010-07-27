package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.ArrayList;

public class Probability implements IProbability{

	private double[][] cov;
	private ArrayList<int[]> attributePaths;
	private int[] attrIndices;
	
	public Probability(){
		cov = null;
	}
	
	public Probability(double[][] sigma){
		if(sigma != null){
			cov = new double[sigma.length][sigma[0].length];
			for(int i =0; i<sigma.length; i++){
				for(int u = 0; u<sigma[i].length; u++){
					cov[u][i] = sigma[u][i];
				}
			}
		}
	}
	
	public double[][] getCovariance(){
		return cov;
	}
	
	public void setCovariance(double[][] sigma){
		this.cov = sigma;
	}
	
	@Override
	public IProbability clone(){
		return new Probability(this.cov);
	}
	
	@Override
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

	@Override
	public int[] getMVAttributeIndices() {
		return attrIndices;
	}

	@Override
	public void setMVAttributeIndices(int[] indices) {
		this.attrIndices = indices;
	}

	@Override
	public ArrayList<int[]> getAttributePaths() {
		return this.attributePaths;
	}

	@Override
	public void setAttributePaths(ArrayList<int[]> paths) {
		this.attributePaths = paths;
	}
	
	@Override
	public int getIndexOfKovMatrix(int[] path) {
		for (int i=0; i<this.attributePaths.size(); i++) {
			if (isEqual(this.attributePaths.get(i), path))
				return i;
		}
		return -1;
	}
	
	private boolean isEqual(int[] a, int[] b) {
		for (int i=0; i<a.length; i++) {
			if (a[i] != b[i])
				return false;
		}
		return true;
	}
}
