package de.uniol.inf.is.odysseus.iql.basic.generator.compiler

import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLClass
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLInterface
import de.uniol.inf.is.odysseus.iql.basic.generator.compiler.helper.IIQLCompilerHelper
import de.uniol.inf.is.odysseus.iql.basic.generator.context.IIQLGeneratorContext
import org.eclipse.xtext.common.types.JvmMember
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLAttribute
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethod
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLMethodDeclaration
import org.eclipse.xtext.common.types.JvmFormalParameter
import org.eclipse.xtext.common.types.JvmTypeReference
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMap
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLArgumentsMapKeyValue
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLJavaMember
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLVariableDeclaration
import de.uniol.inf.is.odysseus.iql.basic.typing.utils.IIQLTypeUtils
import de.uniol.inf.is.odysseus.iql.basic.basicIQL.IQLModelElement
import org.eclipse.xtext.common.types.JvmOperation
import de.uniol.inf.is.odysseus.iql.basic.typing.dictionary.IIQLTypeDictionary

abstract class AbstractIQLCompiler<H extends IIQLCompilerHelper, G extends IIQLGeneratorContext, T extends IIQLTypeCompiler<G>, S extends IIQLStatementCompiler<G>, F extends IIQLTypeDictionary, U extends IIQLTypeUtils> implements IIQLCompiler<G>{
	
	protected H helper;
	
	protected T typeCompiler;
	
	protected F typeDictionary;
		
	protected S stmtCompiler;
		
	protected U typeUtils;
		
	new (H helper, T typeCompiler, S stmtCompiler, F typeDictionary, U typeUtils) {
		this.helper = helper;
		this.typeCompiler = typeCompiler;
		this.stmtCompiler = stmtCompiler;	
		this.typeUtils = typeUtils;
		this.typeDictionary = typeDictionary;
	}
	
	override compile(IQLClass c, G context) {
		var builder = new StringBuilder()
		var element = c.eContainer as IQLModelElement;
		builder.append(compileClass(element, c, context))
		for (String i : context.imports) {
			builder.insert(0, "import "+ i+ ";"+System.lineSeparator)
		}
		builder.toString	
	}
	
	
	def String compileClass(IQLModelElement element, IQLClass c, G context) {
		var name = c.simpleName;
		var superClass = c.extendedClass;
		var interfaces = c.extendedInterfaces;
		var newExpressions = helper.getNewExpressions(c)
		var attributes = helper.getAttributes(c)
		var varStmts = helper.getVarStatements(c)
		
		'''
		«FOR j : element.javametadata»
		«var text = j.java.text»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public class «name»«IF superClass !== null» extends «typeCompiler.compile(superClass, context, true)»«ENDIF»«IF interfaces.size > 0» implements «interfaces.map[el | typeCompiler.compile(el, context, true)].join(",")»«ENDIF» {
			«FOR m : c.members»			
			«compile(m, context)»
			«ENDFOR»
			
			«FOR e : newExpressions»
				«IF e.argsMap !== null && e.argsMap.elements.size > 0»
					«createGetterMethod(e.ref, e.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : attributes»
				«IF a.init !== null && a.init.argsMap !== null && a.init.argsMap.elements.size>0»
					«createGetterMethod(a.type, a.init.argsMap, context)»
				«ENDIF»				
			«ENDFOR»
			
			«FOR a : varStmts»
				«IF a.init !== null && a.init.argsMap !== null && a.init.argsMap.elements.size>0»
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
		var element = interf.eContainer as IQLModelElement;		
		builder.append(compileInterface(element, interf, context))
		for (String i : context.imports) {
			builder.insert(0, "import "+ i+ ";")
		}
		builder.toString	
	}
	
	def String compileInterface(IQLModelElement element,IQLInterface i, G context) {
		var name = i.simpleName;
		var interfaces = i.extendedInterfaces;
		'''
		
		«FOR j : element.javametadata»
		«var text = j.java.text»
		«text»
		«ENDFOR»
		@SuppressWarnings("all")
		public interface «name»«IF interfaces.size > 0» extends «interfaces.map[el |typeCompiler.compile(el, context, true)].join(",")»«ENDIF» {
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
		} else if (m instanceof IQLMethodDeclaration) {
			compile(m as IQLMethodDeclaration, context);
		} else if (m instanceof IQLJavaMember) {
			compile(m as IQLJavaMember, context);
		} else {
			''''''
		}
	}
	
	def String compile(IQLJavaMember m, G context) {
		var text = m.java.text
		'''«text»'''
	}
	
	def String compile(IQLAttribute a, G context) {
		'''
		public «typeCompiler.compile(a.type, context, false)» «a.simpleName»«IF a.init !== null» = «stmtCompiler.compile(a.init, a.type, context)»«ENDIF»;
		
		'''
	}
	
	def String compile(IQLMethod m, G context) {
		var className = helper.getClassName(m);
		var returnT = "";
		if (m.returnType !== null && !m.simpleName.equalsIgnoreCase(className)) {
			returnT = typeCompiler.compile(m.returnType, context, false)
		} else if (m.returnType === null && !m.simpleName.equalsIgnoreCase(className)) {
			returnT = "void"
		}
		'''
		«IF m.isOverride»@Override«ENDIF»
		public «returnT» «m.simpleName»(«IF m.parameters !== null»«m.parameters.map[p | compile(p, context)].join(", ")»«ENDIF»)
		«stmtCompiler.compile(m.body, context)»
		
		'''	
	}
	
	def String compile(JvmFormalParameter p, G context) {
		'''«typeCompiler.compile(p.parameterType, context, false)» «p.name»'''	
	}
	
	
	def String compile(IQLMethodDeclaration m, G context) {
		'''
		public «typeCompiler.compile(m.returnType, context, false)» «m.simpleName»(«m.parameters.map[p | compile(p, context)].join(", ")»);
		
		'''
	}
	
	def createGetterMethod(JvmTypeReference typeRef, IQLArgumentsMap map, G context) {
		'''
		
		private «typeCompiler.compile(typeRef, context, false)» get«typeUtils.getShortName(typeRef, false)»«typeRef.hashCode»(«typeCompiler.compile(typeRef, context, false)» type, «map.elements.map[ el | compile(el, typeRef, context)].join(", ")») {
			«FOR el :map.elements»
				«IF el.key instanceof JvmOperation»
					type.«el.key.simpleName»(«el.key.simpleName»);				
				«ELSE»
					type.«el.key.simpleName» = «el.key.simpleName»;
				«ENDIF»
			«ENDFOR»
			return type;
		}
		'''	
	}
	
	def String compile(IQLArgumentsMapKeyValue e, JvmTypeReference typeRef, G context) {	
		var type = helper.getPropertyType(e.key, typeRef);
		if (type !== null) {
			'''«typeCompiler.compile(type, context, false)» «e.key.simpleName»'''			
		} else {
			''''''
		}
	}
	

}
