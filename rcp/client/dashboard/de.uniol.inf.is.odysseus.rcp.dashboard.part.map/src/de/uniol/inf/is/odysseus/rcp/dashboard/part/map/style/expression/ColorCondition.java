package de.uniol.inf.is.odysseus.rcp.dashboard.part.map.style.expression;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.ColorManager;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.style.PersistentCondition;
import de.uniol.inf.is.odysseus.rcp.dashboard.part.map.model.style.PersistentStyle;

public class ColorCondition extends Condition<Color>{

	public ColorCondition(PersistentCondition condition) {
		super(condition);
	}
	
	public ColorCondition(Color defaultValue) {
		super(defaultValue);
	}
	
	@Override
	protected Color getValue(Object o) {
		if (o instanceof String){
			RGB rgb = PersistentStyle.convertHexadecimalToRGB((String)o);
			return ColorManager.getInstance().getColor(rgb);
		}
		else if (o instanceof Color)
			return (Color) o;
		return getDefault();
	}

	@Override
	protected Object getPersistentDefaultValue() {
		// TODO Auto-generated method stub
		return PersistentStyle.convertRGBToHexadecimal(getDefault().getRGB());
	}

}
