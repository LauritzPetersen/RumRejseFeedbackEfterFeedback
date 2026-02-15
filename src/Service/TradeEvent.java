package Service;

import Model.Spaceship;

import View.GameView;
import java.util.ArrayList;
import java.util.List;

public class TradeEvent implements GameEvent {
    private final GameView gameView;

    public TradeEvent(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public String run(Spaceship spaceship) {
        boolean trading = true;
        List<String> eventLogs = new ArrayList<>();
        eventLogs.add("Event TradeStation");

        while (trading) {
            gameView.printMessage("\n--- EVENT - TradeStation ---\n" +
                    "Et rumvæsen tilbyder handel og opgraderinger\n" +
                    "Vælg handling:\n" +
                    "1) Byt reservedele for brændstof (5 fuel pr reservedel)\n" +
                    "2) Køb repair kit (koster 15 reservedele)\n" +
                    "3) Opgrader shield level (koster 4 reservedele)\n" +
                    "4) Fortsæt Rumrejse");

            int choice = gameView.readUserInput("Dit valg: ");
            try {
                if (choice == 1) {
                    String result = handleTrade(spaceship);
                    gameView.printMessage(result);
                    eventLogs.add(result);
                } else if (choice == 2) {
                    String result = handleRepairKitPurchase(spaceship);
                    gameView.printMessage(result);
                    eventLogs.add(result);
                } else if (choice == 3) {
                    String result = handleShieldPurchase(spaceship);
                    gameView.printMessage(result);
                    eventLogs.add(result);
                } else if (choice == 4) {
                    gameView.printMessage("Du valgte at fortsætte rumrejsen.");
                    eventLogs.add("Spilleren forlod stationen");
                    trading = false;
                } else {
                    gameView.printMessage("Ugyldigt valg, prøv igen.");
                }
            } catch (IllegalArgumentException | InvalidTradeException e) {
                gameView.printMessage("FEJL: " + e.getMessage());
            }

            if (trading) {
                gameView.printStatus(spaceship);
            }
        }
        return String.join(". ", eventLogs);
    }

    private String handleTrade(Spaceship spaceship) {
        int amount = gameView.readUserInput("Antal dele at bytte (tryk 0 for at gå tilbage): ");

        if (amount == 0) return "Handel annulleret";
        if (amount < 0) throw new IllegalArgumentException("Antal kan ikke være negativt.");
        if (amount > spaceship.getSpareParts()) throw new InvalidTradeException("Ikke nok reservedele.");

        int fuelBought = amount * 5;
        if (spaceship.getFuel() + fuelBought > 100) {
            throw new InvalidTradeException("Kan ikke købe så meget brændstof! Maks er 100.");
        }

        spaceship.useSpareParts(amount);
        spaceship.buyFuel(fuelBought);
        return "Byttede " + amount + " dele for " + fuelBought + " fuel";
    }

    private String handleShieldPurchase(Spaceship spaceship) {
        int cost = 4;
        if (spaceship.getSpareParts() < cost) {
            throw new InvalidTradeException("Mangler reservedele (Koster " + cost + ")");
        }
        spaceship.useSpareParts(cost);
        spaceship.upgradeShield();
        return "Opgraderede skjold til level " + spaceship.getShieldLevel();
    }

    private String handleRepairKitPurchase(Spaceship spaceship) {
        int cost = 15;
        if (spaceship.getSpareParts() < cost) {
            throw new InvalidTradeException("Mangler reservedele (Koster " + cost + ")");
        }
        spaceship.useSpareParts(cost);
        spaceship.gainRepairKit(1);
        return "Købte 1 repair kit";
    }
}