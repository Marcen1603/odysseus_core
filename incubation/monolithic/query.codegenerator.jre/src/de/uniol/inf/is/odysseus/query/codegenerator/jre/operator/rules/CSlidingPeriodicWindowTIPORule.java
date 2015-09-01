package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCSlidingPeriodicWindowTIPORule;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingPeriodicWindowTIPO;

public class CSlidingPeriodicWindowTIPORule extends AbstractCSlidingPeriodicWindowTIPORule<TimeWindowAO>{
	
	public CSlidingPeriodicWindowTIPORule(){
		super(CSlidingPeriodicWindowTIPORule.class.getName());
	}


	@Override
	public CodeFragmentInfo getCode(TimeWindowAO operator) {
		CodeFragmentInfo slidingWindow = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();

		String operatorVariable = JreCodegeneratorStatus.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		TimeUnit baseTimeUnit =windowAO.getBaseTimeUnit();
		TimeValueItem windowSize = windowAO.getWindowSize();
		TimeValueItem windowSlide = windowAO.getWindowSlide();
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		CodeFragmentInfo sdfInputSchema  = CreateJreDefaultCode.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		code.append(sdfInputSchema.getCode());
		 
		
		StringTemplate slidingTimeWindowTIPOTemplate = new StringTemplate("operator","slidingPeriodicWindowTIPO");
		slidingTimeWindowTIPOTemplate.getSt().add("baseTimeUnit", baseTimeUnit);
		slidingTimeWindowTIPOTemplate.getSt().add("windowSize", windowSize);
		slidingTimeWindowTIPOTemplate.getSt().add("windowSlide", windowSlide);
		slidingTimeWindowTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		 
		code.append(slidingTimeWindowTIPOTemplate.getSt().render());
	
		slidingWindow.addCode(code.toString());
		slidingWindow.addImport(TimeValueItem.class.getName());
		slidingWindow.addImport(TimeUnit.class.getName());
		slidingWindow.addImport(WindowType.class.getName());
		slidingWindow.addImport(SlidingPeriodicWindowTIPO.class.getName());

		return slidingWindow;
	}

}
