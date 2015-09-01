package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;


import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCUnboundedWindowTIPORule;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.UnboundedWindowTIPO;

public class CUnboundedWindowTIPORule extends AbstractCUnboundedWindowTIPORule<WindowAO>{
	
	
	public CUnboundedWindowTIPORule(){
		super(CUnboundedWindowTIPORule.class.getName());
	}


	
	@Override
	public CodeFragmentInfo getCode(WindowAO operator) {
		CodeFragmentInfo slidingWindow = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();

		String operatorVariable = JreCodegeneratorStatus.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		CodeFragmentInfo sdfInputSchema  = CreateJreDefaultCode.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		code.append(sdfInputSchema.getCode());
		 
		
		StringTemplate slidingTimeWindowTIPOTemplateTemplate = new StringTemplate("operator","unboundedWindowTIPO");
		slidingTimeWindowTIPOTemplateTemplate.getSt().add("operatorVariable", operatorVariable);
		 
		code.append(slidingTimeWindowTIPOTemplateTemplate.getSt().render());
	
		slidingWindow.addCode(code.toString());
		slidingWindow.addImport(TimeValueItem.class.getName());
		slidingWindow.addImport(TimeUnit.class.getName());
		slidingWindow.addImport(WindowType.class.getName());
		slidingWindow.addImport(UnboundedWindowTIPO.class.getName());

		return slidingWindow;
	}


}
