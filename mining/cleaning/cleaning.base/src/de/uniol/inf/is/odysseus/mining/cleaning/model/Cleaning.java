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

package de.uniol.inf.is.odysseus.mining.cleaning.model;

import de.uniol.inf.is.odysseus.mining.cleaning.detection.IDetection;

/**
 * 
 * @author Dennis Geesen
 * Created at: 21.06.2011
 */
public class Cleaning {
	
	private IDetection detection;
	private ICorrection correction;
	
	private String streamName;
	private String attribute;
	
	public Cleaning(){
		
	}
	
	public void setCorrection(ICorrection correction) {
		this.correction = correction;
	}
	public ICorrection getCorrection() {
		return correction;
	}
	public void setDetection(IDetection detection) {
		this.detection = detection;
	}
	public IDetection getDetection() {
		return detection;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getStreamName() {
		return streamName;
	}

}
