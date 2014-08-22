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
package de.uniol.inf.is.odysseus.wrapper.opcda;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.mep.IFunction;
import de.uniol.inf.is.odysseus.mep.IFunctionProvider;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.ErrorFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.QualityFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.TimestampFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.ToOPCValueFunction;
import de.uniol.inf.is.odysseus.wrapper.opcda.function.ValueFunction;

/**
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class OPCDAFunctionProvider implements IFunctionProvider {
    private static final Logger LOG = LoggerFactory.getLogger(OPCDAFunctionProvider.class);

    public OPCDAFunctionProvider() {

    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public List<IFunction<?>> getFunctions() {

        final List<IFunction<?>> functions = new ArrayList<>();
        try {
            functions.add(new ErrorFunction());
            functions.add(new QualityFunction());
            functions.add(new ValueFunction());
            functions.add(new TimestampFunction());

            functions.add(new ToOPCValueFunction());
        }
        catch (final Exception e) {
            OPCDAFunctionProvider.LOG.error(e.getMessage(), e);
        }
        return functions;
    }

}
