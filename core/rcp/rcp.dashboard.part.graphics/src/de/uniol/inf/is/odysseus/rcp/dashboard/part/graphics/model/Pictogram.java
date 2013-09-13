package de.uniol.inf.is.odysseus.rcp.dashboard.part.graphics.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;

import org.eclipse.draw2d.geometry.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.uniol.inf.is.odysseus.core.collection.Tuple;


public abstract class Pictogram extends Observable{
	
	
	private Rectangle constraint;	
	private boolean visibile = true;
	

	public Rectangle getConstraint() {
		return constraint;
	}

	public void setConstraint(Rectangle constraint) {
		this.constraint = constraint;
		setChanged();
		notifyObservers();
	}

	protected abstract void init(Map<String, String> values);
	protected abstract void save(Map<String, String> values);
	protected abstract void process(Tuple<?> tuple);
	protected void internalProcess(Tuple<?> tuple){
		process(tuple);
		setChanged();
		notifyObservers();
	}
	
	public void getXML(Node parent, Document builder){		
		Map<String, String> values = new HashMap<String, String>();
		
		save(values);
		
		for(Entry<String, String> value : values.entrySet()){
			Element element = builder.createElement(value.getKey());
			element.setTextContent(value.getValue());
			parent.appendChild(element);
		}
		
		Element constElement = builder.createElement("position_dimensions");
		constElement.setAttribute("x", Integer.toString(constraint.x));
		constElement.setAttribute("y", Integer.toString(constraint.y));
		constElement.setAttribute("width", Integer.toString(constraint.width));
		constElement.setAttribute("height", Integer.toString(constraint.height));
		parent.appendChild(constElement);				
	}
	
	public void initFromXML(Node parent){
		Map<String, String> values = new HashMap<String, String>();
		NodeList list = parent.getChildNodes();
		for(int i=0;i<list.getLength();i++){
			list.item(i);
			Element elem = (Element) list.item(i);
			if(list.item(i).getNodeName().equals("position_dimensions")){						
				int x = Integer.parseInt(elem.getAttribute("x"));
				int y = Integer.parseInt(elem.getAttribute("y"));
				int width = Integer.parseInt(elem.getAttribute("width"));
				int height = Integer.parseInt(elem.getAttribute("height"));
				Rectangle rect = new Rectangle(x, y, width, height);
				setConstraint(rect);
			}else{
				String key = elem.getNodeName();
				String value = elem.getTextContent();
				values.put(key, value);
			}
		}
		init(values);
		
		
		
	}

	public boolean isVisibile() {
		return visibile;
	}

	public void setVisibile(boolean visibile) {
		this.visibile = visibile;
	}
	

}
