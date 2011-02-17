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

import java.util.ArrayList;

public class Stream {
	private String name;
	
	private ArrayList<String> attributeNames;
	
	public Stream(String name, ArrayList<String> attribute){
		this.name = name;
		this.attributeNames = new ArrayList<String>(attribute);
	}
	
	public Stream(){
		this.name = "";
		this.attributeNames = new ArrayList<String>();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		this.name = n;
	}
	
	/*
	public ArrayList<Attribute> getAttributes(){
		return attributes;
	}
	*/
	
	public ArrayList<String> getAttributeNames(){
		return attributeNames;
	}
	
	public void setAttributeNames(ArrayList<String> names){
		this.attributeNames = names;
	}
	
	public String getIthAttName(int i){
		return attributeNames.get(i).toString();
	}
	
	public String getIthFullAttName(int i){
		return this.getName()+"."+attributeNames.get(i).toString();
	}
	
	public String toString(){
		return this.name+" "+this.attributeNames.toString();
	}

}
