/** Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.mining.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.TimeInterval;

public class MiningMetadata extends TimeInterval implements IMiningMetadata {
	
	private static final long serialVersionUID = 2490005020065173048L;
	private List<String> corrected = new ArrayList<String>();
	private List<String> detected = new ArrayList<String>(); 

	public MiningMetadata(){		
	}

	public MiningMetadata(MiningMetadata mm) {		
		this.corrected = mm.corrected;
		this.detected =  mm.detected;			
	}
	

	@Override
	public String toString() {
		String dS = listToString(detected);
		String cS = listToString(corrected);
		return "detected: "+dS+" | corrected: "+cS;
	}
	
	
	private String listToString(List<String> list){
		String dS = "[";		
		String del = "";
		for(int i=0;i<list.size();i++){
			dS = dS+del+list.get(i);			
			del = ", ";
		}
		dS = dS+"]";
		return dS;
	}
	
	@Override
	public String csvToString() {
		return toString();
	}

	@Override
	public String getCSVHeader() {
		return "Mining";
	}

	@Override
	public boolean isCorrected() {
		return !this.corrected.isEmpty();
	}

		
	@Override
	public MiningMetadata clone(){	
		return new MiningMetadata(this);
	}

	@Override
	public String csvToString(boolean withMetadata){
		return this.csvToString();
	}

	@Override
	public boolean isDetected() {
		return !this.detected.isEmpty();
	}
	

	@Override
	public boolean isDetectedAttribute(String attribute) {
		return this.detected.contains(attribute);
	}

	@Override
	public void setDetectedAttribute(String attribute, boolean detected) {
		if(this.detected.contains(attribute)){
			if(!detected){
				this.detected.remove(attribute);
			}
		}else{
			if(detected){
				this.detected.add(attribute);
			}
		}		
	}


	@Override
	public boolean isCorrectedAttribute(String attribute) {
		return this.corrected.contains(attribute);
	}

	@Override
	public void setCorrectedAttribute(String attribute, boolean corrected) {
		if(this.corrected.contains(attribute)){
			if(!corrected){
				this.corrected.remove(attribute);
			}
		}else{
			if(corrected){
				this.corrected.add(attribute);
			}
		}	
	}

}
