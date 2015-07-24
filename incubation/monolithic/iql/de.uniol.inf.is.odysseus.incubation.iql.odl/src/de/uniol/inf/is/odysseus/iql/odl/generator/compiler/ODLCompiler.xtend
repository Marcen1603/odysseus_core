package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLAO
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLAORule
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLPO
import de.uniol.inf.is.odysseus.core.collection.Tuple
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils
import java.util.Map.Entry
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory
import org.eclipse.xtext.common.types.JvmMember
import org.eclipse.xtext.nodemodel.util.NodeModelUtils

class ODLCompiler extends AbstractIQLCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLStatementCompiler, ODLTypeFactory>{
	
	@Inject
	private ODLMetadataAnnotationCompiler metadataAnnotationCompiler

	
	@Inject
	new(ODLCompilerHelper helper, ODLTypeCompiler typeCompiler, ODLStatementCompiler stmtCompiler, ODLTypeFactory factory) {
		super(helper, typeCompiler, stmtCompiler, factory)
	}
	
	def String compileAO(ODLOperator o, ODLGeneratorContext context) {
		context.ao = true	
		var builder = new StringBuilder()
		builder.append(compileAOIntern(o, context))
		context.addImport(AbstractODLAO.canonicalName)	
		context.addImport(LogicalOperatorCategory.canonicalName)	
		context.addImport(LogicalOperator.canonicalName)	
		context.addImport(Parameter.canonicalName)	

		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	def String compilePO(ODLOperator o, ODLGeneratorContext context) {	
		var builder = new StringBuilder()
		builder.append(compilePOIntern(o, context))
		context.addImport(AbstractODLPO.canonicalName)
		context.addImport(Tuple.canonicalName)
		context.addImport(IMetaAttribute.canonicalName)
			
		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	def String compileAORule(ODLOperator o, ODLGeneratorContext context) {	
		var builder = new StringBuilder()
		builder.append(compileAORuleIntern(o, context))
		context.addImport(AbstractODLAORule.canonicalName)	
		context.addImport(TransformationConfiguration.canonicalName)	
		context.addImport(RuleException.canonicalName)	
		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	
	def String compileAOIntern(ODLOperator o, ODLGeneratorContext context) {	
		var opName = o.simpleName+ODLCompilerHelper.AO_OPERATOR
		var superClass = AbstractODLAO.simpleName;
		var parameters = helper.getParameters(o);
		var newExpressions = helper.getNewExpressions(o);
		var varStmts = helper.getVarStatements(o);
		var odlmethods = helper.getODLMethods(o);
		
		'''
		
		@SuppressWarnings("all")
		@«LogicalOperator.simpleName»(«metadataAnnotationCompiler.getAOAnnotationElements(o, context)»)
		public class «opName» extends «superClass» {
			«FOR p : parameters»
				«compile(p, context)»
			«ENDFOR»
			
			public «opName»() {
				super();
			}
			
			public «opName»(«opName» ao) {
				super(ao);
				«FOR p : parameters»
					«createCloneStatements(p, "ao", context)»
				«ENDFOR»
			}
			
			«FOR p : parameters»
				«var pName = helper.firstCharUpperCase(p.simpleName)»
				«var type = typeCompiler.compile(p.type, context, false)»
				«var validate = helper.hasValidateMethod(o, p)»
				@«Parameter.simpleName»(«metadataAnnotationCompiler.getParameterAnnotationElements(p, context)»)
				public void set«pName»(«type» «p.simpleName») {
					this.«p.simpleName» = «p.simpleName»;
					«IF validate»
					this.validate«p.simpleName»();
					«ENDIF»
				}
			«ENDFOR»
			
			«FOR p : parameters»
				«var pName = helper.firstCharUpperCase(p.simpleName)»
				«var type = typeCompiler.compile(p.type, context, false)»
				public «type» get«pName»() {
					return this.«p.simpleName»;
				}
			«ENDFOR»
			
			«FOR m : odlmethods»			
			«compile(m, context)»
			«ENDFOR»
			
			@Override
			public «superClass» clone() {
				return new «opName»(this);
			}
			
			«FOR e : newExpressions»
				«IF e.argsMap != null && e.argsMap.elements.size > 0»
					«createGetterMethod(e.ref, e.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : parameters»
				«IF a.init != null && a.init.argsMap != null && a.init.argsMap.elements.size>0»
					«createGetterMethod(a.type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : varStmts»
				«IF a.init != null && a.init.argsMap != null && a.init.argsMap.elements.size>0»
					«var type = a.^var.ref»
					«IF type == null»
					«type = a.^var.parameterType»
					«ENDIF»
					«createGetterMethod(type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
		}
		'''
	}	
	
	def String compileAORuleIntern(ODLOperator o, ODLGeneratorContext context) {	
		var name = o.simpleName+ODLCompilerHelper.AO_RULE_OPERATOR
		var superClass = AbstractODLAORule.simpleName;
		var aoName = o.simpleName+ODLCompilerHelper.AO_OPERATOR
		var poName = o.simpleName+ODLCompilerHelper.PO_OPERATOR		
		var transform = TransformationConfiguration.simpleName
		'''
		
		@SuppressWarnings("all")
		public class «name» extends «superClass»<«aoName»> {
			
			@Override
			public void execute(«aoName» operator, «transform» config) throws «RuleException.simpleName» {
				defaultExecute(operator, new «poName»(operator), config, true, true);
			}
		}
		'''
	}

	def String compilePOIntern(ODLOperator o, ODLGeneratorContext context) {
		var opName = o.simpleName+ODLCompilerHelper.PO_OPERATOR
		var aoName = o.simpleName+ODLCompilerHelper.AO_OPERATOR		
		var superClass = AbstractODLPO.simpleName
		var parameters = helper.getParameters(o)
		var attributes = helper.getAttributes(o)		
		var read = Tuple.simpleName
		var write = Tuple.simpleName
		var meta = IMetaAttribute.simpleName
		var newExpressions = helper.getNewExpressions(o);
		var varStmts = helper.getVarStatements(o);
	
		'''

		«FOR j : o.javametadata»
		«var text = NodeModelUtils.getTokenText(NodeModelUtils.getNode(j.text))»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public class «opName» extends «superClass»<«read»<«meta»>,«write»<«meta»>> {
			
			«FOR m : o.members»			
			«compile(m, context)»
			«ENDFOR»
			
			public «opName»() {
				super();
			}
			
			public «opName»(«opName» po) {
				super(po);
				«FOR p : parameters»
					«createCloneStatements(p, "po", context)»
				«ENDFOR»
			}
			
			public «opName»(«aoName» ao) {
				super(ao);
				«FOR p : parameters»
					«createCloneStatements(p, "ao", context)»
				«ENDFOR»
			}
			
			«FOR p : parameters»
				«var pName = helper.firstCharUpperCase(p.simpleName)»
				«var type = typeCompiler.compile(p.type, context, false)»
				public «type» get«pName»() {
					return this.«p.simpleName»;
				}
			«ENDFOR»
			
			«FOR e : newExpressions»
				«IF e.argsMap != null && e.argsMap.elements.size > 0»
					«createGetterMethod(e.ref, e.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : attributes»
				«IF a.init != null && a.init.argsMap != null && a.init.argsMap.elements.size>0»
					«createGetterMethod(a.type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
		
			«FOR a : varStmts»
				«IF a.init != null && a.init.argsMap != null && a.init.argsMap.elements.size>0»
					«var type = a.^var.ref»
					«IF type == null»
					«type = a.^var.parameterType»
					«ENDIF»
					«createGetterMethod(type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
		}
		'''
	}	
	
	override String compile(JvmMember m, ODLGeneratorContext context) {
		if (context.ao && m instanceof ODLParameter) {
			compile(m as ODLParameter, context);
		} else if (m instanceof ODLMethod) {
			compile(m as ODLMethod, context);			
		} else {
			super.compile(m, context);
		}
	}
	
	def String compile(ODLParameter a, ODLGeneratorContext context) {
		'''
		private «typeCompiler.compile(a.type, context, false)» «a.simpleName»«IF a.init != null» = «stmtCompiler.compile(a.init, a.type, context)»«ENDIF»;
		
		'''
	}

	
	def String compile(ODLMethod m,ODLGeneratorContext context) {
		if (m.validate && context.isAo) {
			'''
			private void validate«m.simpleName»()
				«stmtCompiler.compile(m.body, context)»	
			'''	
		} else if (!context.ao && m.on){
			var className = helper.getClassName(m);
			var returnT = "";
			if (m.returnType == null && !m.simpleName.equalsIgnoreCase(className)) {
				returnT = typeCompiler.compile(m.returnType, context, false)
			}
			'''
			@Override
			public «returnT» on«helper.firstCharUpperCase(m.simpleName)»(«IF m.parameters != null»«m.parameters.map[p | compile(p, context)].join(", ")»«ENDIF»)
				«stmtCompiler.compile(m.body, context)»	
			'''	
		} else if (!context.ao) {
			super.compile(m, context);
		}
	}
	
	def String createCloneStatements(ODLParameter p, String varName, ODLGeneratorContext context) {
		var name = p.simpleName
		var pName = helper.firstCharUpperCase(name)
		var type = p.type
		
		if (factory.isList(type)) {
			var listElement = factory.getListElementType(p)
			context.addImport(IQLUtils.canonicalName)			
			if(factory.isClonable(listElement)) {				
				'''
				this.«name» = «IQLUtils.simpleName».createEmptyList();
				for («typeCompiler.compile(listElement, context, false)» e : «varName».get«pName»()) {
					this.«name».add(e.clone()); 
				}
				'''
			} else {
				'''this.«name» = «IQLUtils.simpleName».createList(«varName».get«pName»());'''
			}
		} else if (factory.isMap(type)) {
			var key = factory.getMapKeyType(p)
			var value = factory.getMapValueType(p)
			context.addImport(IQLUtils.canonicalName)				
			if  (factory.isClonable(key) || factory.isClonable(value)) {
				context.addImport(Entry.canonicalName)				
				'''
				this.«name» = «IQLUtils.simpleName».createEmptyMap();
				for («Entry.canonicalName»<«typeCompiler.compile(key,context, true)»,«typeCompiler.compile(value,context, true)»> e : «varName».get«pName»().entrySet()) {
					this.«name».put(e.getKey()«IF factory.isClonable(key)».clone()«ENDIF»,e.getValue()«IF factory.isClonable(value)».clone()«ENDIF»); 
				}
				'''
			} else {
				'''this.«name» = «IQLUtils.simpleName».createMap(«varName».get«pName»());'''
			}			
		} else if (factory.isClonable(type)) {
			'''this.«name» = «varName».get«pName»().clone();'''
		} else {
			'''this.«name» = «varName».get«pName»();'''
		}
	}	

}
