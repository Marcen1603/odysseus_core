package de.uniol.inf.is.odysseus.incubation.graph.physicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.incubation.graph.logicaloperator.SearchNodeAO;

public class SearchNodePO<M extends ITimeInterval> extends AbstractPipe<KeyValueObject<M>, KeyValueObject<M>> {

	private SearchNodeAO searchNodeAo;
	
	public SearchNodePO (SearchNodeAO searchNodeAo) {
		super();
		this.searchNodeAo = searchNodeAo;
	}
	
	public SearchNodePO (SearchNodePO<M> searchNode) {
		super(searchNode);
		this.searchNodeAo = searchNode.searchNodeAo;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}

	@Override
	protected void process_next(KeyValueObject<M> object, int port) {		
		Map<String, Map<String, String>> props = new HashMap<String, Map<String, String>>();
		
		boolean labelEqual = false;
		boolean propsEqual = false;
		
		String labelSearch = searchNodeAo.getNodeLabel();
		List<NamedExpression> tmp = searchNodeAo.getNodeProperties();
		Map<String, String> propsSearch = new HashMap<String, String>();
		
		KeyValueObject<M> kvObject = new KeyValueObject<M>();
		
		for (NamedExpression exp : tmp) {
			//Warum geht das mit den "" nicht anders???
			propsSearch.put(exp.name, exp.expression.getExpressionString().replace("\"", ""));
		}
		
		for (Map.Entry<String, Map<String, String>> entry : props.entrySet()) {
			labelEqual = false;
			propsEqual = false;
			for (Map.Entry<String, String> propsEntry : entry.getValue().entrySet()) {
				if (propsEntry.getKey().equals("label") && propsEntry.getValue().equals(labelSearch)) {
					labelEqual = true;
				} else {
					if (propsEntry.getValue().equals(propsSearch.get(propsEntry.getKey()))) {
						propsEqual = true;
					} else {
						propsEqual = false;
						break;
					}
				}
			}
			
			if (labelEqual && propsEqual) {
				kvObject.addAttributeValue("node", entry.getKey());
				transfer(kvObject);
			}
		}
		
		
		//Gucken, welcher Knoten den übergebenen Werten entspricht!!
	}

}
