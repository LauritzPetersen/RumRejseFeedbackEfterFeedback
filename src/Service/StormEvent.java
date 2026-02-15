package Service;

import Model.Spaceship;
import View.GameView;
import java.util.Random;

public class StormEvent implements GameEvent {
    private final GameView gameView;
    private final Random random = new Random();

    public StormEvent(GameView gameView) {
        this.gameView = gameView;
    }

    @Override
    public String run(Spaceship spaceship) {
        gameView.printMessage("\n---- Event Rumstorm ----\n" +
                "1) Flyv igennem stormen (Høj risiko)\n" +
                "2) Tag en omvej (Større brændstofforbrug)\n");

        while (true) {
            int choice = gameView.readUserInput("Dit valg: ");

            if (choice == 1) {
                int damage = random.nextInt(50) + 10;
                if (spaceship.getShieldLevel() > 0) {
                    damage /= (spaceship.getShieldLevel() + 1);
                }
                spaceship.takeDamage(damage);
                return "Event Rumstorm: Fløj igennem og tog " + damage + " skade";

            } else if (choice == 2) {
                int fuelCost = random.nextInt(15) + 5;
                spaceship.burnFuel(fuelCost);

                int damage = random.nextInt(25) + 1;
                if (spaceship.getShieldLevel() > 0) {
                    damage /= (spaceship.getShieldLevel() + 1);
                }
                spaceship.takeDamage(damage);
                return "Event Rumstorm: Tog omvej, brugte " + fuelCost + " fuel og tog " + damage + " skade";

            } else {
                gameView.printMessage("Ugyldigt valg, prøv igen.");
            }
        }
    }
}