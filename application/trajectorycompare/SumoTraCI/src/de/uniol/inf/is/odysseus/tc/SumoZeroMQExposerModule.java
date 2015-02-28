/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@3e629c28
 * Copyright (c) ${year}, ${owner}, All rights reserved.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package de.uniol.inf.is.odysseus.tc;

import com.google.inject.AbstractModule;

import de.uniol.inf.is.odysseus.tc.chooser.IVehicleChooser;
import de.uniol.inf.is.odysseus.tc.chooser.PeriodicDistanceVehicleChooser;
import de.uniol.inf.is.odysseus.tc.error.IErrorGenerator;
import de.uniol.inf.is.odysseus.tc.error.RandomErrorGenerator;
import de.uniol.inf.is.odysseus.tc.geoconvert.GpsGeoConverter;
import de.uniol.inf.is.odysseus.tc.geoconvert.IGeoConverter;
import de.uniol.inf.is.odysseus.tc.interaction.ISumoInteraction;
import de.uniol.inf.is.odysseus.tc.interaction.SumoInteraction;
import de.uniol.inf.is.odysseus.tc.message.IMessageCreator;
import de.uniol.inf.is.odysseus.tc.message.SizeByteBufferMessageCreator;
import de.uniol.inf.is.odysseus.tc.sendec.ISendDecision;
import de.uniol.inf.is.odysseus.tc.sendec.MinimumSendDecision;
import de.uniol.inf.is.odysseus.tc.sending.ISender;
import de.uniol.inf.is.odysseus.tc.sending.ZeroMQSender;
import de.uniol.inf.is.odysseus.tc.service.IXMLService;
import de.uniol.inf.is.odysseus.tc.service.XPathXMLService;
import de.uniol.inf.is.odysseus.tc.shuffle.IVehicleShuffler;
import de.uniol.inf.is.odysseus.tc.shuffle.RandomVehicleSchuffler;

/**
 * Created by marcus on 28.11.14.
 */
public class SumoZeroMQExposerModule extends AbstractModule {

    private final String[] args;

    public SumoZeroMQExposerModule(String[] args) {
        this.args = args;
    }

    @Override
    protected void configure() {

        this.bind(ISumoInteraction.class).to(SumoInteraction.class).asEagerSingleton();
        this.bind(IVehicleShuffler.class).to(RandomVehicleSchuffler.class);
        this.bind(IGeoConverter.class).to(GpsGeoConverter.class);
        this.bind(ISendDecision.class).to(MinimumSendDecision.class);
        this.bind(IErrorGenerator.class).to(RandomErrorGenerator.class);

        this.bind(IVehicleChooser.class).to(PeriodicDistanceVehicleChooser.class);
        this.bind(IMessageCreator.class).to(SizeByteBufferMessageCreator.class);
        this.bind(ISender.class).to(ZeroMQSender.class);

        this.bind(ISumoParams.class).to(ProgramArgsSumoParams.class);
        this.bind(IXMLService.class).to(XPathXMLService.class);

        //
        bind(String[].class).annotatedWith(ProgramArgsSumoParams.ProgramArgs.class).toInstance(args);
    }
}
