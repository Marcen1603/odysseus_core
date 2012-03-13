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

/**
 * The default implementation of IPredictionFunctionKey<T>
 * @author Andre Bolles
 *
 * @param <T>
 */
public class PredictionFunctionKey<T> implements IPredictionFunctionKey<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1485873166469185595L;
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

	@Override
	public PredictionFunctionKey<T> clone(){
		return new PredictionFunctionKey<T>(this);
	}
	
	@Override
	public String toString(){
		if(this.key != null) {
			return this.key.toString();
		}
        return "PredFctKey: empty";
	}
	
	@Override
	public String csvToString() {
		return toString();
	}
	
	@Override
	public String getCSVHeader() {
		return "PredFctKey";
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.ICSVToString#csvToString(boolean)
	 */
	@Override
	public String csvToString(boolean withMetada) {
		return this.csvToString();
	}
}
