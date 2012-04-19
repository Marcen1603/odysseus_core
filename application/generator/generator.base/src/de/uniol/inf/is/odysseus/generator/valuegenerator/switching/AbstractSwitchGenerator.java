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
import de.uniol.inf.is.odysseus.generator.valuegenerator.AbstractValueGenerator;

/**
 * 
 * @author Dennis Geesen
 * Created at: 19.04.2012
 */
public abstract class AbstractSwitchGenerator extends AbstractValueGenerator {

	protected int upDuration;
	protected double down;
	protected double up;
	protected int downDuration;
	protected int firstSwitchAfter = 0;
	private boolean downActive = true;
	private boolean started = true;

	private int counter = 0;
	
	public AbstractSwitchGenerator(IErrorModel errorModel, double down, double up, int downDuration, int upDuration) {
		super(errorModel);
		this.down = down;
		this.up = up;
		this.downDuration = downDuration;
		this.upDuration = upDuration;
	}

	public AbstractSwitchGenerator(IErrorModel errorModel, double down, double up, int downDuration, int upDuration, int firstSwitchAfter) {
		super(errorModel);
		this.down = down;
		this.up = up;
		this.downDuration = downDuration;
		this.upDuration = upDuration;
		this.firstSwitchAfter = firstSwitchAfter;
		if(this.firstSwitchAfter == 0){
			started = true;
		}
	}
	
	public abstract void switchedUp();
	public abstract void switchedDown();

	@Override
	public double generateValue() {
		if (started) {
			counter++;	
				if (downActive) {
					if (counter == downDuration) {
						counter = 0;
						downActive = false;
						switchedUp();
					}					
					return down;
				} else {
					if (counter == upDuration) {
						counter = 0;
						downActive = true;
						switchedDown();
					}					
					return up;
				}						
		} else {
			counter++;
			if(counter > firstSwitchAfter){
				started = true;
				downActive = false;
				counter = 1;
				return up;
			}else{
				return down;
			}
		}
	}

	@Override
	public void initGenerator() {
		this.counter = 0;
		if(this.firstSwitchAfter==0){
			this.started = true;
		}else{
			this.started = false;
		}
		this.downActive = true;

	}

}
