/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package cc.kuka.odysseus.wrapper.jasper.physicaloperator.access;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class JasperProtocolHandler<T extends Tuple<?>> extends AbstractProtocolHandler<T> {
    private static final Logger LOG = LoggerFactory.getLogger(JasperProtocolHandler.class);

    private static final String TEMPLATE = "template";
    private static final String PARAMETER = "parameter";
    private static final String MULTI = "multi";

    private static final String NAME = "Jasper";

    private OutputStream output;
    private boolean isDone = false;
    private String template;
    private boolean multi = false;
    private JasperReport report;
    private final Map<String, Object> parameter = new HashMap<>();
    private final Collection<Map<String, ?>> columns = new LinkedList<>();

    /**
     *
     */
    public JasperProtocolHandler() {
        super();
    }

    /**
     *
     * Class constructor.
     *
     * @param direction
     *            The transport direction
     * @param access
     *            The access pattern
     * @param dataHandler
     *            The data handler
     */
    private JasperProtocolHandler(final ITransportDirection direction, final IAccessPattern access, final IStreamObjectDataHandler<T> dataHandler, final OptionMap options) {
        super(direction, access, dataHandler, options);
        this.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return JasperProtocolHandler.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access, final OptionMap options, final IStreamObjectDataHandler<T> dataHandler) {
        final JasperProtocolHandler<T> instance = new JasperProtocolHandler<>(direction, access, dataHandler, options);
        return instance;

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        return ITransportExchangePattern.OutOnly;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void open() throws IOException {
        this.getTransportHandler().open();
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.OUT)) {
            try (FileInputStream templateStream = new FileInputStream(this.getTemplate())) {
                try {
                    final JasperDesign design = JRXmlLoader.load(templateStream);
                    this.report = JasperCompileManager.compileReport(design);
                }
                catch (final JRException e) {
                    JasperProtocolHandler.LOG.error(e.getMessage(), e);
                    throw new IOException(e.getMessage(), e);
                }
            }

            this.output = this.getTransportHandler().getOutputStream();
        }
        this.isDone = false;

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.OUT)) {
            if (this.isMulti()) {
                try {
                    final JasperPrint print = JasperFillManager.fillReport(this.report, this.parameter, new JRMapCollectionDataSource(this.columns));
                    JasperExportManager.exportReportToPdfStream(print, this.output);
                }
                catch (final JRException e) {
                    JasperProtocolHandler.LOG.error(e.getMessage(), e);
                    throw new IOException(e.getMessage(), e);
                }
            }
            this.report = null;
            this.output.close();
        }

        this.getTransportHandler().close();

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() throws IOException {
        return false;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public T getNext() throws IOException {
        return null;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public void write(final T object) throws IOException {
        int i = 0;
        final Map<String, Object> column = new HashMap<>();
        for (final SDFAttribute attribute : this.getDataHandler().getSchema()) {
            column.put(attribute.getAttributeName(), object.getAttribute(i));
            i++;
        }
        this.columns.add(column);
        if (!this.isMulti()) {
            try {
                final JasperPrint print = JasperFillManager.fillReport(this.report, this.parameter, new JRMapCollectionDataSource(this.columns));
                JasperExportManager.exportReportToPdfStream(print, this.output);
                this.output.flush();
            }
            catch (final JRException e) {
                JasperProtocolHandler.LOG.error(e.getMessage(), e);
                throw new IOException(e.getMessage(), e);
            }
            this.columns.clear();
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isDone() {
        return this.isDone;
    }

    /**
     * @return the template
     */
    public String getTemplate() {
        return this.template;
    }

    /**
     * @param template
     *            the template to set
     */
    public void setTemplate(final String template) {
        this.template = template;
    }

    /**
     * @return the multi
     */
    public boolean isMulti() {
        return this.multi;
    }

    /**
     * @param multi
     *            the multi to set
     */
    public void setMulti(final boolean multi) {
        this.multi = multi;
    }

    /**
     * @return the parameter
     */
    public Map<String, Object> getParameter() {
        return this.parameter;
    }

    public void addParameter(final String key, final String value) {
        this.parameter.put(key, value);
    }

    public void removeParameter(final String key) {
        this.parameter.remove(key);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> o) {
        if (!(o instanceof JasperProtocolHandler)) {
            return false;
        }
        final JasperProtocolHandler<?> other = (JasperProtocolHandler<?>) o;
        if ((!this.getTemplate().equals(other.getTemplate()))) {
            return false;
        }
        return true;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        if (options.containsKey(JasperProtocolHandler.TEMPLATE)) {
            this.setTemplate(options.get(JasperProtocolHandler.TEMPLATE));
        }
        if (options.containsKey(JasperProtocolHandler.MULTI)) {
            this.setMulti(Boolean.parseBoolean(options.get(JasperProtocolHandler.MULTI)));
        }
        if (options.containsKey(JasperProtocolHandler.PARAMETER)) {
            final String[] params = options.get(JasperProtocolHandler.PARAMETER).split(",");
            for (final String paramPair : params) {
                final String[] param = paramPair.split("=");
                if (param.length >= 2) {
                    this.addParameter(param[0], param[1]);
                }
            }
        }
    }
}
