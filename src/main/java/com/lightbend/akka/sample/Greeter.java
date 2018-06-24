package com.lightbend.akka.sample;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import com.lightbend.akka.sample.Printer.Greeting;

/**
 * when defining Actors and their messages ,keep these recommendations in mind:
 *  it's a good practice to define messages with good names and rich semantic and
 *  domain specific meaning
 */
//#greeter-messages
public class Greeter extends AbstractActor {
//#greeter-messages

  /**
   * it is also a common pattern to use a static props method in the
   * class of the Actor that describes how to construct the Actor
   * @param message
   * @param printerActor
   * @return
   */
  static public Props props(String message, ActorRef printerActor) {
    return Props.create(Greeter.class, () -> new Greeter(message, printerActor));
  }

  //#greeter-messages

  /**
   * it is a good practice to put an actor's associated messages as static classes in
   * the class of the Actor. this make it easier to use, understand and debug
   * actor-based systems
   */
  static public class WhoToGreet {
    public final String who;

    public WhoToGreet(String who) {
        this.who = who;
    }
  }

  /**
   * expect messages
   */
  static public class Greet {
    public Greet() {
    }
  }
  //#greeter-messages

  private final String message;//消息
  private final ActorRef printerActor;//
  private String greeting = "";

  public Greeter(String message, ActorRef printerActor) {
    this.message = message;
    this.printerActor = printerActor;
  }

  /**
   * handler the messages
   * @return
   */
  @Override
  public Receive createReceive() {
    return receiveBuilder()
        .match(WhoToGreet.class, wtg -> {
          this.greeting = message + ", " + wtg.who;
        })
        .match(Greet.class, x -> {
          //#greeter-send-message
          printerActor.tell(new Greeting(greeting), getSelf());
          //#greeter-send-message
        })
        .build();
  }
//#greeter-messages
}
//#greeter-messages
