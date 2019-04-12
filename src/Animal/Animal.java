package Animal;
import Interfaces.I_Living;
import Cell.*;
import Group.*;
import Parameters.*;
import Main.*;

public abstract class Animal extends ObjectMap implements I_Living
{
	private static int count = 0;
	public final int ID;
	
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
	protected boolean isEscaping;
	protected boolean canEat;
	protected double direction;
	protected double effectiveStrength;
	protected double effectiveAgility;
	protected double effectiveAgressivity;

	public Animal()
	{
		ID = count;
		count++;
		
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
	
	//constructeur par copie
	public double getDir() {return direction;}
	public void setDir(double a) {direction = a;}
	
	public boolean getIsEscaping() {return isEscaping;}
	public void setIsEscaping(boolean a)  {isEscaping = a;}
	
	public boolean getCanEat() {return canEat;}
	public void setCanEat(boolean a) {canEat = a;}
	
	public double getHeight() {return height;}
	public void setHeight(double height) { this.height = height;}
	
	public double getVision() {return vision;}
	public void setvision(double vision) { this.vision = vision;}
	
	public void setStrength(double a) {this.effectiveStrength = a;}
	public double getStrength() {return effectiveStrength;}

	public void setSociability(double a) {this.sociability = a;}
	public double getSociability() {return sociability;}

	public void setAgility(double a) {this.effectiveAgility = a;}
	public double getAgility() {return effectiveAgility;}

	public void setAgressivity(double a) {this.effectiveAgressivity = a;}
	public double getAgressivity() {return effectiveAgressivity;}

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
		 * 
		 * Lorsque les paramètres redeviennent normaux, escape = false
		 */
		age++;
	}

	public void move(double dir)
	{
		x+=Math.cos(direction)*(Parms.DELTA_MOVE+getStrength());
		y+=Math.sin(direction)*(Parms.DELTA_MOVE+getStrength());
	}
	
	public void move(Cell[][] map)
	{
		move(direction);
		if(isEscaping)
			return;
		if(canEat)
		{
			eat(map);
			return;
		}
	}
	
	public void move(Cell[][] map, double dir)
	{
		setDir(dir);
		move(map);
	}
	
	public void boost(double coeff)
	{
		setStrength(strength*coeff);
		setAgressivity(agressivity*coeff);
		setAgility(agility*coeff);
	}
	
	public void escape(double dir)
	{
		setIsEscaping(true);
		boost(1.5);
		setDir(dir);
		move(dir);
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
	
	
	public void fight(Group o)
	{
		if(o.getStrength()>getStrength())
		{
			if(getAgility()<o.getAgility())//animal loose the fight
			{
				//setDeath();
				
			}else//animal will loose the fight
			{
				if(getAgressivity()>Math.random())//animal try to attack
				{
					//setDeath();
				}else//animal try to escape
				{
					if(Math.random()<0.5)//fail to espace
					{
						//setDeath();
					}else
					{
						escape(o.getDir());
					}
				}
			}
		}
		else
		{
			if(getAgility()>o.getAgility())//animal loose the fight
			{
				o.setDeath();//all the members of the group die
			}else//animal will loose the fight
			{
				if(getAgressivity()>Math.random())//animal try to attack
				{
					//setDeath();
				}else//animal try to escape
				{
					if(Math.random()<0.5)//fail to espace
					{
						//setDeath();
					}else
					{
						escape(o.getDir());
					}
				}
			}
		}
	}
	
	public String toString()
	{
		String type = isHerbivorous()?"Herbivore": "Carnivore";
		return "Animal(id="+ID+", type="+type+")";
	}
}