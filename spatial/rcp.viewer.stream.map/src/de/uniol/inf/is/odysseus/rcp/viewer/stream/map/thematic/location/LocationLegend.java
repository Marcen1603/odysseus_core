package de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.location;

import java.util.LinkedList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.style.PointStyle;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Operator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.map.thematic.misc.Predicate;

public class LocationLegend {
	LinkedList<LocationLegendEntry> legendList;
	private String attributeName;
	public LocationLegend(String attributeName) {
		legendList = new LinkedList<>();
		this.attributeName = attributeName;
		
		Predicate predicate = new Predicate(attributeName, Operator.SMALLERTHAN, 1);
		LocationStyle style = new LocationStyle(PointStyle.SHAPE.CIRCLE, 5, 1, new Color(Display.getCurrent(), 255,255,255), new Color(Display.getCurrent(), 0, 255, 0), 255);
		LocationLegendEntry entry = new LocationLegendEntry(predicate, style);
		legendList.add(entry);
		
		predicate = new Predicate(attributeName, Operator.SMALLERTHAN, 2);
		style = new LocationStyle(PointStyle.SHAPE.CIRCLE, 5, 1, new Color(Display.getCurrent(), 255,255,255), new Color(Display.getCurrent(), 255, 255, 0), 255);
		entry = new LocationLegendEntry(predicate, style);
		legendList.add(entry);
		
		predicate = new Predicate(attributeName, Operator.SMALLERTHAN, 3);
		style = new LocationStyle(PointStyle.SHAPE.CIRCLE, 5, 1, new Color(Display.getCurrent(), 255,255,255), new Color(Display.getCurrent(), 255, 0, 0), 255);
		entry = new LocationLegendEntry(predicate, style);
		legendList.add(entry);
	}
	public int getSize(){
		return legendList.size();
	}
	public LocationStyle getStyleForValue(Integer value){
		LocationStyle style = new LocationStyle(LocationStyle.DEFAULTLOCATIONSTYLE);
		for (LocationLegendEntry entry : legendList) {
			if(entry.getPredicate().isTrue(value)){
				style = entry.getStyle();
				break;
			}
		}
		return style;
	}
	public String getAttributeName() {
		return attributeName;
	}
}
