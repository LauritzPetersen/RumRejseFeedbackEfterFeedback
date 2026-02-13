programmet: 
navn på skib og kaptajn indtatstes, både tal og bogstaver godtages.
events kører i en fast rækkefølge 
bruger indtaster valg der enten resulterer i at de vinder(når igennem alle events) eller taber("dør" før de når enden af "rejsen")
fejlinput håndteres i de forskellige events. 
en log af spillerens valg printes ved afslutning af spillet uanset sejr/gameover


menupunkter:
eventstorm og eventengine har to menupunkter(1, 2) forkskellige for hvert event. 
tradeevent giver spilleren mulighed for at vælge mellem 4 inputs(1,2,3,4)
vælger brugeren det første input bliver de sendt videre til en ny menu der håndterer køb af fuel. 
de andre menu valg er simple og godtager kun det specefikke input( 2, 3 eller 4) 


custom exceptions:
#criticalstatusexception kastes i checkcriticalstatus() og fanges i handlestatuscheck()
#gameoverexception kastes i checkgameoverstatus() og fanges i startgame()
#invalidtradeexception kastes i handletrade(), handleshieldpurchase(), handlerepairkitpurchase() og fanges i tradeevent()
#enginefailureexception kastes i attemptenginestart() og fanges i engineevent()


Testliste:
#testet om log altid printes uanset sejr/gameover
#testet om konsol crasher ved tomt input 
#testet om konsol crasher ved forkert input
#testet om criticalstatusexception gribes
#testet om gameoverexception gribes
#testet om invalidtradeexception gribes
#testet om enginefailureexception gribes

