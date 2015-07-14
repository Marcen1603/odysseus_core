package de.uniol.inf.is.odysseus.query.transformation.java.operator;

import java.util.List;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowWithWidthAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowType;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.query.transformation.java.mapping.TransformationInformation;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformSDFAttribute;
import de.uniol.inf.is.odysseus.query.transformation.java.utils.TransformSDFSchema;
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
		
		WindowType windowType = windowAO.getWindowType();
		TimeUnit baseTimeUnit =windowAO.getBaseTimeUnit();
		TimeValueItem windowSize = windowAO.getWindowSize();
		TimeValueItem windowAdvance = windowAO.getWindowAdvance();
		TimeValueItem windowSlide = windowAO.getWindowSlide();
		boolean partioned = windowAO.isPartitioned();
		List<SDFAttribute> partitionedBy = windowAO.getPartitionBy();
		SDFSchema inputSchema = windowAO.getInputSchema();
		
		
		 if(partioned){
				CodeFragmentInfo sdfAttributeInfo = TransformSDFAttribute.getCodeForSDFAttribute(partitionedBy, operatorVariable+"PartitionBy");
				code.append(sdfAttributeInfo.getCode());
		}
		 
		 CodeFragmentInfo sdfInputSchema  = TransformSDFSchema.getCodeForSDFSchema(inputSchema, operatorVariable+"InputSchema");
		 code.append(sdfInputSchema.getCode());
	
		 code.append("SlidingAdvanceTimeWindowTIPO "+operatorVariable+"PO = new SlidingAdvanceTimeWindowTIPO(");
		 code.append("WindowType."+windowType.toString().toUpperCase()+",");
		 code.append("TimeUnit."+baseTimeUnit.toString().toUpperCase()+",");
		 code.append("new TimeValueItem("+windowSize.getTime()+",TimeUnit.valueOf(\""+windowSize.getUnit().toString()+"\")),");
		 code.append("new TimeValueItem("+windowAdvance.getTime()+",TimeUnit.valueOf(\""+windowAdvance.getUnit().toString()+"\")),");
		 
		 if(windowSlide!=null){
			 code.append("new TimeValueItem("+windowSlide.getTime()+",TimeUnit.valueOf(\""+windowSlide.getUnit().toString()+"\",");
		 }else{
			 code.append("null,");
		 }
		 code.append(partioned+",");
		 
		 if(partioned){
			     code.append(operatorVariable+"PartitionBySDFAttributeList,");
			}else{
			     code.append("null,");
		 }
		
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
