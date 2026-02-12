package Service;

import Model.CriticalStatusException;
import Model.GameOverException;
import Service.InvalidTradeException;
import Model.Spaceship;
import View.GameView;
import java.util.ArrayList;
import java.util.Random;

public class GameEvents {
    private Spaceship spaceship;
    private GameView gameView = new GameView();
    private Random random = new Random();
    private ArrayList<String> log;

    public GameEvents() {
        this.gameView = new GameView();
        this.random = new Random();
        this.log = new ArrayList<>();
    }

    public void startGame() {
        try {
            spaceship = gameView.promptSpaceShipCreation();
            gameView.printMessage("Spil startede med skib: " + spaceship.getName() + " og kaptajn: " + spaceship.getCaptain());
            log.add("Spil startede med skib: " + spaceship.getName() + " og kaptajn: " + spaceship.getCaptain());
            gameView.printStatus(spaceship);
            eventStorm();
            travelEvent();
            findSparePartsEvent();

            tradeEvent();
            travelEvent();
            findSparePartsEvent();

            eventEngine();
            travelEvent();
            findSparePartsEvent();

            tradeEvent();
            travelEvent();
            findSparePartsEvent();

            eventStorm();
            travelEvent();
            findSparePartsEvent();

            eventEngine();
            gameView.printMessage("\nTillykke! Du gennemførte rejsen!");
            log.add("Spil gennemført.");
        } catch (GameOverException e) {
            gameView.printMessage(e.getMessage());
        } catch (Exception e) {
            gameView.printMessage("En uventet fejl opstod: " + e.getMessage());
        } finally {
            gameView.printMessage("\n----Event Log----\n");
            for (String entry : log) {
                gameView.printMessage(entry);
            }
        }
    }

    public void findSparePartsEvent() {
        int foundParts = random.nextInt(10) + 1;
        spaceship.gainSpareParts(foundParts);
        gameView.printMessage("\nDu fandt " + foundParts + " reservedele, på din rejse!\n");
        log.add("Event Find Spare Parts: Fandt " + foundParts + " reservedele.");
        gameView.printStatus(spaceship);
    }

    public void travelEvent() {
        handleStatusChecks();
        int fuelCost = random.nextInt(15) + 5;
        spaceship.burnFuel(fuelCost);
        gameView.printMessage("\nDu rejser videre... Du forbrændte " + fuelCost + " brændstof.");
        log.add("Rejse: Brugte " + fuelCost + " brændstof.");
        handleStatusChecks();
    }



    public void checkCriticalStatus() {
        if (spaceship.getFuel() > 1 && spaceship.getFuel() < 15) {
            throw new CriticalStatusException("Brændstof er lavt!: " + spaceship.getFuel());
        }
        if (spaceship.getFuel() > 1 && spaceship.getIntegrity() < 20) {
            throw new CriticalStatusException("Integritet er lavt!:  " + spaceship.getIntegrity());
        }
    }


    public void checkGameOverStatus() {
        if (spaceship.getFuel() <= 0) {
            throw new GameOverException("Brændstof er opbrugt!");
        }
        if (spaceship.getIntegrity() <= 0) {
            throw new GameOverException("Skibet er ødelagt!");
        }
    }


    private void handleStatusChecks() {
        try {
            checkCriticalStatus();
        } catch (CriticalStatusException e) {
            gameView.printMessage("\nADVARSEL: " + e.getMessage());
        }
        checkGameOverStatus();
    }



    public void eventStorm() {
        gameView.printMessage("\n----Event Rumstorm----\n" +
                "1) Flyv igennem stormen (Høj risiko)\n" +
                "2) Tag en omvej (Større brændstofs forbrug men lavere risiko)\n");
        while (true) {
            int choice = gameView.readUserInput("Dit valg: ");

            if (choice == 1) {
                int damage = random.nextInt(50) + 10;

                if (spaceship.getShieldLevel() > 0) {
                    damage = damage / (spaceship.getShieldLevel() + 1);
                }
                spaceship.takeDamage(damage);
                log.add("Event Rumstorm: Valgte at flyve igennem stormen og tog " + damage + " skade");
                gameView.printMessage("Du tog " + damage + " skade da du fløj igennem stormen.");
                break;
            } else if (choice == 2) {
                int fuelCost = random.nextInt(15) + 5;
                spaceship.burnFuel(fuelCost);
                int damage = random.nextInt(25) + 1;
                if (spaceship.getShieldLevel() > 0) {
                    damage = damage / (spaceship.getShieldLevel() + 1);
                }
                spaceship.takeDamage(damage);
                log.add("Event Rumstorm: Valgte omvej, forbrændte " + fuelCost + " ekstra brændstof og tog " + damage + " skade");
                gameView.printMessage("Du forbrændte " + fuelCost + " ekstra brændstof og tog " + damage + " skade da du tog omvejen.");
                break;
                } else {
                    gameView.printMessage("Ugyldigt valg, prøv igen.");
                }
            }
            handleStatusChecks();
        }



