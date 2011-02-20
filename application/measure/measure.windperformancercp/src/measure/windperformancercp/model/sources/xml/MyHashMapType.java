package measure.windperformancercp.model.sources.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "SourceMap")
public class MyHashMapType {
	
    private List<MyHashMapEntryType> list;
	
	public MyHashMapType(){
		list = new ArrayList<MyHashMapEntryType>();
	}
	
	public void setList(ArrayList<MyHashMapEntryType> nlist){
		list = nlist;
	}
	
	public List<MyHashMapEntryType> getList(){
		return list;
	}

}
