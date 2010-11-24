package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.settings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

@Target( { ElementType.METHOD } )
@Retention(RetentionPolicy.RUNTIME)
public @interface ChartSetting {

	enum Type { GET, SET, OPTIONS}
	String name(); 
	Type type();
}
