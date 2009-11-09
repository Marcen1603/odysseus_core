package de.uniol.inf.is.odysseus.benchmarker.starter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.adaptor.EclipseStarter;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import de.uniol.inf.is.odysseus.args.Args;
import de.uniol.inf.is.odysseus.args.ArgsException;
import de.uniol.inf.is.odysseus.args.Args.REQUIREMENT;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmark;
import de.uniol.inf.is.odysseus.benchmarker.IBenchmarkResult;

public class BenchmarkStarter {

	private static final String SCHEDULER = "-scheduler";
	private static final String SCHEDULING_STRAGEGY = "-scheduling_stragegy";
	private static final String BUFFER_PLACEMENT = "-buffer_placement";
	private static final String DATA_TYPE = "-data_type";
	private static final String METADATA_TYPES = "-metadata_types";
	private static final String QUERY_LANGUAGE = "-query_language";
	private static final String QUERY = "-query";
	private static final String MAX_RESULTS = "-max_results";
	private static final String PRIORITY = "-priority";
	private static final String PUNCTUATIONS = "-punctuations";
	private static final String LOAD_SHEDDING = "-load_shedding";
	private static final String ECLIPSE_DEBUG = "-debug";
	private static final String ECLIPSE_CLEAN = "-clean";
	private static final String ECLIPSE_CONSOLE = "-console";

