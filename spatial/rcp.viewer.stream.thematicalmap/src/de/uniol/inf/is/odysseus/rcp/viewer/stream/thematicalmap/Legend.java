package de.uniol.inf.is.odysseus.rcp.viewer.stream.thematicalmap;

import java.util.LinkedList;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class Legend{
	LinkedList<LegendEntry> legendList;
	public Legend() {
		legendList = new LinkedList<>();
		
//		LegendEntry defaultLegendEntry = new LegendEntry();
//		Predicate predicate = new Predicate();
//		predicate.setOperator(new Operator(Operator.ELSE));
////		Style defaultStyle = new PolygonStyle(lineWidth, lineColor, fillColor);
//		int lineWidth = 1;
//		Color lineColor = new Color(Display.getCurrent(), 0,0,0);
//		Color fillColor = new Color(Display.getCurrent(), 100,149,237);
//		int transparency = 50;
//		ChoroplethStyle style = new ChoroplethStyle(lineWidth, lineColor, fillColor, transparency);
//		defaultLegendEntry.setPredicate(predicate);
//		defaultLegendEntry.setStyle(style);
//		legendList.add(defaultLegendEntry);
		
		
		
		
		Predicate pred = new Predicate("geometry", Operator.SMALLERTHAN, 10);
		ChoroplethStyle style = new ChoroplethStyle(1, new Color(Display.getCurrent(), 0, 0, 0), new Color(Display.getCurrent(), 237, 248, 251), 50);
		LegendEntry entry = new LegendEntry(pred,style);
		legendList.add(entry);
		
		pred = new Predicate("geometry", Operator.SMALLERTHAN, 20);
		style = new ChoroplethStyle(1, new Color(Display.getCurrent(), 0, 0, 0), new Color(Display.getCurrent(), 204, 236, 230), 50);
		entry = new LegendEntry(pred,style);
		legendList.add(entry);
		
		pred = new Predicate("geometry", Operator.SMALLERTHAN, 30);
		style = new ChoroplethStyle(1, new Color(Display.getCurrent(), 0, 0, 0), new Color(Display.getCurrent(), 153, 216, 201), 50);
		entry = new LegendEntry(pred,style);
		legendList.add(entry);
		
		pred = new Predicate("geometry", Operator.SMALLERTHAN, 40);
		style = new ChoroplethStyle(1, new Color(Display.getCurrent(), 0, 0, 0), new Color(Display.getCurrent(), 102, 194, 164), 50);
		entry = new LegendEntry(pred,style);
		legendList.add(entry);
		
		pred = new Predicate("geometry",Operator.SMALLERTHAN, 50);
		style = new ChoroplethStyle(1, new Color(Display.getCurrent(), 0, 0, 0), new Color(Display.getCurrent(), 44, 162, 95), 50);
		entry = new LegendEntry(pred,style);
		legendList.add(entry);
		
		pred = new Predicate(null,Operator.ELSE, null);
		style = new ChoroplethStyle(1, new Color(Display.getCurrent(), 0, 0, 0), new Color(Display.getCurrent(), 0, 109, 44), 50);
		entry = new LegendEntry(pred,style);
		legendList.add(entry);
		
		
	}
	public int getSize(){
		return legendList.size();
	}
	public ChoroplethStyle getStyleForValue(Integer value){
		ChoroplethStyle style = new ChoroplethStyle(ChoroplethStyle.defaultChoroplethStyle);
		for (LegendEntry entry : legendList) {
			if(entry.getPredicate().isTrue(value)){
				style = entry.getStyle();
				break;
			}
		}
		return style;
	}
	
	
}
