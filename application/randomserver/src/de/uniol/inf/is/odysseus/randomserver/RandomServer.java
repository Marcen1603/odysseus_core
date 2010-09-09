package de.uniol.inf.is.odysseus.randomserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;

import de.uniol.inf.is.odysseus.randomserver.BasicSchema.DataType;

public class RandomServer implements CommandProvider {

	private static RandomServer instance;
	private int defaultPort = 56033;
	private Map<Integer, RandomDataProvider> providers = new HashMap<Integer, RandomDataProvider>();

	private BasicSchema defaultSchema = new BasicSchema();

	private RandomServer() {
		this.defaultSchema.addAttribute(DataType.Integer);
		this.defaultSchema.addAttribute(DataType.Integer);
		this.defaultSchema.addAttribute(DataType.Double);
		this.defaultSchema.addAttribute(DataType.Long);
	}

	public static synchronized RandomServer getInstance() {
		if (instance == null) {
			instance = new RandomServer();
		}
		return instance;
	}

	@Override
	public String getHelp() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("---Random Server commands---\n");
		buffer.append("\tcreate [[s] p] - creates a new server\n");
		buffer.append("\t\t p is the port for the server\n");
		buffer.append("\t\t s is a scheme for the server. A comma-separated list of datatypes. Allowed types are INTEGER, LONG, STRING, DOUBLE and TIME\n");
		buffer.append("\t\t For example \"create INTEGER,LONG,TIME\"\n");
		buffer.append("\t\t You can also add a range for each datatype with type[min:max]. min and max must be parseable into the related datatype\n");
		buffer.append("\t\t For example \"create INTEGER[0:100],DOUBLE[0.0:3.0]\"\n");
		buffer.append("\tplay [p] - starts all paused servers or only the server on port p\n");
		buffer.append("\thold [p] - pauses all running servers or only the server on port p\n");
		buffer.append("\tkill [p] - kills all running servers or only the server on port p\n");
		return buffer.toString();
	}

	public void _create(CommandInterpreter ci) {
		int port = this.defaultPort + providers.size();
		while (this.providers.containsKey(Integer.valueOf(port))) {
			port++;
		}
		BasicSchema currentSchema = this.defaultSchema;
		List<String> args = this.getAllArgs(ci);

		if (args.size() > 0) {

			// is only port?
			try {
				port = Integer.parseInt(args.get(0));
			} catch (NumberFormatException e) {
				// no port -> scheme def?

				String[] attr = args.get(0).split(",");
				BasicSchema schema = new BasicSchema();
				for (int i = 0; i < attr.length; i++) {
					String[] keyval = attr[i].split("\\[");
					String typeStr = keyval[0].toLowerCase();
					DataType type = DataType.Integer;
					if (typeStr.equals("double")) {
						type = DataType.Double;
					} else if (typeStr.equals("string")) {
						type = DataType.String;
					} else if (typeStr.equals("long")) {
						type = DataType.Long;
					} else if (typeStr.equals("integer") || typeStr.equals("int")) {
						type = DataType.Integer;
					} else if (typeStr.equals("time")) {
						type = DataType.Time;
					} else {
						System.err
								.println("Wrong datatype. Only Double, String, Long and Integer allowed");
						return;
					}

					if (keyval.length > 1) {
						String[] vals = keyval[1].split(":");
						String min = vals[0];
						String max = vals[1].substring(0, vals[1].length() - 1);
						schema.addAttribute(type, min, max);
					} else {
						schema.addAttribute(type);
					}

				}

				currentSchema = schema;
				
				if (args.size() > 1) {
					port = Integer.parseInt(args.get(1));
					if (this.providers.containsKey(Integer.valueOf(port))) {
						System.err.println("There is already a server using this port! Try another port!");
						return;
					}
				}
				
			}
		}

		

		System.out.println("Starting server on port " + port + "...");
		RandomDataProvider provider = new RandomDataProvider(port,
				currentSchema);
		provider.start();
		this.providers.put(new Integer(port), provider);
	}

	public void _play(CommandInterpreter ci) {
		System.out.println("Resume...");
		if (ci.nextArgument() == null) {
			for (RandomDataProvider rdp : this.providers.values()) {
				rdp.play();
			}
		} else {
			String portArg = ci.nextArgument();
			try {
				Integer port = new Integer(portArg);
				if (this.providers.containsKey(port)) {
					this.providers.get(port).play();
				} else {
					System.out.println("No Server on Port " + port + " found");
				}
			} catch (NumberFormatException ex) {
				System.out.println("Argument must be an integer!");
			}

		}
	}

	public void _kill(CommandInterpreter ci) {

		this._hold(ci);
		String portArg = ci.nextArgument();
		if (portArg == null) {
			System.out.println("Killing all servers...");
			for (RandomDataProvider rdp : this.providers.values()) {
				rdp.interrupt();
			}
			this.providers.clear();
		} else {
			System.out.println("Killing server on port " + portArg + "...");
			try {
				Integer port = new Integer(portArg);
				if (this.providers.containsKey(port)) {
					this.providers.get(port).interrupt();
					this.providers.remove(Integer.valueOf(port));
				} else {
					System.out.println("No Server on Port " + port + " found");
				}
			} catch (NumberFormatException ex) {
				System.out.println("Argument must be an integer!");
			}
		}
		this._play(ci);
	}

	public void _hold(CommandInterpreter ci) {
		System.out.println("Hold...");
		String portArg = ci.nextArgument();
		if (portArg == null) {
			for (RandomDataProvider rdp : this.providers.values()) {
				rdp.pause();
			}
		} else {
			try {
				Integer port = new Integer(portArg);
				if (this.providers.containsKey(port)) {
					this.providers.get(port).pause();
				} else {
					System.out.println("No Server on Port " + port + " found");
				}
			} catch (NumberFormatException ex) {
				System.out.println("Argument must be an integer!");
			}

		}
	}

	private List<String> getAllArgs(CommandInterpreter ci) {
		String val = ci.nextArgument();
		List<String> args = new ArrayList<String>();
		while (val != null) {
			args.add(val);
			val = ci.nextArgument();
		}
		return args;
	}

}
