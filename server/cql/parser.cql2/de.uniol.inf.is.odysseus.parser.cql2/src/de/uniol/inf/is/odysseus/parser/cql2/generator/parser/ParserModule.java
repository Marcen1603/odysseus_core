package de.uniol.inf.is.odysseus.parser.cql2.generator.parser;

import com.google.inject.AbstractModule;

import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AggregationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.AttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.CreateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ExpressionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.JoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.PredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.ProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.RenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.SelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.WindowParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAggregationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeNameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IAttributeParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ICreateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExistenceParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IExpressionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IJoinParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IPredicateParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IProjectionParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IQuantificationParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IRenameParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.ISelectParser;
import de.uniol.inf.is.odysseus.parser.cql2.generator.parser.interfaces.IWindowParser;

public class ParserModule extends AbstractModule {

	@Override
	protected void configure() {
		
		bind(IPredicateParser.class).to(PredicateParser.class);
		bind(IAttributeNameParser.class).to(AttributeNameParser.class);
		bind(IRenameParser.class).to(RenameParser.class);
		bind(IJoinParser.class).to(JoinParser.class);
		bind(IWindowParser.class).to(WindowParser.class);
		bind(ISelectParser.class).to(SelectParser.class);
		bind(IProjectionParser.class).to(ProjectionParser.class);
		bind(IExistenceParser.class).to(ExistenceParser.class);
		bind(IAggregationParser.class).to(AggregationParser.class);
		bind(IAttributeParser.class).to(AttributeParser.class);
		bind(IExpressionParser.class).to(ExpressionParser.class);
		bind(IQuantificationParser.class).to(QuantificationParser.class);
		bind(ICreateParser.class).to(CreateParser.class);
		
	}

}
