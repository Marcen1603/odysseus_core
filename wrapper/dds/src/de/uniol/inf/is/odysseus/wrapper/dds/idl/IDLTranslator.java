package de.uniol.inf.is.odysseus.wrapper.dds.idl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rti.dds.typecode.TypeCode;

import de.uniol.inf.is.odysseus.wrapper.dds.dds.TypeCodeMapper;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLBaseListener;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLLexer;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser.Const_declContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser.DeclaratorContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser.Enum_typeContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser.ModuleContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser.Simple_type_specContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser.Struct_typeContext;
import de.uniol.inf.is.odysseus.wrapper.dds.idl.generated.IDLParser.Type_declaratorContext;

public class IDLTranslator extends IDLBaseListener {

	private static final Logger LOG = LoggerFactory.getLogger(IDLTranslator.class);

	final Map<String, String> constantValues = new HashMap<>();
	final Map<String, String> constantTypes = new HashMap<>();
	private String structName;
	final private List<String> memberName = new ArrayList<>();
	final private List<TypeCode> memberType = new ArrayList<>();
	final private List<String> isKey = new ArrayList<>();

	final private Map<String, Set<String>> keyMap = new HashMap<String, Set<String>>();
	final private String idlFileName;
	private String currentModule;

	public IDLTranslator(String idlFileName) {
		this.idlFileName = idlFileName;
	}

	private void addConstant(String type, String name, String value) {
		constantValues.put(name, value);
		constantTypes.put(name, type);
	}

	public void putKey(String type, Set<String> keyAttributes) {
		Set<String> keys = keyMap.get(type);
		if (keys != null) {
			keys.addAll(keyAttributes);
		} else {
			keyMap.put(type, keyAttributes);
		}
	}

	private void addKey(String type, String attribute) {
		Set<String> keys = keyMap.get(type);
		if (keys == null) {
			keys = new TreeSet<>();
			keyMap.put(type, keys);
		}
		keys.add(attribute);
	}

	@Override
	public void enterType_declarator(Type_declaratorContext ctx) {
		// Type definition
		ParseTree typeDef = ctx.getChild(0);
		String type = typeDef.getText();

		String name = currentModule + "::" + ctx.getChild(1).getText();
		LOG.trace("Found new typedef " + type + " " + name);
		TypeCode tc = TypeCodeMapper.getOrCreateTypeCode(currentModule, type);
		TypeCodeMapper.createTypeAlias(name, tc);

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
		structName = currentModule + "::" + child.getText();
		memberName.clear();
		memberType.clear();
		isKey.clear();
	}

	@Override
	public void exitStruct_type(Struct_typeContext ctx) {
		LOG.trace("Found new struct " + structName + " " + memberName + " " + memberType + " " + isKey);
		TypeCodeMapper.createComplexType(structName, memberName, memberType, isKey);
	}

	@Override
	public void enterEnum_type(Enum_typeContext ctx) {
		String name = ctx.getChild(1).getText();
		List<String> values = new LinkedList<String>();
		for (int i = 3; i < ctx.getChildCount(); i = i + 2) {
			values.add(ctx.getChild(i).getText());
		}
		TypeCodeMapper.createEnumType(name, values);
	}

	@Override
	public void enterSimple_type_spec(Simple_type_specContext ctx) {
		memberType.add(TypeCodeMapper.getOrCreateTypeCode(currentModule, ctx.getChild(0).getText()));
	}

	public static void dumpTree(ParseTree c) {
		for (int i = 0; i < c.getChildCount(); i++) {
			System.out.println(c.getChild(i).getText());
		}
	}

	@Override
	public void enterDeclarator(DeclaratorContext ctx) {
		String name = ctx.getText();
		memberName.add(name);
		if (structName != null) {
			String toSearch;
			String[] split = structName.split("::");
			if (split.length > 1) {
				toSearch = split[1];
			} else {
				toSearch = split[0];
			}
			Set<String> keys = keyMap.get(toSearch);
			if (keys != null) {
				if (keys.contains(name)) {
					isKey.add(name);
				}
			}
		}

	}

	@Override
	public void enterModule(ModuleContext ctx) {
		currentModule = ctx.getChild(1).getText();
	}

	public void processIDLFile() throws FileNotFoundException {
		BufferedReader input = new BufferedReader(new FileReader(new File(idlFileName)));

		// read line wise and remove #pragma
		StringBuffer idlFile = new StringBuffer();
		String line;
		try {
			String struct = "";
			while ((line = input.readLine()) != null) {
				line = line.trim();
				if (line.contains("#pragma")) {
					String[] tokens = line.split(" ");
					if (tokens.length > 2) {
						if (tokens[0].equalsIgnoreCase("#pragma") && tokens[1].equalsIgnoreCase("keylist")) {
							Set<String> keyAttributes = new TreeSet<>();
							for (int i = 3; i < tokens.length; i++) {
								keyAttributes.add(tokens[i]);
							}
							putKey(tokens[2], keyAttributes);
						}
					}
				} else {
					// try to detect attributes with //@key annotation
					if (line.contains("struct")) {
						String[] tokens = line.split(" ");
						struct = tokens[1];
					}
					if (struct != "" && line.contains("//@key")) {
						StringTokenizer tokenizer = new StringTokenizer(line, " ;");

						String lastToken = "";
						while (tokenizer.hasMoreTokens()) {
							String token = tokenizer.nextToken();
							if (token.equals("//@key")) {
								addKey(struct, lastToken);
							}
							lastToken = token;
						}
					}
					idlFile.append(line).append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ANTLRInputStream in = new ANTLRInputStream(idlFile.toString());
		IDLLexer lexer = new IDLLexer(in);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		IDLParser parser = new IDLParser(tokens);

		ParseTree tree = parser.module();

		ParseTreeWalker walker = new ParseTreeWalker();
		walker.walk(this, tree);
	}

}
