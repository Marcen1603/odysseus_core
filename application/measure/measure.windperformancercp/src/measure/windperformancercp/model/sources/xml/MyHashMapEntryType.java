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
