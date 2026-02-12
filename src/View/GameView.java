package View;

import Model.Spaceship;
import Service.GameEvents;

import java.util.Scanner;

import static java.nio.file.Files.readString;

public class GameView {

    private final Scanner scanner = new Scanner(System.in);


    public int readUserInput(String prompt) throws NumberFormatException {
        while(true){
            System.out.println(prompt);
            return Integer.parseInt(scanner.nextLine());

        }
    }
    public String readLine(String prompt){
        while(true){
            System.out.println(prompt);
            String input = scanner.nextLine().trim();
            if(!input.isEmpty()){
                return input;
            }
            System.out.println("Den kan ikke være tom. Skriv venligst noget.");
        }
    }

    public Spaceship promptSpaceShipCreation(){
        printMessage("====Velkommen til dit rum eventyr====");
        String name = readLine("Skriv skibets navn: ");
        String captain = readLine("Skriv dit navn: ");
        return new Spaceship(name, captain);
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



}
