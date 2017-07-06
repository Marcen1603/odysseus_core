package de.uniol.inf.is.odysseus.server.opcua.core;

import static de.uniol.inf.is.odysseus.opcua.common.utilities.ConfigUtils.toOptMap;
import static de.uniol.inf.is.odysseus.server.opcua.util.TestUtil.loadProps;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.Identifiers;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.LocalizedText;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.QualifiedName;
import com.inductiveautomation.opcua.stack.core.types.builtin.Variant;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UByte;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.inductiveautomation.opcua.stack.core.types.enumerated.BrowseDirection;
import com.inductiveautomation.opcua.stack.core.types.enumerated.NodeClass;
import com.inductiveautomation.opcua.stack.core.types.enumerated.ServerState;
import com.inductiveautomation.opcua.stack.core.types.structured.ServerStatusDataType;
import com.xafero.parau.core.DS;
import com.xafero.parau.core.Loader;
import com.xafero.parau.model.Component;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.core.sdk.enums.ValueRanks;
import com.xafero.turjumaan.server.sdk.api.INodeModel;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;
import de.uniol.inf.is.odysseus.core.mep.IMepExpression;
import de.uniol.inf.is.odysseus.core.mep.IMepFunction;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilderFactory;
import de.uniol.inf.is.odysseus.mep.AbstractExpression;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.security.provider.ISecurityProvider;
import de.uniol.inf.is.odysseus.server.opcua.TestActivator;
import de.uniol.inf.is.odysseus.server.opcua.doc.Colors;
import de.uniol.inf.is.odysseus.server.opcua.doc.Customizers;
import de.uniol.inf.is.odysseus.server.opcua.doc.Customizers.IEdgeCustomizer;
import de.uniol.inf.is.odysseus.server.opcua.doc.GraphBuilder;
import de.uniol.inf.is.odysseus.server.opcua.doc.GraphKind;
import de.uniol.inf.is.odysseus.server.opcua.doc.Layout;
import de.uniol.inf.is.odysseus.server.opcua.util.FluentMap;
import de.uniol.inf.is.odysseus.server.opcua.util.OsgiUtil;
import de.uniol.inf.is.odysseus.server.opcua.util.TestUtil;

public class CoreTest {

	private static final String endpoint = "opc.tcp://127.0.0.1:51510/UA/DemoServer";
	private static final String node1 = "ns=3;i=11022";
	private static final String node2 = "ns=3;i=11021";
	private static final String nodes = node1 + " | " + node2;

	private static final Logger log = LoggerFactory.getLogger(CoreTest.class);

	private static Deque<Component> cmps = new ArrayDeque<Component>();

	private static Bundle bundleC;
	private static Bundle bundleW;
	private static Bundle bundleJ;
	private static Bundle bundleS;

	private static DS dsC;
	private static DS dsW;
	private static DS dsS;

	@BeforeClass
	public static void setUpClass() throws Exception {
		BundleContext ctx = TestActivator.getContext();
		// Get and start common
		String symNameC = "de.uniol.inf.is.odysseus.opcua.common";
		bundleC = OsgiUtil.find(ctx.getBundles(), symNameC);
		bundleC.start();
		dsC = bindAllComponents(bundleC, "common/opcua.common");
		// Get and start client
		String symNameW = "de.uniol.inf.is.odysseus.wrapper.opcua";
		bundleW = OsgiUtil.find(ctx.getBundles(), symNameW);
		bundleW.start();
		dsW = bindAllComponents(bundleW, "wrapper/opcua");
		// Get and start Jena
		String symNameJ = "org.apache.jena.2.13";
		bundleJ = OsgiUtil.find(ctx.getBundles(), symNameJ);
		bundleJ.start();
		// Get and start server
		String symNameS = "de.uniol.inf.is.odysseus.opcua.server";
		bundleS = OsgiUtil.find(ctx.getBundles(), symNameS);
		bundleS.start();
		dsS = bindAllComponents(bundleS, "server/opcua.server");
	}

