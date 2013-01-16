package de.uniol.inf.is.odysseus.generator.telephone;


public class TelephoneCall extends Thread{
	
	final private CallDescriptionRecord cdr;
	final private long sleep;
	final private ICallDecriptionRecordReceiver receiver;
	
	
	public TelephoneCall(CallDescriptionRecord cdr, long sleep, ICallDecriptionRecordReceiver receiver){
		this.cdr = cdr;
		this.sleep = sleep;
		this.receiver = receiver;
	}
	
	@Override
	public void run() {
		cdr.start = System.currentTimeMillis();
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
		}
		cdr.end = System.currentTimeMillis();
		receiver.newCDR(cdr);
	}
}
