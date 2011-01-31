package windperformancercp.model.sources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import windperformancercp.event.EventHandler;
import windperformancercp.event.SourceModelEvent;
import windperformancercp.event.SourceModelEventType;
import windperformancercp.model.IModel;

@XmlRootElement
public class SourceModel extends EventHandler implements IModel {
	
	private static SourceModel instance = new SourceModel();
	
	@XmlTransient
	private ArrayList<ISource> sourcesList;
	
	
	SourceModelEvent newItemEvent = new SourceModelEvent(this,SourceModelEventType.NewItem,null);
	SourceModelEvent deleteItemEvent = new SourceModelEvent(this,SourceModelEventType.DeletedItem,null);
	
	private SourceModel(){
		sourcesList = new ArrayList<ISource>();
	}
	
	private SourceModel(ArrayList<ISource> list){
		sourcesList = new ArrayList<ISource>(list);
	}
	
	public static SourceModel getInstance(){
		return instance;
	}
	
	public void addElement(ISource src){
		sourcesList.add(src);
		fire(newItemEvent);
	}
	
	public void addAll(ArrayList<ISource> list){
		sourcesList.addAll(list);
		fire(newItemEvent);
	}
	
	public void removeElement(int index){
		sourcesList.remove(index);
		fire(deleteItemEvent);
	}
	
	public int removeAllOccurences(ISource src){
		int c = 0;
		while(sourcesList.contains(src)){
			sourcesList.remove(src);
			c++;
		}
		fire(deleteItemEvent);
		return c;
	}
	
	public ArrayList<ISource> getElementsByName(String n){
		ArrayList<ISource> result = new ArrayList<ISource>(); 
		for(ISource s: sourcesList){
			if(s.getName().equals(n))
				result.add(s);
		}
		return result;
	}
	
	public ISource getIthElement(int i){
		if(sourcesList.size()>i){
			return sourcesList.get(i);
		}
		else return null;
	}
	
	@XmlElementWrapper(name = "sourcesList")
	@XmlElementRefs( 
		{ 
		    @XmlElementRef( type = MetMast.class, name = "metMast"), 
		    @XmlElementRef( type = WindTurbine.class, name = "windTurbine" ), 
		} )
	public ArrayList<ISource> getSourcesList(){
		return sourcesList;
	}
	
	public void setSourcesList(ArrayList<ISource> sources){
		sourcesList = sources;
	}
	
	public int getElemCount(){
		return sourcesList.size();
	}

}
