package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.io.InputStream;
import java.util.ArrayList;

import com.google.protobuf.GeneratedMessageLite;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

@SuppressWarnings("all")
public class GPBStreamListener {

	// The camera input stream 
	private InputStream inputStream;
	
	// The list holding the incoming objects to be parsed
	private ArrayList<? extends GeneratedMessageLite> objectList;
	
	
	@SuppressWarnings("rawtypes")
	private ArrayList<KeyValueObject> kVPairs;
	
	@SuppressWarnings("rawtypes")
	public GPBStreamListener(InputStream input,  ArrayList<KeyValueObject> kVList) {
		this.setInputStream(input);
		this.setkVPairs(kVList);
	}
	
	public void getData(){
		
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public ArrayList<KeyValueObject> getkVPairs() {
		return kVPairs;
	}

	public void setkVPairs(ArrayList<KeyValueObject> kVPairs) {
		this.kVPairs = kVPairs;
	}
}
