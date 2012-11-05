package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.layer;

import java.io.Serializable;
import java.util.ArrayList;

public class LayerConfiguration implements Serializable{

    private static final long serialVersionUID = -3897708132891607334L;
    
	private int type = 0;
	private String name = null;
	private String query = null;
	private ArrayList<String> attribute = null;
	private String url = null;
	
	private PersestentStyle style = null;
	
	public LayerConfiguration(String layerName) {
		this.name = layerName;
    }
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
	    return type;
    }

	public void setType(int type) {
	    this.type = type;
    }

	public String getUrl() {
	    return url;
    }

	public void setUrl(String url) {
	    this.url = url;
    }

	public String getQuery() {
	    return query;
    }

	public void setQuery(String query) {
	    this.query = query;
    }

	public ArrayList<String> getAttribute() {
		return attribute;
	}

	public void setAttribute(ArrayList<String> attribute) {
		this.attribute = attribute;
	}

	public PersestentStyle getStyle() {
	    return style;
    }

	public void setStyle(PersestentStyle style) {
	    this.style = style;
    }

	
}
