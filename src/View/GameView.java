package View;

import Model.Spaceship;

import java.util.Scanner;

public class GameView {

    private final Scanner input = new Scanner(System.in);

    public int readUserInput(String prompt){
        while(true){
            System.out.println(prompt);
            try{
                return Integer.parseInt(input.nextLine());
            } catch(NumberFormatException e){
                System.out.println("Ikke et gyldigt tal. Prøv igen");
            }
        }
    }

    public void printMessage(String message){
        System.out.println(message);
    }

    public void printStatus(Spaceship spaceship){
        System.out.println("\n----Ship Status----\n" +
                            "Brændstof:    " + spaceship.getFuel() + "\n" +
                            "Integritet    " + spaceship.getIntegrity() + "\n" +
                            "Reservedele:  " + spaceship.getSpareParts() + "\n" +
                            "Skjold:       " + spaceship.getShieldLevel() + "\n"+
                            "Repair kit:   " + spaceship.isRepairKitUsed() +
                            "\n-------------------\n");
    }

    public void printLog(Spaceship spaceship){
        System.out.println("\n----Event Log----\n");
        for (String entry : spaceship.getLog()){
            System.out.println(entry);
        }
    }

}
