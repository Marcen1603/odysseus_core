package de.uniol.inf.is.odysseus.script.generator.impl;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.parser.pql.generator.IPQLGenerator;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGenerator;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorFooter;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorHeader;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorQueryFooter;
import de.uniol.inf.is.odysseus.script.generator.IOdysseusScriptGeneratorQueryHeader;

public class OdysseusScriptGenerator implements IOdysseusScriptGenerator {

	private static IPQLGenerator pqlGenerator;
	private static Collection<IOdysseusScriptGeneratorHeader> headers = Lists.newArrayList();
	private static Collection<IOdysseusScriptGeneratorFooter> footers = Lists.newArrayList();
	private static Collection<IOdysseusScriptGeneratorQueryHeader> queryHeaders = Lists.newArrayList();
	private static Collection<IOdysseusScriptGeneratorQueryFooter> queryFooters = Lists.newArrayList();

	// called by OSGi
	public static void bindPQLGenerator(IPQLGenerator serv) {
		pqlGenerator = serv;
	}

	// called by OSGi
	public static void unbindPQLGenerator(IPQLGenerator serv) {
		if( pqlGenerator == serv ) {
			pqlGenerator = serv;
		}
	}
	
	// called by OSGi
	public static void bindOdysseusScriptGeneratorHeader( IOdysseusScriptGeneratorHeader serv ) {
		headers.add(serv);
	}
	
	// called by OSGi
	public static void unbindOdysseusScriptGeneratorHeader( IOdysseusScriptGeneratorHeader serv ) {
		headers.remove(serv);
	}
	
	// called by OSGi
	public static void bindOdysseusScriptGeneratorFooter( IOdysseusScriptGeneratorFooter serv ) {
		footers.add(serv);
	}
	
	// called by OSGi
	public static void unbindOdysseusScriptGeneratorFooter( IOdysseusScriptGeneratorFooter serv ) {
		footers.remove(serv);
	}

	// called by OSGi
	public static void bindOdysseusScriptGeneratorQueryHeader( IOdysseusScriptGeneratorQueryHeader serv ) {
		queryHeaders.add(serv);
	}
	
	// called by OSGi
	public static void unbindOdysseusScriptGeneratorQueryHeader( IOdysseusScriptGeneratorQueryHeader serv ) {
		queryHeaders.remove(serv);
	}
	
	// called by OSGi
	public static void bindOdysseusScriptGeneratorQueryFooter( IOdysseusScriptGeneratorQueryFooter serv ) {
		queryFooters.add(serv);
	}
	
	// called by OSGi
	public static void unbindOdysseusScriptGeneratorQueryFooter( IOdysseusScriptGeneratorQueryFooter serv ) {
		queryFooters.remove(serv);
	}

	@Override
	public String generate(List<ILogicalQuery> queries, QueryBuildConfiguration config) {
		Preconditions.checkNotNull(queries, "List of queries to generate odysseus script must not be null!");
		Preconditions.checkArgument(!queries.isEmpty(), "List of queries to generate odysseus script must not be empty!");
		Preconditions.checkNotNull(config, "QueryBuildConfiguration must not be null!");
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("#PARSER PQL\n");
		
		sb.append(generateHeader(queries, config));
		for( ILogicalQuery query : queries ) {
			
			sb.append(generateQueryHeader(query, config));
			
			sb.append("#ADDQUERY\n");
			sb.append(pqlGenerator.generatePQLStatement(query.getLogicalPlan()));
			
			sb.append(generateQueryFooter(query, config));
			sb.append("\n\n");
		}
		
		sb.append( generateFooter(queries, config ));
		
		return sb.toString();
	}

	private static String generateQueryHeader(ILogicalQuery query, QueryBuildConfiguration config) {
		StringBuilder sb = new StringBuilder();
		
		for( IOdysseusScriptGeneratorQueryHeader queryHeader : queryHeaders ) {
			String text = queryHeader.getQueryHeader(query, config);
			if( !Strings.isNullOrEmpty(text)) {
				sb.append(text);
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}
	
	private static String generateQueryFooter(ILogicalQuery query, QueryBuildConfiguration config) {
		StringBuilder sb = new StringBuilder();
		
		for( IOdysseusScriptGeneratorQueryFooter queryFooter : queryFooters ) {
			String text = queryFooter.getQueryFooter(query, config);
			if( !Strings.isNullOrEmpty(text)) {
				sb.append(text);
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}

	private static String generateFooter(List<ILogicalQuery> queries, QueryBuildConfiguration config) {
		StringBuilder sb = new StringBuilder();
		
		for( IOdysseusScriptGeneratorFooter footer : footers ) {
			String text = footer.getFooter(queries, config);
			if( !Strings.isNullOrEmpty(text)) {
				sb.append(text);
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}

	private static String generateHeader(List<ILogicalQuery> queries, QueryBuildConfiguration config) {
		StringBuilder sb = new StringBuilder();
		
		for( IOdysseusScriptGeneratorHeader header : headers ) {
			String text = header.getHeader(queries, config);
			if( !Strings.isNullOrEmpty(text)) {
				sb.append(text);
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}

}
