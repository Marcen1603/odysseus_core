/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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
package de.uniol.inf.is.odysseus.wrapper.xls.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.extentech.ExtenXLS.CellHandle;
import com.extentech.ExtenXLS.WorkBookHandle;
import com.extentech.ExtenXLS.WorkSheetHandle;
import com.extentech.formats.XLS.CellNotFoundException;
import com.extentech.formats.XLS.WorkSheetNotFoundException;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.ProtocolMonitor;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class XLSProtocolHandler<T extends Tuple<?>> extends AbstractProtocolHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(XLSProtocolHandler.class);
    private static final String NAME = "XLS";

    private static final String WORKSHEET = "worksheet";
    private static final String XLS_FLOATING_FORMATTER = "xls.floatingformatter";
    private static final String XLS_NUMBER_FORMATTER = "xls.numberformatter";
    private static final String XLS_WRITE_METADATA = "xls.writemetadata";
    private static final String XLS_TRIM = "xls.trim";
    private static final String DELAY = "delay";
    private static final String NANODELAY = "delay";
    private static final String DELAYEACH = "delayeach";
    private static final String ADDLINENUMBERS = "addlinenumber";
    private static final String READFIRSTLINE = "readfirstline";
    private static final String DUMP_EACH_LINE = "dumpeachline";
    private static final String MEASURE_EACH_LINE = "measureeachline";
    private static final String LAST_LINE = "lastline";
    private static final String MAX_LINES = "maxlines";
    private static final String DEBUG = "debug";
    private static final String DUMPFILE = "dumpfile";

    private InputStream input;
    private OutputStream output;
    private WorkBookHandle workBook;
    private WorkSheetHandle sheet;
    private DecimalFormat floatingFormatter;
    private DecimalFormat numberFormatter;
    private String worksheet;
    private long delay;
    private int nanodelay;
    private int delayEach = 0;
    private long delayCounter = 0L;
    private boolean writeMetadata;
    private boolean trim = false;
    private boolean addLineNumber = false;
    private boolean readFirstLine = true;
    private boolean firstLineSkipped = false;
    private long dumpEachLine = -1;
    private long lastLine = -1;
    private int lineCounter = 0;
    private boolean debug = false;
    private boolean isDone = false;
    private long measureEachLine = -1;
    private long lastDumpTime = 0;
    private long lastMeasureTime = 0;
    private long basetime;
    private String dumpFile = null;
    private PrintWriter dumpOut;

    /**
     *
     */
    public XLSProtocolHandler() {
        super();
    }

    /**
     * Create a new HTML Data Handler
     * 
     * @param transfer
     * @param dataHandler
     * 
     * @param schema
     */
    private XLSProtocolHandler(final ITransportDirection direction, final IAccessPattern access, final IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
        super(direction, access, dataHandler, optionsMap);
        init_internal();
    }

    private void init_internal() {
    	OptionMap options = optionsMap;
        if (options.containsKey(XLSProtocolHandler.WORKSHEET)) {
            this.setWorksheet(options.get(XLSProtocolHandler.WORKSHEET));
        }
        if (options.containsKey(XLSProtocolHandler.DELAY)) {
            this.setDelay(Long.parseLong(options.get(XLSProtocolHandler.DELAY)));
        }
        if (options.containsKey(XLSProtocolHandler.NANODELAY)) {
            this.setNanodelay(Integer.parseInt(options.get(XLSProtocolHandler.NANODELAY)));
        }
        if (options.containsKey(XLSProtocolHandler.DELAYEACH)) {
            this.setDelayEach(Integer.parseInt(options.get(XLSProtocolHandler.DELAYEACH)));
        }
        if (options.containsKey(XLSProtocolHandler.XLS_FLOATING_FORMATTER)) {
            this.setFloatingFormatter(new DecimalFormat(options.get(XLSProtocolHandler.XLS_FLOATING_FORMATTER)));
        }
        if (options.containsKey(XLSProtocolHandler.XLS_NUMBER_FORMATTER)) {
            this.setNumberFormatter(new DecimalFormat(options.get(XLSProtocolHandler.XLS_NUMBER_FORMATTER)));
        }

        if (options.containsKey(XLSProtocolHandler.XLS_WRITE_METADATA)) {
            this.setWriteMetadata(Boolean.parseBoolean(options.get(XLSProtocolHandler.XLS_WRITE_METADATA)));
        }
        if (options.containsKey(XLSProtocolHandler.XLS_TRIM)) {
            this.setTrim(Boolean.parseBoolean(options.get(XLSProtocolHandler.XLS_TRIM)));
        }
        if (options.containsKey(XLSProtocolHandler.ADDLINENUMBERS)) {
            this.setAddLineNumber(Boolean.parseBoolean(options.get(XLSProtocolHandler.ADDLINENUMBERS)));
        }
        if (options.containsKey(XLSProtocolHandler.READFIRSTLINE)) {
            this.setReadFirstLine(Boolean.parseBoolean(options.get(XLSProtocolHandler.READFIRSTLINE)));
        }
        else {
            this.setReadFirstLine(true);
        }
        if (options.containsKey(XLSProtocolHandler.DUMP_EACH_LINE)) {
            this.setDumpEachLine(Integer.parseInt(options.get(XLSProtocolHandler.DUMP_EACH_LINE)));
        }

        if (options.containsKey(XLSProtocolHandler.MEASURE_EACH_LINE)) {
            this.setMeasureEachLine(Integer.parseInt(options.get(XLSProtocolHandler.MEASURE_EACH_LINE)));
        }

        if (options.containsKey(XLSProtocolHandler.LAST_LINE)) {
            this.setLastLine(Integer.parseInt(options.get(XLSProtocolHandler.LAST_LINE)));
        }
        if (options.containsKey(XLSProtocolHandler.MAX_LINES)) {
            this.setLastLine(Integer.parseInt(options.get(XLSProtocolHandler.MAX_LINES)));
        }
        if (options.containsKey(XLSProtocolHandler.DEBUG)) {
            this.setDebug(Boolean.parseBoolean(options.get(XLSProtocolHandler.DEBUG)));
        }
        if (options.containsKey(XLSProtocolHandler.DUMPFILE)) {
            this.setDumpFile(options.get(XLSProtocolHandler.DUMPFILE));
        }
        this.lastDumpTime = System.currentTimeMillis();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public IProtocolHandler<T> createInstance(final ITransportDirection direction, final IAccessPattern access, final OptionMap options, final IStreamObjectDataHandler<T> dataHandler) {
        final XLSProtocolHandler<T> instance = new XLSProtocolHandler<>(direction, access, dataHandler, options);
        return instance;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return XLSProtocolHandler.NAME;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ITransportExchangePattern getExchangePattern() {
        if (this.getDirection() != null && this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        }
        return ITransportExchangePattern.OutOnly;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void open() throws IOException {
        this.getTransportHandler().open();
        this.lineCounter = 0;
        if (this.getDirection().equals(ITransportDirection.IN)) {
            if ((this.getAccessPattern().equals(IAccessPattern.PULL)) || (this.getAccessPattern().equals(IAccessPattern.ROBUST_PULL))) {
                this.input = this.getTransportHandler().getInputStream();
                this.workBook = new WorkBookHandle(this.input);
                try {
                    if ("".equals(this.getWorksheet())) {
                        this.sheet = this.workBook.getWorkSheet(0);
                    }
                    else {
                        this.sheet = this.workBook.getWorkSheet(this.getWorksheet());
                    }
                }
                catch (final WorkSheetNotFoundException e) {
                    XLSProtocolHandler.LOG.error(e.getMessage(), e);
                    throw new IOException(e);
                }
            }
        }
        else {
            this.output = this.getTransportHandler().getOutputStream();
            this.workBook = new WorkBookHandle();
            try {
                if ("".equals(this.getWorksheet())) {
                    this.sheet = this.workBook.getWorkSheet(0);
                }
                else {
                    boolean contains = false;
                    for (final WorkSheetHandle ws : this.workBook.getWorkSheets()) {
                        if (ws.getSheetName().equalsIgnoreCase(this.getWorksheet())) {
                            contains = true;
                        }
                    }
                    if (!contains) {
                        this.sheet = this.workBook.createWorkSheet(this.getWorksheet());
                    }
                    else {
                        this.sheet = this.workBook.getWorkSheet(this.getWorksheet());
                    }
                }
            }
            catch (final WorkSheetNotFoundException e) {
                XLSProtocolHandler.LOG.debug(e.getMessage(), e);
                this.sheet = this.workBook.createWorkSheet(this.getWorksheet());
            }
            this.sheet.setFastCellAdds(true);
            final SDFSchema schema = this.getDataHandler().getSchema();
            int i = 0;
            if (this.isAddLineNumber()) {
                i++;
            }
            for (; i < schema.size(); i++) {
                this.sheet.add(schema.get(i).getAttributeName(), this.lineCounter, i);
            }
            this.lineCounter++;
        }
        this.delayCounter = 0;

        this.isDone = false;
        this.firstLineSkipped = false;
        if (this.isDebug()) {
            ProtocolMonitor.getInstance().addToMonitor(this);
            if (this.getDumpFile() != null) {
                this.dumpOut = new PrintWriter(this.getDumpFile());
            }
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (this.getDirection().equals(ITransportDirection.OUT)) {
            if (this.workBook != null) {
                this.workBook.write(this.output);
            }
        }
        if (this.workBook != null) {
            if (this.sheet != null) {
                this.sheet.close();
            }
            this.workBook.close();
        }
        if (this.getDirection().equals(ITransportDirection.IN)) {
            if (this.input != null) {
                this.input.close();
            }
        }
        else {
            if (this.output != null) {
                this.output.flush();
                this.output.close();
            }
        }

        this.getTransportHandler().close();
        if (this.isDebug()) {
            ProtocolMonitor.getInstance().informMonitor(this, this.lineCounter);
            ProtocolMonitor.getInstance().removeFromMonitor(this);
            if (this.dumpOut != null) {
                this.dumpOut.close();
            }
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() throws IOException {
        try {
            return ((this.sheet != null) && (this.lineCounter < this.sheet.getNumRows()));
        }
        catch (final NullPointerException e) {
            return false;
        }
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public T getNext() throws IOException {
        final T tuple = this.getNextLine();
        return tuple;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public void write(final T object) throws IOException {
        final SDFSchema schema = this.getDataHandler().getSchema();
        int i = 0;
        if (this.isAddLineNumber()) {
            this.sheet.add(this.lineCounter + "", this.lineCounter, i);
        }
        for (; i < schema.size(); i++) {
            final Object curAttribute = object.getAttribute(i);
            if (curAttribute instanceof Number) {
                if (((curAttribute instanceof Double) || (curAttribute instanceof Float)) && (this.getFloatingFormatter() != null)) {
                    this.sheet.add(this.getFloatingFormatter().format(curAttribute), this.lineCounter, i);
                }
                else if (!(((curAttribute instanceof Double) || (curAttribute instanceof Float))) && (this.getNumberFormatter() != null)) {
                    this.sheet.add(this.getNumberFormatter().format(curAttribute), this.lineCounter, i);
                }
                else {
                    this.sheet.add(curAttribute, this.lineCounter, i);
                }
            }
            else {
                this.sheet.add(curAttribute, this.lineCounter, i);
            }
        }
        if (this.isWriteMetadata()) {
            this.sheet.add(object.getMetadata().toString(), this.lineCounter, i + 1);
        }
        this.lineCounter++;
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
     * 
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final IProtocolHandler<?> o) {
        if (!(o instanceof XLSProtocolHandler)) {
            return false;
        }
        final XLSProtocolHandler<?> other = (XLSProtocolHandler<?>) o;
        if ((this.nanodelay != other.getNanodelay()) || (this.delay != other.getDelay()) || (this.delayEach != other.getDelayEach()) || (this.dumpEachLine != other.getDumpEachLine())
                || (this.measureEachLine != other.getMeasureEachLine()) || (this.lastLine != other.getLastLine()) || (this.debug != other.isDebug()) || (this.readFirstLine != other.isReadFirstLine())) {
            return false;
        }
        return true;
    }

    /**
     * @return the worksheet
     */
    public String getWorksheet() {
        return this.worksheet;
    }

    /**
     * @param worksheet
     *            the worksheet to set
     */
    public void setWorksheet(final String worksheet) {
        this.worksheet = worksheet;
    }

    /**
     * @return the delay
     */
    public long getDelay() {
        return this.delay;
    }

    /**
     * @param delay
     *            the delay to set
     */
    public void setDelay(final long delay) {
        this.delay = delay;
    }

    /**
     * @return the nanodelay
     */
    public int getNanodelay() {
        return this.nanodelay;
    }

    /**
     * @param nanodelay
     *            the nanodelay to set
     */
    public void setNanodelay(final int nanodelay) {
        this.nanodelay = nanodelay;
    }

    /**
     * @return the delayEach
     */
    public int getDelayEach() {
        return this.delayEach;
    }

    /**
     * @param delayEach
     *            the delayEach to set
     */
    public void setDelayEach(final int delayEach) {
        this.delayEach = delayEach;
    }

    /**
     * @return the readFirstLine
     */
    public boolean isReadFirstLine() {
        return this.readFirstLine;
    }

    /**
     * @param readFirstLine
     *            the readFirstLine to set
     */
    public void setReadFirstLine(final boolean readFirstLine) {
        this.readFirstLine = readFirstLine;
    }

    /**
     * @return the dumpEachLine
     */
    public long getDumpEachLine() {
        return this.dumpEachLine;
    }

    /**
     * @param dumpEachLine
     *            the dumpEachLine to set
     */
    public void setDumpEachLine(final long dumpEachLine) {
        this.dumpEachLine = dumpEachLine;
    }

    /**
     * @return the lastLine
     */
    public long getLastLine() {
        return this.lastLine;
    }

    /**
     * @param lastLine
     *            the lastLine to set
     */
    public void setLastLine(final long lastLine) {
        this.lastLine = lastLine;
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return this.debug;
    }

    /**
     * @param debug
     *            the debug to set
     */
    public void setDebug(final boolean debug) {
        this.debug = debug;
    }

    /**
     * @return the measureEachLine
     */
    public long getMeasureEachLine() {
        return this.measureEachLine;
    }

    /**
     * @param measureEachLine
     *            the measureEachLine to set
     */
    public void setMeasureEachLine(final long measureEachLine) {
        this.measureEachLine = measureEachLine;
    }

    /**
     * @return the dumpFile
     */
    public String getDumpFile() {
        return this.dumpFile;
    }

    /**
     * @param dumpFile
     *            the dumpFile to set
     */
    public void setDumpFile(final String dumpFile) {
        this.dumpFile = dumpFile;
    }

    /**
     * @return the floatingFormatter
     */
    public DecimalFormat getFloatingFormatter() {
        return this.floatingFormatter;
    }

    /**
     * @param floatingFormatter
     *            the floatingFormatter to set
     */
    public void setFloatingFormatter(final DecimalFormat floatingFormatter) {
        this.floatingFormatter = floatingFormatter;
    }

    /**
     * @return the numberFormatter
     */
    public DecimalFormat getNumberFormatter() {
        return this.numberFormatter;
    }

    /**
     * @param numberFormatter
     *            the numberFormatter to set
     */
    public void setNumberFormatter(final DecimalFormat numberFormatter) {
        this.numberFormatter = numberFormatter;
    }

    /**
     * @return the writeMetadata
     */
    public boolean isWriteMetadata() {
        return this.writeMetadata;
    }

    /**
     * @param writeMetadata
     *            the writeMetadata to set
     */
    public void setWriteMetadata(final boolean writeMetadata) {
        this.writeMetadata = writeMetadata;
    }

    /**
     * @return the trim
     */
    public boolean isTrim() {
        return this.trim;
    }

    /**
     * @param trim
     *            the trim to set
     */
    public void setTrim(final boolean trim) {
        this.trim = trim;
    }

    /**
     * @return the addLineNumber
     */
    public boolean isAddLineNumber() {
        return this.addLineNumber;
    }

    /**
     * @param addLineNumber
     *            the addLineNumber to set
     */
    public void setAddLineNumber(final boolean addLineNumber) {
        this.addLineNumber = addLineNumber;
    }

    protected T getNextLine() {
        if (!this.firstLineSkipped && !this.isReadFirstLine()) {
            this.lineCounter++;
            this.firstLineSkipped = true;
        }
        this.delay();
        final SDFSchema schema = this.getDataHandler().getSchema();
        final List<String> tuple = new ArrayList<String>(schema.size() + (this.addLineNumber ? 1 : 0));
        int i = 0;
        if (this.isAddLineNumber()) {
            tuple.add(this.lineCounter + "");
            i++;
        }
        for (; i < schema.size(); i++) {
            try {
                final CellHandle value = this.sheet.getCell(this.lineCounter, i);
                String valueString = value.getStringVal();
                if (this.isTrim()) {
                    valueString = valueString.trim();
                }
                tuple.add(valueString);
            }
            catch (final CellNotFoundException e) {
                tuple.add(null);
                XLSProtocolHandler.LOG.debug(e.getMessage(), e);
            }
        }
        if (this.isDebug()) {
            if (this.getDumpEachLine() > 0) {
                if ((this.lineCounter % this.getDumpEachLine()) == 0) {
                    final long time = System.currentTimeMillis();
                    XLSProtocolHandler.LOG.debug(this.lineCounter + " " + time + " " + (time - this.lastDumpTime) + " (" + Integer.toHexString(this.hashCode()) + ") line: " + tuple);
                    this.lastDumpTime = time;
                }
            }
            if (this.getMeasureEachLine() > 0) {
                if ((this.lineCounter % this.getMeasureEachLine()) == 0) {
                    final long time = System.currentTimeMillis();
                    // measurements.append(lineCounter).append(";").append(time
                    // - basetime).append("\n");
                    if (this.dumpOut != null) {
                        this.dumpOut.println(this.lineCounter + ";" + (time - this.basetime) + ";" + (time - this.lastMeasureTime));
                    }
                    this.lastMeasureTime = time;
                }
            }

            if ((this.getLastLine() == this.lineCounter) || (this.lineCounter == 0)) {
                final long time = System.currentTimeMillis();
                if (this.lineCounter == 0) {
                    this.basetime = time;
                }
                XLSProtocolHandler.LOG.debug(this.lineCounter + " " + time);
                if (this.dumpOut != null) {
                    this.dumpOut.println(this.lineCounter + ";" + (time - this.basetime) + ";" + (time - this.lastMeasureTime));
                }
                // measurements.append(lineCounter).append(";").append(time -
                // basetime).append("\n");
                if (this.getLastLine() == this.lineCounter) {
                    // System.out.println(measurements);
                    ProtocolMonitor.getInstance().informMonitor(this, this.lineCounter);
                    this.isDone = true;
                }
            }
        }
        this.lineCounter++;

        return this.getDataHandler().readData(tuple.iterator());
    }

    private void delay() {
        if (this.getDelayEach() > 0) {
            this.delayCounter++;
            if (this.delayCounter < this.getDelayEach()) {
                return;
            }
            this.delayCounter = 0;
        }
        if (this.getDelay() > 0) {
            try {
                Thread.sleep(this.getDelay());
            }
            catch (final InterruptedException e) {
                // interrupting the delay might be correct
                // e.printStackTrace();
            }
        }
        else {
            if (this.getNanodelay() > 0) {
                try {
                    Thread.sleep(0L, this.getNanodelay());
                }
                catch (final InterruptedException e) {
                    // interrupting the delay might be correct
                    // e.printStackTrace();
                }
            }
        }
    }

 

}
