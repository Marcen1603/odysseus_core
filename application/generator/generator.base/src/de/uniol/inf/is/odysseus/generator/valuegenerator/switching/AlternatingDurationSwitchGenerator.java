/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.generator.valuegenerator.switching;

import de.uniol.inf.is.odysseus.generator.error.IErrorModel;

/**
 * 
 * @author Dennis Geesen
 * Created at: 19.04.2012
 */
public class AlternatingDurationSwitchGenerator extends AbstractSwitchGenerator {

	private int upMaxDuration;
	private int downMaxDuration;
	
	private boolean upIncreasing = true;
	private boolean downIncreasing = true;
	
	private int downMinDuration;
	private int upMinDuration;

	public AlternatingDurationSwitchGenerator(IErrorModel errorModel, double down, double up, int downMinDuration, int downMaxDuration, int upMinDuration, int upMaxDuration) {
		super(errorModel, down, up, downMinDuration, upMinDuration);	
		this.downMinDuration = downMinDuration;
		this.downMaxDuration = downMaxDuration;
		this.upMinDuration = upMinDuration;
		this.upMaxDuration = upMaxDuration;
	}

	public AlternatingDurationSwitchGenerator(IErrorModel errorModel, double down, double up, int downMinDuration, int downMaxDuration, int upMinDuration, int upMaxDuration, int firstSwitchAfter) {
		super(errorModel, down, up, downMinDuration, upMinDuration, firstSwitchAfter);
		this.downMinDuration = downMinDuration;
		this.downMaxDuration = downMaxDuration;
		this.upMinDuration = upMinDuration;
		this.upMaxDuration = upMaxDuration;	
	}

	@Override
	public void switchedUp() {		
		if(downIncreasing){
			if(super.downDuration<downMaxDuration){
				super.downDuration++;
			}else{
				downIncreasing = false;
			}
		}else{
			if(super.downDuration>downMinDuration){
				super.downDuration--;
			}else{
				downIncreasing = true;
			}			
		}			
	}

	@Override
	public void switchedDown() {
		if(upIncreasing){
			if(super.upDuration<upMaxDuration){
				super.upDuration++;
			}else{
				upIncreasing = false;
			}
		}else{
			if(super.upDuration>upMinDuration){
				super.upDuration--;				
			}else{
				upIncreasing = true;
			}
		}
		
	}


	

	

}
