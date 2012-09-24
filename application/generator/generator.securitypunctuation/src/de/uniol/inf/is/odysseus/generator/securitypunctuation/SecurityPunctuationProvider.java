/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
	private Integer counter = 0;
	private boolean benchmark;
	private Float spProbability;
	
	/**
	 * Neuer SecurityPunctuationProvider, der zufällige Datentupel und SP erzeugt.
	 * 
	 * @param spActivated 		true: Stream beinhaltet SP; false: Stream beinhaltet keine SP
	 * @param spType			Type der SP, die erzeugt werden (z.B. attribute)
	 * @param delay				Verzögerung zwischen dem Senden zweier Tupel
	 * @param name				Name des Stream
	 * @param benchmark			true: Senden wird nach bestimmter Anzahl von Tupel automatsch angehalten
	 * @param spProbability		Wahrscheinlichkeit mit der ein SP statt eines Datentupel gesendet wird
	 */
	public SecurityPunctuationProvider(Boolean spActivated, String spType, Integer delay, String name, boolean benchmark, Float spProbability) {
		super();
		this.spActivated = spActivated;
		this.spType = spType;
		this.delay = delay;
		this.name = name;
		this.benchmark = benchmark;
		this.spProbability = spProbability;
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
		if(!benchmark || counter++ < 50000) {
			List<DataTuple> list = new ArrayList<DataTuple>();
			if(spActivated) {
				if(Math.random() > spProbability) {
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
		return null;
	}

	@Override
	public StreamClientHandler clone() {
		return new SecurityPunctuationProvider(spActivated, spType, delay, name, benchmark, spProbability);
	}
	
	private SADataTuple generateDataTuple() {
		SADataTuple tuple = new SADataTuple(false);
		//Hier schon TS einbauen!!!
		tuple.addAttribute(new Long(counterTS++));
		tuple.addAttribute(new Long(counterTS + 9));
//		tuple.addAttribute(new Long(counterTS * (Math.round(Math.random() + 1)) + 1));
		tuple.addAttribute("beispiel text");
//		tuple.addAttribute(new Integer(76));
		tuple.addAttribute(new Integer((int) Math.round((Math.random() * 100))));
		return tuple;
	}

	private SADataTuple generateAttributeSP() {
		SADataTuple tuple = new SADataTuple(true);
		double random = Math.random();
		if(random > 0.9) {
			// DDP - Stream (mehrere Werte mit Komma getrennt) ("" --> Beschränkung / * --> keine Beschränkung)
//			tuple.addAttribute("stream1, stream2, stream3, stream4, stream5");
			tuple.addAttribute("*");
			// DDP - Starttupel (-1 bedeutet keine Beschränkung)
	//		tuple.addAttribute(new Long(counterTS));
			tuple.addAttribute(new Long(-1));
			// DDP - Endtupel (-1 bedeutet keine Beschränkung)
	//		tuple.addAttribute(new Long(Math.round((Math.random() * 200) + counterTS)));
			tuple.addAttribute(new Long(-1));
			// DDP - Attribute (mehrere Werte mit Komma getrennt) ("" --> Beschränkung / * --> keine Beschränkung)
			tuple.addAttribute("*");
			// SRP - Rollen (mehrere Werte mit Komma getrennt) ("" --> Beschränkung / * --> keine Beschränkung)
			tuple.addAttribute("*");
			// Sign
			tuple.addAttribute(new Integer(1));
			// Immutable
			tuple.addAttribute(new Integer(1));
			// ts
			tuple.addAttribute(new Long(counterTS));
		} else if(random < 0.8) {
			tuple.addAttribute("stream1, stream2, stream3, stream4, stream5");
			tuple.addAttribute(new Long(counterTS));
			tuple.addAttribute(new Long(Math.round((Math.random() * 200) + counterTS)));
			tuple.addAttribute("Attribut1,Attribut2,Attribut12,Attribut22,ts,long");
			tuple.addAttribute("sys_admin");
			tuple.addAttribute(new Integer(1));
			tuple.addAttribute(new Integer(1));
			tuple.addAttribute(new Long(counterTS));
		} else {
			tuple.addAttribute("");
			tuple.addAttribute(new Long(counterTS));
			tuple.addAttribute(new Long(Math.round((Math.random() * 200) + counterTS)));
			tuple.addAttribute("");
			tuple.addAttribute("");
			tuple.addAttribute(new Integer(1));
			tuple.addAttribute(new Integer(1));
			tuple.addAttribute(new Long(counterTS));
		}
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
