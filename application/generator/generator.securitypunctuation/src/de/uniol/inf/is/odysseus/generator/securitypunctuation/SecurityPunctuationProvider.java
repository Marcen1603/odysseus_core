package de.uniol.inf.is.odysseus.generator.securitypunctuation;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.generator.DataTuple;
import de.uniol.inf.is.odysseus.generator.StreamClientHandler;

public class SecurityPunctuationProvider extends StreamClientHandler {

	Integer i = 0;
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DataTuple> next() {
		System.out.println("next");
		List<DataTuple> list = new ArrayList<DataTuple>();
		if(Math.random() > 0.5) {
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
		return list;
	}

	@Override
	public StreamClientHandler clone() {
		return new SecurityPunctuationProvider();
	}

	private DataTuple generateDataTuple() {
		DataTuple tuple = new DataTuple();
		tuple.addAttribute(new Integer(55));
		tuple.addAttribute("beispiel text");
		tuple.addAttribute(new Integer(76));
		return tuple;
	}

	private DataTuple generateSecurityPunctuation() {
		DataTuple tuple = new DataTuple(true);
		// Security Punctuation Flag
		tuple.addAttribute("SecurityPunctuation");
		// DDP - Stream
		tuple.addAttribute("Stream");
		// DDP - Starttupel
		tuple.addAttribute(new Integer(76));
		// DDP - Endtupel
		tuple.addAttribute(new Integer(79));
		// DDP - Attribute
		tuple.addAttribute("Name");
		// SRP - Rollen
		tuple.addAttribute("Rolle1");
		// Sign
		tuple.addAttribute(new Integer(1));
		// ts
		tuple.addAttribute(new Integer(i));
		return tuple;
	}
	
}
