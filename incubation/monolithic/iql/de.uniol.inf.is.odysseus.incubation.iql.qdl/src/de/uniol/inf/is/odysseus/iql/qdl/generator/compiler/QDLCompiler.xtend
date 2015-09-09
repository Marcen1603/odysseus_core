package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.IQDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.AbstractQDLQuery
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataMethodCompiler
import java.util.Collection
import java.util.ArrayList
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.DefaultQDLSource
import de.uniol.inf.is.odysseus.iql.qdl.generator.context.IQDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.typing.utils.IQDLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement
import org.eclipse.xtext.common.types.JvmOperation
import de.uniol.inf.is.odysseus.iql.qdl.typing.dictionary.IQDLTypeDictionary

class QDLCompiler extends AbstractIQLCompiler<IQDLCompilerHelper, IQDLGeneratorContext, IQDLTypeCompiler, IQDLStatementCompiler, IQDLTypeDictionary, IQDLTypeUtils> implements IQDLCompiler{
	
	@Inject
	IQDLMetadataMethodCompiler methodCompiler;
	
	@Inject
	new(IQDLCompilerHelper helper, IQDLTypeCompiler typeCompiler, IQDLStatementCompiler stmtCompiler, IQDLTypeDictionary typeDictionary, IQDLTypeUtils typeUtils) {
		super(helper, typeCompiler, stmtCompiler, typeDictionary, typeUtils)
	}
	
	override String compile(QDLQuery q, IQDLGeneratorContext context) {
		var builder = new StringBuilder()
		var element = q.eContainer as IQLModelElement;
		builder.append(compileQuery(element, q, context))
		context.addImport(AbstractQDLQuery.canonicalName)
		context.addImport(Collection.canonicalName)
		context.addImport(IQDLOperator.canonicalName)
		context.addImport(ArrayList.canonicalName)
		
		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	def String compileQuery(IQLModelElement element, QDLQuery q, IQDLGeneratorContext context) {
		var name = q.simpleName
		var superClass = AbstractQDLQuery.simpleName
		var metadata = q.metadataList != null
		var block = q.statements as IQLStatementBlock
		var newExpressions = helper.getNewExpressions(q);
		var varStmts = helper.getVarStatements(q);
		
		'''
		
		«FOR j : element.javametadata»
		«var text = j.java.text»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public class «name» extends «superClass» {
			
			public «name»() {
				super();
				«IF metadata»
					«IIQLMetadataMethodCompiler.CREATE_METADATA_METHOD_NAME»();
				«ENDIF»
			}
			
			@Override
			public String getName() {
				return "«q.simpleName»";
			}
			
			public «Collection.simpleName»<«IQDLOperator.simpleName»> execute() {
				«Collection.simpleName»<«IQDLOperator.simpleName»> operators = new «ArrayList.simpleName»<>();
			 	«FOR source : typeDictionary.sources»
			 		«context.addImport(StreamAO.canonicalName)»
			 		«context.addImport(DefaultQDLSource.canonicalName)»			 		
			 		«DefaultQDLSource.simpleName» «source» = getSource«source.toLowerCase»(new «DefaultQDLSource.simpleName»("«source»"), operators);
				«ENDFOR»
			 	«FOR stmt : block.statements»
			 		«stmtCompiler.compile(stmt, context)»
				«ENDFOR»			 	
				return operators;
			}
			
			
			«FOR source : typeDictionary.sources»
				private «DefaultQDLSource.simpleName» getSource«source.toLowerCase»(«DefaultQDLSource.simpleName» type, «Collection.simpleName»<«IQDLOperator.simpleName»> operators) {
					operators.add(type);
					return type;
				}
			«ENDFOR»
			
			«FOR a : varStmts»
				«var decl = a.^var as IQLVariableDeclaration»
				«var type = decl.ref»
				«IF a.init != null && a.init.argsMap!= null && a.init.argsMap.elements.size>0»
					«createGetterMethod(type, a.init.argsMap, context)»
				«ELSEIF a.init != null && a.init.argsList!=null && (helper.isOperator(type))»	
					«createGetterMethod(type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
				
			«FOR e : newExpressions»
				«IF e.argsMap != null && e.argsMap.elements.size>0»
					«createGetterMethod(e.ref, e.argsMap, context)»
				«ELSEIF helper.isOperator(e.ref)»	
					«createGetterMethod(e.ref, e.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«IF metadata»
				«methodCompiler.compile(q.metadataList, context)»
			«ENDIF»
		} 
		'''			
	}
	
	override createGetterMethod(JvmTypeReference typeRef, IQLArgumentsMap map, IQDLGeneratorContext context) {
		if (helper.isOperator(typeRef)) {
			'''
			
			private «typeCompiler.compile(typeRef, context, false)» getOperator«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(«typeCompiler.compile(typeRef, context, false)» type, «Collection.simpleName»<«IQDLOperator.simpleName»> operators«IF map != null && map.elements.size > 0», «map.elements.map[ el | super.compile(el, typeRef, context)].join(", ")»«ENDIF») {
				operators.add(type);
				«IF map != null»
					«FOR el :map.elements»
						«var attrName = el.key.simpleName»
						«IF helper.isParameter(attrName, typeRef)»
							type.setParameter("«attrName»", «attrName»);
						«ELSE»	
							«IF el.key instanceof JvmOperation»
								type.«el.key.simpleName»(«el.key.simpleName»);				
							«ELSE»
								type.«el.key.simpleName» = «el.key.simpleName»;
							«ENDIF»			
						«ENDIF»
					«ENDFOR»
				«ENDIF»
				return type;
			}
			'''
		} else {
			super.createGetterMethod(typeRef, map, context);
		}		
	}
}
