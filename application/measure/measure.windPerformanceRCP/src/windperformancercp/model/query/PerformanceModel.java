package windperformancercp.model.query;

import java.util.ArrayList;

import windperformancercp.event.EventHandler;

public class PerformanceModel extends EventHandler{
	private static PerformanceModel instance = new PerformanceModel();
	private ArrayList<IPerformanceQuery> queryList;
	
	//PerformanceModelEvent newItemEvent = new PerformanceModelEvent(this,PerformanceModelType.NewItem,null);
	//PerformanceModelEvent deleteItemEvent = new PerformanceModelEvent(this,PerformanceModelEventType.DeletedItem,null);
	
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
		//fire(newItemEvent);
	}
	
	public void addAll(ArrayList<IPerformanceQuery> list){
		queryList.addAll(list);
		//fire(newItemEvent);
	}
	
	public void removeElement(int index){
		queryList.remove(index);
		//fire(deleteItemEvent);
	}
	
	public int removeAllOccurences(IPerformanceQuery src){
		int c = 0;
		while(queryList.contains(src)){
			queryList.remove(src);
			c++;
		}
		//fire(deleteItemEvent);
		return c;
	}
	
	public IPerformanceQuery getIthElement(int i){
		if(queryList.size()>i){
			return queryList.get(i);
		}
		else return null;
	}
	
	public ArrayList<IPerformanceQuery> getQueryList(){
		return queryList;
	}
	
	public int getElemCount(){
		return queryList.size();
	}


}
