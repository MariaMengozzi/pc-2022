// Agent that use the artifact

/* Initial beliefs and rules */

/* Initial goals */
!go.

/* Plans */
+!go <- !createCounter(C);
        !useCounter(C).

//create an artifact this is done by recursion.
+!createCounter(Id)
    <- makeArtifact(c0, "tools.Counter", [10] ,Id); //create a new piece of environment dynamically, c0 is the logical name it is different from the id, because this last is more at the pc level
    println("counter created ", Id);
    focus(Id). //focus on the artifact and observe its state. the observe property of the artifact it is automatically translated in my belief, so now I can react to the changing of state of the artifact. I need to add focus before using the artifact

+!useCounter(C)
    <- println("going to use counter ", C);
        inc; // qui non specifico quale artefatto prendere. dato che ne ho solo uno sceglierà per forza c0, ma se ne avessi di più l'artefatto viene scelto in modo non deterministico.
        inc [aid(C)]; //i use annotation to add information. 
        println("done").

+count(C) 
    <- println("new count: ", C).

//these actions are not atomic together. so there are not race condition between action but can be between palns due to they are interlived. 
// so it is still important saying that also plan sometimes must be performed atomically. for doing this we use an annotation.
+tick : number_ticks(N)
    <- println("tick!"); //events of artifacts are not added to the belief of the agent, so if i want to react to them i need to add manually to my belifs
    -+number_ticks(N + 1).


{ include("$jacamoJar/templates/common-cartago.asl") }
{ include("$jacamoJar/templates/common-moise.asl") }

// uncomment the include below to have an agent compliant with its organisation
//{ include("$moiseJar/asl/org-obedient.asl") }
