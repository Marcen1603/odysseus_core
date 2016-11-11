package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.io.InputStream;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.GeneratedMessageLite;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

@SuppressWarnings("all")
public abstract class AXovisEventDataParser implements Runnable  {

	protected final Logger LOG = LoggerFactory.getLogger(XovisEventStreamListener.class);

	protected Thread t;
	
	public AXovisEventDataParser(InputStream input, ArrayList<KeyValueObject> kVList, ArrayList<? extends GeneratedMessageLite> objectList){
		
	}
	
	public void start() {
		if (t == null) {
			t = new Thread(this);
			t.setName("XovisEventDataParser");
			t.start();
			LOG.debug(this.getClass().toString() + " started.");
		} else {
			LOG.debug("Trying to start XovisStreamListener - but it was already running");
		}
		parse();
	}
	
	public abstract void parse();
	
	public void close() {
		
		if (t != null) {
			t.interrupt();
		}
		t = null;
		LOG.debug(this.getClass().toString() + " closed.");
	}

	public Thread getT() {
		return t;
	}

	public void setT(Thread t) {
		this.t = t;
	}

	@Override
	public void run() {

	}
}
