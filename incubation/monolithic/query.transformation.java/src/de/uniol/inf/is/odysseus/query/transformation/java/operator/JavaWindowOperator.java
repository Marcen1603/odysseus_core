package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.operator.AbstractTransformationOperator;
import de.uniol.inf.is.odysseus.server.intervalapproach.window.SlidingAdvanceTimeWindowTIPO;

public class JavaWindowOperator extends AbstractTransformationOperator {

	private final String name = "TimeWindowAO";
	private final String targetPlatform = "Java";

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getTargetPlatform() {
		return targetPlatform;
	}

	@Override
	public String getCode(ILogicalOperator operator) {
		StringBuilder code = new StringBuilder();

		String operatorVariable = TransformationInformation.getInstance()
				.getVariable(operator);

		AbstractWindowWithWidthAO windowAO = (AbstractWindowWithWidthAO) operator;
		
		TimeValueItem windowSize = windowAO.getWindowSize();
		TimeValueItem windowSlide = windowAO.getWindowSlide();
		TimeValueItem windowAdvance = windowAO.getWindowAdvance();

		code.append("TimeWindowAO "+operatorVariable+"TimeWindowAO = new TimeWindowAO();");
		code.append("\n");

		code.append(operatorVariable+"TimeWindowAO.setWindowSize(new TimeValueItem("+windowSize.getTime()+",TimeUnit.valueOf(\""+windowSize.getUnit().toString()+"\")));");
		code.append("\n");
		if(windowSlide != null){
			code.append(operatorVariable+"TimeWindowAO.setWindowSize(new TimeValueItem("+windowSlide.getTime()+",TimeUnit.valueOf(\""+windowSlide.getUnit().toString()+"\")));");
			code.append("\n");
		}
		
		code.append(operatorVariable+"TimeWindowAO.setWindowSize(new TimeValueItem("+windowAdvance.getTime()+",TimeUnit.valueOf(\""+windowAdvance.getUnit().toString()+"\")));");
		code.append("\n");
		code.append("SlidingAdvanceTimeWindowTIPO "+operatorVariable+"PO = new SlidingAdvanceTimeWindowTIPO("+operatorVariable+"TimeWindowAO);");
		code.append("\n");
	
		return code.toString();
	}
	

	@Override
	public Set<String> getNeededImports() {
		Set<String> imoportList = new HashSet<String>();
		imoportList.add(TimeWindowAO.class.getPackage().getName() + "."
				+ TimeWindowAO.class.getSimpleName());
		imoportList.add(SlidingAdvanceTimeWindowTIPO.class.getPackage()
				.getName()
				+ "."
				+ SlidingAdvanceTimeWindowTIPO.class.getSimpleName());
		return imoportList;
	}
}
