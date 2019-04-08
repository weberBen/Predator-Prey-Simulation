package Animal;
import Interfaces.I_Living;
import Cell.*;
import Group.*;
import Parameters.*;


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
	protected double vision;
	protected Group group;

	public Animal()
	{
		tParent = 0;
		tGestation = 0;
		nbPossibleKid=0;
		specie = "";
		isMale = false;
	}
	
	public double getNeedsToEat()
	{
		double res = energy - Parms.hungerThreshold;
		return (res<0)?Math.abs(res):0;
	}
	
	public boolean needToEat()
	{
		return getNeedsToEat()!=0;
	}
	
	public Group getGroup() {return group;}
	public void setGroup(Group group) { this.group = group;}
	
	public double getHeight() {return height;}
	public void setHeight(double height) { this.height = height;}
	
	public double getVision() {return vision;}
	public void setvision(double vision) { this.vision = vision;}
	
	public void setStrength(double a) {this.strength = a;}
	public double getStrength() {return strength;}

	public void setSociability(double a) {this.sociability = a;}
	public double getSociability() {return sociability;}

	public void setAgility(double a) {this.agility = a;}
	public double getAgility() {return agility;}

	public void setAggressivity(double a) {this.agressivity = a;}
	public double getAggressivity() {return agressivity;}

	public String getSpecie() {return specie;}

	public void age() 
	{
		/* LA fonction est appellée à chaque itération, elle garantie le veillessement de l'animal
		 * Si l'animal a très faim (energy en deçà d'un certain seuil criticalLifeThreshold a chaque appel de 
		 * la fonction l'animal voie sa force, son agility décroite de manière exponentielle et son agresivité s'accroit
		 * 
		 * Pour un enfant, sa force et son agility augmente peu à peu jusqu'a à certain (propre a chaque espece) au delà duquel
		 * l'age de la force et l'agility decroit peu à peu
		 * 
		 * Il faut deux arguments un arguments donné la valeur d'un paramètre au meilleur de la forme et un autre donnant la valeur effective 
		 * (exemple force au meilleure de la forme et force effective qui est affecté en cas de seuil d'enrgie extreme, pour l'agility pareil
		 * et pour l'agressivité une fois l'energie remonté au dessa du seuil revient à la normal, en fonction de l'erngie comme x) La valeur
		 * au meilleur de la forme augmente ou diminue selon l'age
		 */
		age++;
	}

	public void move(double dx, double dy)
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
	
	public abstract void eat(Cell[][] map);
	
	public Cell getCell(Cell[][] map)
	{ 
		return map[(int)x][(int)y];
	}
}