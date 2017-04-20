package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class DataDescriptionPart {
	String tupleRange;
	List<String> attributes;
	List<String> streams;

	DataDescriptionPart() {

	}

	public DataDescriptionPart(String streams, String tupleRange, String attributes) {
		this.streams= Arrays.asList(streams.split(","));
		Collections.sort(this.streams);
		this.tupleRange = tupleRange;
		this.attributes= Arrays.asList(attributes.split(","));
		Collections.sort(this.attributes);
	}

	public DataDescriptionPart(DataDescriptionPart ddp) {
		this.streams=ddp.getStreams();
		this.tupleRange=ddp.getTupleRange();
		this.attributes=ddp.getAttributes();
	}

	public List<String> getStreams() {
		return this.streams;
	}

	public List<String> getAttributes() {
		return this.attributes;
	}

	public String getTupleRange() {
		return this.tupleRange;
	}
	//muss für verschiedene Arten von SPs angepasst werden->in AbstractDDP übernehmen
	public boolean match(Tuple<?> t){
		for(int i=0;i<t.size();i++){
			if(t.getAttribute(i)==null||t.getAttribute(i).equals(tupleRange)||attributes.contains(t.getAttribute(i))||streams.contains(t.getAttribute(i))){
				return true;
			}
		}return false;
	}
}
