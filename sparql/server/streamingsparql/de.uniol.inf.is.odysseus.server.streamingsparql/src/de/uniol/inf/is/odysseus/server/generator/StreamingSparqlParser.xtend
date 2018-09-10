package de.uniol.inf.is.odysseus.server.generator

import de.uniol.inf.is.odysseus.core.collection.Context
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor
import de.uniol.inf.is.odysseus.core.usermanagement.ISession
import de.uniol.inf.is.odysseus.server.streamingsparql.SPARQLQuery
import de.uniol.inf.is.odysseus.server.streamingsparql.SelectQuery
import java.util.ArrayList
import java.util.Collection
import java.util.HashMap
import java.util.HashSet
import java.util.Iterator
import java.util.LinkedHashMap
import java.util.LinkedHashSet
import java.util.List
import org.eclipse.emf.ecore.resource.Resource
import com.google.inject.Inject
import org.eclipse.xtext.testing.util.ParseHelper

class StreamingSparqlParser implements IStreamingSparqlParser{
	
	@Inject 
	ParseHelper<SPARQLQuery> helper;
	
	SelectQuery qry
	HashMap<String,String> prefixMap = new HashMap();
	HashMap<String,String> streamMap = new HashMap();
	LinkedHashMap<String,String> joinMap = new LinkedHashMap();
	HashMap<String,String> unionMap = new HashMap();
	HashMap<String,StreamingSparqlTriplePatternMatching> triplePatternMatchingMap = new HashMap();
	var cnt = 0
	
	override parse(Resource resource) {
		prefixMap.clear
		streamMap.clear
		joinMap.clear
		triplePatternMatchingMap.clear
		unionMap.clear
		qry = resource.allContents.filter(SelectQuery).next
			
		qry.parse
}
	