	private static DS bindAllComponents(Bundle bundle, String path) {
		BundleContext ctx = bundle.getBundleContext();
		ClassLoader loader = OsgiUtil.getClassloader(ctx);
		DS ds = new DS(loader);
		prepareMocks(ds);
		File osgiDir = new File("../../" + path + "/" + "OSGI-INF");
		for (File osgiFile : osgiDir.listFiles(OsgiUtil.XmlFiles)) {
			log.info("Binding '{}'...", osgiFile);
			assertTrue(osgiFile.length() > 0);
			Component cmp = Loader.load(osgiFile);
			cmps.push(cmp);
			ds.bind(cmp);
		}
		return ds;
	}

	private static void prepareMocks(DS ds) {
		prepareMock(ds, IExecutor.class);
		prepareMock(ds, IOperatorBuilderFactory.class);
		prepareMock(ds, ISecurityProvider.class);
	}

	private static void prepareMock(DS ds, Class<?> clazz) {
		Object inst;
		ds.Registered.put(clazz, inst = mock(clazz));
		assertNotNull(inst);
	}

	@Test
	public void testFuncProviders() {
		List<IMepFunction<?>> funcs = new LinkedList<>();
		funcs.addAll(testFuncProvider(dsS, 2));
		funcs.addAll(testFuncProvider(dsW, 5));
		testFunctions(funcs);
	}

	private List<IMepFunction<?>> testFuncProvider(DS ds, int size) {
		Object fpc = ds.Registered.get(IFunctionProvider.class);
		assertNotNull(fpc);
		assertTrue(fpc instanceof IFunctionProvider);
		IFunctionProvider fp = (IFunctionProvider) fpc;
		List<IMepFunction<?>> funcs = fp.getFunctions();
		assertEquals(size, funcs.size());
		return funcs;
	}

