package de.uniol.inf.is.odysseus.server.generator

import org.eclipse.xtend.lib.annotations.Accessors
import org.eclipse.xtend.lib.annotations.Data
import de.uniol.inf.is.odysseus.server.generator.StreamingSparqlTriplePatternMatching

@Accessors
@Data
class TripleToJoin {
	
	StreamingSparqlTriplePatternMatching pattern1
	StreamingSparqlTriplePatternMatching pattern2
	String variable
	
	new (StreamingSparqlTriplePatternMatching pattern1, StreamingSparqlTriplePatternMatching pattern2, String variable){
		this.pattern1 = pattern1
		this.pattern2 = pattern2
		this.variable = variable
	}
	
	
}