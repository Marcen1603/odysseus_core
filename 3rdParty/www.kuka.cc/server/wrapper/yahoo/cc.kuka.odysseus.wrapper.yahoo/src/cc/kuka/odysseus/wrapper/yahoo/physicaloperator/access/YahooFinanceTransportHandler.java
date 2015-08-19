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
package cc.kuka.odysseus.wrapper.yahoo.physicaloperator.access;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class YahooFinanceTransportHandler extends YahooTransportHandler {
    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(YahooFinanceTransportHandler.class);

    /** The name of the protocol handler. */
    private static final String NAME = "YahooFinance";
    /** Init parameter. */
    private static final String SYMBOLS = "symbols";

    /**
     * Class constructor.
     *
     */
    public YahooFinanceTransportHandler() {
        super();
    }

    /**
     * Class constructor.
     *
     * @param protocolHandler
     * @param options
     */
    public YahooFinanceTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
    }

    @Override
    protected void init(final OptionMap options) {
        if (options.containsKey(YahooFinanceTransportHandler.SYMBOLS)) {
            final String[] symbols = options.get(YahooFinanceTransportHandler.SYMBOLS).split(",");
            final StringBuilder yqlQuery = new StringBuilder();
            yqlQuery.append("select * from yahoo.finance.options%s where ");
            int i = 0;
            for (final String symbol : symbols) {
                if (i > 0) {
                    yqlQuery.append("OR ");
                }
                yqlQuery.append(String.format("symbol='%s'", symbol));
                i++;
            }
            this.setYQL(yqlQuery.toString());
        }
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return YahooFinanceTransportHandler.NAME;
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        return new YahooFinanceTransportHandler(protocolHandler, options);
    }

}