	def parse(SelectQuery q){
		var last = ""
		for (prefix : q.prefixes) {
			var pf = prefix.iref.replace('>','').replace('<','')
			if(pf.endsWith("/")){
				pf = pf.substring(0,pf.length-1)
			}
			prefixMap.put(prefix.name, pf);
		}
		for (clause : q.datasetClauses) {
			if(clause.type === null){
				streamMap.put(clause.name ,clause.dataSet.value.replace('>','').replace('<',''))
			}else if(clause.type.equals("TIME")){
				streamMap.put(clause.name, '''«clause.name» = TIMEWINDOW({SIZE = «IF clause.unit !== null»[«clause.size», '«clause.unit»']«ELSE»«clause.size»«ENDIF»«IF clause.advance !== 0», advance = «clause.advance»«ENDIF»}, «clause.dataSet.value.replace('>','').replace('<','')»)''')
			}else if(clause.type.equals("ELEMENT")){
				streamMap.put(clause.name, '''«clause.name» = ELEMENTWINDOW({SIZE = «clause.size» «IF clause.advance !== 0», advance = «clause.advance»«ENDIF»}, «clause.dataSet.value.replace('>','').replace('<','')»)''')
			}
		}
		
		for (whereClause : q.whereClause.whereclauses) {
			for (triple : whereClause.groupGraphPattern.graphPatterns) {
				for (propob : triple.propertyList) {
					if(streamMap.get(whereClause.name.name).contains('=')){
						var object = if(propob.object.variable === null) propob.object.literal else "?" + propob.object.variable.unnamed.name
						val triplePattern = new StreamingSparqlTriplePatternMatching("?" + triple.subject.variable.unnamed.name,prefixMap.get(propob.property.variable.property.prefix.name) + "/" + propob.property.variable.property.name, object, whereClause.name.name, "pattern" + cnt)
						last = "pattern" + cnt
						triplePatternMatchingMap.put(last, triplePattern)
					}else{
						var object = if(propob.object.variable === null) propob.object.literal else "?" + propob.object.variable.unnamed.name
						val triplePattern = new StreamingSparqlTriplePatternMatching("?" + triple.subject.variable.unnamed.name,prefixMap.get(propob.property.variable.property.prefix.name) + "/" + propob.property.variable.property.name,object, streamMap.get(whereClause.name.name), "pattern" + cnt)
						last = "pattern" + cnt
						triplePatternMatchingMap.put(last, triplePattern)
					}
					cnt++
				}
			}
		}
		
		val allVariables = new ArrayList<String>()
		for (pattern : triplePatternMatchingMap.entrySet) {
			allVariables.add(pattern.value.subject)
			allVariables.add(pattern.value.object)
		}
		
		val duplicates = findDuplicates(allVariables).toList
		
		val varToPattern = new LinkedHashMap()
		for (variable : duplicates) {
			varToPattern.put(variable,triplePatternMatchingMap.values.filter[p | p.subject.equals(variable) || p.object.equals(variable)].toList)
		}
		
		var listToJoin = new ArrayList<TripleToJoin>()

		
		if(listToJoin.empty){
			val v = duplicates.get(0)
			for (i : 0..< varToPattern.get(v).size - 1) {
				listToJoin.add(new TripleToJoin(varToPattern.get(v).get(i),varToPattern.get(v).get(i+1),v.replace('?','')))
			}
			duplicates.remove(v)
		}
		
		var duplicatesIterator = duplicates.iterator
		
		while(!duplicatesIterator.empty){

			val toAdd = listToJoinIteration(duplicatesIterator, varToPattern, listToJoin)
			if(toAdd !== null && !toAdd.empty){
				listToJoin.addAll(toAdd)
			}
			
			if(!duplicatesIterator.hasNext){
				duplicatesIterator = duplicates.iterator
			}
			
		}
		
		val listOfMultiplePredicates = new ArrayList<TripleToJoin>
		
		//gleiche pattern2 müssen an das ende der tripleToJoin Liste für das Erstellen der Statements
		val newlist = new ArrayList
		val listwithmorethanonepattern2 = new ArrayList
		
		for (element : listToJoin) {
			val j = listToJoin.filter[p | p.pattern2 == element.pattern2].toList
			if(j.size == 1){
				newlist.addAll(j)
			}else if(j.size > 1 && !listwithmorethanonepattern2.containsAll(j)){
				listwithmorethanonepattern2.addAll(j)
			}
		}
		
		newlist.addAll(listwithmorethanonepattern2)
		listToJoin = newlist
		
		for (element : listToJoin) {
			
			val j = listToJoin.filter[p | p.pattern2 == element.pattern2].toList
			
			if(joinMap.empty){
				joinMap.put("join" + cnt, "join" + cnt + " = join({predicate = '" + element.pattern1.id + "." + element.variable +
					 " = " + element.pattern2.id + "." + element.variable + "'}, " + element.pattern1.id + ", " + element.pattern2.id + ")"
				)
				last = "join" + cnt
				cnt++
			}else if(!listOfMultiplePredicates.containsAll(j)){
				
				var qry = '''
					join«cnt» = join({predicate = '«FOR elem : j SEPARATOR ' && '»«elem.pattern1.id».«elem.variable» = «elem.pattern2.id».«elem.variable»«ENDFOR»'}, «element.pattern2.id», join«cnt-1»)'''
				joinMap.put("join" + cnt, qry)
				last = "join" + cnt
				cnt++
			}
			if(j.size > 1){
				listOfMultiplePredicates.addAll(j)
			}
			
		}
				
		var filter = ""
		if(q.filterclause !== null){
			filter = '''
			filter=SELECT({predicate='«q.filterclause.left.unnamed.name» «q.filterclause.operator» «q.filterclause.right.unnamed.name»'},«last»)'''
			last = "filter"
		}
		
		var aggregate = ""
		if(q.aggregateClause !== null){
			val aggregateClause = q.aggregateClause
			var name = "aggregate"
			var aggregations = "";
			var groupBy = "";
			if(aggregateClause.aggregations !== null){
				aggregations = '''AGGREGATIONS=[«FOR clause : aggregateClause.aggregations SEPARATOR ','»['«clause.function»','«clause.varToAgg.unnamed.name»','«clause.aggName»'«IF clause.datatype !== null»,'«clause.datatype»'«ENDIF»]«ENDFOR»]'''
			}
			if(aggregateClause.groupby !== null){
				groupBy = '''group_by = [«FOR v : aggregateClause.groupby.variables SEPARATOR ','»'«v.unnamed.name»'«ENDFOR»]'''
			}
			aggregate = '''«name» = AGGREGATE({«IF !aggregations.equals("")» «aggregations»«ENDIF»«IF !groupBy.equals("")», «groupBy»«ENDIF»}, «last»)'''
			last = name
		}
		
		var projection = '''
		result = project({attributes = [«FOR variable : q.variables SEPARATOR ','»'«variable.unnamed.name»'«ENDFOR»]},«last»)
		'''
		
		
		val query = new StringBuilder()
		query.append("#PARSER PQL\n")
		if(q.method !== null){
			query.append(q.method + "\n")
		}else{
			query.append("#ADDQUERY\n")
		}
		for (win : streamMap.entrySet) {
			if(win.value.contains('=')){
				query.append(win.value + "\n")	
			}
		}
		for (triple : triplePatternMatchingMap.entrySet) {
			query.append(triple.value.stmt + "\n")
		}
		for (triple : joinMap.entrySet) {
			query.append(triple.value + "\n")
		}
		for (triple : unionMap.entrySet) {
			query.append(triple.value + "\n")
		}
		if(q.filterclause !== null){
			query.append(filter + "\n")
		}
//		if(q.groupClause !== null){
//			query.append(group + "\n")
//		}
		if(q.aggregateClause !== null){
			query.append(aggregate + "\n")
		}
		query.append(projection)
		if(q.filesinkclause !== null){
			query.append("res = CSVFILESINK({SINK = 'sink', FILENAME = '" + q.filesinkclause.path.replace('/', '\\') + "'}, result)")
		}
		print(query)
		query.toString
		
	}
	
