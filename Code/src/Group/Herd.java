package Group;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;
import Cell.*;
import Main.*;

/* A Herd is exclusively composed of herbivorous animals.
 * There is no hierarchy between members of the herd
 * 
 * Note that a herd cannot be a part of another group
 */

public class Herd extends Group
{
	/************************************************************************************************
	 * 
	 * 										ATTRIBUTES
	 * 
	 ************************************************************************************************/
	
	ArrayList<Animal> members;//list of members of the herd
	
	private Herd()
	{
		super();
		members = new ArrayList<Animal>();
	}
	
	public Herd(Animal a) throws IllegalArgumentException
	{
		//an herbivorous is considered as a herd of one animal
		this();
		if(!a.isHerbivorous())
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
		
		addAnimal(a);
	}
	
	
	/************************************************************************************************
	 * 
	 * 										GETTER/SETTER
	 * 
	 ************************************************************************************************/
	
	public Animal _getAnimal()
	{
		/* For a single herbivorous animal return the animal
		 * contains in it (thus the first element of the list of members)
		 */
		if(members.size()!=1)
			return null;
		
		return members.get(0);
	}
	
	
	public int getSize()
	{
		return members.size();
	}
	
	
	public double getStrength()
	{
		/* return the global strength of the pack (which is the sum of the strength of its members)*/
		double res = 0;
		for(Animal o : members)
		{
			res+=o.getStrength();
		}
		
		return res*getSociability();
	}
	
	public double getAgility()
	{
		/* return the global strength of the pack (which is the average of the agility of its members)*/
		double res = 0;
		
		for(Animal o : members)
		{
			res+=o.getAgility();
		}
		
		return (res/members.size());
	}
	
	
	public double getAgressivity()
	{
		/* Because the chief make all the decision, agressivity of the
		 * pack only depends on the chief's agressivity.
		 */
		double res = 0;
		
		for(Animal o : members)
		{
			res+=o.getAgressivity();
		}
		
		return (res/members.size());
	}
	
	public double getSociability()
	{
		/* The sociability of a group must decrease while its size (its number of members) increase*/
		return Parms.sociabilityHerd(getSize());
	}
	
	
	public String getSpecie()
	{
		/* If all the members are part of the same specie then return that one
		 * else return unknown specie
		 */
		String specie = null;
		if(members.size()>0)
		{
			specie = members.get(0).getSpecie();
			for(Animal o: members)
			{
				if(!specie.equals(o.getSpecie()))
				{
					return null;
				}
			}
		}
		
		return specie;
	}
	
	public boolean isCarnivorous()
	{
		return false;
	}
	
	public boolean isHerbivorous()
	{
		return true;
	}
	
	/************************************************************************************************
	 * 
	 * 										ADD METHODS
	 * 
	 ************************************************************************************************/
	
	public void add(Group o) throws IllegalArgumentException 
	{
		if(o==null || o.getSize()==0)
			return;
		
		if(!Parms.nearToInteract(this, o))
		{
			/* Group can only interact (especially merge) if there are close enough*/
			throw new IllegalArgumentException("Les deux groupes d'animaux doivent être proches pour intéragir");
			/* If the group to add is not on the same cell that the current one, some troubles could appears 
			 * during the removal of the group to add on the map
			*/
		}
		
		o.setGroup(this);
		if(o.isHerbivorousAnimal() || o.isHerd())//a single herbivorous animal or a herd (for merging two herd)
		{
			addHerd((Herd)o);
		}else if(o.isHerbivorousFamily())//an herbivorous family
		{
			addFamily((Family)o);
		}else
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}

