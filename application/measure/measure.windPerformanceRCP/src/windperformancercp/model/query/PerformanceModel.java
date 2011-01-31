package windperformancercp.model.query;

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
public class PerformanceModel extends EventHandler implements IModel{
	private static PerformanceModel instance = new PerformanceModel();
	
	@XmlTransient
	private ArrayList<IPerformanceQuery> queryList;
	
	SourceModelEvent newItemEvent = new SourceModelEvent(this,SourceModelEventType.NewItem,null);
	SourceModelEvent deleteItemEvent = new SourceModelEvent(this,SourceModelEventType.DeletedItem,null);
	
	private PerformanceModel(){
		queryList = new ArrayList<IPerformanceQuery>();
	}
	
	private PerformanceModel(ArrayList<IPerformanceQuery> list){
		queryList = new ArrayList<IPerformanceQuery>(list);
	}
	
	public static PerformanceModel getInstance(){
		return instance;
	}
	
	public void addElement(IPerformanceQuery src){
		queryList.add(src);
		fire(newItemEvent);
	}
	
	public void addAll(ArrayList<IPerformanceQuery> list){
		queryList.addAll(list);
		fire(newItemEvent);
	}
	
	public void removeElement(int index){
		queryList.remove(index);
		fire(deleteItemEvent);
	}
	
	public int removeAllOccurences(IPerformanceQuery src){
		int c = 0;
		while(queryList.contains(src)){
			queryList.remove(src);
			c++;
		}
		fire(deleteItemEvent);
		return c;
	}
	
	public IPerformanceQuery getIthElement(int i){
		if(queryList.size()>i){
			return queryList.get(i);
		}
		else return null;
	}
	
	@XmlElementWrapper(name = "queryList")
	@XmlElementRefs( 
		{ 
		    @XmlElementRef( type = MeasureIEC.class), 
		    @XmlElementRef( type = MeasureLangevin.class), 
		} )
	public ArrayList<IPerformanceQuery> getQueryList(){
		return queryList;
	}
	
	public void setQueryList(ArrayList<IPerformanceQuery> ql){
		queryList = ql;
	}
	
	public int getElemCount(){
		return queryList.size();
	}


}
