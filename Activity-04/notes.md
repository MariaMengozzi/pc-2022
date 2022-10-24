# Activity 04 

in a jacamo project we can also find the env path in which we put all the things that are linked to the environment in which our agent is putted inside

the jcm file is the main of the project. here we can define the first agent of our system

in Jason a goal is managed by events.

in jacamo the ! represent a goal (es !say("hello")). here we can specify the parameters that we expect. and like prolog we can use the _ for the parameters that we want to ignore.

the cycle represented in the slide is one threaded. so we can not have race conditions

note that if we run the application with only !say("hello") the result is a failure we need to specify a plan even for these case

//the plan is for responding to a goal that is say something.

//I respond to it by 2 actions that were executed in 2 cycle of our loop

+!say(*Something*) <- .print("Hello");

​                    .print(*Something*).

the output will be:

[sample_agent] Hello -> this is the first print

[sample_agent] hello -> this is the second print

-------

/* Initial beliefs and rules */

//doing that i'm tolding that my goal depends on the language. note that I have updated the plan

*language*(*ita*).

//language(eng).

//language(jap).



/* Initial goals */



//!start.

!greet.



/* Plans */



//the plan is for responding to a goal depending on the belief, so the language.

+!greet : *language*(*eng*) 

​    <- .print("Hello").



+!greet : *language*(*eng*) 

​    <- .print("Ciao").



doing that according to the belief to the same goal I can respond in different ways.

according to our belief the output will be [sample_agent] Ciao

------

//language(ita).

*language*(*eng*).

//language(jap).



/* Initial goals */



//!start.

!greet.



/* Plans */



//the plan is for responding to a goal depending on the belief and decomposing it with 2 sub goal

+!greet : *language*(*eng*) 

​    <- .println("starting greeting");

​        !sayHello; //before going on you need to achieve this activity

​        !sayWorld; //these are sub goals. we need to declare plans also for them

​        .println("complete greeting").



-!greet //this plan is used to catch failure. now I'm reacting also to filures (so no plans)

​    <- .println("I can't react to this").

output:

starting greeting
I can't react to this

-----

/* Initial beliefs and rules */

//doing that i'm tolding that my goal depends on the language. note that I have updated the plan

//language(ita).

*language*(*eng*).

//language(jap).

/* Initial goals */

//!start.

!greet.

/* Plans */

//the plan is for responding to a goal depending on the belief and decomposing it with 2 sub goal

+!greet : *language*(*eng*) 

​    <- .println("starting greeting");

​        !sayHello; //before going on you need to achieve this activity

​        !sayWorld; //these are sub goals. we need to declare plans also for them

​        .println("complete greeting").



-!greet //this plan is used to catch failure. now I'm reacting also to failure (so no plans)

​    <- .println("I can't react to this").



+!sayHello <- .println("Hello").

+!sayWorld <- .println("world").

output:

starting greeting
Hello
world
complete greeting

----

the belief can being also from other agent. this is important to consider when thei become from itself or from someone else.

intentions are the plans that are in execution.



/* Initial beliefs and rules */

//doing that i'm tolding that my goal depends on the language. note that I have updated the plan

//language(ita).

*language*(*eng*).

//language(jap).



/* Initial goals */



//!start.

!greet.

!anotherGoal("bla", 13, *p*(1,_)).



/* Plans */



//the plan is for responding to a goal depending on the belief and decomposing it with 2 sub goal

+!greet : *language*(*eng*) 

​    <- .println("starting greeting");

​        !sayHello; //before going on you need to achieve this activity

​		.wait(10000);

​        !sayWorld; //these are sub goals. we need to declare plans also for them

​        .println("complete greeting").



-!greet //this plan is used to catch failure. now I'm reacting also to failure (so no plans)

​    <- .println("I can't react to this").



+!sayHello <- .println("Hello").

+!sayWorld <- .println("world").



//like in prolog goals are resolved by the unification

+!anotherGoal(Info1, Num, Str)

​    <- .println("another goal...", Info1);

​        .wait(10000);

​        .println("done").