		World.addToLog("\t+ Assimilation dans "+ getFormatedInfo() +" de "+o);//write to log
	}
	
	private void addAnimal(Animal a)
	{
		/* add a single animal */
		members.add(a);
	}
	
	private void addFamily(Family f)
	{
		/* Inside a herd all the family disappears and replaced by
		 * a stand alone animals
		 */
		members.add(((Herd)f.getFather()).getAnimal());
		members.add(((Herd)f.getMother()).getAnimal());
		members.addAll(f.getChildren());
		World.removeGroup(f);
	}
	
	private void addHerd(Herd herd)
	{
		/* merge two herd or add a single animal */
		members.addAll(herd.members);
		if(herd.getGroup()==null)
			World.removeGroup(herd);
	}
	
	
	
	/************************************************************************************************
	 * 
	 * 										DEATH METHODS
	 * 
	 ************************************************************************************************/
	
	public double setDeath()
	{
		double res = 0;
		for (Animal a : members)
			res+=a.getEnergy();
		members.clear();
		World.removeGroup(this);
		return res;
	}
	
	public double setDeath(Group o)
	{
		if(o==null)
			return 0;
		
		double res = 0;
		if(o!=null && o.isHerd())
		{
			Herd h = (Herd)o;
			Animal a = h.members.get(0);
			res = setDeath(a);
		}
		return res;
	}
	
	public double setDeath(Animal a)
	{
		double res = a.getEnergy();
		members.remove(a);
		manageIntegrity();
		return res;
	}
	
	private double setDeathRandom()
	{
		int index = (int)(Math.random()*members.size());
		return setDeath(members.get(index));
	}
	
	public double setDeath(int number)
	{
		if(number>getSize())
		{
			return setDeath();
		}
		double res = 0;
		for(int i=0; i<number; i++)
		{
			res+=setDeathRandom();
		}
		return res;
	}
	
	
	private void checkDeath()
	{
		/* If an animal dies of old age, or dies because it energy was too low
		 * then we need to remove it from the pack.
		 */
		ArrayList<Animal> temp = new ArrayList<Animal>(members);
		for(Animal a : temp)
		{
			if(a.isDead())
			{
				setDeath(a);
			}
		}
		
		manageIntegrity();
	}
	
	private boolean manageIntegrity()
	{
		/* A herd is not a herd anymore when there is no more animals in it */
		if(members.size()==0)
		{
			World.removeGroup(this);
			return true;
		}
		
		return false;
	}
	
	
	
	/************************************************************************************************
	 * 
	 * 										UPDATE METHODS
	 * 
	 ************************************************************************************************/
	
	public void age()
	{
		/* Based on the method age of Animal, only apply that method to each member of the pack*/
		for(Animal o : members)
		{
			o.age();
		}
		
		checkDeath();
	}
	
	public boolean interact(Group o)
	{
		if(o==null)
			return false;
		/*les herbivores sont paisibles; ils n'attaqueront jamais un autre groupe
		 * only interacts whith other herbivorous
		 */
		
		if(o.isHerd() || o.isHerbivorousAnimal())
		{
			if (o.isHerbivorousAnimal() && this.isHerbivorousAnimal() && 
				((Herd)o)._getAnimal().isMale()^this._getAnimal().isMale() &&
				this.getSpecie().equals(o.getSpecie()))
			{
				Family f = new Family(this,o);
				World.addGroup(f);
				World.removeGroup(o);
				World.removeGroup(this);
				o.acted = true;
				return true;
			}
			
			if(Math.min(this.getSociability(),o.getSociability())>0.7)
			{
				o.acted = true;
				addHerd((Herd)o);
				return true;
			}
		}
		else if(o.isHerbivorousFamily() && Math.min(this.getSociability(),o.getSociability())>0.7)
		{
			o.acted = true;
			addFamily((Family)o);
			return true;
		}
		return false;
	}
	
	public void update(Cell[][] map)
	{
		/* d�placer
		 * interragir avec ses voisins
		 * manger si il faut
		 * vieillir 
		 * */
		if(acted)
			return;
		if(manageIntegrity())
			return;
		
		double angle = Parms.getDirectionForAnimal(map, this.members.get(0));
		moveTo(angle);
		if(members.get(0).getIsEscaping())
		{
			age();
			return;
		}
		
		Cell pos = World.getCell(location.getX(), location.getY());
		for(Group o : new ArrayList<Group>(pos.getGroups()))
		{
			if(o!=this)
				if(interact(o))
					break;
		}
		eat(pos);
		age();
		if(Math.random()<0.1)
		{
			if(getSize()==0)
				return;
			
			Animal h = members.get((int)(Math.random()*getSize()));
			Animal child = h.createChild(members.get((int)(Math.random()*getSize())));
			
			add(World.createGroup(child, this));
		}
	}
	/************************************************************************************************
	 * 
	 * 										FOOD METHODS
	 * 
	 ************************************************************************************************/
	
	public boolean needToEat()
	{
		/* The herd need to eat if one animal inside it needs to eat*/
		return getNeedsToEat()!=0;
	}
	
	public double getNeedsToEat()
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		
		for(Animal o : members)
		{
			if(o.needToEat())
			{
				output+=o.getNeedsToEat();//if the group does not need to eat return 0
			}
		}
		
		return output;
	}
	
	
	public void findFood(Cell[][] map)
	{
		double angle = 0;
		
		for(Animal o : members)
		{
			angle+=Parms.getDirectionForAnimal(map, o);
		}
		angle/=getSize();
		setDir(angle);
		
		for(Animal o :members)
		{
			o.setCanEat(true);
			o.move(map, angle);
			o.setCanEat(false);
		}
	}
	
	public void eat(Cell cell)
	{
		/*if there is enough food, every animal eats enough
		 * else the food is shared between all the hungry animals
		 */
		double aManger = this.getNeedsToEat();
		ArrayList<Animal> hungry = new ArrayList<Animal>();
		for(Animal a : members)
		{
			if (a.needToEat())
			{
				hungry.add(a);
			}
		}
		
		double food;
		for(Animal a : hungry)
		{
			food = cell.getFood(aManger/hungry.size());
			a.addToEnergy(food);
		}
	}
	
	public double setSatiety()
	{
		for (Animal a : members)
		{
			a.setSatiety();
		}
		return 0;
	}
	
	public double feed(double dispo)
	{
		return 0;
	}
	
	
	/************************************************************************************************
	 * 
	 * 										LOCATION METHODS
	 * 
	 ************************************************************************************************/
	
	public Point2D getLocation()
	{
		if(members.size()==0)
			return null;
		
		Animal a = members.get(0);
		return new Point2D.Double(a.getX(), a.getY());
	}
	
	public void moveTo(double x, double y)
	{
		Point2D old_pos = getLocation();
		
		for(Animal a : members)
		{
			a.moveTo(x, y);
		}
		
		Point2D new_pos = getLocation();
		updateLocationOnMap(old_pos, new_pos);
		setLocation();//need to update location
	}
	
	
	
	
	public String toString()
	{
		if(members.size()==0)
			return null;
		
		String res = getFormatedInfo()+ "\n";
		for(Animal a : members)
			res+="\t"+a.toString()+"\n";
		
		return res;
	}
}
