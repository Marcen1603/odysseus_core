package de.uniol.inf.is.odysseus.wrapper.dds.idl;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;

import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.Const_declContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.Const_typeContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.DeclaratorContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.MemberContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.Simple_type_specContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.Struct_typeContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.Template_type_specContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.IDLParser.Type_declContext;

public class IDLTranslator extends IDLBaseListener {

	final Map<String, String> constantValues = new HashMap<>();
	final Map<String, String> constantTypes = new HashMap<>();
	private String structName;
	private String memberName;
	private String memberType;
	private int memberSize;

	private void addConstant(String type, String name, String value) {
		constantValues.put(name, value);
		constantTypes.put(name, type);
	}

	@Override
	public void enterConst_decl(Const_declContext ctx) {
		ParseTree child = ctx.getChild(1);
		String type = child.getText();
		child = ctx.getChild(2);
		String name = child.getText();
		child = ctx.getChild(4);
		String value = child.getText();
		addConstant(type, name, value);
	}

	@Override
	public void enterStruct_type(Struct_typeContext ctx) {
		ParseTree child = ctx.getChild(1);
		structName = child.getText();
		System.out.println("Found new struct " + structName);
	}
	
	@Override
	public void enterTemplate_type_spec(Template_type_specContext ctx) {
		ParseTree c = ctx.getChild(0);
		memberType = c.getChild(0).getText();
		if ("sequence".equalsIgnoreCase(memberType)) {
			System.err.println("Found sequence");
		} else {
			String size = c.getChild(2).getText();
			// Test if constant
			String val = constantValues.get(size);
			if (val == null) {
				val = size;
			}
			memberSize = Integer.parseInt(val);
		}
	}

	@Override
	public void enterSimple_type_spec(Simple_type_specContext ctx) {
		memberType = ctx.getChild(0).getText();
	}

	public static void dumpTree(ParseTree c) {
		for (int i=0;i<c.getChildCount();i++){
			System.out.println(c.getChild(i).getText());
		}
	}
	
	@Override
	public void enterMember(MemberContext ctx) {
		memberName = null;
		memberType = null;
		memberSize = -1;
	}

	@Override
	public void enterDeclarator(DeclaratorContext ctx) {
		memberName = ctx.getText();
	}

	@Override
	public void exitMember(MemberContext ctx) {
		System.out.println("Found member " + memberName + " with type "
				+ memberType + " " + memberSize);
	}

}
