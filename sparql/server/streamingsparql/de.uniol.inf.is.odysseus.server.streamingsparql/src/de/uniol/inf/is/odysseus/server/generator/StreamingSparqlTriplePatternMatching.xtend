package de.uniol.inf.is.odysseus.server.generator

import org.eclipse.xtend.lib.annotations.Accessors

@Accessors class StreamingSparqlTriplePatternMatching {

 	String subject
	String property
	String object
	String id
	String stmt
	
	new(String subject,String property, String object, String source, String id){
		this.subject = subject
		this.property = property
		this.object = object
		this.id = id
		stmt = id + "=TRIPLEPATTERNMATCHING({condition=['" + subject + "','" + property + "','" + object + "'],type = '" + id + "'}," + source + ")"
	}
	
	
	
}
		