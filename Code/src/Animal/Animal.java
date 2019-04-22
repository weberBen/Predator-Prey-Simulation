package Animal;
import Interfaces.I_Living;

import java.awt.Point;

import Cell.*;
import Group.*;
import Parameters.*;
import Main.*;

public abstract class Animal extends ObjectMap implements I_Living
{
	/************************************************************************************************
	 * 
	 * 										ATTRIBUTES
	 * 
	 ************************************************************************************************/
	
	private static int count = 0;
	public final int ID;
	
	protected final String specie;
	protected double energy;
	public double strength;
	protected double agility;
	protected double agressivity;
	protected double sociability;
	protected Cell zoneLive;
	protected int liveRadius;
	protected final boolean isMale;
	public final int NB_POSSIBLE_KID;
	protected int age;
	protected double height;
	protected double vision;
	protected boolean isEscaping = false;
	protected boolean canEat = false;
	protected double direction;
	protected double effectiveStrength;
	protected double effectiveAgility;
	protected double effectiveAgressivity;
	public final int teenAge;
	public final int oldAge;
	public final int maxAge;
	protected boolean isDead = false;
	
	public Animal()
	{
		ID = count;
		count++;
		
		NB_POSSIBLE_KID=3;
		specie = "";
		isMale = Math.random()<0.5;
		teenAge = 10;
		oldAge = 20;
		maxAge = 30;
		energy = 1;
		strength = Math.random();
		agility = Math.random();
		agressivity = Math.random();
		effectiveStrength = strength;
		effectiveAgility = agility;
		effectiveAgressivity = agressivity;
		age = 3;
	}
	
	public Animal(String specie,
			  double energy, 
			  double strength, 
			  double agility,
			  double agressivity, 
			  double sociability,
			  Cell zoneLive,
			  int liveRadius,
			  int NB_POSSIBLE_KID, 
			  int age,
			  double height, 
			  double vision,
			  int teenAge,
			  int oldAge,
			  int maxAge)
{
	ID = count;
	count++;
	
	this.energy = energy;
	this.specie = specie;
	this.strength = strength;
	this.agility = agility;
	this.agressivity = agressivity;
	this.sociability = sociability;
	this.isMale = Math.random()<0.5;
	this.NB_POSSIBLE_KID = NB_POSSIBLE_KID;
	this.age = age;
	this.height = height;
	this.vision = vision;
	this.teenAge = teenAge;
	this.oldAge = oldAge;
	this.maxAge = maxAge;
}
	
	/************************************************************************************************
	 * 
	 * 										GETTER/SETTER
	 * 
	 ************************************************************************************************/
	
	public Cell getZoneLive() {
		return zoneLive;
	}

	public void setZoneLive(Cell zoneLive) {
		this.zoneLive = zoneLive;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setEscaping(boolean isEscaping) 
	{
		/*When an animal is escaping its characteristics are boosted and slowly decrease to
		 * the normal stage. When come back to normal the animal cannot not be escaping anymore
		*/
		this.isEscaping = isEscaping;
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
	
	public Cell getCell(Cell[][] map)
	{ 
		Point pt = Parms.getCellCoordinate(x, y);
		return map[pt.x][pt.y];
	}
	
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
	
	public void setEnergy(double a) {this.energy=a;}
	public double getEnergy() {return energy;}
	
	public double getAge() {return age;}
	public int getID() {return ID;}
	public String getSpecie() {return specie;}
	
	public boolean isDead()
	{
		return (age>=maxAge||energy<=0);
	}
	
	/************************************************************************************************
	 * 
	 * 										FOOD METHODS
	 * 
	 ************************************************************************************************/
	
	public void addToEnergy(double d)
	{
		setEnergy(getEnergy()+d);
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
	
	
	public void eat(Cell[][] map, double dir)
	{
		setCanEat(true);
		move(map, dir);
		setCanEat(false);
	}
	
	public double setSatiety()
	{
		//the animal is fully fed
		double res = (Math.max(0, Parms.hungerThreshold-getEnergy()));
		setEnergy(Math.max(Parms.hungerThreshold,getEnergy()));
		return res;
	}

	

	/************************************************************************************************
	 * 
	 * 										UPDATE METHODS
	 * 
	 ************************************************************************************************/
	
	
	public void age() 
	{
		/* At each iteration energy of the animal is decreased and it age increased. When the animal reach a specific age
		 * (related to its specie) then its characteristics as its strength and agility definitely decreased. For a child these
		 * characteristics increased until it reach teen age.
		 * 
		 * When the animal's characteristics are boosted then the effective characteristics (for example the effective 
		 * strength) are increased but not the real characteristics. These effectives characteristics then slowly decrease.
		 */
		age++;
		energy-=Parms.DELTA_ENERGY;
		if (age<teenAge)
		{
			effectiveStrength+=(strength/age);
			effectiveAgility+=agility/age;
			strength+=(strength/age);
			agility+=agility/age;
		}
		else if (age>oldAge)//old age
		{
			strength*=0.9;
			agility*=0.9;
			effectiveStrength*=0.9;
			effectiveAgility*=0.9;
		}
		
		if (energy<Parms.criticalLifeThreshold)
		{
			effectiveStrength*=0.75;
			effectiveAgility*=0.75;
			effectiveAgressivity=Math.max(1,effectiveAgressivity*1.25);
		}
		else if (effectiveStrength<strength*1.01)
		{
			effectiveStrength = (effectiveStrength+strength)/2;
			effectiveAgility = (effectiveAgility+agility)/2;
			effectiveAgressivity = agressivity;
			if (isEscaping)
			{
				isEscaping = false;
				effectiveStrength = strength;
				effectiveAgility = agility;
			}
		}
	}

	
	
	/************************************************************************************************
	 * 
	 * 										LOCATION METHODS
	 * 
	 ************************************************************************************************/
	
	public void move(double dir)
	{
		x+=Math.cos(dir)*(Parms.DELTA_MOVE+getStrength());
		y+=Math.sin(dir)*(Parms.DELTA_MOVE+getStrength());
	}
	
	public void move(Cell[][] map)
	{
		move(direction);
		if(isEscaping)
			return;
		if(canEat)
		{
			//eat(map);
			return;
		}
	}
	
	public void move(Cell[][] map, double dir)
	{
		setDir(dir);
		move(map);
	}

	
	
	public abstract Animal createChild(Animal father);
	
	
	
	public String toString()
	{
		String type = isHerbivorous()?"Herbivore": "Carnivore";
		return "Animal(id="+ID+", type="+type+")";
	}
}