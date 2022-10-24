// Agent sample_agent in project helloworld

/* Initial beliefs and rules */
//doing that i'm tolding that my goal depends on the language. note that I have updated the plan
//language(ita).
language(eng).
//language(jap).
p(1).

/* Initial goals */

//!start.
!greet.
!anotherGoal("bla", 13, p(1,_)).

/* Plans */

//the plan is for responding to a goal depending on the belief and decomposing it with 2 sub goal
+!greet : language(eng) 
    <- .println("starting greeting");
        !sayHello; //before going on you need to achieve this activity
        ?p(X); //explore my belief
        .println(X); //like prolog the variable is assigned to the value.
        ?q(Y); //questo fallisce e quindi fallisce tutto il piano. posso aggiungere un piano per specificare che non ho info, ma in quel modo non fallisce, semplicemente esegue il piano di q(Y)
        .println(Y);
        !sayWorld; //these are sub goals. we need to declare plans also for them
        .println("complete greeting").

+?q(X) <- .println("I don't know about q...").

-!greet //this plan is used to catch failure. now I'm reacting also to failure (so no plans)
    <- .println("I can't react to this").

+!sayHello <- .println("Hello").
+!sayWorld <- .println("world").

//like in prolog goals are resolved by the unification
+!anotherGoal(Info1, Num, Str)
    <- .println("another goal...", Info1);
        .wait(10000);
        .println("done").



+!start : true <- .print("hello world.").

{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
