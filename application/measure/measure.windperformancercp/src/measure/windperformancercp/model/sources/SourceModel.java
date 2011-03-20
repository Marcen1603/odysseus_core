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
package measure.windperformancercp.model.sources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import measure.windperformancercp.event.EventHandler;
import measure.windperformancercp.event.ModelEvent;
import measure.windperformancercp.event.ModelEventType;
import measure.windperformancercp.model.IDialogResult;
import measure.windperformancercp.model.IModel;
import measure.windperformancercp.model.sources.xml.XMLHashMappingHelper;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "sourceModel")
public class SourceModel extends EventHandler implements IModel {
	
	private static SourceModel instance = new SourceModel();
	
	@XmlJavaTypeAdapter(XMLHashMappingHelper.class)
	private HashMap<String,ISource> sourcesList;
	
	@XmlTransient
	private ModelEvent newItemEvent = new ModelEvent(this,ModelEventType.NewItem,null);
	@XmlTransient
	private ModelEvent deleteItemEvent = new ModelEvent(this,ModelEventType.DeletedItem,null);
	@XmlTransient
	private ModelEvent changeItemEvent = new ModelEvent(this,ModelEventType.ModifyItem,null);
	
	private SourceModel(){
		//sourcesList = new ArrayList<ISource>();
		sourcesList = new HashMap<String,ISource>();
	}
	
	//private SourceModel(ArrayList<ISource> list){
	private SourceModel(HashMap<String,ISource> list){
		sourcesList = list;//new ArrayList<ISource>(list);
	}
	
	public static SourceModel getInstance(){
		return instance;
	}
	
	public void addElement(ISource src){	
		//sourcesList.add(src);
		sourcesList.put(src.getName(), src);
		fire(newItemEvent);
	}
	
	//public void addAll(ArrayList<ISource> list){
	public void addAll(Map<String,ISource> map){
		//sourcesList.addAll(list);
		sourcesList.putAll(map);
		fire(newItemEvent);
	}
	
//	public void removeElement(int index){
	//sourcesList.remove(index);
	public void removeElement(String key){
		sourcesList.remove(key);
		fire(deleteItemEvent);
	}
	
	/*public int removeAllOccurences(ISource src){
		int c = 0;
		while(sourcesList.contains(src)){
			sourcesList.remove(src);
			c++;
		}
		fire(deleteItemEvent);
		return c;
	}*/
	
	public int removeAllOccurences(String key){
		int c = 0;
		while(sourcesList.containsKey(key)){
			sourcesList.remove(key);
			c++;
		}
		fire(deleteItemEvent);
		return c;
	}
	
	/*
	public ArrayList<ISource> getElementsByName(String n){
		ArrayList<ISource> result = new ArrayList<ISource>(); 
		for(ISource s: sourcesList){
			if(s.getName().equals(n))
				result.add(s);
		}
		return result;
	}*/
	
	public ISource getElementByName(String key){
		return sourcesList.get(key);
	}
	
	
	
/*	public ISource getIthElement(int i){
		if(sourcesList.size()>i){
			return sourcesList.get(i);
		}
		else return null;
	}
*/	
	
	public ArrayList<ISource> getSourcesListB(){
	//public HashMap<String,ISource> getSourcesList(){
		ArrayList<ISource> srcList = new ArrayList<ISource>(sourcesList.values()); 
		return srcList;
	}
	
	public ArrayList<String> getSourcesKeyList(){
		ArrayList<String> keyList = new ArrayList<String>(sourcesList.keySet());
		return keyList;
	}
	
	//public void setSourcesList(ArrayList<ISource> sources){
	public void setSourcesList(HashMap<String,ISource> sources){
		sourcesList = sources;
		fire(changeItemEvent);
	}
	
	//@XmlElementWrapper(name = "sourcesList")
	//@XmlElementRefs( 
	//	{ 
	//	    @XmlElementRef( type = MetMast.class, name = "metMast"), 
	//	    @XmlElementRef( type = WindTurbine.class, name = "windTurbine" ), 
	//	} )
	
//	 @XmlElementRefs( 
//		{ 
//		    @XmlElementRef( type = MetMast.class, name = "metMast"), 
//		    @XmlElementRef( type = WindTurbine.class, name = "windTurbine" ), 
//		} )
//	@XmlJavaTypeAdapter(XMLHashMappingHelper.class)
	public HashMap<String,ISource> getSourcesList(){
		return sourcesList;
	}
	
	public int getElemCount(){
		return sourcesList.size();
	}

	@Override
	public void somethingChanged(IDialogResult res) {
		fire(changeItemEvent);
	}

}
