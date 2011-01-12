package windperformancercp.model.sources;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import windperformancercp.event.EventHandler;
import windperformancercp.event.SourceModelEvent;
import windperformancercp.event.SourceModelEventType;


public class SourceModel extends EventHandler{
	
	private static SourceModel instance = new SourceModel();
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
	
	public ISource getIthElement(int i){
		if(sourcesList.size()>i){
			return sourcesList.get(i);
		}
		else return null;
	}
	
	public ArrayList<ISource> getSourcesList(){
		return sourcesList;
	}
	
	public int getElemCount(){
		return sourcesList.size();
	}

}