+!start : true <- .print("hello world.").



{ include("$jacamoJar/templates/common-cartago.asl") }

{ include("$jacamoJar/templates/common-moise.asl") }

output:

[sample_agent] starting greeting
[sample_agent] another goal...bla
[sample_agent] Hello
[sample_agent] world
[sample_agent] complete greeting
[sample_agent] done

---

you can also add and remove belief to the belief base of an agent.

when an plan fail it no more goes on, but other plans can go on

// Agent sample_agent in project helloworld



/* Initial beliefs and rules */

//doing that i'm tolding that my goal depends on the language. note that I have updated the plan

//language(ita).

*language*(*eng*).

//language(jap).

*p*(1).



/* Initial goals */



//!start.

!greet.

!anotherGoal("bla", 13, *p*(1,_)).



/* Plans */



//the plan is for responding to a goal depending on the belief and decomposing it with 2 sub goal

+!greet : *language*(*eng*) 

​    <- .println("starting greeting");

​        !sayHello; //before going on you need to achieve this activity

​        ?p(X); //explore my belief

​        .println(X); //like prolog the variable is assigned to the value.

​        ?q(Y); //questo fallisce e quindi fallisce tutto il piano. posso aggiungere un piano per specificare che non ho info, ma in quel modo non fallisce, semplicemente esegue il piano di q(Y)

​        .println(Y);

​        !sayWorld; //these are sub goals. we need to declare plans also for them

​        .println("complete greeting").



+?q(X) <- .println("I don't know about q...").



-!greet //this plan is used to catch failure. now I'm reacting also to failure (so no plans)

​    <- .println("I can't react to this").



+!sayHello <- .println("Hello").

+!sayWorld <- .println("world").



//like in prolog goals are resolved by the unification

+!anotherGoal(Info1, Num, Str)

​    <- .println("another goal...", Info1);

​        .wait(10000);

​        .println("done").

+!start : true <- .print("hello world.").



{ include("$jacamoJar/templates/common-cartago.asl") }

{ include("$jacamoJar/templates/common-moise.asl") }

output

[sample_agent] starting greeting
[sample_agent] another goal...bla
[sample_agent] Hello
[sample_agent] 1
[sample_agent] I don't know about q...
[sample_agent] Y<no-value>
[sample_agent] world
[sample_agent] complete greeting
[sample_agent] done

------

qui è normale reagire ad un evento, poi può chiamare un azione interno che stoppa il piano, effettua delle altre cose, poi ritorna dove lo aveva stoppato. tutto questo senza avere corse critiche.

So I can react to some events while I'm performing long actions, only suspending and then resume the long action. during the suspends I react to the new event.

abbiamo visto in teoria che possiamo avere due tipologie di goal, maintainence e achievement, in jason, per poter mantenere all'infinito il piano dobbiamo gistirlo aggiungendo come subgoal del piano il goal stesso. in questo modo lo ri aggiunge alla lista degli eventi da eseguire. è come facevamo con gli attori e i messaggi che si automandavano per spezzettare la computazione e rimanere reattivi agli eventi. dobbiamo fare in questo modo perchè in jason abbiamo che un piano ha una dimensione finita, quindi dopo una sequenza di azioni quello termina sempre. per far si che persista quel goal dobbiamo mettere come ultima operazione il goal stesso

+!greet : *language*(*eng*) 

​    <- .println("starting greeting");

​        !sayHello; //before going on you need to achieve this activity

​        ?p(X); //explore my belief

​        .println(X); 

​        ?q(Y);

​        .println(Y);

​        !sayWorld; //these are sub goals. we need to declare plans also for them

​        .println("complete greeting");

​		!greet.

se ho terminato gli eventi e ho un piano in esecuzione, quello continua ad andare avanti, gli eventi quando arrivano vengono messi nella coda e poi presi.

---

in the env folder we have the artifacts that represents part of our environment. artifacts are described by model/template 

observability is a first class abstraction of our artifacts. 

artifacts are like monitor, so they can execute only one operation per time. so there aren't any race condition
