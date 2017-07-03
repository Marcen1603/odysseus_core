package de.uniol.inf.is.odysseus.generator.securitypunctuations;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.AbstractDataGenerator;
import de.uniol.inf.is.odysseus.generator.DataTuple;

public class SPProvider extends AbstractDataGenerator {
	private long timestamp;
	// zählt die Anzahl der versendeten Datentupel
	private int counter = 0;
	private long delay;
	private String streamingAttribute;
	boolean secondStream;

	public SPProvider(String streamingAttribute, long delay, boolean secondStream) {
		super();
		this.delay = delay;
		this.streamingAttribute = streamingAttribute;
		this.secondStream = secondStream;
	}

	/*
	 * @Override public List<DataTuple> next() throws InterruptedException {
	 * timestamp= System.currentTimeMillis(); List<DataTuple> list=new
	 * ArrayList<DataTuple>(); DataTuple tuple = new DataTuple(); if
	 * ((Math.random() * SPProbability)+1 > SPProbability) { for(int
	 * i=(int)SPAmount;i>0;i--){ tuple = generateSPs(timestamp);
	 * list.add(tuple);
	 * 
	 * } } else { tuple.addAttribute(new String("s" + (Math.round((Math.random()
	 * * streamAmount) + 1))));
	 * 
	 * tuple.addAttribute(new Long(counter++)); tuple.addAttribute(new
	 * String("Heartbeat")); tuple.addAttribute(new
	 * Long((long)Math.random()*100+50)); tuple.addAttribute(new
	 * Long(timestamp)); list.add(tuple);
	 * 
	 * } Thread.sleep(delay); return list;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * private DataTuple generateSPs(long timestamp) { DataTuple tuple = new
	 * DataTuple(); DataDescriptionPart ddp; SecurityRestrictionPart srp; String
	 * streams = "*"; String tupleRange = "*"; String attributes = "*"; String
	 * roles = "*"; boolean sign; boolean immutable;
	 * 
	 * int signRandomizer = (int) Math.random() * 2; int immutableRandomizer =
	 * (int) Math.random() * 2; int streamRandomizer = (int) Math.random() * 9;
	 * int tupleRandomizer = (int) Math.random() * 3; int attributeRandomizer =
	 * (int) Math.random() * 4; int roleRandomizer = (int) Math.random() * 4; if
	 * (signRandomizer == 1) { sign = true; } else sign = false; if
	 * (immutableRandomizer == 1) { immutable = true; } else immutable = false;
	 * 
	 * if (streamRandomizer == 1) { streams = "s1,s2,s4"; } else if
	 * (streamRandomizer == 2) { streams = "s1,s3"; } else if (streamRandomizer
	 * == 3) { streams = "s2,s4"; } else if (streamRandomizer == 4) { streams =
	 * "s2"; } else if (streamRandomizer == 5) { streams = "s1,s3,s4"; } else if
	 * (streamRandomizer == 6) { streams = "s2,s3"; } else if (streamRandomizer
	 * == 7) { streams = "s4"; } // 1/3 Chance eine Begrenzung für die SP zu
	 * bekommen if (tupleRandomizer == 0) { tupleRange =
	 * counter+10+","+counter+20; } if (attributeRandomizer == 1) { attributes =
	 * "Heartbeat"; } else if (attributeRandomizer == 2) { attributes =
	 * "BreathingRate,Temperature"; } else if (attributeRandomizer == 3) {
	 * attributes = "BreathingRate,Heartbeat"; }
	 * 
	 * if (roleRandomizer == 1) { roles = "D,ND"; } else if (roleRandomizer ==
	 * 2) { roles = "D,C"; } else if (roleRandomizer == 3) { roles = "ND"; } ddp
	 * = new DataDescriptionPart(streams, tupleRange, attributes); srp = new
	 * SecurityRestrictionPart(roles); tuple.addAttribute(new
	 * SecurityPunctuation(ddp, srp, sign, immutable, timestamp)); return tuple;
	 * }
	 */
	@Override
	protected void process_init() {
		// TODO Auto-generated method stub

	}

	@Override
	public SPProvider newCleanInstance() {
		return new SPProvider(streamingAttribute, delay, secondStream);
	}

	@Override
	public void close() {

	}

	@Override
	public List<DataTuple> next() throws InterruptedException {
		if(secondStream){
		List<DataTuple> list = new ArrayList<DataTuple>();
		int streamingValue = ((int) Math.random() * 20);
		timestamp = System.currentTimeMillis();
		DataTuple tuple = new DataTuple();
		tuple.addAttribute(counter);
		tuple.addAttribute(streamingAttribute);
		tuple.addAttribute(streamingValue);
		tuple.addAttribute(timestamp);
		tuple.addAttribute(timestamp + 10000);
		counter++;
		list.add(tuple);
		Thread.sleep(delay);
		return list;
		}else{
			List<DataTuple> list = new ArrayList<DataTuple>();
			int streamingValue = ((int) Math.random() * 20);
			timestamp = System.currentTimeMillis();
			DataTuple tuple = new DataTuple();

			tuple.addAttribute(counter);
			tuple.addAttribute(streamingAttribute);
			tuple.addAttribute(streamingValue);
			tuple.addAttribute(timestamp);
			tuple.addAttribute(timestamp + 10000);
			counter++;
			list.add(tuple);
			Thread.sleep(delay);
			return list;
		}
	}

}
