/*
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
 */

package org.royrusso.app;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import akka.routing.SmallestMailboxPool;
import flow.prototype.CounterVar;
import flow.prototype.NearReference;
import org.royrusso.actor.Consumer;
import org.royrusso.actor.Controller;
import org.royrusso.command.ProcessDataCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main runtime class.
 */
public class Runtime {

    public static final NearReference<Integer> counter = new NearReference<>(-1);

    public static ActorRef counterRef;

    private static final Logger log = LoggerFactory.getLogger(Runtime.class);

    public static void main(String... args) throws Exception {

        ActorSystem system = ActorSystem.create("ClusterSystem");

        Thread.sleep(5000);

        if (args.length > 0) {

            final ActorRef consumers = system.actorOf(new RoundRobinPool(4)
                    .props(Props.create(Consumer.class)), "consumers");

            final ActorSelection controller = system.actorSelection(
                    "akka.tcp://ClusterSystem@127.0.0.1:2553/user/controller");

            controller.tell("Hello from node!", ActorRef.noSender());
            controller.tell(new ProcessDataCommand(5000, 100000), ActorRef.noSender());

            system.awaitTermination();
        } else {
            createProducer(system);
        }
    }

    private static void createProducer(ActorSystem system) throws Exception {

        final ActorRef controller = system.actorOf(Props.create(Controller.class), "controller");
        counterRef = system.actorOf(CounterVar.create(counter));

        controller.tell("Initializing controller...", ActorRef.noSender());

    }
}
