package de.uniol.inf.is.odysseus.iql.odl.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.AbstractIQLCompiler
import de.uniol.inf.is.odysseus.iql.odl.generator.compiler.helper.ODLCompilerHelper
import javax.inject.Inject
import de.uniol.inf.is.odysseus.iql.odl.generator.ODLGeneratorContext
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLOperator
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter
import de.uniol.inf.is.odysseus.iql.odl.types.impl.useroperator.AbstractODLAORule
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLParameter
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeFactory
import de.uniol.inf.is.odysseus.iql.basic.types.IQLUtils
import de.uniol.inf.is.odysseus.iql.odl.oDL.ODLMethod
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory
import org.eclipse.xtext.common.types.JvmMember
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLTypeDefinition
import de.uniol.inf.is.odysseus.iql.odl.typing.ODLTypeUtils
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHasPredicates
import java.util.List
import java.util.ArrayList
import de.uniol.inf.is.odysseus.core.predicate.IPredicate
import java.util.Map
import java.util.HashMap
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLAO
import de.uniol.inf.is.odysseus.iql.odl.typing.eventmethods.EventMethodsFactory
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator
import de.uniol.inf.is.odysseus.iql.odl.types.useroperator.IODLPO
import de.uniol.inf.is.odysseus.iql.odl.lookup.ODLLookUp

class ODLCompiler extends AbstractIQLCompiler<ODLCompilerHelper, ODLGeneratorContext, ODLTypeCompiler, ODLStatementCompiler, ODLTypeFactory, ODLTypeUtils>{
	
	@Inject
	private ODLMetadataAnnotationCompiler metadataAnnotationCompiler
	
	@Inject
	private ODLLookUp lookUp;
	
	@Inject
	new(ODLCompilerHelper helper, ODLTypeCompiler typeCompiler, ODLStatementCompiler stmtCompiler, ODLTypeFactory factory, ODLTypeUtils typeUtils) {
		super(helper, typeCompiler, stmtCompiler, factory, typeUtils)
	}
	
