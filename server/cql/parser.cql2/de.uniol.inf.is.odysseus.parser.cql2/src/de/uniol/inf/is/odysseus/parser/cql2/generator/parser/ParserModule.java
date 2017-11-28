package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.AbstractModule;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(IPredicateParser.class).to(CQLPredicateParser.class);
		bind(IAttributeNameParser.class).to(AttributeNameParser.class);
		bind(IRenameParser.class).to(RenameParser.class);
		bind(IJoinParser.class).to(JoinParser.class);
		bind(IWindowParser.class).to(WindowParser.class);
		bind(ISelectParser.class).to(SelectParser.class);
		bind(IProjectionParser.class).to(ProjectionParser.class);
		bind(IExistenceParser.class).to(ExistenceParser.class);
		
	}

}
