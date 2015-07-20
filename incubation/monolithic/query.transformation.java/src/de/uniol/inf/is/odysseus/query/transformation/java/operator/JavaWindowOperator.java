package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformSDFSchema;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

public class JavaWindowOperator extends AbstractTransformationOperator {

	public JavaWindowOperator(){
		super(SlidingAdvanceTimeWindowTIPO.class, "TimeWindowAO","Java");
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
		TimeValueItem windowAdvance = windowAO.getWindowAdvance();
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		 CodeFragmentInfo sdfInputSchema  = TransformSDFSchema.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		 code.append(sdfInputSchema.getCode());
	
		 code.append("SlidingAdvanceTimeWindowTIPO "+operatorVariable+"PO = new SlidingAdvanceTimeWindowTIPO(");
		 code.append("TimeUnit."+baseTimeUnit.toString().toUpperCase()+",");
		 code.append("new TimeValueItem("+windowSize.getTime()+",TimeUnit.valueOf(\""+windowSize.getUnit().toString()+"\")),");
		 code.append("new TimeValueItem("+windowAdvance.getTime()+",TimeUnit.valueOf(\""+windowAdvance.getUnit().toString()+"\")),");
	     code.append(operatorVariable+"InputSchemaSDFSchema");
		 code.append(");");
		
	
		slidingWindow.addCode(code.toString());
		slidingWindow.addImport(TimeValueItem.class.getName());
		slidingWindow.addImport(TimeUnit.class.getName());
		slidingWindow.addImport(WindowType.class.getName());

		
	
		return slidingWindow;
	}

	@Override
	public void defineImports() {

		
	}
	
}
