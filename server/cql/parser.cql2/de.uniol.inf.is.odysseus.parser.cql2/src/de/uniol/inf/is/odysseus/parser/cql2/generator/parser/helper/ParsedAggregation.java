package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.eclipse.xtext.EcoreUtil2;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SelectExpression;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QuerySource;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IParsedObject;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

public class ParsedAggregation implements IParsedObject {

	private static IUtilityService utilityService = CQLGenerator.injector.getInstance(IUtilityService.class);
	private static IAttributeParser attributeParser = CQLGenerator.injector.getInstance(IAttributeParser.class);
	
	private String name;
	private String alias;
	public SelectExpression expression;
	public Collection<QuerySource> relatedSources = new ArrayList<>();
	public Collection<QueryAttribute> relatedAttributes = new ArrayList<>();
	
	public ParsedAggregation(SimpleSelect select, SelectExpression expression, String name) {
		this.expression = expression;
		this.name = name;
		this.alias = expression.getAlias() != null ? expression.getAlias().getName() : null;
		
		EcoreUtil2.getAllContentsOfType(expression, Attribute.class).forEach(e -> {
			
			Collection<QuerySource> attributeSources = new ArrayList<>();
			attributeParser.computeSourceCandidates(e, select.getSources()).forEach(k -> {
				final QuerySource querySource = new QuerySource(k.getE1()); 
				relatedSources.add(querySource);
				attributeSources.add(querySource);
			});
			
			ParsedAttribute parsedAttribute = new ParsedAttribute(e);
			relatedAttributes.add(new QueryAttribute(e, parsedAttribute, attributeSources, utilityService.getDataTypeFrom(parsedAttribute, attributeSources)));
		});
		
		
	}

	public ParsedAggregation(ParsedAggregation parsedAggregation) {
		this.name = parsedAggregation.getName();
		this.expression = parsedAggregation.expression;
		this.relatedAttributes = parsedAggregation.relatedAttributes.stream().map(e -> new QueryAttribute(e)).collect(Collectors.toList());
		this.relatedSources = parsedAggregation.relatedSources.stream().map(e -> new QuerySource(e)).collect(Collectors.toList());
	}

	@Override
	public Type getType() {
		return Type.AGGREGATION;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getAlias() {
		return this.alias;
	}
	
}
