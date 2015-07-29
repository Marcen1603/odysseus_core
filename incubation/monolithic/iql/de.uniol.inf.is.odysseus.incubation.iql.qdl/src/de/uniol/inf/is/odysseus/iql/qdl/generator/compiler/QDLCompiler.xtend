package de.uniol.inf.is.odysseus.iql.qdl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler
import de.uniol.inf.is.odysseus.iql.qdl.generator.compiler.helper.QDLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.qdl.generator.QDLGeneratorContext
import de.uniol.inf.is.odysseus.iql.qdl.qDL.QDLQuery
import de.uniol.inf.is.odysseus.iql.qdl.types.impl.query.AbstractQDLQuery
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLStatementBlock
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.IIQLMetadataMethodCompiler
import java.util.Collection
import java.util.ArrayList
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
import de.uniol.inf.is.odysseus.iql.qdl.typing.QDLTypeFactory
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.iql.qdl.types.operator.IQDLOperator

class QDLCompiler extends AbstractIQLCompiler<QDLCompilerHelper, QDLGeneratorContext, QDLTypeCompiler, QDLStatementCompiler, QDLTypeFactory>{
	
	@Inject
	QDLMetadataMethodCompiler methodCompiler;
	
	@Inject
	new(QDLCompilerHelper helper, QDLTypeCompiler typeCompiler, QDLStatementCompiler stmtCompiler,QDLTypeFactory factory) {
		super(helper, typeCompiler, stmtCompiler, factory)
	}
	
	def String compile(QDLQuery q, QDLGeneratorContext context) {
		var builder = new StringBuilder()
		builder.append(compileQuery(q, context))
		context.addImport(AbstractQDLQuery.canonicalName)
		context.addImport(Collection.canonicalName)
		context.addImport(IQDLOperator.canonicalName)
		context.addImport(ArrayList.canonicalName)
		
		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	def String compileQuery(QDLQuery q, QDLGeneratorContext context) {
		var name = q.simpleName
		var superClass = AbstractQDLQuery.simpleName
		var metadata = q.metadataList != null
		var block = q.statements as IQLStatementBlock
		var newExpressions = helper.getNewExpressions(q);
		var varStmts = helper.getVarStatements(q);
		
		'''
		
		«FOR j : q.javametadata»
		«var text = NodeModelUtils.getTokenText(NodeModelUtils.getNode(j.text))»
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
			
			public «Collection.simpleName»<«IQDLOperator.simpleName»<?>> execute() {
			 	«Collection.simpleName»<«IQDLOperator.simpleName»<?>> operators = new «ArrayList.simpleName»<>();
			 	«FOR stmt : block.statements»
			 		«stmtCompiler.compile(stmt, context)»
				«ENDFOR»			 	
				return operators;
			}
			
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
	
	override createGetterMethod(JvmTypeReference typeRef, IQLArgumentsMap map, QDLGeneratorContext context) {
		if (helper.isOperator(typeRef)) {
			var opName = helper.getLogicalOperatorName(typeRef)
			context.addImport(factory.getLogicalOperator(factory.getShortName(typeRef, false)).canonicalName)
			'''
			
			private «typeCompiler.compile(typeRef, context, false)» getOperator«factory.getShortName(typeRef, false)»«typeRef.hashCode»(«typeCompiler.compile(typeRef, context, false)»<«opName»> type, «Collection.simpleName»<«IQDLOperator.simpleName»<?>> operators«IF map != null && map.elements.size > 0», «map.elements.map[ el | super.compile(el, typeRef, context)].join(", ")»«ENDIF») {
				operators.add(type);
				«IF map != null»
					«FOR el :map.elements»
						«var attrName = el.key»
						«IF helper.isParameter(attrName, typeRef)»
							type.setParameter("«attrName»", «attrName»);
						«ELSE»	
							«var type = helper.getPropertyType(el.key, typeRef)»
							«IF type !=null && helper.isSetter(el.key, typeRef, type)»
								«var methodName = helper.getMethodName("set"+el.key, typeRef)»
								type.«methodName»(«el.key»);
							«ELSE»
								type.«el.key» = «el.key»;
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
