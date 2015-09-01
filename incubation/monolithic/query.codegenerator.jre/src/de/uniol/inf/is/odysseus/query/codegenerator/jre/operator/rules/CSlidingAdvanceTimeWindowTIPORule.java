package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJavaDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCSlidingAdvanceTimeWindowTIPORule;
import de.uniol.inf.is.odysseus.query.codegenerator.utils.JavaTransformationInformation;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

public class CSlidingAdvanceTimeWindowTIPORule extends AbstractCSlidingAdvanceTimeWindowTIPORule<TimeWindowAO>{


	public CSlidingAdvanceTimeWindowTIPORule(){
		super(CSlidingAdvanceTimeWindowTIPORule.class.getName());
	}
	
	@Override
	public CodeFragmentInfo getCode(TimeWindowAO operator) {
		CodeFragmentInfo slidingWindow = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();

		String operatorVariable = JavaTransformationInformation.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		TimeUnit baseTimeUnit =windowAO.getBaseTimeUnit();
		TimeValueItem windowSize = windowAO.getWindowSize();
		TimeValueItem windowAdvance = windowAO.getWindowAdvance();
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		CodeFragmentInfo sdfInputSchema  = CreateJavaDefaultCode.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		code.append(sdfInputSchema.getCode());
		 
		StringTemplate slidingAdvanceTimeWindowTIPOTemplate = new StringTemplate("operator","slidingAdvanceTimeWindowTIPO");
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("windowSize", windowSize);
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("windowAdvance", windowAdvance);
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("baseTimeUnit", baseTimeUnit);
		slidingAdvanceTimeWindowTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		 
		code.append(slidingAdvanceTimeWindowTIPOTemplate.getSt().render());
	
		slidingWindow.addCode(code.toString());
		slidingWindow.addImport(TimeValueItem.class.getName());
		slidingWindow.addImport(TimeUnit.class.getName());
		slidingWindow.addImport(WindowType.class.getName());
		slidingWindow.addImport(SlidingAdvanceTimeWindowTIPO.class.getName());

		return slidingWindow;
	}



}