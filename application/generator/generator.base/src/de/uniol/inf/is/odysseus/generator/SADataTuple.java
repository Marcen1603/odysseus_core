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
package de.uniol.inf.is.odysseus.generator;

/**
 * A lightweight adaption of Tuple from Odysseus with no direct dependencies!
 * 
 * @author JSchwarz
 * 
 */
public class SADataTuple extends DataTuple {

	/**
	 * Gibt an, ob es sich um ein normales Datentupel oder eine Security Punctuation handelt.
	 */
	protected Boolean isSP = false;
	private String spType = "attribute";
	protected Boolean isSA = true;	

	public SADataTuple(Boolean isSP) {
		super();
		this.isSP = isSP;
	}

	@Override
	public int memSize(boolean calcNew) {
		super.memSize(calcNew);
		if(isSA) {
			memSize = memSize + 4;
		}
		return memSize;
	}
	
	public Boolean isSP() {
		return isSP;
	}

	public String getSPType() {
		return spType;
	}

	public void setSPType(String spType) {
		this.spType = spType;
	}
}
