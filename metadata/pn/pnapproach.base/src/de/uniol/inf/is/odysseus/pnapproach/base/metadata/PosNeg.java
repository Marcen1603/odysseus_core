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
package de.uniol.inf.is.odysseus.pnapproach.base.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.PointInTime;

public class PosNeg implements IPosNeg{
	
	private static final long serialVersionUID = -2524903215947378386L;

	private ElementType type;
	private List<Long> id;
	private PointInTime timestamp;
	
	public PosNeg(){
		this.type = ElementType.POSITIVE;
		this.id = null;
		this.timestamp = new PointInTime();
//		try{
//			throw new Exception("PosNeg erzeugt");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	public PosNeg(ElementType type, List<Long> id, PointInTime timestamp){
		this.type = type;
		this.id = id;
		this.timestamp = timestamp;
//		try{
//			throw new Exception("PosNeg erzeugt");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	protected PosNeg(PosNeg original){
		this.id = new ArrayList<Long>(original.getID());
		this.timestamp = original.getTimestamp().clone();
		this.type = original.type;
//		try{
//			throw new Exception("PosNeg erzeugt");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
	}
	
	@Override
	public ElementType getElementType(){
		return this.type;
	}
	
	@Override
	public void setElementType(ElementType type){
		this.type = type;
	}
	
	@Override
	public List<Long> getID(){
		return this.id;
	}
	
	@Override
	public void setID(List<Long> id){
		this.id = id;
	}
	
	@Override
	public PointInTime getTimestamp(){
		return this.timestamp;
	}
	
	@Override
	public void setTimestamp(PointInTime timestamp){
		this.timestamp = timestamp;
	}
	
	public PointInTime getStart(){
		return this.getTimestamp();
	}
	
	public void setStart(PointInTime timestamp){
		this.setTimestamp(timestamp);
	}

	@Override
	public PosNeg clone(){
		return new PosNeg(this);
	}
	
	@Override
	public int compareTo(IPosNeg pn2){
		if(this.timestamp.before(pn2.getTimestamp())){
			return -1;
		}
		else if(this.timestamp.equals(pn2.getTimestamp())){
			return 0;
		}
		return 1;
	}
	
	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("ElementType: " + this.type + " ID: " + this.id + " Timestamp: " + this.timestamp);
		return buffer.toString();
	}
	
	@Override
	public String csvToString() {
		return this.type+";"+this.id+";"+this.timestamp;
	}
	
	@Override
	public String getCSVHeader() {
		return "Type;Id;Timestamp";
	}
}
