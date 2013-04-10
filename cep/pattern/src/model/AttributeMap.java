package model;


public class AttributeMap {
	private int port;
	private int attrPos;
	
	public AttributeMap(int port, int attrPos) {
		this.setPort(port);
		this.setAttrPos(attrPos);				
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getAttrPos() {
		return attrPos;
	}

	public void setAttrPos(int attrPos) {
		this.attrPos = attrPos;
	}
}
