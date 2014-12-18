/*
 * com.maddyhome.idea.copyright.pattern.ProjectInfo@3188017d
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

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.tc.chooser.IVehicleChooser;
import de.uniol.inf.is.odysseus.tc.error.IErrorGenerator;
import de.uniol.inf.is.odysseus.tc.geoconvert.IGeoConverter;
import de.uniol.inf.is.odysseus.tc.interaction.ISumoInteraction;
import de.uniol.inf.is.odysseus.tc.message.IMessageCreator;
import de.uniol.inf.is.odysseus.tc.sendec.ISendDecision;
import de.uniol.inf.is.odysseus.tc.sending.ISender;
import de.uniol.inf.is.odysseus.tc.shuffle.IVehicleShuffler;

/**
 * Created by marcus on 29.11.14.
 */
public class ApplicationLoop {

    private final ISumoInteraction sumoInteraction;
    private final IVehicleChooser vehicleChoser;
    private final IVehicleShuffler vehicleShuffler;
    private final IGeoConverter geoConverter;
    private final IErrorGenerator errorService;
    private final ISendDecision sendDecision;
    private final IMessageCreator messageCreator;
    private final ISender sender;

    @Inject
    public ApplicationLoop(ISumoInteraction sumoInteraction, IVehicleChooser vehicleChoser, IVehicleShuffler vehicleShuffler, IGeoConverter geoConverter, IErrorGenerator errorService, ISendDecision sendDecision, IMessageCreator messageCreator, ISender sender) {
        this.sumoInteraction = sumoInteraction;
        this.vehicleChoser = vehicleChoser;
        this.vehicleShuffler = vehicleShuffler;
        this.geoConverter = geoConverter;
        this.errorService = errorService;
        this.sendDecision = sendDecision;
        this.messageCreator = messageCreator;
        this.sender = sender;
    }

    public void run() {
        while(true) {
                sender.send(
                        //
                        messageCreator.create(
                                //
                                sendDecision.getValuesToSend(
                                        // Decorate
                                        errorService.accumulate(
                                                //
                                                geoConverter.convert(
                                                        //Decorate
                                                        vehicleShuffler.shuffle(
                                                                //
                                                                vehicleChoser.choose(
                                                                        //
                                                                        sumoInteraction.next()
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                );

        }
    }
}
