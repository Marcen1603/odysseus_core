package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProtocolMonitor {
	
	private static ProtocolMonitor instance = null;
	private ArrayList<IProtocolHandler<?>> listener = new ArrayList<>();
	private long startedAt = 0;
	private Map<IProtocolHandler<?>, Long> ticks = new HashMap<IProtocolHandler<?>, Long>();
	
	private ProtocolMonitor(){
		
	}
	
	public static synchronized ProtocolMonitor getInstance(){
		if(instance == null){
			instance = new ProtocolMonitor();
		}
		
		return instance;
	}
	
	
	public void addToMonitor(IProtocolHandler<?> p){
		this.listener.add(p);
		this.startedAt = System.currentTimeMillis();
	}
	
	public void removeFromMonitor(IProtocolHandler<?> p){
		this.listener.remove(p);
		if(this.listener.size()==0){
			finish();
		}
	}

	private void finish() {
		double totalTime = System.currentTimeMillis() - startedAt;
		
		double totalCount = 0;
		for(Entry<IProtocolHandler<?>, Long> tick : this.ticks.entrySet()){
			totalCount = totalCount + tick.getValue();
		}
		System.out.println("-------------- TOTAL THROUGHPUT ------------- ");
		System.out.println("TOTAL LINES: "+totalCount+" Tupel");
		System.out.println("TOTAL TIME : "+totalTime+" ms");
		System.out.println("THROUGHPUT : "+(totalCount/totalTime)*1000+ " Tupel pro s");
		
		
		ticks.clear();
	}

	public void informMonitor(IProtocolHandler<?> p, long ticks){
		this.ticks.put(p, ticks);
	}
	
}