	def findDuplicates(Collection<String> list) {

	    val duplicates = new LinkedHashSet<String>();
	    val uniques = new HashSet<String>();
	
	    for(t : list) {
	        if(!uniques.add(t)) {
	            duplicates.add(t);
	        }
	    }

    	return duplicates;
	}
	
	
	def listToJoinIteration(Iterator<String> duplicatesIterator, LinkedHashMap<String, List<StreamingSparqlTriplePatternMatching>> varToPattern, ArrayList<TripleToJoin> listToJoin){
		val listToJoinIterator = listToJoin.iterator
		val listToAdd = new ArrayList<TripleToJoin>()
		val variable = duplicatesIterator.next
		val patterns = varToPattern.get(variable)
		while(listToJoinIterator.hasNext){
			val joinElement = listToJoinIterator.next
			var filteredList  = patterns.filter[p | p == joinElement.pattern1 || p == joinElement.pattern2].toList
			if(filteredList.size > 0){
				
				patterns.remove(filteredList.get(0))
				listToAdd.add(new TripleToJoin(filteredList.get(0), patterns.get(0), variable.replace('?','')))
				for (k : 0 ..< patterns.size - 1) {
					listToAdd.add(new TripleToJoin(patterns.get(k), patterns.get(k+1), variable.replace('?','')))
				}
				duplicatesIterator.remove
				return listToAdd
		}
	}
	}
	
	override parse(String query, ISession user, IDataDictionary dd, Context context, IMetaAttribute metaAttribute, IServerExecutor executor) {
		
		val q = helper.parse(query)
		if(q instanceof SelectQuery){
			parse(q)
		}
	}


	
}