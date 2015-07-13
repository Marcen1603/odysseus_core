package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.query.transformation.operator.CodeFragmentInfo;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

public class JavaWindowOperator extends AbstractTransformationOperator {

	public JavaWindowOperator(){
		this.implClass = SlidingAdvanceTimeWindowTIPO.class;
		this.name = "TimeWindowAO";
		this.targetPlatform = "Java";
		defaultImports();
	}

	@Override
	public CodeFragmentInfo getCode(ILogicalOperator operator) {
		CodeFragmentInfo slidingWindow = new CodeFragmentInfo();
		
		StringBuilder code = new StringBuilder();

		String operatorVariable = TransformationInformation.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		TimeValueItem windowSize = windowAO.getWindowSize();
		TimeValueItem windowSlide = windowAO.getWindowSlide();
		TimeValueItem windowAdvance = windowAO.getWindowAdvance();
		

		code.append("TimeWindowAO "+operatorVariable+"TimeWindowAO = new TimeWindowAO();");
		code.append("\n");
		
		code.append(operatorVariable+"TimeWindowAO.setBaseTimeUnit(TimeUnit.MICROSECONDS);");
		code.append("\n");
		
		code.append(operatorVariable+"TimeWindowAO.setWindowSize(new TimeValueItem("+windowSize.getTime()+",TimeUnit.valueOf(\""+windowSize.getUnit().toString()+"\")));");
		code.append("\n");
		if(windowSlide != null){
			code.append(operatorVariable+"TimeWindowAO.setWindowSlide(new TimeValueItem("+windowSlide.getTime()+",TimeUnit.valueOf(\""+windowSlide.getUnit().toString()+"\")));");
			code.append("\n");
		}
		
		code.append(operatorVariable+"TimeWindowAO.setWindowAdvance(new TimeValueItem("+windowAdvance.getTime()+",TimeUnit.valueOf(\""+windowAdvance.getUnit().toString()+"\")));");
		code.append("\n");
		code.append(operatorVariable+"TimeWindowAO.setOutputSchema("+operatorVariable+"SDFSchema);");
		code.append("\n");
		code.append("SlidingAdvanceTimeWindowTIPO "+operatorVariable+"PO = new SlidingAdvanceTimeWindowTIPO("+operatorVariable+"TimeWindowAO);");
		code.append("\n");

	
		slidingWindow.addCode(code.toString());
		
		slidingWindow.addImport(TimeWindowAO.class.getName());
		slidingWindow.addImport(TimeValueItem.class.getName());
		slidingWindow.addImport(TimeUnit.class.getName());
		
		
		return slidingWindow;
	}

	@Override
	public void defineImports() {

		
	}
	
}