	def String compileAO(ODLOperator o, ODLGeneratorContext context) {
		context.ao = true	
		var builder = new StringBuilder()
		builder.append(compileAOIntern(o, context))
		context.addImport(AbstractLogicalOperator.canonicalName)	
		context.addImport(LogicalOperatorCategory.canonicalName)	
		context.addImport(LogicalOperator.canonicalName)	
		context.addImport(Parameter.canonicalName)	

		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	def String compilePO(IQLTypeDefinition typeDef, ODLOperator o, ODLGeneratorContext context) {	
		var builder = new StringBuilder()
		builder.append(compilePOIntern(typeDef, o, context))
			
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
		for (String i : context.getImports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString
	}
	
	
	def String compileAOIntern(ODLOperator o, ODLGeneratorContext context) {	
		var opName = o.simpleName+ODLCompilerHelper.AO_OPERATOR
		var superClass = AbstractLogicalOperator.simpleName;
		var parameters = helper.getParameters(o);
		var newExpressions = helper.getNewExpressions(o);
		var varStmts = helper.getVarStatements(o);
		var odlmethods = helper.getODLMethods(o);
		var hasPredicate = helper.hasPredicate(o);
		var predicates = helper.getPredicates(o);
		var predicateArrays = helper.getPredicateArrays(o);
		var operatorValidate = helper.hasOperatorValidate(o);
		var parametersToValidate = new ArrayList();
		for (p : parameters) {
			if (helper.hasValidateMethod(o, p)) {
				parametersToValidate.add(p);
			}
		}
		context.addImport(List.canonicalName)
		context.addImport(ArrayList.canonicalName)
		context.addImport(Map.canonicalName)
		context.addImport(HashMap.canonicalName)
		context.addImport(IODLAO.canonicalName)

		if (hasPredicate) {
			context.addImport(IHasPredicates.canonicalName)			
		}
		'''
		
		@SuppressWarnings("all")
		@«LogicalOperator.simpleName»(«metadataAnnotationCompiler.getAOAnnotationElements(o, context)»)
		public class «opName» extends «superClass» implements «IODLAO.simpleName»«IF hasPredicate», «IHasPredicates.simpleName»«ENDIF» {
			«FOR p : parameters»
				«compile(p, context)»
			«ENDFOR»
			
			private «Map.simpleName»<«String.simpleName», «List.simpleName»<«Object.simpleName»>> metadata = new «HashMap.simpleName»<>();
			
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
				@«Parameter.simpleName»(«metadataAnnotationCompiler.getParameterAnnotationElements(p, context)»)
				public void set«pName»(«type» «p.simpleName») {
					this.«p.simpleName» = «p.simpleName»;
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
			
			«IF operatorValidate ||  parametersToValidate.size > 0»
			@Override
			public boolean isValid() {
				return super.isValid()«IF operatorValidate» && validate()«ENDIF»«IF parametersToValidate.size > 0» && validateParameters()«ENDIF»;
			}
			«ENDIF»
			
			«IF parametersToValidate.size > 0»
			protected boolean validateParameters() {
				return «parametersToValidate.map[p | "this.validate"+p.simpleName+"()"].join("&&")»;
			}
			«ENDIF»
			
			@Override
			public «superClass» clone() {
				return new «opName»(this);
			}
			
			«IF hasPredicate»
			@Override
			public «List.simpleName»<IPredicate<?>> getPredicates() {
				«List.simpleName» result = new «ArrayList.simpleName»<>();
				«FOR pred : predicates»
				result.add(this.«pred»);
				«ENDFOR»
				«FOR pred : predicateArrays»
				for («IPredicate.simpleName» p : «pred») {
					result.add(p);	
				}
				«ENDFOR»
				return result;
			}
			«ENDIF»
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
					«var decl = a.^var as IQLVariableDeclaration»
					«var type = decl.ref»
					«createGetterMethod(type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			@Override
			public «Map.simpleName»<«String.simpleName», «List.simpleName»<«Object.simpleName»>> getMetadata() {
				return metadata;
			}

			@Override
			public void addMetadata(«String.simpleName» key, «Object.simpleName» value) {
				«List.simpleName»<«Object.simpleName»> valueList = metadata.get(key);
				if (valueList == null) {
					valueList = new «ArrayList.simpleName»<>();
				}
				valueList.add(value);
			}
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
			public void execute(«aoName» operator, «transform» config) {
				defaultExecute(operator, new «poName»(operator), config, true, true);
			}
		}
		'''
	}

	def String compilePOIntern(IQLTypeDefinition typeDef, ODLOperator o, ODLGeneratorContext context) {
		var opName = o.simpleName+ODLCompilerHelper.PO_OPERATOR
		var aoName = o.simpleName+ODLCompilerHelper.AO_OPERATOR		
		var parameters = helper.getParameters(o)
		var attributes = helper.getAttributes(o)	
		var superClass = AbstractPipe	
		var read = helper.determineReadType(o)
		var write = read
		var meta = helper.determineMetadataType(o)
		
		var newExpressions = helper.getNewExpressions(o);
		var varStmts = helper.getVarStatements(o);
		var outputmode = helper.determineOutputMode(o);
		var hasInit = helper.hasInitMethod(o);
		var hasProcessNext = helper.hasProcessNext(o);
		var hasProcessPunctuation = helper.hasProcessPunctuation(o);
		
		context.addImport(superClass.canonicalName)
		context.addImport(outputmode.class.canonicalName)		
		context.addImport(IODLPO.canonicalName)		
		context.addImport(IPunctuation.canonicalName)		
		
		'''

		«FOR j : typeDef.javametadata»
		«var text = NodeModelUtils.getTokenText(NodeModelUtils.getNode(j.text))»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public class «opName» extends «superClass.simpleName»<«typeCompiler.compile(read, context, false)»<«typeCompiler.compile(meta, context, false)»>,«typeCompiler.compile(write, context, false)»<«typeCompiler.compile(meta, context, false)»>> implements «IODLPO.simpleName»<«typeCompiler.compile(read, context, false)»<«typeCompiler.compile(meta, context, false)»>,«typeCompiler.compile(write, context, false)»<«typeCompiler.compile(meta, context, false)»>> {
			
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
				«FOR p : parameters»
					«createCloneStatements(p, "ao", context)»
				«ENDFOR»
				«IF hasInit»
					init();
				«ENDIF»
			}
			
			@«Override.simpleName»
			public «OutputMode.simpleName» getOutputMode() {
				return «OutputMode.simpleName».«outputmode.toString»;
			}
			
			«FOR p : parameters»
				«var pName = helper.firstCharUpperCase(p.simpleName)»
				«var type = typeCompiler.compile(p.type, context, false)»
				public «type» get«pName»() {
					return this.«p.simpleName»;
				}
			«ENDFOR»
			
			«IF !hasProcessPunctuation»
			@Override
			public void processPunctuation(«IPunctuation.simpleName» punctuation, int port) {
				sendPunctuation(punctuation);
			}
			
			«ENDIF»
			
			«IF !hasProcessNext»
			@Override
			protected void process_next(«typeCompiler.compile(read, context, false)» object, int port) {
				transfer(object);
			}
			
			«ENDIF»
			
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
					«var decl = a.^var as IQLVariableDeclaration»
					«var type = decl.ref»
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
		if (m.validate && m.simpleName != null && context.isAo) {
			'''
			private boolean validate«m.simpleName»()
				«stmtCompiler.compile(m.body, context)»	
			
			'''	
		} else if (m.validate && m.simpleName == null && context.isAo) {
			'''
			protected boolean validate()
				«stmtCompiler.compile(m.body, context)»	
			
			'''	
		} else if (m.on && ((context.ao && EventMethodsFactory.getInstance.hasEventMethod(true, m.simpleName, m.parameters)) ||  (!context.ao && EventMethodsFactory.getInstance.hasEventMethod(false, m.simpleName, m.parameters)))){
			var className = helper.getClassName(m);
			var returnT = "";
			var eventMethod = EventMethodsFactory.getInstance.getEventMethod(context.ao, m.simpleName, m.parameters);
			if (m.returnType != null && !m.simpleName.equalsIgnoreCase(className)) {
				returnT = typeCompiler.compile(m.returnType, context, false)
			} else if (m.returnType == null && !m.simpleName.equalsIgnoreCase(className)) {
				returnT = "void"
			}
			'''
			«IF eventMethod.isOverride»
			@Override
			«ENDIF»
			public «returnT» «eventMethod.methodName»(«IF m.parameters != null»«m.parameters.map[p | compile(p, context)].join(", ")»«ENDIF»)
				«stmtCompiler.compile(m.body, context)»	
			
			'''	
		} 
	}
	
	def String createCloneStatements(ODLParameter p, String varName, ODLGeneratorContext context) {
		var name = p.simpleName
		var pName = helper.firstCharUpperCase(name)
		var type = p.type
		
		if (lookUp.isList(type)) {
			context.addImport(IQLUtils.canonicalName)			
			'''this.«name» = «IQLUtils.simpleName».createList(«varName».get«pName»());'''

		} else if (lookUp.isMap(type)) {
			context.addImport(IQLUtils.canonicalName)				
			'''this.«name» = «IQLUtils.simpleName».createMap(«varName».get«pName»());'''		
		} else if (lookUp.isClonable(type)) {
			'''this.«name» = «varName».get«pName»().clone();'''
		} else {
			'''this.«name» = «varName».get«pName»();'''
		}
	}	

}
