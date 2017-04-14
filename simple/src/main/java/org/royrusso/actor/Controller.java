/**
 * (C) Copyright 2014 Roy Russo
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 *
 */


package org.royrusso.actor;

import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.Broadcast;
import akka.routing.RoundRobinPool;
import akka.routing.SmallestMailboxPool;
import org.royrusso.app.Runtime;
import org.royrusso.command.ProcessDataCommand;
import org.royrusso.command.Shutdown;
import org.royrusso.event.ProcessingFinishedEvent;

import java.util.UUID;

/**
 * Controller receives an instance of Shutdown and emits an Event.
 *
 * @author royrusso
 */
public class Controller extends UntypedActor {

    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final ActorRef producers;

    private long startTime;

    public Controller() {
        log.info("Controller constructor");

        Props routeeProps = Props.create(Producer.class);
        producers = context().actorOf(new RoundRobinPool(4).props(routeeProps));
    }

    @Override
    public void onReceive(Object msg) throws Exception {

        if (msg instanceof ProcessDataCommand) {
            if (Runtime.counter.getValue() >= 0) {
                log.error("Process data is already occurring at the moment!");
                unhandled(msg);
            } else {
                startTime = System.currentTimeMillis();

                Runtime.counter.setValue(0);
                producers.tell(new Broadcast(msg), self());
            }
        } else if (msg instanceof Shutdown) {
            context().system().shutdown();

        } else if (msg instanceof ProcessingFinishedEvent) {
            log.info("Producing completed by " + (System.currentTimeMillis() - startTime));
        } else if (msg instanceof String) {
            log.info(msg.toString());
        } else {
            unhandled(msg);
        }
    }
}