	@SuppressWarnings("unchecked")
	private <T> AbstractExpression<T> newConstant(Class<T> clazz, T value, SDFDatatype type) {
		try {
			ClassLoader cl = AbstractExpression.class.getClassLoader();
			Class<?> ct = Class.forName("de.uniol.inf.is.odysseus.mep.intern.Constant", true, cl);
			Constructor<?> cc = ct.getConstructors()[0];
			Object ci = cc.newInstance(value, type);
			return (AbstractExpression<T>) ci;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void testFunctions(List<IMepFunction<?>> funcs) {
		Object obj = null;
		SDFDatatype type = ((IDatatypeProvider) dsC.Registered.get(IDatatypeProvider.class)).getDatatypes().get(0);
		long ts = System.currentTimeMillis();
		double val = 42.123;
		int quality = 0x800;
		long error = 899L;
		for (IMepFunction<?> func : funcs) {
			String funcName = func.getClass().getSimpleName();
			switch (funcName) {
			case "ToOPCValueFunction":
				func.setArguments(newConstant(Number.class, ts, SDFDatatype.LONG),
						newConstant(Number.class, val, SDFDatatype.DOUBLE),
						newConstant(Number.class, quality, SDFDatatype.INTEGER),
						newConstant(Number.class, error, SDFDatatype.LONG));
				obj = func.getValue();
				assertNotNull(obj);
				break;
			case "TimestampFunction":
				func.setArguments(newConstant(Object.class, obj, type));
				assertEquals(ts, func.getValue());
				break;
			case "QualityFunction":
				func.setArguments(newConstant(Object.class, obj, type));
				assertEquals(quality, func.getValue());
				break;
			case "ErrorFunction":
				func.setArguments(newConstant(Object.class, obj, type));
				assertEquals(error, func.getValue());
				break;
			case "ValueFunction":
				func.setArguments(newConstant(Object.class, obj, type));
				assertEquals(val, func.getValue());
				assertTrue(func.determineTypeFromInput());
				assertEquals(SDFDatatype.DOUBLE,
						func.determineType(new IMepExpression<?>[] { newConstant(Object.class, obj, type) }));
				assertNull(func.determineType(new IMepExpression<?>[0]));
				break;
			case "ToErrorStrFunction":
				func.setArguments(newConstant(Number.class, 0x809d0000L, SDFDatatype.LONG));
				assertEquals("Bad_DataLost", func.getValue());
				break;
			case "ToErrorValFunction":
				func.setArguments(newConstant(String.class, "Good_MoreData", SDFDatatype.STRING));
				assertEquals(0xa60000L, func.getValue());
				break;
			default:
				throw new UnsupportedOperationException(funcName);
			}
		}
	}

	@Test
	public void testDataTypeProvider() {
		Object dtp = dsC.Registered.get(IDatatypeProvider.class);
		assertNotNull(dtp);
		assertTrue(dtp instanceof IDatatypeProvider);
		IDatatypeProvider dt = (IDatatypeProvider) dtp;
		List<SDFDatatype> types = dt.getDatatypes();
		assertEquals(1, types.size());
	}

	@Test
	public void testDataHandler() throws Exception {
		Object dhp = dsC.Registered.get(IDataHandler.class);
		assertNotNull(dhp);
		assertTrue(dhp instanceof IDataHandler);
		IDataHandler<?> dh = (IDataHandler<?>) dhp;
		assertEquals("[OPCValue]", dh.getSupportedDataTypes() + "");
		assertEquals("OPCValue", dh.createsType().getSimpleName());
		assertEquals(8 + 8 + 4 + 8, dh.memSize(null));
		assertNotNull(dh.createInstance(dh.getSchema()));
		// Test byte serialization
		final Object val = dh.createsType().getConstructors()[0].newInstance(32L, 42.123, 13, 89L);
		ByteBuffer buff = ByteBuffer.allocate(1024);
		dh.writeData(buff, val);
		byte[] array = buff.array();
		assertTrue(array.length > 0);
		buff.rewind();
		Object val2 = dh.readData(buff);
		assertEquals(val + "", val2 + "");
		// Test string serialization
		StringBuilder bld = new StringBuilder();
		dh.writeData(bld, val);
		String text = bld.toString();
		assertFalse(text.isEmpty());
		val2 = dh.readData(text);
		assertEquals(val + "", val2 + "");
	}

	@Test
	public void testDataHandler2() throws Exception {
		IDataHandler<?> dh = (IDataHandler<?>) dsC.Registered.get(IDataHandler.class);
		// Load a nice schema
		ClassLoader l = dh.getClass().getClassLoader();
		SDFSchema so = (SDFSchema) TestUtil.loadAsJavaObj("res/SDFSchema_dh.jobj", l);
		// Create handler
		dh = dh.createInstance(so);
		// Check it
		assertNotNull(dh);
	}

	@Test
	public void testTransportHandler() throws Exception {
		Object tph = dsW.Registered.get(ITransportHandler.class);
		assertNotNull(tph);
		assertTrue(tph instanceof ITransportHandler);
		ITransportHandler tp = (ITransportHandler) tph;
		assertEquals("OPC-UA", tp.getName());
		// Set options
		String nodes = node1;
		// Create map from options
		OptionMap options = new OptionMap();
		options.setOption("endpoint", endpoint);
		options.setOption("nodes", nodes);
		// Initialize memory
		MemoryProtocolHandler<?> proto = new MemoryProtocolHandler<>();
		// Create new handler instance
		ITransportHandler handler = tp.createInstance(proto, options);
		assertNotNull(handler);
		// Load a nice schema
		ClassLoader l = handler.getClass().getClassLoader();
		Object so = TestUtil.loadAsJavaObj("res/SDFSchema_dh.jobj", l);
		assertNotNull(so);
		assertTrue(so instanceof SDFSchema);
		SDFSchema s = (SDFSchema) so;
		// Open the handler
		handler.setSchema(s);
		handler.processInOpen();
		// Wait some time...
		Thread.sleep(5 * 1000);
		// Close the handler
		handler.processInClose();
		handler.close();
		// Check memory
		assertNotNull(proto.getQueue());
		assertFalse(proto.getQueue().isEmpty());
		assertTrue(proto.getQueue().size() >= 2);
		// Check first item
		IStreamObject<?> first = proto.getQueue().poll();
		assertNotNull(first);
		// Clear queue
		proto.getQueue().clear();
		assertTrue(proto.getQueue().isEmpty());
		// Test other stuff
		assertTrue(handler.isSemanticallyEqual(handler));
		assertFalse(handler.isSemanticallyEqual(tp));
	}

	@Test
	public void testTransportHandler2() throws Exception {
		ITransportHandler tp = (ITransportHandler) dsW.Registered.get(ITransportHandler.class);
		// Set options
		OptionMap options = new OptionMap();
		options.setOption("endpoint", endpoint);
		options.setOption("nodes", nodes);
		// Initialize memory
		MemoryProtocolHandler<?> proto = new MemoryProtocolHandler<>();
		// Create new handler instance
		ITransportHandler handler = tp.createInstance(proto, options);
		// Load a nice schema
		ClassLoader l = handler.getClass().getClassLoader();
		SDFSchema so = (SDFSchema) TestUtil.loadAsJavaObj("res/SDFSchema_tp.jobj", l);
		// Open the handler
		handler.setSchema(so);
		handler.processInOpen();
		// Wait some time...
		Thread.sleep(6 * 1000);
		// Close the handler
		handler.processInClose();
		handler.close();
		// Check memory
		assertTrue(proto.getQueue().size() >= 2);
		// Check first item
		IStreamObject<?> first = proto.getQueue().poll();
		assertNotNull(first);
		// Clear queue
		proto.getQueue().clear();
		assertTrue(proto.getQueue().isEmpty());
	}

	@Test
	public void testTransportHandlerSink() throws IOException {
		String input = "-1.33884403712E11[timestamp=130817462344640893, quality=0, error=0],"
				+ "1.7295632E-26[timestamp=130817462344640893, quality=0, error=0]";
		// Get initial instance
		ITransportHandler tp = (ITransportHandler) dsS.Registered.get(ITransportHandler.class);
		// Set options
		OptionMap options = new OptionMap();
		options.setOption("endpoint", endpoint);
		options.setOption("nodes", nodes);
		// Initialize memory
		MemoryProtocolHandler<?> proto = new MemoryProtocolHandler<>();
		// Create new handler instance
		ITransportHandler handler = tp.createInstance(proto, options);
		// Open the handler as sink
		handler.processOutOpen();
		// Send some test input...
		handler.send(input.getBytes("UTF8"));
		// Close the handler
		handler.processOutClose();
		// Check memory
		assertTrue(proto.getQueue().size() >= 0);
		// Clear queue
		proto.getQueue().clear();
		assertTrue(proto.getQueue().isEmpty());
	}

	private INodeModel findModel(ITransportHandler tp) throws IllegalArgumentException,
			IllegalAccessException, ClassNotFoundException, NoSuchFieldException, SecurityException {
		Class<?> compCl = tp.getClass().getClassLoader().loadClass("de.uniol.inf.is" +
				".odysseus.server" + ".opcua.binding.OPCUAComponent");
		Object compInst = compCl.getField("Instance").get(null);
		Field compFld = compCl.getDeclaredField("model");
		compFld.setAccessible(true);
		return (INodeModel) compFld.get(compInst);
	}

	@Test
	public void testTransportHandlerSink2() throws Exception {
		String input1 = "-1.33884403712E11[timestamp=130817462344640893, quality=0, error=0],1457926604959";
		String input2 = "1.7295632E-26[timestamp=130817462344640893, quality=0, error=0]";
		// Get initial instance
		ITransportHandler tp = (ITransportHandler)dsS.Registered.get(ITransportHandler.class);
		INodeModel model = findModel(tp);
		// Set first options
		OptionMap opt1 = toOptMap(loadProps("res/model-def-t1a.txt"));
		opt1.setOption("nodes", "ns=1;i=93 | ns=1;i=94");
		// Initialize first memory
		MemoryProtocolHandler<?> ph1 = new MemoryProtocolHandler<>();
		// Create first handler instance
		ITransportHandler handler1 = tp.createInstance(ph1, opt1);
		// Open first handler as sink
		handler1.processOutOpen();
		// Set second options
		OptionMap opt2 = toOptMap(loadProps("res/model-def-t1b.txt"));
		opt2.setOption("nodes", "ns=2;i=19");
		// Initialize second memory
		MemoryProtocolHandler<?> ph2 = new MemoryProtocolHandler<>();
		// Create second handler instance
		ITransportHandler handler2 = tp.createInstance(ph2, opt2);
		// Open second handler as sink
		handler2.processOutOpen();
		// Send some test input...
		handler1.send(input1.getBytes("UTF8"));
		handler2.send(input2.getBytes("UTF8"));
		// Check the variables!
		check(model);
		// Graph it!
		graph(model);
		// Close the handler
		handler2.processOutClose();
		// Close first handler
		handler1.processOutClose();
		// Check memory
		assertTrue(ph1.getQueue().size() >= 0);
		assertTrue(ph2.getQueue().size() >= 0);
		// Clear queue
		ph1.getQueue().clear();
		ph2.getQueue().clear();
		assertTrue(ph1.getQueue().isEmpty());
		assertTrue(ph2.getQueue().isEmpty());
	}

	private Map<AttributeIds, Object> getNodeDetails(INodeModel onm, String nid) {
		NodeId id = NodeId.parse(nid);
		Map<AttributeIds, Object> details = new LinkedHashMap<>();
		for (AttributeIds attr : AttributeIds.values()) {
			DataValue dv = onm.read(id, attr);
			Variant var = dv.getValue();
			Object raw = var.getValue();
			if (raw instanceof LocalizedText)
				raw = ((LocalizedText)raw).getText();
			if (raw instanceof QualifiedName)
				raw = ((QualifiedName)raw).getName();
			details.put(attr, raw);
		}
		return details;
	}

	private void check(INodeModel onm) {
		// UA NodeSet
		Map<AttributeIds, Object> a = getNodeDetails(onm, "i=2256");
		assertEquals("ServerStatus", a.get(AttributeIds.DisplayName));
		assertEquals(Identifiers.ServerStatusDataType, a.get(AttributeIds.DataType));
		assertEquals(NodeClass.Variable, a.get(AttributeIds.NodeClass));
		assertEquals(ServerState.Running, ((ServerStatusDataType)a.get(AttributeIds.Value)).getState());
	    assertEquals("ServerStatus", a.get(AttributeIds.BrowseName));
	    assertEquals(1000.0, a.get(AttributeIds.MinimumSamplingInterval));
	    assertEquals(NodeId.parse("i=2256"), a.get(AttributeIds.NodeId));
	    assertEquals(UByte.valueOf(1), a.get(AttributeIds.AccessLevel));
	    assertEquals(UByte.valueOf(1), a.get(AttributeIds.UserAccessLevel));
	    assertEquals(UInteger.valueOf(0), a.get(AttributeIds.WriteMask));
	    assertEquals(UInteger.valueOf(0), a.get(AttributeIds.UserWriteMask));
	    assertEquals(false, a.get(AttributeIds.Historizing));
	    assertEquals("The current status of the server.", a.get(AttributeIds.Description));
	    assertEquals(null, a.get(AttributeIds.ArrayDimensions));
		assertEquals(ValueRanks.Scalar.getValue(), a.get(AttributeIds.ValueRank));
		// DSMS
		Map<AttributeIds, Object> b = getNodeDetails(onm, "ns=1;s=1004_bundleId");
		assertEquals("BundleId", b.get(AttributeIds.DisplayName));
		assertEquals(Identifiers.Int64, b.get(AttributeIds.DataType));
		assertEquals(NodeClass.Variable, b.get(AttributeIds.NodeClass));
		assertEquals(12L, b.get(AttributeIds.Value));
		assertEquals("BundleId", b.get(AttributeIds.BrowseName));
		assertEquals(1000.0, b.get(AttributeIds.MinimumSamplingInterval));
		assertEquals(NodeId.parse("ns=1;s=1004_bundleId"), b.get(AttributeIds.NodeId));
		assertEquals(UByte.valueOf(1), b.get(AttributeIds.AccessLevel));
		assertEquals(UByte.valueOf(1), b.get(AttributeIds.UserAccessLevel));
		assertEquals(UInteger.valueOf(0), b.get(AttributeIds.WriteMask));
		assertEquals(UInteger.valueOf(0), b.get(AttributeIds.UserWriteMask));
		assertEquals(false, b.get(AttributeIds.Historizing));
		assertEquals("This bundle's unique identifier", b.get(AttributeIds.Description));
		assertEquals(null, b.get(AttributeIds.ArrayDimensions));
		assertEquals(ValueRanks.Scalar.getValue(), b.get(AttributeIds.ValueRank));
		// Small user
		Map<AttributeIds, Object> c = getNodeDetails(onm, "ns=1;i=93");
		assertEquals("ThermalEnergy", c.get(AttributeIds.DisplayName));
		assertEquals(Identifiers.Double, c.get(AttributeIds.DataType));
		assertEquals(NodeClass.Variable, c.get(AttributeIds.NodeClass));
		assertEquals(-1.33884403712E11, c.get(AttributeIds.Value));
		assertEquals("ThermalEnergy", c.get(AttributeIds.BrowseName));
		assertEquals(0.0, c.get(AttributeIds.MinimumSamplingInterval));
		assertEquals(NodeId.parse("ns=1;i=93"), c.get(AttributeIds.NodeId));
		assertEquals(UByte.valueOf(1), c.get(AttributeIds.AccessLevel));
		assertEquals(UByte.valueOf(1), c.get(AttributeIds.UserAccessLevel));
		assertEquals(UInteger.valueOf(0), c.get(AttributeIds.WriteMask));
		assertEquals(UInteger.valueOf(0), c.get(AttributeIds.UserWriteMask));
		assertEquals(false, c.get(AttributeIds.Historizing));
		assertEquals("The heating's warmth", c.get(AttributeIds.Description));
		assertEquals(null, c.get(AttributeIds.ArrayDimensions));
		assertEquals(ValueRanks.Scalar.getValue(), c.get(AttributeIds.ValueRank));
		// Small user #2
		c = getNodeDetails(onm, "ns=1;i=94");
		assertEquals("CurrentPower", c.get(AttributeIds.DisplayName));
		assertEquals(Identifiers.Double, c.get(AttributeIds.DataType));
		assertEquals(NodeClass.Variable, c.get(AttributeIds.NodeClass));
		assertEquals(1.457926604959E12, c.get(AttributeIds.Value));
		assertEquals("CurrentPower", c.get(AttributeIds.BrowseName));
		assertEquals(0.0, c.get(AttributeIds.MinimumSamplingInterval));
		assertEquals(NodeId.parse("ns=1;i=94"), c.get(AttributeIds.NodeId));
		assertEquals(UByte.valueOf(1), c.get(AttributeIds.AccessLevel));
		assertEquals(UByte.valueOf(1), c.get(AttributeIds.UserAccessLevel));
		assertEquals(UInteger.valueOf(0), c.get(AttributeIds.WriteMask));
		assertEquals(UInteger.valueOf(0), c.get(AttributeIds.UserWriteMask));
		assertEquals(false, c.get(AttributeIds.Historizing));
		assertEquals("The heating's power", c.get(AttributeIds.Description));
		assertEquals(null, c.get(AttributeIds.ArrayDimensions));
		assertEquals(ValueRanks.Scalar.getValue(), c.get(AttributeIds.ValueRank));
		// Big user
		Map<AttributeIds, Object> d = getNodeDetails(onm, "ns=2;i=19");
		assertEquals("Valve_3", d.get(AttributeIds.DisplayName));
		assertEquals(Identifiers.Double, d.get(AttributeIds.DataType));
		assertEquals(NodeClass.Variable, d.get(AttributeIds.NodeClass));
		assertEquals(1.7295632E-26, d.get(AttributeIds.Value));
		assertEquals("Valve_3", d.get(AttributeIds.BrowseName));
		assertEquals(0.0, d.get(AttributeIds.MinimumSamplingInterval));
		assertEquals(NodeId.parse("ns=2;i=19"), d.get(AttributeIds.NodeId));
		assertEquals(UByte.valueOf(1), d.get(AttributeIds.AccessLevel));
		assertEquals(UByte.valueOf(1), d.get(AttributeIds.UserAccessLevel));
		assertEquals(UInteger.valueOf(0), d.get(AttributeIds.WriteMask));
		assertEquals(UInteger.valueOf(0), d.get(AttributeIds.UserWriteMask));
		assertEquals(false, d.get(AttributeIds.Historizing));
		assertEquals("The valve's pressure", d.get(AttributeIds.Description));
		assertEquals(null, d.get(AttributeIds.ArrayDimensions));
		assertEquals(ValueRanks.Scalar.getValue(), d.get(AttributeIds.ValueRank));
	}

	private void graph(INodeModel onm) throws IOException, InterruptedException {
		(new File("out")).mkdir();
		// Set entry
		NodeId s = Identifiers.ObjectsFolder;
		// Coloring options
		Map<NodeId, Colors> colorMap = (new FluentMap<NodeId, Colors>())
				.set(Identifiers.Organizes, Colors.blue)
				.set(Identifiers.HasComponent, Colors.green)
				.set(Identifiers.HasProperty, Colors.red);
		IEdgeCustomizer ec = Customizers.newEdgerByMap(colorMap);
		// Create graph builder
		GraphBuilder g = GraphBuilder.create().model(onm)
				.dir(BrowseDirection.Forward).kind(GraphKind.Undirected)
				.entry(s).edger(ec);
		// Graph it!
		g.layout(Layout.fdp).dot(new File("out/test1f.dot")).browse().write().graph().clean();
		g.layout(Layout.sfdp).dot(new File("out/test2f.dot")).browse().write().graph().clean();
		g.layout(Layout.twopi).dot(new File("out/test3f.dot")).browse().write().graph().clean();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTransportHandlerNoInStreamImpl() {
		ITransportHandler tp = (ITransportHandler) dsW.Registered.get(ITransportHandler.class);
		tp.getInputStream();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testTransportHandlerNoOutStreamImpl() {
		ITransportHandler tp = (ITransportHandler) dsW.Registered.get(ITransportHandler.class);
		tp.getOutputStream();
	}

	private static void unbind(DS ds, Component cmp) {
		try {
			ds.unbind(cmp);
		} catch (Exception e) {
			if (e instanceof RuntimeException && e.getCause() instanceof ClassNotFoundException)
				return; // NO-OP!
			throw e;
		}
	}

	private static void unbindAllComponents() {
		for (Component cmp : cmps) {
			unbind(dsS, cmp);
			unbind(dsW, cmp);
			unbind(dsC, cmp);
		}
		cmps.clear();
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
		unbindAllComponents();
		bundleS.stop();
		bundleW.stop();
		bundleC.stop();
	}
}