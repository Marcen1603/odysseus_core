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
package measure.windperformancercp.model.sources.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import measure.windperformancercp.model.sources.AbstractSource;
import measure.windperformancercp.model.sources.ISource;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "source")
@XmlSeeAlso(AbstractSource.class)
public class MyHashMapEntryType {

	@XmlAttribute
    private String key; 
	
	@XmlTransient
    private ISource value;	//TODO: kann man das auch generisch machen?

	
	public MyHashMapEntryType(String k, ISource src){
		this.key = k;
		this.value = src;
	}

	public MyHashMapEntryType(){
		this.key = null;
		this.value = null;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setKey(String k){
		this.key = k;
	}
	
	@XmlJavaTypeAdapter(AbstractSource.Adapter.class)
	@XmlElement
	public ISource getValue(){
		return value;
	}
	
	public void setValue(ISource s){
		this.value = s;
	}
	
}
