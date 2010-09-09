package de.uniol.inf.is.odysseus.iec61970.library.serializable;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.iec61970.library.daf_service.IResourceID;


/**
 * Repräsentation eines HSDA Objektes. Dieser enthält alle wichtigen Daten eines Item Elements aus dem HSDA Datenmodell
 * @author Mart Köhler
 *
 */
public class HSDAObject implements Serializable, IClone{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2765862423263445607L;
	/**
	 * 
	 */
	private IResourceID id;
	private String timestamp;
	private Object value;
	private String quality;
	public HSDAObject(IResourceID identifier, Object value, String quality, String timestamp) {
		this.id = identifier;
		this.value = value;
		this.quality = quality;
		this.timestamp = timestamp;
	}
	public IResourceID getId() {
		return id;
	}
	public void setId(IResourceID id) {
		this.id = id;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getQuality() {
		return quality;
	}
	public void setQuality(String quality) {
		this.quality = quality;
	}
	@Override
	public IClone clone() {
		return new HSDAObject(id,value,quality,timestamp);
		
	}
	
}
