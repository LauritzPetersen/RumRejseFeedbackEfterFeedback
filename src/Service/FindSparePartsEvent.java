package Service;

import Model.Spaceship;
import java.util.Random;

public class FindSparePartsEvent implements GameEvent {
    private final Random random = new Random();

    @Override
    public String run(Spaceship spaceship) {
        int foundParts = random.nextInt(7) + 1;
        spaceship.gainSpareParts(foundParts);
        return "Event Find Spare Parts: Fandt " + foundParts + " reservedele";
    }
}