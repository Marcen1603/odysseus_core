package de.uniol.inf.is.odysseus.generator.securitypunctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.SADataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class SecurityPunctuationProvider extends StreamClientHandler {
	
	private Long counterTS = Long.valueOf(0);
	private boolean spActivated;
	private String spType;
	private Integer delay;
	private String name;
	
	public SecurityPunctuationProvider(Boolean spActivated, String spType, Integer delay, String name) {
		super();
		this.spActivated = spActivated;
		this.spType = spType;
		this.delay = delay;
		this.name = name;
	}
	
	@Override
	public void init() {
		super.setIsSA(true);
	}

	@Override
	public void close() {
	}

	@Override
	public List<DataTuple> next() {
		List<DataTuple> list = new ArrayList<DataTuple>();
		if(spActivated) {
			if(Math.random() > 0.2) {
				list.add(generateDataTuple());
			} else {
				if(spType.equals("attribute")) {
					list.add(generateAttributeSP());
				} else {
					list.add(generatePredicateSP());
				}
			}
		} else {
			list.add(generateDataTuple());
		}
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("next Tuple from " + name + ": " + list.get(0).getAttributes());
		return list;
	}

	@Override
	public StreamClientHandler clone() {
		return new SecurityPunctuationProvider(spActivated, spType, delay, name);
	}
	
	private SADataTuple generateDataTuple() {
		SADataTuple tuple = new SADataTuple(false);
		//Hier schon TS einbauen!!!
		tuple.addAttribute(new Long(counterTS++));
//		tuple.addAttribute(new Integer(55));
		tuple.addAttribute(new Integer((int) Math.round((Math.random() * 100))));
		tuple.addAttribute("beispiel text");
		tuple.addAttribute(new Integer(76));
		return tuple;
	}

	private SADataTuple generateAttributeSP() {
		SADataTuple tuple = new SADataTuple(true);
		// Security Punctuation Flag
//		tuple.addAttribute("SecurityPunctuation");
		// DDP - Stream (mehrere Werte mit Komma getrennt) ("" --> Beschränkung / * --> keine Beschränkung)
		tuple.addAttribute("Stream, Test, Test2");
		// DDP - Starttupel (-1 bedeutet keine Beschränkung)
//		tuple.addAttribute(new Long(counterTS));
		tuple.addAttribute(new Long(-1));
		// DDP - Endtupel (-1 bedeutet keine Beschränkung)
//		tuple.addAttribute(new Long(Math.round((Math.random() * 200) + counterTS)));
		tuple.addAttribute(new Long(-1));
		// DDP - Attribute (mehrere Werte mit Komma getrennt) ("" --> Beschränkung / * --> keine Beschränkung)
		tuple.addAttribute("Attribut1, Attribut2");
		// SRP - Rollen (mehrere Werte mit Komma getrennt) ("" --> Beschränkung / * --> keine Beschränkung)
		tuple.addAttribute("sys_admin");
		// Sign
		tuple.addAttribute(new Integer(1));
		// Immutable
		tuple.addAttribute(new Integer(1));
		// ts
		tuple.addAttribute(new Long(counterTS));
		return tuple;
	}
	
	private SADataTuple generatePredicateSP() {
		SADataTuple tuple = new SADataTuple(true);
		tuple.setSPType("predicate");
		//Prädikat
//		tuple.addAttribute("(ts>1) && (has_sys_admin) && (streamname = Test)");
		tuple.addAttribute("(ts>1) && (has_sys_admin)");
		// Sign
		tuple.addAttribute(new Integer(1));
		// Immutable
		tuple.addAttribute(new Integer(1));
		// ts
		tuple.addAttribute(new Long(counterTS++));
		return tuple;
	}
	
	public void setSPActivated(boolean spActivated) {
		this.spActivated = spActivated;
	}
	
}
