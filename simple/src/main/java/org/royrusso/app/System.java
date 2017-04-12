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
import org.royrusso.actor.SimpleActor;
import org.royrusso.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main runtime class.
 */
public class System {

    private static final Logger log = LoggerFactory.getLogger(System.class);

    public static void main(String... args) throws Exception {

        final ActorSystem actorSystem = ActorSystem.create("ClusterSystem");

        Thread.sleep(5000);

        if (args.length > 0) {

            final ActorSelection simpleActorRef = actorSystem.actorSelection(
                    "akka.tcp://ClusterSystem@127.0.0.1:2553/user/simple-actor");

            simpleActorRef.tell(new Command("Hello from daddy!"), null);

            actorSystem.awaitTermination();
            return;
        }

        final ActorRef actorRef = actorSystem.actorOf(Props.create(SimpleActor.class), "simple-actor");

        actorRef.tell(new Command("CMD 1"), null);
        actorRef.tell(new Command("CMD 2"), null);
        actorRef.tell(new Command("CMD 3"), null);
        actorRef.tell(new Command("CMD 4"), null);
        actorRef.tell(new Command("CMD 5"), null);

        Thread.sleep(50000);

        log.debug("Actor System Shutdown Starting...");

        actorSystem.shutdown();
    }
}