	// private static Logger logger =
	// LoggerFactory.getLogger(BenchmarkStarter.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Args arguments = new Args();
		try {
			initArgs(arguments, args);

			String[] eclipseArguments = getEclipseArgs(arguments);

			Map<String, String> initialProperties = new HashMap<String, String>();
			initialProperties
					.put(
							"org.osgi.framework.system.packages",
							"javax.accessibility,  javax.activity,  javax.crypto,  javax.crypto.interfaces,  javax.crypto.spec,  javax.imageio,  javax.imageio.event,  javax.imageio.metadata,  javax.imageio.plugins.bmp,  javax.imageio.plugins.jpeg,  javax.imageio.spi,  javax.imageio.stream,  javax.management,  javax.management.loading,  javax.management.modelmbean,  javax.management.monitor,  javax.management.openmbean,  javax.management.relation,  javax.management.remote,  javax.management.remote.rmi,  javax.management.timer,  javax.naming,  javax.naming.directory,  javax.naming.event,  javax.naming.ldap,  javax.naming.spi,  javax.net,  javax.net.ssl,  javax.print,  javax.print.attribute,  javax.print.attribute.standard,  javax.print.event,  javax.rmi,  javax.rmi.CORBA,  javax.rmi.ssl,  javax.security.auth,  javax.security.auth.callback,  javax.security.auth.kerberos,  javax.security.auth.login,  javax.security.auth.spi,  javax.security.auth.x500,  javax.security.cert,  javax.security.sasl,  javax.sound.midi,  javax.sound.midi.spi,  javax.sound.sampled,  javax.sound.sampled.spi,  javax.sql,  javax.sql.rowset,  javax.sql.rowset.serial,  javax.sql.rowset.spi,  javax.swing,  javax.swing.border,  javax.swing.colorchooser,  javax.swing.event,  javax.swing.filechooser,  javax.swing.plaf,  javax.swing.plaf.basic,  javax.swing.plaf.metal,  javax.swing.plaf.multi,  javax.swing.plaf.synth,  javax.swing.table,  javax.swing.text,  javax.swing.text.html,  javax.swing.text.html.parser,  javax.swing.text.rtf,  javax.swing.tree,  javax.swing.undo,  javax.transaction,  javax.transaction.xa,  javax.xml,  javax.xml.datatype,  javax.xml.namespace,  javax.xml.parsers,  javax.xml.transform,  javax.xml.transform.dom,  javax.xml.transform.sax,  javax.xml.transform.stream,  javax.xml.validation,  javax.xml.xpath,  org.ietf.jgss,  org.omg.CORBA,  org.omg.CORBA_2_3,  org.omg.CORBA_2_3.portable,  org.omg.CORBA.DynAnyPackage,  org.omg.CORBA.ORBPackage,  org.omg.CORBA.portable,  org.omg.CORBA.TypeCodePackage,  org.omg.CosNaming,  org.omg.CosNaming.NamingContextExtPackage,  org.omg.CosNaming.NamingContextPackage,  org.omg.Dynamic,  org.omg.DynamicAny,  org.omg.DynamicAny.DynAnyFactoryPackage,  org.omg.DynamicAny.DynAnyPackage,  org.omg.IOP,  org.omg.IOP.CodecFactoryPackage,  org.omg.IOP.CodecPackage,  org.omg.Messaging,  org.omg.PortableInterceptor,  org.omg.PortableInterceptor.ORBInitInfoPackage,  org.omg.PortableServer,  org.omg.PortableServer.CurrentPackage,  org.omg.PortableServer.POAManagerPackage,  org.omg.PortableServer.POAPackage,  org.omg.PortableServer.portable,  org.omg.PortableServer.ServantLocatorPackage,  org.omg.SendingContext,  org.omg.stub.java.rmi,  org.w3c.dom,  org.w3c.dom.bootstrap,  org.w3c.dom.css,  org.w3c.dom.events,  org.w3c.dom.html,  org.w3c.dom.ls,  org.w3c.dom.ranges,  org.w3c.dom.stylesheets,  org.w3c.dom.traversal,  org.w3c.dom.views ,  org.xml.sax,  org.xml.sax.ext,  org.xml.sax.helpers,"
									+ IBenchmark.class.getPackage().getName());

			EclipseStarter.setInitialProperties(initialProperties);
			BundleContext ctx = EclipseStarter.startup(eclipseArguments, null);

			for (Bundle b : ctx.getBundles()) {
				try {
					if (b.getHeaders().get("Fragment-Host") == null
							&& b.getState() != Bundle.ACTIVE) {
						b.start();
					}
				} catch (Throwable t) {
					System.err
							.println("could not load: " + b.getSymbolicName());
					t.printStackTrace();
				}

			}
			ServiceTracker t = new ServiceTracker(ctx, IBenchmark.class
					.getName(), null);
			t.open();
			IBenchmark benchmark = (IBenchmark) t.waitForService(30000);
			if (benchmark == null) {
				throw new Exception("cannot find benchmark service");
			}

			configureBenchmark(benchmark, arguments);

			IBenchmarkResult<?> result = benchmark.runBenchmark();
			Serializer serializer = new Persister();
			serializer.write(result, System.out);
		} catch (ArgsException e) {
			e.printStackTrace();
			System.out.println("usage:\n");
			System.out.println(arguments.getHelpText());
			System.exit(-1);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		} finally {
			try {
				EclipseStarter.shutdown();
			} catch (Exception e1) {
			}
		}
	}

	private static String[] getEclipseArgs(Args arguments) {
		ArrayList<String> eclipseArguments = new ArrayList<String>();
		if (arguments.hasParameter(ECLIPSE_DEBUG)) {
			eclipseArguments.add("-debug");
			eclipseArguments.add(arguments.getString(ECLIPSE_DEBUG));
		}
		if (arguments.get(ECLIPSE_CLEAN)) {
			eclipseArguments.add("-clean");
		}
		if (arguments.get(ECLIPSE_CONSOLE)) {
			eclipseArguments.add("-console");
		}

		return eclipseArguments.toArray(new String[] {});
	}

	private static void configureBenchmark(IBenchmark benchmark, Args arguments) {
		if (arguments.hasParameter(SCHEDULER)) {
			String scheduler = arguments.get(SCHEDULER);
			benchmark.setScheduler(scheduler);
		}

		if (arguments.hasParameter(SCHEDULING_STRAGEGY)) {
			String schedulingStrategy = arguments.get(SCHEDULING_STRAGEGY);
			benchmark.setSchedulingStrategy(schedulingStrategy);
		}

		if (arguments.hasParameter(BUFFER_PLACEMENT)) {
			String bufferPlacement = arguments.get(BUFFER_PLACEMENT);
			benchmark.setBufferPlacementStrategy(bufferPlacement);
		}

		if (arguments.hasParameter(DATA_TYPE)) {
			String dataType = arguments.get(DATA_TYPE);
			benchmark.setDataType(dataType);
		}

		String[] metaTypes = benchmark.getMetadataTypes();
		if (arguments.hasParameter(METADATA_TYPES)) {
			String metaTypesStr = arguments.get(METADATA_TYPES);
			metaTypes = metaTypesStr.split(":");
		}

		if (arguments.get(PRIORITY)) {
			metaTypes = Arrays.copyOf(metaTypes, metaTypes.length + 1);
			metaTypes[metaTypes.length - 1] = "de.uniol.inf.is.odysseus.priority.IPriority";
			benchmark
					.setResultFactory("de.uniol.inf.is.odysseus.benchmarker.impl.PriorityBenchmarkResultFactory");
		}

		benchmark.setMetadataTypes(metaTypes);

		if (arguments.get(PUNCTUATIONS)) {
			benchmark.setUsePunctuations(true);
		}
		
		if (arguments.get(LOAD_SHEDDING)) {
			benchmark.setUseLoadShedding(true);
		}
		
		if (arguments.hasParameter(MAX_RESULTS)) {
			Long maxResults = arguments.getLong(MAX_RESULTS);
			benchmark.setMaxResults(maxResults);
		}

		String queryLanguage = arguments.get(QUERY_LANGUAGE);
		String query = arguments.get(QUERY);

		benchmark.addQuery(queryLanguage, query);
	}

	private static void initArgs(Args arguments, String[] args)
			throws ArgsException {
		arguments.addString(SCHEDULER, REQUIREMENT.OPTIONAL,
				"<scheduler> - sets the scheduler");
		arguments.addString(SCHEDULING_STRAGEGY, REQUIREMENT.OPTIONAL,
				"<scheduling strategy> - sets the scheduling strategy");
		arguments.addString(BUFFER_PLACEMENT, REQUIREMENT.OPTIONAL,
				"<buffer strategy> - sets the buffer placement strategy");
		arguments.addString(DATA_TYPE, REQUIREMENT.OPTIONAL,
				"<data type> - sets the data type (default=relational)");
		arguments
				.addString(
						METADATA_TYPES,
						REQUIREMENT.OPTIONAL,
						"<metadata types> - ':' separated list of metadata types (default=de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval)");
		arguments
				.addLong(
						MAX_RESULTS,
						REQUIREMENT.OPTIONAL,
						"<int> - stop processing if stream ends or <int> results were produced by the queries");
		arguments.addString(QUERY_LANGUAGE, REQUIREMENT.MANDATORY,
				"<language> - query language");
		arguments
				.addString(QUERY, REQUIREMENT.MANDATORY,
						"<query> - query string. may contain multiple queries in one string.");
		arguments
				.addBoolean(
						PRIORITY,
						" - enables tracking of latencies of prioritized elements, automatically adds 'de.uniol.inf.is.odysseus.priority.IPriority' to metadata types");
		
		arguments.addBoolean(PUNCTUATIONS, " - enables usage of punctuations");
		
		arguments.addBoolean(LOAD_SHEDDING, " - enables usage of load shedding");
		
		arguments
				.addString(
						ECLIPSE_DEBUG,
						REQUIREMENT.OPTIONAL,
						"<filename> - adds -debug <filename> when starting the embedded equinox environment");
		arguments
				.addBoolean(ECLIPSE_CLEAN,
						" - adds -clean when starting the embedded equinox environment");
		arguments
				.addBoolean(ECLIPSE_CONSOLE,
						" - adds -console when starting the embedded equinox environment");
		

		arguments.parse(args);
	}
}
