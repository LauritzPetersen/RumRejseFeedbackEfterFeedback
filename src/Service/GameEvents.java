package Service;

import Model.CriticalStatusException;
import Service.InvalidTradeException;
import Model.Spaceship;
import View.GameView;
import java.util.ArrayList;
import java.util.Random;

public class GameEvents {
    private Spaceship spaceship;
    private GameView gameView = new GameView();
    private Random random =  new Random();
    private ArrayList<String> log;

    public GameEvents() {
        this.gameView = new GameView();
        this.log = new ArrayList<>();
    }

    public void startGame(){
        try{
            spaceship = gameView.promptSpaceShipCreation();
            eventStorm();
            tradeEvent();
        } catch (CriticalStatusException e) {
            gameView.printMessage("\n GAMER OVER:");
        } finally {
            gameView.printMessage("\n----Event Log----\n");
            for (String entry : log) {
                gameView.printMessage(entry);
            }
        }

    }

    public void checkCriticalStatus(){
        if (spaceship.getFuel() < 10){
            throw new CriticalStatusException("Brændstof er lavt!: " + spaceship.getFuel());
        }
        if (spaceship.getIntegrity() < 20){
            throw new CriticalStatusException("Integritet er lavt!:  " + spaceship.getIntegrity());
        }
    }

    public void eventStorm(){
        gameView.printMessage("\n----Event 1----\n" +
                                "1) Flyv igennem stormen (Høj risiko)\n" +
                                "2) Tag en omvej (-10 Brændstof lav risiko)\n");

        boolean validChoise = false;
        while (!validChoise) {
            try{
                int choice = gameView.readUserInput("Dit valg: ");
                int damage;

                switch (choice){
                    case 1->{
                        damage = random.nextInt(50)+1;
                        if(spaceship.getShieldLevel() > 0){
                            damage = damage / spaceship.getShieldLevel() + 1;
                            log.add("Event 1: Valgt Storm, Skade = " + damage);
                        }
                        spaceship.takeDamage(damage);
                        gameView.printMessage("\nSkade taget: " + damage);
                        gameView.printStatus(spaceship);
                        validChoise =  true;
                    }
                    case 2 ->{
                        damage = random.nextInt(20)+1;
                        spaceship.burnFuel(10);
                        spaceship.takeDamage(damage);
                        gameView.printMessage("\nBrændstof:  -10" +"\n" +
                                "Skade:      -" + damage);
                        log.add("Event 1:" +" Brændstof:  -" + spaceship.getFuel() + "Valgt omvej, Skade =  -" + damage);
                        gameView.printStatus(spaceship);
                        validChoise =  true;
                    }
                    default ->{
                        gameView.printMessage("Ikke gyldigt. Tryk venligst 1 eller 2.\n");
                    }
                }
            } catch (NumberFormatException e) {
                gameView.printMessage("Skriv venligst et tal.");
            } catch (IllegalArgumentException e) {
                gameView.printMessage("Skriv venligst et tal. Som enten er 1 eller 2\n");
            }
            checkCriticalStatus();

        }


    }


    public void tradeEvent() {
        boolean validChoise = false;
        while (!validChoise) {
            gameView.printMessage("EVENT 2 - HANDEL OG SHIELD\n" +
                    "Et rumvæsen tilbyder handel og opgraderinger\n" +
                    "Vælg handling:\n" +
                    "1) Byt reservedele for brændstof\n" +
                    "2) Opgrader shield level (koster " + "4" + " reservedele)\n" +
                    "3) Forsæt Rumrejse");
            try {
                int choice = gameView.readUserInput("Dit valg: ");

                switch (choice) {
                    case 1:
                        handleTrade();
                        break;
                    case 2:
                        handleShieldPurchase();
                        break;
                    case 3:
                        gameView.printMessage("Springer over handel/shield");
                        log.add("Event 2: Springede over handel/shield");
                        validChoise = true;
                        break;
                    default:
                        gameView.printMessage("Ugyldigt valg - ingen handling udført\n");
                        break;
                }
            } catch (NumberFormatException e) {
                gameView.printMessage("Bogstaver eller tegn er ikke gyldigt. Skriv venligst et tal.\n");
            } catch (IllegalArgumentException e) {
                gameView.printMessage("Skriv venligst et tal. Som enten er 1 eller 2\n");
            }
        }
    }


    private void handleTrade() {
        boolean validChoise = false;
        while (!validChoise) {
                try {
                    int amount = gameView.readUserInput("Indtast antal reservedele du vil handle (eller tryk 0 for at afslutte handle): > ");
                    if (amount < 0) {
                        throw new IllegalArgumentException("Antal reservedele skal være positivt.");
                    }
                    if(amount == 0){
                        validChoise = true;
                    }
                    if (amount > spaceship.getSpareParts()) {
                        validChoise =  true;
                        throw new InvalidTradeException("Du har ikke nok reservedele.");
                    }
                    spaceship.useSpareParts(amount);
                    int fuelGained = amount * 5;
                    spaceship.buyFuel(fuelGained);

                    gameView.printMessage("Handel gennemført - " + amount + " reservedele -> +" + (fuelGained) + " brændstof\n");
                    log.add("Event 2: Handel " + amount + " reservedele -> +" + (fuelGained) + " brændstof");
                    break;
                } catch (IllegalArgumentException e) {
                    gameView.printMessage("FEJL: " + e.getMessage());
                   // log.add("Event 2: Ugyldigt handel (negativ/zero)");
                } catch (InvalidTradeException e) {
                    gameView.printMessage("FEJL: " + e.getMessage());
                   // log.add("Event 2: Ugyldigt handel (for mange reservedele)");
                }
        }
    }


    private void handleShieldPurchase() {
            int SHIELD_COST = 4;
            boolean validChoise = false;
            while (!validChoise) {
                gameView.printMessage("Vil du købe shield level 1 for " + SHIELD_COST + " reservedele?");
                gameView.printMessage("1) Ja");
                gameView.printMessage("2) Nej");
                int answer = gameView.readUserInput("> ");
                switch (answer) {
                    case 1 -> {
                        if (spaceship.getSpareParts() < SHIELD_COST) {
                            log.add("Event 2: Shield køb fejlet - utilstrækkelige reservedele");
                            throw new InvalidTradeException("FEJL: Ikke nok reservedele til at købe shield.");
                        } else {
                            spaceship.useSpareParts(SHIELD_COST);
                            spaceship.upgradeShield();
                            gameView.printMessage("Shield opgraderet og aktiveret");
                            log.add("Event 2: shield level " + spaceship.getShieldLevel() + " købt");
                        }
                        validChoise = true;
                    }
                    case 2 ->{
                        gameView.printMessage("Shield-køb afbrudt");
                        log.add("Event 2: Shield køb afbrudt af spiller");
                        validChoise = true;

                    }
                    default ->{
                        gameView.printMessage("Ugyldigt input");
                    }
                }
            }
    }






}


