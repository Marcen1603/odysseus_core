package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import org.eclipse.xtext.common.types.JvmMember
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclarationMember
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue
import de.uniol.inf.is.odysseus.iql.basic.typing.factory.IIQLTypeFactory
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember
import org.eclipse.xtext.nodemodel.util.NodeModelUtils
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration

abstract class AbstractIQLCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, S extends IIQLStatementCompiler<G>, F extends IIQLTypeFactory> implements IIQLCompiler<G>{
	
	protected H helper;
	
	protected T typeCompiler;
		
	protected S stmtCompiler;
		
	protected F factory;
		
	new (H helper, T typeCompiler, S stmtCompiler, F factory) {
		this.helper = helper;
		this.typeCompiler = typeCompiler;
		this.stmtCompiler = stmtCompiler;	
		this.factory = factory;
	}
	
	override compile(IQLClass c, G context) {
		var builder = new StringBuilder()
		builder.append(compileClass(c, context))
		for (String i : context.imports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString	
	}
	
	
	def String compileClass(IQLClass c, G context) {
		var name = c.simpleName;
		var superClass = c.extendedClass;
		var interfaces = c.extendedInterfaces;
		var newExpressions = helper.getNewExpressions(c)
		var attributes = helper.getAttributes(c)
		var varStmts = helper.getVarStatements(c)
		
		'''
		«FOR j : c.javametadata»
		«var text = NodeModelUtils.getTokenText(NodeModelUtils.getNode(j.text))»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public class «name»«IF superClass != null» extends «typeCompiler.compile(superClass, context, true)»«ENDIF»«IF interfaces.size > 0» implements «interfaces.map[el | typeCompiler.compile(el, context, true)]»«ENDIF» {
			«FOR m : c.members»			
			«compile(m, context)»
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
					«var decl = a.^var as IQLVariableDeclaration»
					«var type = decl.ref»
					«createGetterMethod(type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
		}
		'''
	}
	
	override compile(IQLInterface interf, G context) {
		var builder = new StringBuilder()
		builder.append(compileInterface(interf, context))
		for (String i : context.imports) {
			builder.insert(0, "import "+ i+ ";")
		}
		builder.toString	
	}
	
	def String compileInterface(IQLInterface i, G context) {
		var name = i.simpleName;
		var interfaces = i.extendedInterfaces;
		'''
		
		«FOR j : i.javametadata»
		«var text = NodeModelUtils.getTokenText(NodeModelUtils.getNode(j.text))»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public interface «name»«IF interfaces.size > 0» extends «interfaces.map[el |typeCompiler.compile(el, context, true)]»«ENDIF» {
			«FOR m : i.members»			
			«compile(m, context)»
			«ENDFOR»
		}
		'''
	}
	
	def String compile(JvmMember m, G context) {
		if (m instanceof IQLAttribute) {
			compile(m as IQLAttribute, context);
		} else if (m instanceof IQLMethod) {
			compile(m as IQLMethod, context);			
		} else if (m instanceof IQLMethodDeclarationMember) {
			compile(m as IQLMethodDeclarationMember, context);
		} else if (m instanceof IQLJavaMember) {
			compile(m as IQLJavaMember, context);
		} else {
			''''''
		}
	}
	
	def String compile(IQLJavaMember m, G context) {
		var text = NodeModelUtils.getTokenText(NodeModelUtils.getNode(m.text))
		'''«text»'''
	}
	
	def String compile(IQLAttribute a, G context) {
		'''
		public «typeCompiler.compile(a.type, context, false)» «a.simpleName»«IF a.init != null» = «stmtCompiler.compile(a.init, a.type, context)»«ENDIF»;
		
		'''
	}
	
	def String compile(IQLMethod m, G context) {
		var className = helper.getClassName(m);
		var returnT = "";
		if (m.returnType == null && !m.simpleName.equalsIgnoreCase(className)) {
			returnT = typeCompiler.compile(m.returnType, context, false)
		}
		'''
		public «returnT» «m.simpleName»(«IF m.parameters != null»«m.parameters.map[p | compile(p, context)].join(", ")»«ENDIF»)
		«stmtCompiler.compile(m.body, context)»
		
		'''	
	}
	
	def String compile(JvmFormalParameter p, G context) {
		'''«typeCompiler.compile(p.parameterType, context, false)» «p.name»'''	
	}
	
	
	def String compile(IQLMethodDeclarationMember m, G context) {
		'''
		public «typeCompiler.compile(m.returnType, context, false)» «m.simpleName»(«m.parameters.map[p | compile(p, context)].join(", ")»);
		
		'''
	}
	
	def createGetterMethod(JvmTypeReference typeRef, IQLArgumentsMap map, G context) {
		'''
		
		private «typeCompiler.compile(typeRef, context, false)» get«factory.getShortName(typeRef, false)»«typeRef.hashCode»(«typeCompiler.compile(typeRef, context, false)» type, «map.elements.map[ el | compile(el, typeRef, context)].join(", ")») {
			«FOR el :map.elements»
				«var type = helper.getPropertyType(el.key, typeRef)»
				«IF type !=null && helper.isSetter(el.key, typeRef, type)»
					«var methodName = helper.getMethodName("set"+el.key, typeRef)»				
					type.«methodName»(«el.key»);
				«ELSE»
					type.«el.key» = «el.key»;
				«ENDIF»
			«ENDFOR»
			return type;
		}
		'''	
	}
	
	def String compile(IQLArgumentsMapKeyValue e, JvmTypeReference typeRef, G context) {	
		var type = helper.getPropertyType(e.key, typeRef);
		if (type != null) {
			'''«typeCompiler.compile(type, context, false)» «e.key»'''			
		} else {
			''''''
		}
	}
	

}
