/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.objecttracking.metadata;

import java.util.ArrayList;
import java.util.List;

public class Probability implements IProbability{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1722880482644626903L;
	private double[][] cov;
	private ArrayList<int[]> attributePaths;
	private int[] attrIndices;
	
	private List<String> indices;
	
	public Probability(){
		cov = null;
		indices = new ArrayList<String>();
	}
	
	public Probability(double[][] sigma){
		if(sigma != null && sigma.length > 0){
			cov = new double[sigma.length][sigma[0].length];
			for(int i =0; i<sigma.length; i++){
				for(int u = 0; u<sigma[i].length; u++){
					cov[u][i] = sigma[u][i];
				}
			}
		}
	}
	
	public Probability(double[][] sigma, List<String> mapping) {
		if(sigma != null && sigma.length > 0){
			cov = new double[sigma.length][sigma[0].length];
			for(int i =0; i<sigma.length; i++){
				for(int u = 0; u<sigma[i].length; u++){
					cov[u][i] = sigma[u][i];
				}
			}
		}
		this.indices = new ArrayList<String>(mapping);
	}
	
	@Override
	public double[][] getCovariance(){
		return cov;
	}
	
	@Override
	public void setCovariance(double[][] sigma){
		this.cov = sigma;
	}
	
	@Override
	public IProbability clone(){
		return new Probability(this.cov, indices);
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
	
	@Override
	public String csvToString() {
		return null;
	}
	
	@Override
	public String getCSVHeader() {
		return null;
	}

	@Override
	public int getCovarianceIndex(String fullAttributeName) {
		return indices.indexOf(fullAttributeName);
	}

	@Override
	public String getAttributeName(int index) {
		return indices.get(index);
	}

	@Override
	public void setAttributeMapping(List<String> indices) {
		this.indices = indices;
		
	}

	@Override
	public List<String> getAttributMapping() {
		return this.indices;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.ICSVToString#csvToString(boolean)
	 */
	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}
}