    public void tradeEvent() {
        boolean trading = true;
        while (trading) {
            gameView.printMessage("EVENT - TradeStation\n" +
                    "Et rumvæsen tilbyder handel og opgraderinger\n" +
                    "Vælg handling:\n" +
                    "1) Byt reservedele for brændstof (5 fuel pr reservedel)\n" +
                    "2) køb repair kit (koster 15 reservedele)\n" +
                    "3) Opgrader shield level (koster 4 reservedele)\n" +
                    "4) Forsæt Rumrejse");

            int choice = gameView.readUserInput("Dit valg: ");
            try {
                if (choice == 1) {
                    handleTrade();
                } else if (choice == 2) {
                    handleRepairKitPurchase();
                } else if (choice == 3) {
                    handleShieldPurchase();
                } else if (choice == 4) {
                    gameView.printMessage("Du valgte at forsætte rumrejsen");
                    log.add("Event TradeStation: spiller fortsatte rumrejsen");
                    trading = false;
                } else {
                    gameView.printMessage("Ugyldigt valg, prøv igen.");
                }
            } catch (IllegalArgumentException | InvalidTradeException e) {
                gameView.printMessage("FEJl: " + e.getMessage());
            }
            if (trading) {
                gameView.printStatus(spaceship);
            }
        }
        handleStatusChecks();
    }


    private void handleTrade() {
        int amount = gameView.readUserInput("Antal dele at bytte (tryk 0 for at gå tilbage) ");

        if (amount == 0) return;
        if (amount < 0) throw new IllegalArgumentException("Antal kan ikke være negativt.");
        if (amount > spaceship.getSpareParts()) throw new InvalidTradeException("Ikke nok reservedele.");

        int fuelBought = amount * 5;
        if(spaceship.getFuel() + fuelBought > 100){
            throw new InvalidTradeException("kan ikke købe så meget brændstof!, maks brændstof er 100.");
        }
        spaceship.useSpareParts(amount);
        spaceship.buyFuel(fuelBought);

        gameView.printMessage("Succes! Byttede " + amount + " dele for " + fuelBought + " fuel.");
        log.add("Event TradeStation: Byttede " + amount + " dele for " + fuelBought + " fuel.");
    }


    private void handleShieldPurchase() {
        int cost = 4;
        if (spaceship.getSpareParts() < cost) {
            throw new InvalidTradeException("Mangler reservedele (Koster " + cost + ", du har " + spaceship.getSpareParts() + ")");
        }
        spaceship.useSpareParts(cost);
        spaceship.upgradeShield();
        gameView.printMessage("Shield opgraderet til level " + spaceship.getShieldLevel() + "!");
        log.add("Event TradeStation: Opgraderede Shield.");
    }

    private void handleRepairKitPurchase() {
        int cost = 15;
        if (spaceship.getSpareParts() < cost) {
            throw new InvalidTradeException("Mangler reservedele (Koster " + cost + ", du har " + spaceship.getSpareParts() + ")");
        }
        spaceship.useSpareParts(cost);
        spaceship.gainRepairKit(1);
        gameView.printMessage("Købte 1 repair kit! Du har nu " + spaceship.getRepairKit() + " repair kits.");
        log.add("Event TradeStation: Købte 1 repair kit.");
    }




    public void eventEngine() {
        gameView.printMessage("\n----Event Motorfejl----\n" + "Din motor er stoppet!");

        boolean engineRunning = false;
        int attempts = 0;

        while (!engineRunning) {
            gameView.printStatus(spaceship);
            gameView.printMessage("\nMuligheder:\n" +
                    "1) Brug et repair kit (Sikker succes)\n" +
                    "2) Forsøg at genstarte motoren (Risikabelt - Forsøg nr. " + (attempts + 1) + ")\n");

            int choice = gameView.readUserInput("Dit valg: ");

            if (choice == 1) {
                if (spaceship.getRepairKit() > 0) {
                    spaceship.useRepairKit(1);
                    gameView.printMessage("Du brugte et repair kit...");
                    log.add("Event Motorfejl: Brugte Repair Kit og fixede motoren.");
                    engineRunning = true;
                } else {
                    gameView.printMessage("Du har ikke flere repair kits. Du må bruge manuel genstart.");
                }
            } else if (choice == 2) {
                gameView.printMessage("Gør klar til manuel genstart...");
                try {
                    attemptEngineStart();
                    engineRunning = true;
                    gameView.printMessage("Motoren startede igen!");
                    log.add("Event Motorfejl: Manuel genstart lykkedes ved forsøg " + (attempts + 1));
                } catch (EngineFailureException e) {
                    attempts++;
                    int damage = random.nextInt(30) + 5;
                    if (spaceship.getShieldLevel() > 0) {
                        damage = damage / (spaceship.getShieldLevel() + 1);
                        spaceship.takeDamage(damage);

                        gameView.printMessage("FEJL: " + e.getMessage());
                        gameView.printMessage("Skibet tog " + damage + " skade.");
                        log.add("Event Motorfejl: Genstart fejlede (Forsøg " + attempts + ").");

                        handleStatusChecks();
                    }
                }
            } else {
                gameView.printMessage("Ugyldigt valg, prøv igen.");
            }
        }
    }

    private void attemptEngineStart () {
        int chance = random.nextInt(100) + 1;
        if (chance > 20) {
            throw new EngineFailureException("Motoren hoster, hakker og går i stå igen!");
        }
    }
}







