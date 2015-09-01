package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import java.lang.reflect.ParameterizedType;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractRule<T extends ILogicalOperator> implements IOperatorRule<T> {

	private ComponentContext context;

	private String name = "";
	private String targetPlatform = "";

	public AbstractRule() {
	}

	public AbstractRule(String name) {
		this.name = name;
	}

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
}
