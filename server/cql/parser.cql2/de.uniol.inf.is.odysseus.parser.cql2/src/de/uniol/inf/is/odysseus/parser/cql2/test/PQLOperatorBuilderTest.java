package de.uniol.inf.is.odysseus.parser.cql2.test;

import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.parser.cql2.generator.builder.PQLOperatorBuilder;

public class PQLOperatorBuilderTest {

	private PQLOperatorBuilder builder = new PQLOperatorBuilder();

	@Test
	public void test_buildSenderAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("wrapper", "WRAPPER");
		args.put("protocol", "PROTOCOL");
		args.put("datahandler", "DATAHANDLER");
		args.put("transport", "TRANSPORT");
		args.put("options", "OPTION1, OPTION2, OPTION3");
		args.put("input", "INPUT");
		args.put("sink", "SINKNAME");

		String expected = "Sender({options=[OPTION1, OPTION2, OPTION3],wrapper='WRAPPER',datahandler='DATAHANDLER',protocol='PROTOCOL',transport='TRANSPORT',sink='SINKNAME'},INPUT)";
		String actual = builder.build(SenderAO.class, args);
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void test_buildAccessAO_successful() {
		Map<String, String> args = new HashMap<>();
		args.put("wrapper", "WRAPPER");
		args.put("protocol", "PROTOCOL");
		args.put("datahandler", "DATAHANDLER");
		args.put("transport", "TRANSPORT");
		args.put("source", "SOURCENAME");
		args.put("options", "OPTION1, OPTION2, OPTION3");

		String expected = "ACCESS({wrapper='WRAPPER',datahandler='DATAHANDLER',protocol='PROTOCOL',transport='TRANSPORT',source='SOURCENAME',options=[OPTION1, OPTION2, OPTION3]})";
		String actual = builder.build(AccessAO.class, args);
		Assert.assertEquals(expected, actual);
	}
	
}
