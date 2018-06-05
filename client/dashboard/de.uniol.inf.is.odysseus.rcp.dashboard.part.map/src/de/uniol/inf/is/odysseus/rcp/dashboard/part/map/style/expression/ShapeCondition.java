package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;


import org.eclipse.nebula.cwt.svg_custom.SvgDocument;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.style.PersistentCondition;

public class ShapeCondition extends Condition<SvgDocument>{
	String defaultSvg = null;
	public ShapeCondition(PersistentCondition condition) {
		super(condition);
		this.defaultSvg = (String) condition.defaultValue;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SvgDocument getValue(Object o) {
		if (o instanceof String){
			String s = (String) o;
			s.hashCode();
			if (s.startsWith("file:")){
				try {
					URL url = new URL(s);
					return SvgDocument.load(url.openStream());
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				return SvgDocument.load(s);
			}
		}
		return null;
	}

	@Override
	protected Object getPersistentDefaultValue() {
		// TODO Auto-generated method stub
		return this.defaultSvg;
	}
}
