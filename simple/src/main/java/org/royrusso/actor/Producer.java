package org.royrusso.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import flow.prototype.messages.IncVarCommand;
import flow.prototype.messages.IncVarResult;
import org.royrusso.app.Runtime;
import org.royrusso.command.ProcessDataCommand;
import org.royrusso.event.ProcessElementEvent;
import org.royrusso.event.ProcessingFinishedEvent;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by Vlad on 12.04.2017.
 */
public class Producer extends UntypedActor {

    private final String consumerPath = "akka.tcp://ClusterSystem@127.0.0.1:2554/user/consumers";
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private final ActorSelection consumers;

    private ProcessDataCommand pdata;
    private long roundStart;
    private int generatedByRound;
    private boolean isGenerating;
    private ActorRef controller;

    public Producer() {
        consumers = getContext().actorSelection(consumerPath);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof IncVarResult) {
            produce((IncVarResult)message);
        } else if (message instanceof ProcessDataCommand) {
            if (isGenerating) {
                log.error("Invalid producer state");
                return;
            }

            isGenerating = true;
            controller = sender();
            pdata = (ProcessDataCommand)message;

            generatedByRound = 0;
            roundStart = System.currentTimeMillis();

            if (Runtime.counter.getValue() < pdata.getTotal()) {
                Runtime.counterRef.tell(new IncVarCommand(pdata.getTotal()), self());
            }
        } else {
            unhandled(message);
        }
    }

    private void produce(IncVarResult counter) {
        if (counter.isSuccess()) {

            generatedByRound++;
            consumers.tell(new ProcessElementEvent(String.valueOf(counter.getResult())), getContext().parent());

            if (counter.getResult() == pdata.getTotal()) {
                isGenerating = false;
                controller.tell(new ProcessingFinishedEvent(), getContext().parent());

            } else if (Runtime.counter.getValue() < pdata.getTotal()) {

                IncVarCommand nextIter = new IncVarCommand(pdata.getTotal());

                //Sleep if
                if (generatedByRound == pdata.getTempo()) {
                    generatedByRound = 0;

                    long delay = 1000 - (System.currentTimeMillis() - roundStart);

                    roundStart = System.currentTimeMillis() + delay;
                    log.info("Going sleep for " + delay + " ms.");

                    getContext().system().scheduler().scheduleOnce(
                            Duration.create(delay, TimeUnit.MILLISECONDS),
                            Runtime.counterRef, nextIter, getContext().dispatcher(), self()
                    );

                } else {
                    Runtime.counterRef.tell(nextIter, self());
                }
            }
        } else {
            log.info("Producing finished at - " + self().path());
        }
    }
}
