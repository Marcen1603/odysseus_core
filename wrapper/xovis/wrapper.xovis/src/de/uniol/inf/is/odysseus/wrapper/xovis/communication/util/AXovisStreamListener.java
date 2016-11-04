package de.uniol.inf.is.odysseus.wrapper.xovis.communication.util;

import java.net.DatagramSocket;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.CodedInputStream;
import com.google.protobuf.GeneratedMessageLite;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;

public abstract class AXovisStreamListener implements Runnable {

		protected final Logger LOG = LoggerFactory.getLogger(XovisEventStreamListener.class);

		protected Thread t;
		
		protected ArrayList<? extends GeneratedMessageLite> objectList;
		
		/* 
		 * The coded input stream 
		 */
		protected CodedInputStream inputStream;
		/* 
		 * The UDP-Socket - Only required when using XOVIS UDP connection
		 */
		protected DatagramSocket uDPClientSocket;
		
		@SuppressWarnings("rawtypes")
		protected ArrayList<KeyValueObject> kVPairs = new ArrayList<KeyValueObject>();
		
		public AXovisStreamListener(CodedInputStream input, ArrayList<? extends GeneratedMessageLite> objList) {
			this.inputStream = input;
			this.objectList = objList;
		}
		
		public AXovisStreamListener(DatagramSocket uDPSocket, ArrayList<? extends GeneratedMessageLite> objList){
			this.uDPClientSocket = uDPSocket;
			this.objectList = objList;
		}

		public void start() {
			if (t == null) {
				t = new Thread(this);
				t.setName("XovisStreamListener");
				t.start();
				LOG.debug(this.getClass().toString() + " started.");
			} else {
				LOG.debug("Trying to start XovisStreamListener - but it was already running");
			}
		}
		
		public abstract void parseStream();
		
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
			parseStream();
		}

		@SuppressWarnings("rawtypes")
		public ArrayList<KeyValueObject> getkVPairs() {
			return kVPairs;
		}
}
