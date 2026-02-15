package Service;

import Model.Spaceship;
import java.util.Random;

public class TravelEvent implements GameEvent {
    private final Random random = new Random();

    @Override
    public String run(Spaceship spaceship) {
        int fuelCost = random.nextInt(15) + 5;
        spaceship.burnFuel(fuelCost);
        return "Rejse: Brugte " + fuelCost + " brændstof";
    }
}
