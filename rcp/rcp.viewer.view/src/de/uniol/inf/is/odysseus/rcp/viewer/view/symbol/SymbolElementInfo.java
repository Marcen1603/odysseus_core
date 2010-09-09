package de.uniol.inf.is.odysseus.rcp.viewer.view.symbol;

import java.util.Map;


public final class SymbolElementInfo {

	private String type;
	private Map<String,String> params;
	private int width;
	private int height;
	
	public SymbolElementInfo( String symbolType, Map<String,String> params, int width, int height ) {
		type = symbolType;
		this.params = params;
		this.width = width;
		this.height = height;
	}
	
	public String getType() {
		return type;
	}
	
	public Map<String,String> getParams() {
		return params;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
