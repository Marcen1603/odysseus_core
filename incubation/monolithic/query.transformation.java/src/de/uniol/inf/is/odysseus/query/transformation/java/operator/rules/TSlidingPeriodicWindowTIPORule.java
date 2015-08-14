package de.uniol.inf.is.odysseus.query.transformation.java.operator.rules;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.CreateDefaultCode;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.transformation.operator.rule.AbstractRule;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingPeriodicWindowTIPO;

public class TSlidingPeriodicWindowTIPORule extends AbstractRule{
	
	public TSlidingPeriodicWindowTIPORule(){
		super("TSlidingPeriodicWindowTIPORule", "Java");
	}
	


	@Override
	public Class<?> getConditionClass() {
		return AbstractWindowAO.class;
	}

	@Override
	public boolean isExecutable(ILogicalOperator logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		
	if(logicalOperator instanceof AbstractWindowAO){
		AbstractWindowAO operator = (AbstractWindowAO) logicalOperator;
		switch (operator.getWindowType()) {
			case TIME:
				if (operator.getWindowSlide() == null && operator.getWindowAdvance() == null || operator.getWindowSlide() == null) {
					return false;
				} else {
					return true;
				}
			default:
				return false;
			
			}
		
	}
	return false;
	
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo slidingWindow = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();

		String operatorVariable = TransformationInformation.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		TimeUnit baseTimeUnit =windowAO.getBaseTimeUnit();
		TimeValueItem windowSize = windowAO.getWindowSize();
		TimeValueItem windowSlide = windowAO.getWindowSlide();
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		CodeFragmentInfo sdfInputSchema  = CreateDefaultCode.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
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
