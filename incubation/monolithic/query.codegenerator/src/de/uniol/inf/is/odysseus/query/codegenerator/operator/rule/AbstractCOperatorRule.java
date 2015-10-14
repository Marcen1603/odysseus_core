package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.mep.IBinaryOperator;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCOperatorRule<T extends ILogicalOperator> implements ICOperatorRule<T> {

	private ComponentContext context;

	private String name = "";
	private String targetPlatform = "";

	public AbstractCOperatorRule() {
	}

	public AbstractCOperatorRule(String name) {
		this.name = name;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	public String getName() {
		return this.name;
	}

	public void setTragetPlattform(String targetPlatform) {
		this.targetPlatform = targetPlatform;
	}

	public String getTargetPlatform() {
		return this.targetPlatform;
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getConditionClass() {
		ParameterizedType parameterizedType = (ParameterizedType) getClass()
				.getGenericSuperclass();
		return (Class<T>) parameterizedType.getActualTypeArguments()[0];
	}

	
	/*
	 * optional
	 */
	@Override
	public void analyseOperator(T logicalOperator,
			QueryAnalyseInformation transformationInformation) {

	}

	@Override
	public void addOperatorConfiguration(T logicalOperator,
			QueryAnalyseInformation transformationInformation) {

	}

	/*
	 * call by osgi
	 */
	protected void activate(ComponentContext context) {
		this.context = context;
		initRule();
	}

	private void initRule() {
		String targetPlattform = (String) this.context.getProperties().get(
				"targetPlatform");
		setTragetPlattform(targetPlattform);
	}

	/*
	 * Helper
	 */
	@Override
	public void addDataHandlerFromSDFSchema(ILogicalOperator logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		SDFSchema sdfSchema = logicalOperator.getOutputSchema();

		for (SDFAttribute attribute : sdfSchema.getAttributes()) {
			transformationInformation.addDataHandler(attribute.getDatatype()
					.toString());
		}

	}
	
	
	public  Map<String,IExpression<?>> getAllMEPFunctions( IExpression<?> expression){
		   Map<String,IExpression<?>> functionList = new HashMap<String,IExpression<?>>();
		
			if (expression.isFunction()) {
				functionList.put(expression.getClass().getName(), expression);
			
				IBinaryOperator<?> binaryOperator = (IBinaryOperator<?>) expression;
				IExpression<?> argument1 = binaryOperator.getArgument(0);
				if (argument1.isFunction()) {
					functionList.put(argument1.getClass().getName(), argument1);
					functionList.putAll(getAllMEPFunctions(argument1));
				}
				IExpression<?> argument2 = binaryOperator.getArgument(1);
				if (argument2.isFunction()) {
					functionList.put(argument2.getClass().getName(), argument2);
					functionList.putAll(getAllMEPFunctions(argument2));
				}
			}
			return functionList;
	}

}
