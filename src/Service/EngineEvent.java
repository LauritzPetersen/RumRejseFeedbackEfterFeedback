package Service;

import Model.Spaceship;
import Service.EngineFailureException;
import View.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EngineEvent implements GameEvent {
    private final GameView gameView;
    private final Random random = new Random();

    public EngineEvent(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public String run(Spaceship spaceship) {
        gameView.printMessage("\n---- Event Motorfejl ----\nDin motor er stoppet!");

        List<String> eventLogs = new ArrayList<>();
        eventLogs.add("Event Motorfejl");

        boolean engineRunning = false;
        int attempts = 0;

        while (!engineRunning) {
            gameView.printStatus(spaceship);
            gameView.printMessage("\nMuligheder:\n" +
                    "1) Brug et repair kit (Sikker succes)\n" +
                    "2) Forsøg at genstarte motoren (Risikabelt - Forsøg nr. " + (attempts + 1) + ")");

            int choice = gameView.readUserInput("Dit valg: ");

            if (choice == 1) {
                if (spaceship.getRepairKit() > 0) {
                    spaceship.useRepairKit(1);
                    gameView.printMessage("Du brugte et repair kit...");
                    eventLogs.add("Brugte Repair Kit og fixede motoren");
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
                    eventLogs.add("Manuel genstart lykkedes ved forsøg " + (attempts + 1));
                } catch (EngineFailureException e) {
                    attempts++;
                    int damage = random.nextInt(15) + 5;
                    spaceship.takeDamage(damage);

                    gameView.printMessage("FEJL: " + e.getMessage());
                    gameView.printMessage("Skibet tog " + damage + " skade.");
                    eventLogs.add("Genstart fejlede (Forsøg " + attempts + ", tog " + damage + " skade)");

                    if (spaceship.getIntegrity() <= 0) {
                        return String.join(": ", eventLogs) + " - SKIBET BLEV ØDELAGT UNDER REPARATION";
                    }
                }
            } else {
                gameView.printMessage("Ugyldigt valg, prøv igen.");
            }
        }

        return String.join(": ", eventLogs);
    }

    private void attemptEngineStart() {
        int chance = random.nextInt(100) + 1;
        if (chance > 15) {
            throw new EngineFailureException("Motoren hoster, hakker og går i stå igen!");
        }
    }
}