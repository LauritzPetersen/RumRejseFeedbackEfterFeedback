package Model;

import java.util.ArrayList;

public class Spaceship {

    private String name;
    private String captain;
    private int fuel = 100;
    private int integrity = 100;
    private int spareParts = 10;
    private int shieldLevel = 0;
    private boolean repairKitUsed = false;

    private final ArrayList<String> log = new ArrayList<>();

    public Spaceship(String name, String captain) {
        this.name = name;
        this.captain = captain;
    }

    public void burnFuel(int amount){
        this.fuel -= amount;
        if (this.fuel < 0){
            this.fuel = 0;
        }
    }
    public void buyFuel(int amount){
        this.fuel += amount;
    }

    public int takeDamage(int amount){
        this.integrity -= amount;
        if(this.integrity < 0){
            this.integrity = 0;
        }
        return this.integrity;
    }

    public void useSpareParts(int amount){
        this.spareParts -= amount;
        if (this.spareParts < 0){
            spareParts = 0;
        }
    }
    public void gainSpareParts(int amount){
        this.spareParts += amount;
    }

    public void upgradeShield(){
        this.shieldLevel++;
    }

    public boolean useRepairKit(){
        if (repairKitUsed){
            return false;
        }
        repairKitUsed = true;
        return true;
    }


    public String getName() {
        return name;
    }
    public String getCaptain() {
        return captain;
    }
    public int getFuel() {
        return fuel;
    }
    public int getIntegrity() {
        return integrity;
    }
    public int getSpareParts() {
        return spareParts;
    }
    public int getShieldLevel() {
        return shieldLevel;
    }
    public boolean isRepairKitUsed() {
        return repairKitUsed;
    }


}
