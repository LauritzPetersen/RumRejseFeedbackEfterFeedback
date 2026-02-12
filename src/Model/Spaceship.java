package Model;

import java.util.ArrayList;

public class Spaceship {

    private String name;
    private String captain;
    private int fuel = 100;
    private int integrity = 100;
    private int spareParts = 5;
    private int shieldLevel = 0;
    private int repairKit = 1;

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
        if(this.fuel > 100){
            this.fuel = 100;
        }
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

    public void useRepairKit(int amount){
        this.repairKit -= amount;
        if (this.repairKit < 0){
            repairKit = 0;
        }
    }
    public void gainRepairKit(int amount){
        this.repairKit += amount;
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
    public int getRepairKit() {
        return repairKit;
    }


}
