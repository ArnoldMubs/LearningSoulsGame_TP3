package lsg.characters;
import java.lang.String;
import java.util.Locale;
import lsg.helpers.*;
import lsg.weapons.*;


public abstract class Character {
    private String name;
    private int life;
    private int maxLife;
    private int stamina;
    private int maxStamina;
    private Dice dice;
    private Weapon weapon;

    public Character (String name) {
        this.name = name;
        this.dice = new Dice(101);
    }

    public String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    public int getLife() {
        return life;
    }

    protected void setLife(int life) {
        this.life = life;
    }

    public int getMaxLife() {
        return maxLife;
    }

    protected void setMaxLife(int maxLife) {
        this.maxLife = maxLife;
    }

    public int getStamina() {
        return stamina;
    }

    protected void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    protected void setMaxStamina(int maxStamina) {
        this.maxStamina = maxStamina;
    }

    public void printStats () {
        System.out.println(this.toString());
    }

    public boolean isAlive() {
        return this.life > 0;
    }

    public Weapon getWeapon() { return weapon; }

    public void setWeapon(Weapon weapon) { this.weapon = weapon; }

    private int attackWith(Weapon weapon) {
        int roll,degats;
        if (weapon.isBroken()){
            degats = 0;
        }else {
            roll = dice.roll();
            if (roll == 0){
                degats = weapon.getMinDamage();
            }else if (roll == 100){
                degats = weapon.getMaxDamage();
            }else {
                degats = weapon.getMinDamage() + (int)(((weapon.getMaxDamage() - weapon.getMinDamage())*((float)roll/100)));
            }

        }
        if (((float)this.stamina/weapon.getStamCost()) >= 1.0){
            this.stamina -= weapon.getStamCost();
        }
        else{
            degats = Math.round(degats * ((float)this.stamina/weapon.getStamCost()));
            this.stamina = 0;
        }
        weapon.use();
        degats = degats + Math.round(degats*this.computeBuff()/100);
        return degats;
    }

    public int attack (){
        return attackWith(this.weapon);
    }

    public int getHitWith(int value){
        int v;
        float protection = this.computeProtection();
        if ( protection > 100.0f) {
            v = 0;
        }else {
            v = value - Math.round(value * protection/100);
        }

        this.life -= v > this.life ? this.life:v;
        return v;
    }

    protected  abstract float computeProtection();
    protected  abstract float computeBuff();

    @Override
    public String toString() {
        if (isAlive()) {
            return String.format("%-20s %-20s LIFE:%-10s STAMINA:%-10s PROTECTION:%-10s BUFF:%-10s(ALIVE)","[ "+getClass().getSimpleName()+" ]",this.getName(),String.format("%5d",this.getLife()),String.format("%5d",this.getStamina()),String.format(Locale.US,"%6.2f",this.computeProtection()),String.format(Locale.US,"%6.2f",this.computeBuff()));
        }
        return String.format("%-20s %-20s LIFE:%-10s STAMINA:%-10s PROTECTION:%-10s BUFF:%-10s","[ "+getClass().getSimpleName()+" ]",this.getName(),String.format("%5d",this.getLife()),String.format("%5d",this.getStamina()),String.format(Locale.US,"%6.2f",this.computeProtection()),String.format(Locale.US,"%6.2f",this.computeBuff()));
    }
}
