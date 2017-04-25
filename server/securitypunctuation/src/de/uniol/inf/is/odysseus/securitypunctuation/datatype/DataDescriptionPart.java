package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

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
	public boolean  match(IStreamObject<?> object){
		Tuple<?> temp=(Tuple<?>)object;
		for(int i=0;i<temp.size();i++){
			if(temp.getAttribute(i)==null||temp.getAttribute(i).equals(tupleRange)||attributes.contains(temp.getAttribute(i))||streams.contains(temp.getAttribute(i))){
				return true;
			}
		}return false;
		
	}


}
