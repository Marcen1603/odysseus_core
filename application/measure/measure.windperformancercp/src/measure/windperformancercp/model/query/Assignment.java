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
package measure.windperformancercp.model.query;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;

import measure.windperformancercp.model.sources.Attribute;
import measure.windperformancercp.model.sources.ISource;
import measure.windperformancercp.model.sources.MetMast;
import measure.windperformancercp.model.sources.WindTurbine;


public class Assignment {
	
		
		//String kind; //TODO

		Attribute.AttributeType attType;
		ISource respSource;
		Stream respStream;
		int attributeId;
		
		public Assignment(Attribute.AttributeType dt, ISource rs, int ind){
		
			this.attType = dt;
			this.respStream = null;
			this.respSource = rs;
			this.attributeId = ind;
			
		}
		
		public Assignment(Attribute.AttributeType at, Stream rs, int ind){
			
			this.attType = at;
			this.respStream = rs;
			this.respSource = null;
			this.attributeId = ind;
		}

		
		public Assignment(Attribute.AttributeType at){
			this();
			this.attType = at;
		}
		
		public Assignment(){
			this.attType = null;
			this.respSource = null;
			this.attributeId = -1;
		}
		
		
		
		public boolean isValid(){
			if(respSource != null){
				if((attributeId>=0)&&(respSource.getNumberOfAtts()>attributeId)){
					if(attType.equals(respSource.getAttributeList().get(attributeId))){
						return true;
					}
				}
			}
			return false;
		}
		
		
		
		public Attribute.AttributeType getAttType(){
			return attType;
		}
		
		public void setType(Attribute.AttributeType at){
			this.attType = at;
		}
		
		public ISource getRespSource(){
			return respSource;
		}
		
		@XmlElementRefs( 
				{ 
				    @XmlElementRef( type = MetMast.class, name = "metMast"), 
				    @XmlElementRef( type = WindTurbine.class, name = "windTurbine" ), 
				} )
		public void setRespSource(ISource src){
			this.respSource = src;
		}
		
		public Stream getRespStream(){
			return respStream;
		}
		
		public void setRespStream(Stream str){
			this.respStream = str;
		}
		
		public int getAttributeId(){
			return attributeId;
		}
		
		public String toString(){
			String info = "";
			
			if(this.respSource !=null)
				info = info + this.attType.toString()+" "+this.respSource.getName()+" "+this.respSource.getIthAtt(attributeId).getName();
			if(this.respStream !=null)
				info = info + this.attType.toString()+" "+this.respStream.getName()+" "+this.respStream.getIthAttName(this.attributeId);
			return info;
		}

	}
