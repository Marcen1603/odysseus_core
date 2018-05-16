package de.uniol.inf.is.odysseus.parser.cql2.test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilder;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilderException;

public class PQLOperatorBuilderTest {

	private PQLOperatorBuilder builder = new PQLOperatorBuilder();

	@Test
	public void test_buildSenderAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("wrapper", "WRAPPER");
		args.put("protocol", "PROTOCOL");
		args.put("datahandler", "DATAHANDLER");
		args.put("transport", "TRANSPORT");
		args.put("options", "['OPTION1', 'OPTION2']");
		args.put("input", "INPUT");
		args.put("sink", "SINKNAME");

		String expected = "Sender({datahandler='DATAHANDLER',options=[['OPTION1','OPTION2']],protocol='PROTOCOL',sink='SINKNAME',transport='TRANSPORT',wrapper='WRAPPER'},INPUT)";
		String actual;
		try {
			actual = builder.build(SenderAO.class, args);
			Assert.assertEquals(expected, actual);
		} catch (PQLOperatorBuilderException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_buildAccessAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("wrapper", "WRAPPER");
		args.put("protocol", "PROTOCOL");
		args.put("datahandler", "DATAHANDLER");
		args.put("transport", "TRANSPORT");
		args.put("source", "SOURCENAME");
		args.put("schema", "[s1,Integer]");
		args.put("options", "['OPTION1', 'OPTION2']");

		String expected = "ACCESS({datahandler='DATAHANDLER',options=[['OPTION1','OPTION2']],protocol='PROTOCOL',schema=[[s1,Integer]],source='SOURCENAME',transport='TRANSPORT',wrapper='WRAPPER'})";
		String actual;
		try {
			actual = builder.build(AccessAO.class, args);
			Assert.assertEquals(expected, actual);
		} catch (PQLOperatorBuilderException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_buildMapAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("expressions", "e1,e2,e3,e4,e5");
		args.put("input", "INPUT");

		String expected = "MAP({expressions=[e1,e2,e3,e4,e5]},INPUT)";
		String actual;
		try {
			actual = builder.build(MapAO.class, args);
			Assert.assertEquals(expected, actual);
		} catch (PQLOperatorBuilderException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_buildRenameAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("aliases", "'alias1', 'var1','alias2', 'var2','alias3', 'var3'");
		args.put("pairs", "true");
		args.put("input", "INPUT");

		String expected = "RENAME({aliases=['alias1','var1','alias2','var2','alias3','var3'],pairs='true'},INPUT)";
		String actual;
		try {
			// TODO remove this after debugging
//			for (int i = 0; i < 100000; i ++) {
				actual = builder.build(RenameAO.class, args);
				Assert.assertEquals(expected, actual);
//			}
		} catch (PQLOperatorBuilderException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_buildSelectAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("predicate", "100 > attr1");
		args.put("input", "INPUT");

		String expected = "SELECT({predicate='100>attr1'},INPUT)";
		String actual;
		try {
			actual = builder.build(SelectAO.class, args);
			Assert.assertEquals(expected, actual);
		} catch (PQLOperatorBuilderException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_build_WindowAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("advance", "1");
		args.put("size", "5");

		args.put("input", "INPUT");

		String expected = "TIMEWINDOW({advance=[1],size=[5]},INPUT)";
		String actual;
		try {
			actual = builder.build(TimeWindowAO.class, args);
		} catch (PQLOperatorBuilderException e) {
			e.printStackTrace();
		}
	}

}
