package de.uniol.inf.is.odysseus.query.codegenerator.jre.operator.rules;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.CreateJreDefaultCode;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.JreCodegeneratorStatus;
import de.uniol.inf.is.odysseus.query.codegenerator.jre.utils.StringTemplate;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.query.codegenerator.operator.rule.AbstractCSlidingElementWindowTIPORule;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingElementWindowTIPO;

public class CSlidingElementWindowTIPORule extends AbstractCSlidingElementWindowTIPORule<ElementWindowAO>{

	public CSlidingElementWindowTIPORule() {
		super(CSlidingElementWindowTIPORule.class.getName());
	}

	
	@Override
	public CodeFragmentInfo getCode(ElementWindowAO operator) {
		CodeFragmentInfo slidingElementWindowCode = new CodeFragmentInfo();
	
		String operatorVariable = JreCodegeneratorStatus.getInstance()
				.getVariable(operator);

		boolean isWindowSlideNull = true;
		
		TimeValueItem windowSize = operator.getWindowSize();
		TimeValueItem windowAdvance = operator.getWindowAdvance();
		TimeValueItem windowSlide = operator.getWindowSlide();
		SDFSchema inputSchema = operator.getInputSchema();
		List<SDFAttribute> partitonByList = operator.getPartitionBy();
		TimeUnit baseTimeUnit =operator.getBaseTimeUnit();
		
		CodeFragmentInfo sdfInputSchema  = CreateJreDefaultCode.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		slidingElementWindowCode.addCode(sdfInputSchema.getCode());
		
		
		CodeFragmentInfo sdfPartitonSchema  = CreateJreDefaultCode.getCodeForSDFAttributeList(partitonByList, operatorVariable+"Partition");
		slidingElementWindowCode.addCode(sdfPartitonSchema.getCode());
		
		
		if(windowSlide != null){
			isWindowSlideNull= false;
		}
		
		
		StringTemplate slidingTimeWindowTIPOTemplate = new StringTemplate("operator","slidingElementWindowTIPO");
		slidingTimeWindowTIPOTemplate.getSt().add("operatorVariable", operatorVariable);
		slidingTimeWindowTIPOTemplate.getSt().add("windowSize", windowSize);
		slidingTimeWindowTIPOTemplate.getSt().add("windowAdvance", windowAdvance);
		slidingTimeWindowTIPOTemplate.getSt().add("windowSlide", windowSlide);
		slidingTimeWindowTIPOTemplate.getSt().add("isWindowSlideNull", isWindowSlideNull);
		slidingTimeWindowTIPOTemplate.getSt().add("baseTimeUnit", baseTimeUnit);
		
		
		
		
		slidingElementWindowCode.addCode(slidingTimeWindowTIPOTemplate.getSt().render());
	
	
		slidingElementWindowCode.addImport(TimeValueItem.class.getName());
		slidingElementWindowCode.addImport(TimeUnit.class.getName());
		slidingElementWindowCode.addImport(WindowType.class.getName());
		slidingElementWindowCode.addImport(SlidingElementWindowTIPO.class.getName());

		return slidingElementWindowCode;
	}



}

	
