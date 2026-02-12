import Model.Spaceship;
import Service.GameEvents;
import View.GameView;

public class Main {
    public static void main(String[] args) {
        GameEvents gameEvents = new GameEvents();
        gameEvents.startGame();
    }
}
