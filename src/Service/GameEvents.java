package Service;

import Model.Spaceship;
import View.GameView;
import java.util.ArrayList;
import java.util.List;

public class GameEvents {
    private Spaceship spaceship;
    private final GameView gameView = new GameView();
    private final List<String> log = new ArrayList<>();

    public void startGame() {
        try {
            spaceship = gameView.promptSpaceShipCreation();
            log.add("Spil startede med skib: " + spaceship.getName() + " og kaptajn: " + spaceship.getCaptain());

            gameView.printMessage("Velkommen ombord på " + spaceship.getName() + ", Kaptajn " + spaceship.getCaptain() + "!");
            gameView.printStatus(spaceship);


            List<GameEvent> route = new ArrayList<>();
            route.add(new StormEvent(gameView));
            route.add(new TravelEvent());
            route.add(new FindSparePartsEvent());
            route.add(new TradeEvent(gameView));
            route.add(new TravelEvent());
            route.add(new FindSparePartsEvent());
            route.add(new EngineEvent(gameView));
            route.add(new TravelEvent());
            route.add(new FindSparePartsEvent());
            route.add(new StormEvent(gameView));
            route.add(new TravelEvent());
            route.add(new FindSparePartsEvent());
            route.add(new TradeEvent(gameView));
            route.add(new TravelEvent());
            route.add(new EngineEvent(gameView));


            for (GameEvent event : route) {
                String result = event.run(spaceship);

                gameView.printMessage(result);
                log.add(result);
                gameView.printStatus(spaceship);
                handleStatusChecks();
            }

            gameView.printMessage("\nTillykke! Du gennemførte rejsen!");
            log.add("Spil gennemført med succes.");

        } catch (GameOverException e) {
            gameView.printMessage("\n" + e.getMessage());
        } catch (Exception e) {
            gameView.printMessage("En uventet fejl opstod: " + e.getMessage());
        } finally {
            printFinalLog();
        }
    }

    private void handleStatusChecks() {
        if (spaceship.getFuel() >= 0 && spaceship.getFuel() < 16) {
            String msg = "ADVARSEL: Brændstof er lavt! (" + spaceship.getFuel() + ")";
            gameView.printMessage(msg);
            log.add(msg);
        }

        if (spaceship.getIntegrity() >= 0 && spaceship.getIntegrity() < 21) {
            String msg = "ADVARSEL: Skibets tilstand er kritisk! (" + spaceship.getIntegrity() + ")";
            gameView.printMessage(msg);
            log.add(msg);
        }

        if (spaceship.getFuel() <= 0) {
            throw new GameOverException("---- GAME OVER ----\nLøb tør for brændstof.");
        }
        if (spaceship.getIntegrity() <= 0) {
            throw new GameOverException("---- GAME OVER ----\nSkibet eksploderede!");
        }
    }

    private void printFinalLog() {
        gameView.printMessage("\n---- Game-Events ----");
        for (String entry : log) {
            gameView.printMessage("- " + entry);
        }
    }
}






