package de.uniol.inf.is.odysseus.parser.cql2.generator.parser.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.Attribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.CQLGenerator;
import de.uniol.inf.is.odysseus.parser.cql2.generator.SystemAttribute;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.ICacheService;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.QueryAggregate;
import de.uniol.inf.is.odysseus.parser.cql2.generator.cache.QueryCache.SubQuery;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IParsedObject;
import de.uniol.inf.is.odysseus.parser.cql2.generator.utility.IUtilityService;

public class ParsedAttribute implements IParsedObject {

	private static IUtilityService utilityService = CQLGenerator.injector.getInstance(IUtilityService.class);
	private static ICacheService cacheService = CQLGenerator.injector.getInstance(ICacheService.class);
	
	private String name = null;
	private String alias = null;
	public String prefix = null;
	public String suffix = null;
	public String sourcename = null;
	public String sourcealias = null;
	public Collection<String> containedBySubQuery = new ArrayList<>();

	public ParsedAttribute(Attribute e) {

		name = e.getName();
		if (name.contains(".")) {

			final String[] split = e.getName().split("\\.");
			prefix = split[0];
			suffix = split[1];
			sourcename = split[0];

			if (utilityService.isSourceAlias(sourcename)) {
				sourcealias = sourcename;
				sourcename = utilityService.getSourcenameFromAlias(sourcename);
			}
			
			Optional<SubQuery> o = utilityService.isSubQuery(sourcename);
			
			if (o.isPresent()) {
				final SubQuery subQuery = o.get();
				containedBySubQuery.addAll(cacheService.getQueryCache().getQueryAttributes(subQuery.select)
						.stream()
						.map(k -> k.getName())
						.collect(Collectors.toList())
				);
			}
		}
		
		alias = e.getAlias() != null ? e.getAlias().getName() : null;

	}
	
	public ParsedAttribute(ParsedAttribute parsedAttribute) {
		this.alias = parsedAttribute.alias;
		this.name = parsedAttribute.name;
		this.prefix = parsedAttribute.prefix;
		this.suffix = parsedAttribute.suffix;
		this.sourcealias = parsedAttribute.sourcealias;
		this.sourcename = parsedAttribute.sourcename;
		this.containedBySubQuery = parsedAttribute.containedBySubQuery.stream().map(e -> e).collect(Collectors.toList());
	}

	private ParsedAttribute() {
	}

	public boolean matches(SystemAttribute e) {
		return name.equals(e.getAttributename()) 
				|| (suffix != null && suffix.equals(e.getAttributename()));
	}

	public boolean hasStarPrefix() {
		return prefix != null ? prefix.equals("*") : false;
	}
	
	public static ParsedAttribute convert(QueryAggregate aggregate) {
		
		ParsedAttribute parsed = new ParsedAttribute();
		parsed.name = aggregate.getAlias();
		
		return parsed;
	}
	
	@Override
	public String toString() {
		
		if (!name.contains(".") && !utilityService.isAggregationAttribute(name)) {
			if (sourcealias != null) {
				return sourcealias + "." + suffix;
			} else if (sourcename != null) {
				return sourcename + "." + suffix;
			}
		}
		
		if (alias != null) {
			return alias;
		}
		
		return name;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof ParsedAttribute) {
			
			if (this.suffix != null && ((ParsedAttribute) obj).getName().equals(this.suffix)) {
				return true;
			}
			
			return this.getName().equals(((ParsedAttribute) obj).getName());
			
//			if (this.alias != null && this.alias.equals(((ParsedAttribute) obj).getName())) {
//				return true;
//			}
//			
//			if (this.name.equals(((ParsedAttribute) obj).getName()) 
//					&& this.datatype.equals(((ParsedAttribute) obj).getDataType())
//					&& this.sourcename.equals(((ParsedAttribute) obj).sourcename)) {
//				if (this.alias != null || ((ParsedAttribute) obj).getAlias() != null) {
//					if (this.alias != null && ((ParsedAttribute) obj).getAlias() != null) {
//						return this.alias.equals(((ParsedAttribute) obj).getAlias());
//					}
//					return false;
//				}
//			}
					
		}
		
		return false;
	}

	@Override
	public Type getType() {
		return Type.ATTRIBUTE;
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
