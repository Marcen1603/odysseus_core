package de.uniol.inf.is.odysseus.wrapper.zeromq.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;

public class ZMQContextProvider implements Runnable {

private final Logger LOG = LoggerFactory.getLogger(ZMQPushPublisher.class);
	
	private Thread t;
	
	private int numOfThreads = 1;
	
	private Context context = null;
	
	public ZMQContextProvider(int threads){
		numOfThreads = threads;
	}
	
	@Override
	public void run() {
		context = ZMQ.context(numOfThreads);
		LOG.debug("ZeroMQ context created using " + numOfThreads + " threads.");
	}
	
	public void start(){
		if(t==null){
			t = new Thread(this);
			t.start();
		}
	}
	
	public void close(){
		context.term();
		context.close();
		t.interrupt();
		t = null;
		LOG.debug("ZeroMQ context closed.");
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

}
