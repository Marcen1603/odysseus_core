package de.uniol.inf.is.odysseus.codegenerator.jre.operator.rules;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.codegenerator.model.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.codegenerator.operator.rule.AbstractCSlidingAdvanceTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.codegenerator.utils.DefaultCodegeneratorStatus;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

/**
 * This rule generate from a TimeWindowAO the code for the 
 * SlidingAdvanceTimeWindowTIPO operator. 
 * 
 * template: operator/slidingAdvanceTimeWindowTIPO.st
 * 
 * @author MarcPreuschaft
 *
 */
public class CSlidingAdvanceTimeWindowTIPORule extends AbstractCSlidingAdvanceTimeWindowTIPORule<TimeWindowAO>{


	public CSlidingAdvanceTimeWindowTIPORule(){
		super(CSlidingAdvanceTimeWindowTIPORule.class.getName());
	}
	
	@Override
	public CodeFragmentInfo getCode(TimeWindowAO operator) {
		CodeFragmentInfo slidingWindow = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();
		
		//get unique operator variable
		String operatorVariable = DefaultCodegeneratorStatus.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		//get needed values from TimeWindowAO
		TimeUnit baseTimeUnit =windowAO.getBaseTimeUnit();
		TimeValueItem windowSize = windowAO.getWindowSize();
		TimeValueItem windowAdvance = windowAO.getWindowAdvance();
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		//generate code for inputSchema
		CodeFragmentInfo sdfInputSchema  = CreateJreDefaultCode.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		code.append(sdfInputSchema.getCode());
		 
		//generate code for slidingAdvanceTimeWindowTIPO
		StringTemplate slidingAdvanceTimeWindowTIPOTemplate = new StringTemplate("operator","slidingAdvanceTimeWindowTIPO");
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("windowSize", windowSize);
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("windowAdvance", windowAdvance);
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("baseTimeUnit", baseTimeUnit);
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		 
		//render template
		code.append(slidingAdvanceTimeWindowTIPOTemplate.getSt().render());
	
		slidingWindow.addCode(code.toString());
		
		//add framework imports
		slidingWindow.addFrameworkImport(TimeValueItem.class.getName());
		slidingWindow.addFrameworkImport(TimeUnit.class.getName());
		slidingWindow.addFrameworkImport(WindowType.class.getName());
		slidingWindow.addFrameworkImport(SlidingAdvanceTimeWindowTIPO.class.getName());

		return slidingWindow;
	}



}
