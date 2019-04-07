package Animal;
import Interfaces.I_Living;
import Cell.*;
import Group.*;

public abstract class Animal extends ObjectMap implements I_Living
{
	protected final String specie;
	protected double energy;
	protected double strength;
	protected double agility;
	protected double agressivity;
	protected double sociability;
	protected double soc;
	protected Cell zoneFood;
	protected int foodRadius;
	protected Cell zoneLive;
	protected int liveRadius;
	protected final boolean isMale;
	protected final int nbPossibleKid;
	protected int age;
	protected final int tGestation;
	protected final int tParent;
	protected double height;
	protected Group group;

	public Animal()
	{
		tParent = 0;
		tGestation = 0;
		nbPossibleKid=0;
		specie = "";
		isMale = false;
	}
	
	public Group getGroup() {return group;}
	public void setGroup(Group group) { this.group = group;}
	
	public double getHeight() {return height;}
	public void setHeight(double height) { this.height = height;}
	
	public void setStrength(double a) {this.strength = a;}
	public double getStrength() {return strength;}

	public void setSociability(double a) {this.sociability = a;}
	public double getSociability() {return sociability;}

	public void setAgility(double a) {this.agility = a;}
	public double getAgility() {return agility;}

	public void setAggressivity(double a) {this.agressivity = a;}
	public double getAggressivity() {return agressivity;}

	public String getSpecie() {return specie;}

	public void age() {age++;}

	public void move(int dx, int dy)
	{
		x+=dx;
		y+=dy;
	}
	
	public boolean isCarnivorous()
	{
		return (this instanceof Carnivorous);
	}
	
	public boolean isHerbivorous()
	{
		return (this instanceof Herbivorous);
	}
}