package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.Region;

public class ParameterPart {
	
	public enum EntryType {
		STRING, NUMBER, LIST
	}

	private List<ParameterPart> childs = new ArrayList<>();
	private EntryType type = EntryType.NUMBER;
	private String value = "";	
	private ParameterPart parent;
	private Region region;

	public ParameterPart(ParameterPart parent, EntryType type, int start) {
		this.parent = parent;
		this.type = type;
		if (parent != null) {
			this.parent.childs.add(this);
		}
		this.region = new Region(start, 0);
	}

	public ParameterPart(EntryType type, int start) {
		this.parent = null;		
		this.type = type;
		this.region = new Region(start, 0);
	}

	public ParameterPart getParent() {
		return parent;
	}

	public void setParent(ParameterPart parent) {
		this.parent = parent;
	}

	public List<ParameterPart> getChilds() {
		return childs;
	}

	public void setChilds(List<ParameterPart> childs) {
		this.childs = childs;
	}

	public EntryType getType() {
		return type;
	}

	public void setType(EntryType type) {
		this.type = type;
	}

	public boolean isList() {
		return type.equals(EntryType.LIST);
	}
	
	public boolean isString(){
		return type.equals(EntryType.STRING);
	}
	
	public boolean isNumber(){
		return type.equals(EntryType.NUMBER);
	}
	
	@Override
	public String toString() {
		return getValue()+" (Type: "+this.type.name()+", offset: "+this.region.getOffset()+", length: "+this.region.getLength();		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void appendValue(String val){
		this.value = this.value+val;
	}
	
	public void appendChar(char c){
		this.value = this.value+c;
	}

	public Region getRegion() {
		return region;
	}
	
	public void setEnd(int end){
		this.region = new Region(this.region.getOffset(), end-this.region.getOffset());
	}
	
	public Region getRegion(int offset){
		return new Region(this.region.getOffset()+offset, this.region.getLength());
	}
	
}