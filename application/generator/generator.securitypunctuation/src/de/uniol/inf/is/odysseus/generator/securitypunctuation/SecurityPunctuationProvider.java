package de.uniol.inf.is.odysseus.generator.securitypunctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.SADataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class SecurityPunctuationProvider extends StreamClientHandler {
	
	Integer i = 0;
	Long counterTS = Long.valueOf(0);
	
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
		if(Math.random() > 0.2) {
			list.add(generateDataTuple());
		} else {
			list.add(generateSecurityPunctuation());
		}

		i++;
		
		if(i > 1) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("next " + list.get(0).getAttributes());
		return list;
	}

	@Override
	public StreamClientHandler clone() {
		return new SecurityPunctuationProvider();
	}

	private SADataTuple generateDataTuple() {
		SADataTuple tuple = new SADataTuple(false);
		//Hier schon TS einbauen!!!
		tuple.addAttribute(new Long(counterTS++));
		tuple.addAttribute(new Integer(55));
		tuple.addAttribute("beispiel text");
		tuple.addAttribute(new Integer(76));
		return tuple;
	}

	private SADataTuple generateSecurityPunctuation() {
		SADataTuple tuple = new SADataTuple(true);
		// Security Punctuation Flag
//		tuple.addAttribute("SecurityPunctuation");
		// DDP - Stream (mehrere Werte mit Komma getrennt)
		tuple.addAttribute("Stream, Test");
		// DDP - Starttupel (-1 bedeutet keine Beschränkung)
		tuple.addAttribute(new Integer(counterTS.intValue()));
		// DDP - Endtupel (-1 bedeutet keine Beschränkung)
		tuple.addAttribute(new Integer((int) Math.round((Math.random() * 25) + counterTS)));
		// DDP - Attribute (mehrere Werte mit Komma getrennt)
		tuple.addAttribute("Attribut1, Attribut2");
		// SRP - Rollen (mehrere Werte mit Komma getrennt)
		tuple.addAttribute("sys_admin, PUBLIC");
		// Sign
		tuple.addAttribute(new Integer(1));
		// ts
		tuple.addAttribute(new Long(counterTS++));
		return tuple;
	}
	
}
