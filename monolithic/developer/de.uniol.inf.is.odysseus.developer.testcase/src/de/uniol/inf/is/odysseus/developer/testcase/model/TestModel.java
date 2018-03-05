/*******************************************************************************
 * Copyright 2015 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.developer.testcase.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.developer.testcase.Activator;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class TestModel implements Serializable {
	static final Logger LOG = LoggerFactory.getLogger(TestModel.class);

	/**
	 *
	 */
	private static final long serialVersionUID = 1128821262486943753L;
	private static final String TESTCASE = "TESTCASE";
	private static final String OPERATOR = "OPERATOR";
	private static final String SCHEMA = "SCHEMA";
	private static final String PORT = "PORT";
	private static final String ATTRIBUTE = "ATTRIBUTE";
	private static final String PARAMETERS = "PARAMETERS";
	private static final String PARAMETER = "PARAMETER";
	private static final String VALUE = "VALUE";
	private static final String METADATAS = "METADATAS";
	private static final String METADATA = "METADATA";
	private static final String DIRECTORY = "DIRECTORY";
	private static final String TYPE = "TYPE";
	private static final String MIN = "MIN";
	private static final String MAX = "MAX";
	private static final String NULL = "NULL";
	private static final String NAME = "NAME";
	private static final String ACTIVE = "ACTIVE";
	private static final String TIMESTAMP = "TIMESTAMP";
	private static final String WINDOW = "WINDOW";

	public static TestModel createEmpty(/* @NonNull */final LogicalOperatorInformation operator) {
		Objects.requireNonNull(operator);
		final TestModel model = new TestModel();
		model.setOperator(operator);
		return model;
	}

	public static TestModel load(/* @NonNull */final IFile file) throws Exception {
		Objects.requireNonNull(file);
		final TestModel model = new TestModel();
		model.loadFromFile(file);
		return model;
	}

	public static Object getValue(final String value, /* @NonNull */final SDFDatatype type) {
		Objects.requireNonNull(type);
		final IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(type.getQualName(), (SDFSchema) null);
		if (handler != null) {
			return handler.readData(value);
		}
		TestModel.LOG.error("No data handler for datatype {} found", type.getQualName());
		return null;
	}

	public static String getString(final Object value, /* @NonNull */final SDFDatatype type) {
		Objects.requireNonNull(type);
		final IDataHandler<?> handler = DataHandlerRegistry.getDataHandler(type.getQualName(), (SDFSchema) null);
		if (handler != null) {
			final StringBuilder sb = new StringBuilder();
			handler.writeData(sb, value);
			return sb.toString();
		}
		TestModel.LOG.error("No data handler for datatype {} found", type.getQualName());
		return "";
	}

	private LogicalOperatorInformation operator;
	private final List<Map<String, AttributeParameter>> schema = new ArrayList<>();
	private final List<Integer> windows = new ArrayList<>();
	private final Map<String, String> parameters = new HashMap<>();
	private final List<String> metadatas = new ArrayList<>();
	private String directory = "";
	private String name = "";
	private boolean timestamp = true;

	/**
	 * Class constructor.
	 *
	 */
	public TestModel() {
	}

	public void save(/* @NonNull */final File file) {
		Objects.requireNonNull(file);
		this.saveToLocalFile(file);
	}

	public void save(/* @NonNull */final IFile file) {
		Objects.requireNonNull(file);
		this.saveToIFile(file);
	}

	/**
	 * @return the operator
	 */
	public LogicalOperatorInformation getOperator() {
		return this.operator;
	}

	/**
	 * @param operator
	 *            the operator to set
	 */
	public void setOperator(/* @NonNull */final LogicalOperatorInformation operator) {
		Objects.requireNonNull(operator);
		this.operator = operator;
		this.parameters.clear();
		for (final LogicalParameterInformation parameter : operator.getParameters()) {
			this.setParameter(parameter.getName(), "");
		}
	}

	public void addAttribute(final int port, /* @NonNull */final String name, final AttributeParameter parameter) {
		Objects.requireNonNull(name);
		if ((port < 0) || (port >= 10)) {
			return;
		}

		while (this.schema.size() <= port) {
			this.schema.add(new LinkedHashMap<String, AttributeParameter>());
		}
		this.schema.get(port).put(name, parameter);
	}

	public void removeAttribute(final int port, /* @NonNull */final String name) {
		Objects.requireNonNull(name);
		if ((port < 0) || (port >= 10)) {
			return;
		}

		while (this.schema.size() <= port) {
			this.schema.add(new LinkedHashMap<String, AttributeParameter>());
		}
		this.schema.get(port).remove(name);
	}

	public void renameAttribute(final int port, /* @NonNull */final String name,
			/*
			 * @ NonNull
			 */final String newName) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(newName);
		if ((port < 0) || (port >= 10)) {
			return;
		}

		while (this.schema.size() <= port) {
			this.schema.add(new LinkedHashMap<String, AttributeParameter>());
		}

		final Map<String, AttributeParameter> newSchema = new LinkedHashMap<>();
		for (final Entry<String, AttributeParameter> entry : this.schema.get(port).entrySet()) {
			if (entry.getKey().equalsIgnoreCase(name)) {
				newSchema.put(newName, entry.getValue());
			} else {
				newSchema.put(entry.getKey(), entry.getValue());
			}
		}
		this.schema.set(port, newSchema);
	}

	/**
	 * @return the schema
	 */
	public Map<String, AttributeParameter> getSchema(final int port) {
		if ((port < 0) || (port >= 10)) {
			return null;
		}

		if (this.schema.size() <= port) {
			return new LinkedHashMap<>();
		}
		return Collections.unmodifiableMap(this.schema.get(port));
	}

	public void addMetadata(/* @NonNull */final String name) {
		Objects.requireNonNull(name);
		this.metadatas.add(name);
	}

	public void removeMetadata(/* @NonNull */final String name) {
		Objects.requireNonNull(name);
		this.metadatas.remove(name);
	}

	public void setMetadata(/* @NonNull */final String name, final boolean flag) {
		Objects.requireNonNull(name);
		if (flag) {
			this.metadatas.add(name);
		} else {
			this.metadatas.remove(name);
		}
	}

	public boolean getMetadata(/* @NonNull */final String name) {
		Objects.requireNonNull(name);
		return this.metadatas.contains(name);
	}

	public Collection<String> getMetadatas() {
		return Collections.unmodifiableCollection(this.metadatas);
	}

	public void addParameter(/* @NonNull */final String name, final String value) {
		Objects.requireNonNull(name);
		this.parameters.put(name, value);
	}

	public void removeParameter(/* @NonNull */final String name) {
		Objects.requireNonNull(name);
		this.parameters.remove(name);
	}

	public void setParameter(/* @NonNull */final String name, final String value) {
		Objects.requireNonNull(name);
		this.parameters.put(name, value);
	}

	public void addWindow(final int port, final Integer size) {
		if ((port < 0) || (port >= 10)) {
			return;
		}
		while (this.windows.size() <= port) {
			this.windows.add(0);
		}
		this.windows.set(port, size);
	}

	public void removeWindow(final int port) {
		if ((port < 0) || (port >= 10)) {
			return;
		}
		while (this.windows.size() <= port) {
			this.windows.add(0);
		}
		this.windows.set(port, 0);
	}

	public Integer getWindow(int port) {
		if ((port < 0) || (port >= 10) || (this.windows.size() <= port) || (this.windows.get(port) == null)) {
			return 0;
		}
		return this.windows.get(port);
	}

	/**
	 * @return the directory
	 */
	public String getDirectory() {
		return this.directory;
	}

	/**
	 * @param directory
	 *            the directory to set
	 */
	public void setDirectory(/* @NonNull */final String directory) {
		Objects.requireNonNull(directory);
		this.directory = directory;
	}

	/**
	 * @return the timestamp
	 */
	public boolean isTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public void setTimestamp(boolean timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(/* @NonNull */final String name) {
		Objects.requireNonNull(name);
		this.name = name;
	}

	/**
	 * @return the parameters
	 */
	public Map<String, String> getParameters() {
		return Collections.unmodifiableMap(this.parameters);
	}

	private String createInputStreamFile() throws IOException {
		final XMLMemento memento = XMLMemento.createWriteRoot(TestModel.TESTCASE);
		memento.putString(TestModel.OPERATOR, this.operator.getOperatorName());
		memento.putString(TestModel.DIRECTORY, this.directory);
		memento.putString(TestModel.NAME, this.name);
		memento.putBoolean(TestModel.TIMESTAMP, this.timestamp);

		final IMemento paramMem = memento.createChild(TestModel.PARAMETERS);
		for (final Entry<String, String> parameter : this.parameters.entrySet()) {
			if ((parameter.getValue() != null) && (!"".equals(parameter.getValue()))) {
				final IMemento paramChild = paramMem.createChild(TestModel.PARAMETER, parameter.getKey());
				paramChild.putString(TestModel.VALUE, parameter.getValue());
			}
		}

		for (int port = 0; port < this.schema.size() && port < 10; port++) {
			final IMemento attrMem = memento.createChild(TestModel.SCHEMA);
			attrMem.putInteger(TestModel.PORT, port);
			attrMem.putInteger(TestModel.WINDOW, windows.get(port));
			for (final Entry<String, AttributeParameter> attribute : this.schema.get(port).entrySet()) {
				final IMemento attrChild = attrMem.createChild(TestModel.ATTRIBUTE, attribute.getKey());
				attrChild.putString(TestModel.TYPE, attribute.getValue().getType().getQualName());
				attrChild.putString(TestModel.MIN,
						TestModel.getString(attribute.getValue().getMin(), attribute.getValue().getType()));
				attrChild.putString(TestModel.MAX,
						TestModel.getString(attribute.getValue().getMax(), attribute.getValue().getType()));
				attrChild.putBoolean(TestModel.NULL, attribute.getValue().isNullValue());
				attrChild.putBoolean(TestModel.ACTIVE, attribute.getValue().isActive());
			}
		}

		final IMemento metaMem = memento.createChild(TestModel.METADATAS);
		for (final String metadata : this.metadatas) {
			final IMemento metaChild = metaMem.createChild(TestModel.METADATA, metadata);
			metaChild.putBoolean(TestModel.VALUE, true);
		}
		final StringWriter sw = new StringWriter();
		memento.save(sw);

		return sw.toString();
	}

	private void loadFromFile(final IFile file) throws Exception {
		try (InputStream is = file.getContents()) {
			final String content = IOUtils.toString(is);
			if (!content.isEmpty()) {
				final StringReader sr = new StringReader(content);
				final XMLMemento memento = XMLMemento.createReadRoot(sr);
				final String operatorName = memento.getString(TestModel.OPERATOR);
				this.setOperator(this.getOperator(operatorName));
				this.setDirectory(memento.getString(TestModel.DIRECTORY));
				this.setName(memento.getString(TestModel.NAME));
				this.setTimestamp(memento.getBoolean(TestModel.TIMESTAMP));

				final IMemento paramMem = memento.getChild(TestModel.PARAMETERS);
				if (paramMem != null) {
					for (final IMemento mem : paramMem.getChildren(TestModel.PARAMETER)) {
						final String name = mem.getID();
						final String value = mem.getString(TestModel.VALUE);
						this.addParameter(name, value);
					}
				}
				final IMemento metaMem = memento.getChild(TestModel.METADATAS);
				if (metaMem != null) {
					for (final IMemento mem : metaMem.getChildren(TestModel.METADATA)) {
						final String name = mem.getID();
						final Boolean value = mem.getBoolean(TestModel.VALUE);
						if (value) {
							this.addMetadata(name);
						}
					}
				}

				for (IMemento attrMem : memento.getChildren(TestModel.SCHEMA)) {
					if (attrMem != null) {
						final int port = attrMem.getInteger(TestModel.PORT);
						this.addWindow(port, attrMem.getInteger(TestModel.WINDOW));
						for (final IMemento mem : attrMem.getChildren(TestModel.ATTRIBUTE)) {
							final String name = mem.getID();
							final SDFDatatype type = this.getSDFDatatype(mem.getString(TestModel.TYPE));
							final Object min = TestModel.getValue(mem.getString(TestModel.MIN), type);
							final Object max = TestModel.getValue(mem.getString(TestModel.MAX), type);
							final boolean nullValue = mem.getBoolean(TestModel.NULL);
							final boolean active = mem.getBoolean(TestModel.ACTIVE);
							this.addAttribute(port, name, new AttributeParameter(type, min, max, nullValue, active));
						}
					}
				}
			}
		} catch (final Exception e) {
			TestModel.LOG.error(e.getMessage(), e);
			throw e;
		}
	}

	private SDFDatatype getSDFDatatype(final String name) {
		final ISession session = Activator.getSession();
		final Set<SDFDatatype> datatypes = Activator.getExecutor().getRegisteredDatatypes(session);
		for (final SDFDatatype datatype : datatypes) {
			if (datatype.getQualName().equalsIgnoreCase(name)) {
				return datatype;
			}
		}
		return null;
	}

	private LogicalOperatorInformation getOperator(final String name) throws OperatorNotFoundException {
		final ISession session = Activator.getSession();
		final List<LogicalOperatorInformation> operators = Activator.getExecutor().getOperatorInformations(session);
		for (final LogicalOperatorInformation operator : operators) {
			if (operator.getOperatorName().equalsIgnoreCase(name)) {
				return operator;
			}
		}
		throw new OperatorNotFoundException(name);
	}

	private void saveToIFile(final IFile file) {
		try {
			final String fileContents = this.createInputStreamFile();
			try (InputStream is = IOUtils.toInputStream(fileContents)) {
				file.setContents(is, true, true, new NullProgressMonitor());
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private void saveToLocalFile(final File file) {
		try {
			final String fileContents = this.createInputStreamFile();
			FileUtils.write(file, fileContents);
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public Object getMin(final int port, final String attribute) {
		final AttributeParameter parameter = this.getSchema(port).get(attribute);
		if (parameter.getMin() == null) {
			final SDFDatatype type = parameter.getType();
			if (type.equals(SDFDatatype.BOOLEAN)) {
				return new Boolean(false);
			} else if (type.equals(SDFDatatype.BYTE)) {
				return new Byte(Byte.MIN_VALUE);
			} else if (type.equals(SDFDatatype.SHORT)) {
				return new Short(Short.MIN_VALUE);
			} else if (type.equals(SDFDatatype.INTEGER)) {
				return new Integer(Integer.MIN_VALUE);
			} else if ((type.equals(SDFDatatype.LONG)) || (type.equals(SDFDatatype.START_TIMESTAMP))) {
				return new Long(Long.MIN_VALUE);
			} else if (type.equals(SDFDatatype.FLOAT)) {
				return new Float(-Float.MIN_VALUE);
			} else if (type.equals(SDFDatatype.DOUBLE)) {
				return new Double(-Double.MIN_VALUE);
			} else if (type.equals(SDFDatatype.CHAR)) {
				return new Character(Character.MIN_VALUE);
			} else if (type.equals(SDFDatatype.STRING)) {
				return new String("");
			} else if (type.equals(SDFDatatype.DATE)) {
				return new Date(Long.MIN_VALUE);
			}
		}
		return parameter.getMin();
	}

	public Object getMax(final int port, final String attribute) {
		final AttributeParameter parameter = this.getSchema(port).get(attribute);
		if (parameter.getMax() == null) {
			final SDFDatatype type = parameter.getType();
			if (type.equals(SDFDatatype.BOOLEAN)) {
				return new Boolean(true);
			} else if (type.equals(SDFDatatype.BYTE)) {
				return new Byte(Byte.MAX_VALUE);
			} else if (type.equals(SDFDatatype.SHORT)) {
				return new Short(Short.MAX_VALUE);
			} else if (type.equals(SDFDatatype.INTEGER)) {
				return new Integer(Integer.MAX_VALUE);
			} else if ((type.equals(SDFDatatype.LONG)) || (type.equals(SDFDatatype.START_TIMESTAMP))) {
				return new Long(Long.MAX_VALUE);
			} else if (type.equals(SDFDatatype.FLOAT)) {
				return new Float(Float.MAX_VALUE);
			} else if (type.equals(SDFDatatype.DOUBLE)) {
				return new Double(Double.MAX_VALUE);
			} else if (type.equals(SDFDatatype.CHAR)) {
				return new Character(Character.MAX_VALUE);
			} else if (type.equals(SDFDatatype.STRING)) {
				return "\"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'-+_=#$%&*()[]{}~<>?.!:/\"";
				// String text =
				// "Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
				// Etiam lobortis facilisis sem. Nullam nec mi et neque pharetra
				// sollicitudin. Praesent imperdiet mi nec ante. Donec
				// ullamcorper, felis non sodales commodo, lectus velit ultrices
				// augue, a dignissim nibh lectus placerat pede. Vivamus nunc
				// nunc, molestie ut, ultricies vel, semper in, velit. Ut
				// porttitor. Praesent in sapien. Lorem ipsum dolor sit amet,
				// consectetuer adipiscing elit. Duis fringilla tristique neque.
				// Sed interdum libero ut metus. Pellentesque placerat. Nam
				// rutrum augue a leo. Morbi sed elit sit amet ante lobortis
				// sollicitudin. Praesent blandit blandit mauris. Praesent
				// lectus tellus, aliquet aliquam, luctus a, egestas a, turpis.
				// Mauris lacinia lorem sit amet ipsum. Nunc quis urna dictum
				// turpis accumsan semper.";
				// StringBuilder sb = new StringBuilder(text);
				// for (int i = 0; i < 256; i++) {
				// sb.append(text);
				// }
				// return sb.toString();
			} else if (type.equals(SDFDatatype.DATE)) {
				return new Date(Long.MAX_VALUE);
			}
		}
		return parameter.getMax();
	}

	public Object getZero(final int port, final String attribute) {
		final AttributeParameter parameter = this.getSchema(port).get(attribute);
		final SDFDatatype type = parameter.getType();
		if (type.equals(SDFDatatype.BOOLEAN)) {
			return new Boolean(false);
		} else if (type.equals(SDFDatatype.BYTE)) {
			return new Byte((byte) 0);
		} else if (type.equals(SDFDatatype.SHORT)) {
			return new Short((short) 0);
		} else if (type.equals(SDFDatatype.INTEGER)) {
			return new Integer(0);
		} else if ((type.equals(SDFDatatype.LONG)) || (type.equals(SDFDatatype.START_TIMESTAMP))) {
			return new Long(0l);
		} else if (type.equals(SDFDatatype.FLOAT)) {
			return new Float(0f);
		} else if (type.equals(SDFDatatype.DOUBLE)) {
			return new Double(0.0);
		} else if (type.equals(SDFDatatype.CHAR)) {
			return new Character((char) 0);
		} else if (type.equals(SDFDatatype.STRING)) {
			return "";
		} else if (type.equals(SDFDatatype.DATE)) {
			return new Date(0l);
		}

		return null;
	}

	public Object getAverage(final int port, final String attribute) {
		final AttributeParameter parameter = this.getSchema(port).get(attribute);
		final SDFDatatype type = parameter.getType();
		final Object min = this.getMin(port, attribute);
		final Object max = this.getMax(port, attribute);
		if (type.equals(SDFDatatype.BOOLEAN)) {
			return new Boolean(false);
		} else if (type.equals(SDFDatatype.BYTE)) {
			return new Byte((byte) (((((Byte) min) + ((Byte) max)) - ((Byte) min)) / 2.0));
		} else if (type.equals(SDFDatatype.SHORT)) {
			return new Short((short) (((((Short) min) + ((Short) max)) - ((Short) min)) / 2.0));
		} else if (type.equals(SDFDatatype.INTEGER)) {
			return new Integer((int) (((((Integer) min) + ((Integer) max)) - ((Integer) min)) / 2.0));
		} else if ((type.equals(SDFDatatype.LONG)) || (type.equals(SDFDatatype.START_TIMESTAMP))) {
			return new Long((long) (((((Long) min) + ((Long) max)) - ((Long) min)) / 2.0));
		} else if (type.equals(SDFDatatype.FLOAT)) {
			return new Float((float) (((((Float) min) + ((Float) max)) - ((Float) min)) / 2.0));
		} else if (type.equals(SDFDatatype.DOUBLE)) {
			return new Double(((((Double) min) + ((Double) max)) - ((Double) min)) / 2.0);
		} else if (type.equals(SDFDatatype.CHAR)) {
			return new Character((char) (((((Character) min) + ((Character) max)) - ((Character) min)) / 2.0));
		} else if (type.equals(SDFDatatype.STRING)) {
			return "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		} else if (type.equals(SDFDatatype.DATE)) {
			return new Date((long) (Long.MIN_VALUE + ((Long.MAX_VALUE - Long.MIN_VALUE) / 2.0)));
		}
		return null;
	}

	public static class AttributeParameter {
		private SDFDatatype type;
		private Object min;
		private Object max;
		private boolean nullValue;
		private boolean active;

		/**
		 * Class constructor.
		 *
		 * @param type
		 * @param min
		 * @param max
		 */
		public AttributeParameter(final SDFDatatype type, final Object min, final Object max, final boolean nullValue,
				final boolean active) {
			super();
			this.type = type;
			this.min = min;
			this.max = max;
			this.nullValue = nullValue;
			this.active = active;
		}

		/**
		 * @return the type
		 */
		public SDFDatatype getType() {
			return this.type;
		}

		/**
		 * @param type
		 *            the type to set
		 */
		public void setType(final SDFDatatype type) {
			this.type = type;
		}

		/**
		 * @return the min
		 */
		public Object getMin() {
			return this.min;
		}

		/**
		 * @param min
		 *            the min to set
		 */
		public void setMin(final Object min) {
			this.min = min;
		}

		/**
		 * @return the max
		 */
		public Object getMax() {
			return this.max;
		}

		/**
		 * @param max
		 *            the max to set
		 */
		public void setMax(final Object max) {
			this.max = max;
		}

		/**
		 * @return the nullValue
		 */
		public boolean isNullValue() {
			return this.nullValue;
		}

		/**
		 * @param nullValue
		 *            the nullValue to set
		 */
		public void setNullValue(final boolean nullValue) {
			this.nullValue = nullValue;
		}

		/**
		 *
		 * @return the active
		 */
		public boolean isActive() {
			return this.active;
		}

		/**
		 * @param active
		 *            the active to set
		 */
		public void setActive(final boolean active) {
			this.active = active;
		}

	}

	public static class SessionContext {

		private final String username;
		private final String password;

		/**
		 * Class constructor.
		 *
		 * @param username
		 * @param password
		 */
		public SessionContext(final String username, final String password) {
			super();
			this.username = username;
			this.password = password;
		}

		public String getPassword() {
			return this.password;
		}

		public String getUsername() {
			return this.username;
		}

	}

}
