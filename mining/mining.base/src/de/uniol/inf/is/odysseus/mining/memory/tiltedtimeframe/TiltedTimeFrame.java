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

package de.uniol.inf.is.odysseus.mining.memory.tiltedtimeframe;

import de.uniol.inf.is.odysseus.mining.memory.ISnapshot;
import de.uniol.inf.is.odysseus.mining.memory.ISnapshotMergeFunction;


/**
 * 
 * @author Dennis Geesen
 * Created at: 02.09.2011
 */
public class TiltedTimeFrame<S extends ISnapshot> {

	private TimeFrame<S> normalTimeFrame;
	private TimeFrame<S> bufferTimeFrame;

	public TiltedTimeFrame(TimeFrame<S> normalFrame){
		this.normalTimeFrame = normalFrame;
	}
	
	public TiltedTimeFrame(TimeFrame<S> normal, TimeFrame<S> buffer){
		this.setNormalTimeFrame(normal);
		this.setBufferTimeFrame(buffer);
	}

	public TimeFrame<S> getBufferTimeFrame() {
		return bufferTimeFrame;
	}

	public void setBufferTimeFrame(TimeFrame<S> bufferTimeFrame) {
		this.bufferTimeFrame = bufferTimeFrame;
	}

	public TimeFrame<S> getNormalTimeFrame() {
		return normalTimeFrame;
	}

	public void setNormalTimeFrame(TimeFrame<S> normalTimeFrame) {
		this.normalTimeFrame = normalTimeFrame;
	}
	
	public TimeFrame<S> insert(TimeFrame<S> toInsert, ISnapshotMergeFunction<S> mergeFunction){
		if(this.isNormalEmpty()){
			this.setNormalTimeFrame(toInsert);
			return null;
		}else{
			if(this.isBufferEmpty()){
				this.setBufferTimeFrame(this.normalTimeFrame);
				this.setNormalTimeFrame(toInsert);
				return null;
			}else{
				TimeFrame<S> merged = TimeFrameMergeFunction.merge(this.normalTimeFrame, this.bufferTimeFrame, mergeFunction);
				this.normalTimeFrame = toInsert;
				this.bufferTimeFrame = null;
				return merged;
			}
		}
	}
	
	
	public boolean isBufferEmpty(){
		return (this.bufferTimeFrame==null);
	}
	
	public boolean isNormalEmpty(){
		return (this.normalTimeFrame == null);
	}
	
	public boolean isEmpty(){
		return (isNormalEmpty() && isBufferEmpty());
	}
	
	@Override
	public String toString() {
		return "Normal: "+getNormalTimeFrame()+"\nBuffer: "+getBufferTimeFrame();
	}
}
