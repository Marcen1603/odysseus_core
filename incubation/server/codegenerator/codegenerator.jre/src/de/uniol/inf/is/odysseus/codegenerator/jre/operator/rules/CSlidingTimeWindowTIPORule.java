package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.modell.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCSlidingTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingTimeWindowTIPO;

/**
 * This rule generate from a TimeWindowAO the code for the 
 * SlidingTimeWindowTIPO operator. 
 * 
 * template: operator/slidingTimeWindowTIPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CSlidingTimeWindowTIPORule extends AbstractCSlidingTimeWindowTIPORule<TimeWindowAO>{
	
	public CSlidingTimeWindowTIPORule(){
		super(CSlidingTimeWindowTIPORule.class.getName());
	}
	


	@Override
	public CodeFragmentInfo getCode(TimeWindowAO operator) {
	CodeFragmentInfo slidingWindow = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();

		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		TimeUnit baseTimeUnit =windowAO.getBaseTimeUnit();
		TimeValueItem windowSize = windowAO.getWindowSize();
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		CodeFragmentInfo sdfInputSchema  = CreateJreDefaultCode.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		code.append(sdfInputSchema.getCode());
		 
		StringTemplate slidingTimeWindowTIPOTemplate = new StringTemplate("operator","slidingTimeWindowTIPO");
		slidingTimeWindowTIPOTemplate.getSt().add("baseTimeUnit", baseTimeUnit);
		slidingTimeWindowTIPOTemplate.getSt().add("windowSize", windowSize);
		slidingTimeWindowTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		 
		code.append(slidingTimeWindowTIPOTemplate.getSt().render());
	
		slidingWindow.addCode(code.toString());
		slidingWindow.addFrameworkImport(TimeValueItem.class.getName());
		slidingWindow.addFrameworkImport(TimeUnit.class.getName());
		slidingWindow.addFrameworkImport(WindowType.class.getName());
		slidingWindow.addFrameworkImport(SlidingTimeWindowTIPO.class.getName());

		return slidingWindow;
	}

}


