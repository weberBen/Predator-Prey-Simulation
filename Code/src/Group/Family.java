package Group;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import Animal.Animal;
import Cell.Cell;
import Parameters.Parms;
import Main.*;

/* A family is composed of :
 *  - a "father" (not related to it sex) which must find food for it family
 *  - a "mother" which must protect the children
 *  - a list of child
 *  
 *  A family can be herbivorous or carnivorous
 *  
 *  Note that the "father" of the family is the one which must move and must interact with others group. Then few parameters 
 *  will be only based on the "father"
 */

public class Family extends Group
{
	/************************************************************************************************
	 * 
	 * 									ATTRIBUTES
	 * 
	 ************************************************************************************************/
	
	private Animal father;
	private Animal mother;
	private ArrayList<Animal> children;
	
	private Family(){};
	
	public Family(Group g_father, Group g_mother) throws IllegalArgumentException
	{
		super();
		
		if(!g_father.isAnimal() || !g_mother.isAnimal())//a family is only made of single animals
			throw new IllegalArgumentException("Group incompatible avec une famille");
		else if(!g_father.sameFoodDiet(g_mother))//mother and father must have the same diet
			throw new IllegalArgumentException("Les membres d'une famille doivent être de le même catégorie (herbivore ou carnivore)");
		else if(!Parms.nearToInteract(g_father, g_mother))
		{
			/* Group can only interact (especially merge) if there are close enough*/
			throw new IllegalArgumentException("Les deux groupes d'animaux doivent être proches pour intéragir\n"
					+ "\tPosition groupe 1 : "+g_father.getLocation()+"\n\tPosition groupe 2 : "+g_mother.getLocation());
			/* If the group to add is not on the same cell that the current one, some troubles could appears 
			 * during the removal of the group to add on the map
			*/
		}
		
		children = new ArrayList<Animal>();
		setFather(g_father);
		setMother(g_mother);
		addChildren();
	}
	
	
	
	/************************************************************************************************
	 * 
	 * 									GETTER/SETTER
	 * 
	 ************************************************************************************************/
	
	private void setFather(Group o)
	{
		/* Before adding the group as the father
		 * we need to check if the group is a stand alone group
		 * or part of another group. If the group is a stand alone one, then
		 * we need to remove its reference from the list of groups in the main (the run of the simulation)
		 */
		
		father = o.getAnimal();
		if(o.getGroup()==null)//stand alone group
		{
			World.removeGroup(o);//remove reference in the list of groups in the run of the simulation
		}
	}
	
	
	public Group getFather()
	{
		/* Return the father as a single animal (represented by a group)*/
		if(father==null)
			return null;
		
		return World.createGroup(father, this);
	}
	
	
	private void setMother(Group o)
	{
		mother = o.getAnimal();
		if(o.getGroup()==null)
		{
			World.removeGroup(o);
		}
	}
	
	public Group getMother()
	{
		if(mother==null)
			return null;
		
		return World.createGroup(mother, this);
	}
	
	
	public ArrayList<Animal> getChildren()
	{
		return new ArrayList<Animal>(children);
	}
	
	
	
	
	
	public double getStrength()
	{
		/* return a sum of strength of the members*/
		double res = 0;
		if(father!=null)
		{
			res+=father.getStrength();
		}
		
		if(mother!=null)
		{
			res+=mother.getStrength();
		}
		
		for(Animal a : children)
		{
			if(a!=null)
				res+=a.getStrength();
		}
		
		return res;
	}	

	
	public double getSociability()
	{
		/* return an average of sociability of the members*/
		double res = 0;
		int count = 0;
		if(father!=null)
		{
			res+= father.getSociability();
			count++;
		}
		
		if(mother!=null)
		{
			res+=mother.getSociability();
			count++;
		}
		
		return res/count;
	}
	

	public double getAgility()
	{
		/* return an average of agility of the members*/
		double res = 0;
		int count=0;
		if(father!=null)
		{
			res+=father.getAgility();
			count++;
		}
		
		if(mother!=null)
		{
			res+=mother.getAgility();
			count++;
		}
		
		for(Animal a : children)
		{
			res+=a.getAgility();
			count++;
		}
		
		return res/count;
	}
	
	
	public double getAgressivity()
	{
		/* return an average of agility of the members (only of the father and of the mother)*/
		double res = 0;
		int count = 0;
		if(father!=null)
		{
			res+=father.getAgressivity();
			count++;
		}
		
		if(mother!=null)
		{
			res+=mother.getAgressivity();
			count++;
		}
		
		return res/count;
	}
	
	
	public String getSpecie()
	{
		/* All the member of the family must by of the same specie*/
		return father.getSpecie();
	}
	
	
	public int getSize()
	{
		if(father==null)
			return 0;
		
		int count = 0;
		if(father!=null)
		{
			count++;
		}
		
		if(mother!=null)
		{
			count++;
		}
		
		return count + children.size();
	}	
	
	
	private int getNewMother()
	{
		/* When the mother dies we need to find a new one inside the children.
		 * The child with the highest strength will become the new mother.
		 * Return the index of that child inside the list of child
		 */
		double max_strength = -1;
		double temp;
		int index = -1;
		
		for(int i=0; i<children.size(); i++)
		{
			Animal a = children.get(i);
			if((temp=a.getStrength())>max_strength)
			{
				max_strength = temp;
				index = i;
			}
		}
		
		return index;
	}
	
	public boolean isCarnivorous()
	{
		/* Members of group must have the same diet*/
		return father!=null && father.isCarnivorous();
	}
	
	
	public boolean isHerbivorous()
	{
		/* Members of group must have the same diet*/
		return father!=null && father.isHerbivorous();
	}
	
	
	/************************************************************************************************
	 * 
	 * 									ADD METHODS
	 * 
	 ************************************************************************************************/
	
	
	public void add(Group o)
	{
		//do nothing (cannot add an element into a family)
	}
	
	private void addChildren()
	{
		/* Based on the possible number of the specie modulated create the correct number of children*/
		
		int numChildren = (int)(1 + Math.random()*(mother.NB_POSSIBLE_KID));
		
		Animal a;
		
		for(int i=0; i<numChildren; i++)
		{
			a = mother.createChild(father);
			addChild(a);
		}
	}

	private void addChild(Animal a)
	{
		children.add(a);
	}
	

	/************************************************************************************************
	 * 
	 * 									DEATH METHODS
	 * 
	 ************************************************************************************************/
	
	private void checkDeath()
	{
		/* If an animal dies of old age, or dies because it energy was too low
		 * then we need to remove it from the family.
		 */
		ArrayList<Animal> temp = new ArrayList<Animal>(children);
		for(Animal a : temp)
		{
			if(a.isDead())
			{
				setDeath(a);
			}
		}
		
		if(mother!=null && mother.isDead())
		{
			setDeathForMother();
		}
		
		if(father!=null && father.isDead())
		{
			setDeathForFather();
		}
		
		manageIntegrity();
	}
	
	
	private double setDeathRandom()
	{
		//do not check integrity after that function (if the function is called to kill more than one member in a loop
		int index = (int)(Math.random()*getSize());
		if(index==0)
		{
			return setDeathForFather();
		}else if(index==1)
		{
			return setDeathForMother();
		}else
		{
			return setDeathForChild(children.get(index-2));
		}
		//do not check integrety after that function (if the function is called to kill more than one member in a loop
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
		
		manageIntegrity();
		return res;
	}
	
	public double setDeath()
	{	
		double res = father.getEnergy()+mother.getEnergy();
		for(Animal o : children)
		{
			res+=o.getEnergy();
		}
		/* Before killing everey animals inside the family we need to check if the family
		 * is a part of an other group (which can only be a pack). If so, then we need to update 
		 * the pack by notifying it that the family dies. Else the family is a stand alone group
		 * and we need to remove it from the list of groups in the run of the simulation
		 */
		if(getGroup()!=null)//part of a pack
		{
			//the following declarations match the operation of the function inside split
			father = null;
			mother = null;
			
			Pack p = (Pack)getGroup();
			p.splitFamily(this);//call a function of pack
		}else
		{
			World.removeGroup(this);
		}
		return res;
	}
	
	public double setDeath(Animal a)
	{
		if(a==null)
			return 0;
		
		double res = 0;
		if(a==father)
		{
			res = setDeathForFather();
		}else if(a==mother)
		{
			res = setDeathForMother();
		}else 
		{
			res = setDeathForChild(a);
		}
		
		manageIntegrity();
		return res;
	}
	
	
	public double setDeath(Group o)
	{
		if(o==null)
			return 0;
		
		/* We can only kill a single animal because a family is only made of single animals */
		if(!o.isAnimal())
			return 0;
		
		Animal a = o.getAnimal();
		return setDeath(a);
	}
	
	
	private double setDeathForFather()
	{
		/* When the father dies, the mother must take father's place 
		 * and one of the child must take mother's place
		 */
		double res = 0;
		if(father!=null)
			res = father.getEnergy();
		father = mother;//mother becomes father
		setDeathForMother();//kill the previous mother
		return res;
	}
	
	
	private double setDeathForMother()
	{
		/* When the mother dies one of the child must take mother's place*/
		double res = 0;
		if(mother!=null)
			res = mother.getEnergy();
		
		if(children.size()==0)
		{
			mother = null;
			return res;
		}
		
		int index = getNewMother();
		mother = children.get(index);//a child becomes the mother
		children.remove(index);//remove the child from the list of child
		return res;
	}
	
	
	private double setDeathForChild(Animal a)
	{
		children.remove(a);
		return a.getEnergy();
	}
	
	
	private boolean manageIntegrity()
	{
		/* Check if the family is still a family (the size of the family is more than 2 animals)*/
		boolean res = false;
		if(father==null)
		{
			/* If there is no father, then based on the previous method to kill the father, it implies that
			 * there is no more animals inside the family. Then we need to remove the family from the 
			 * list of groups in the run of the simulation of from the "parent" group
			 */
			if(getGroup()!=null && isCarnivorous())//family is part of an other group
			{
				//the following declarations match the operation of the function inside split
				father = null;
				mother = null;
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				World.removeGroup(this);
			}
			res=true;
		}else if(mother==null)
		{
			/* If there is no mother, then based on the previous method to kill the mother, it implies that
			 * there is only one animal inside the family : the father. 
			 * Then we need to add the father as a stand alone group inside the list of the groups in the run of the simulation 
			 * or as a member of the "parent" group
			 */
			if(getGroup()!=null && isCarnivorous())//family is part of an other group
			{
				//the following declarations match the operation of the function inside split
				mother = null;
				
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				World.addGroup(father);
				World.removeGroup(this);
			}	
			res=true;
		}else if(children.size()==0)
		{
			/* If there is no children, then the size of family is only 2 then we need to split the family. Then add the father
			 * and the mother as stand alone groups inside the list of groups in the run of the simulation or as members of 
			 * the "parent" group
			 */
			if(getGroup()!=null && isCarnivorous())//family is part of an other group
			{
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				World.addGroup(father);
				World.addGroup(mother);
				World.removeGroup(this);
			}
			res=true;
		}
		
		return res;
	}
	

	/************************************************************************************************
	 * 
	 * 									FOOD METHODS
	 * 
	 ************************************************************************************************/
	
	public boolean needToEat()
	{
		/* If an animal in the family need to eat, then the whole family need to eat*/
		return Parms.AnimalNeedToEat(getNeedsToEat());
	}
	
	public double getNeedsToEat()
	{
		//check if each animal need to eat, if so, add the amount of needs to the sum
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		if(father!=null && father.needToEat())
		{
			output+=father.getNeedsToEat();
		}
		
		if(mother!=null && mother.needToEat())
		{
			output+=mother.getNeedsToEat();
		}
		
		for(Animal a : children)
		{
			if(a.needToEat())
			{
				output+=a.getNeedsToEat();
			}
		}
		
		return output;
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	

	public void findFood(Cell[][] map)
	{
		double angle = Parms.getDirectionForAnimal(map, father);//the direction of the group is the father's one
		eat(map, angle);
	}
	
	public void eat(Cell[][] map, double dir)
	{
		/* Based on the method eat from Animal */
		father.eat(map, dir);
		mother.eat(map, dir);
		for(Animal a : children)
		{
			a.eat(map, dir);
			a.moveTo(mother.getX(), mother.getY());//children go back with the mother
		}
	}
	
	
	public double setSatiety()
	{
		/*
		 * the family is fully fed
		 */
		double res = 0;
		if(father!=null)
			res+=father.setSatiety();
		if(mother!=null)
			res+=mother.setSatiety();
		for (Animal a : children)
			res+=a.setSatiety();
		return res;
	}
	
	public double feed(double dispo)
	{
		/*there is not enough food to feed the entire family
		 * the children are fed first, then the hunter, then the mother
		 */
		for (Animal a : children)
		{
			dispo-=a.setSatiety();
			if(dispo<=0)
			{
				a.setEnergy(a.getEnergy()+dispo);
				break;
			}
		}
		if(dispo>0)
		{
			dispo-=father.setSatiety();
			if(dispo<=0)
			{
				father.setEnergy(father.getEnergy()+dispo);
				return 0;
			}
		}
		if(dispo>0)
		{
			dispo-=mother.setSatiety();
			if(dispo<=0)
			{
				mother.setEnergy(mother.getEnergy()+dispo);
				return 0;
			}
		}
		return Math.max(0, dispo);
	}
	
	/************************************************************************************************
	 * 
	 * 									LOCATION METHODS
	 * 
	 ************************************************************************************************/
	
	
	public Point2D getLocation()
	{
		/* The location of the group is the father's one*/
		if(father==null)
			return null;
		
		return new Point2D.Double(father.getX(), father.getY());
	}
	
	public void moveTo(double x, double y)
	{
		Point2D old_pos = getLocation();
		
		if(father!=null)
			father.moveTo(x, y);
		if(mother!=null)
			mother.moveTo(x, y);
		for(Animal a : children)
		{
			a.moveTo(x, y);
		}
		
		Point2D new_pos = getLocation();
		if(getGroup()==null)//the family is not part of any other group*
		{
			/* When the current group move to another cell, then the current group is added to the new cell 
			 * and remove from the previous one. If the current group is part of another, the we must not 
			 * add it to the cell because the whole group which if the "parent" of the current one will be added later
			 */
			updateLocationOnMap(old_pos, new_pos);
		}
		
		setLocation();//need to update the location
	}
	
	
	/************************************************************************************************
	 * 
	 * 									UPDATE METHODS
	 * 
	 ************************************************************************************************/
	
	
	public void age()
	{
		/* Based on the method age of Animal, only apply that method to each member of the pack*/
		if(father!=null)
		{
			father.age();
		}
		
		if(mother!=null)
		{
			mother.age();
		}
		
		/* When a child is too old we need to remove it from the family*/
		for(Animal a : new ArrayList<Animal>(children))
		{
			a.age();
			if (a.getAge()>a.teenAge)
			{
				this.children.remove(a);
				if (this.getGroup()==null)
					World.addGroup(a);
				else
				{
					this.getGroup().add(World.createGroup(a, null));
				}
			}
		}
		
		checkDeath();
	}
	
	
	public boolean interact(Group o)
	{
		if(o==null)
			return false;
		
		if (this.isHerbivorousFamily())
		{
			if(o.isHerbivorousFamily())
			{
				Herd h = (Herd)World.createGroup(father, this);
				h.add(this);
				h.setDeath(father);
				h.add(o);
				World.removeGroup(this);
				World.removeGroup(o);
				World.addGroup(h);
				return true;
			}
			else
			{
				return o.interact(this);
			}
			
		}
		return false;
	}
	
	
	public void update(Cell[][] map)
	{
		/*move
		 * interact
		 * feed or hunt
		 * age
		 */
		if(acted)
			return;
		if(manageIntegrity())
			return;
		
		double angle = Parms.getDirectionForAnimal(map, father);
		moveTo(angle);
		
		
		if(this.isHerbivorousFamily())
		{
			if(father.getIsEscaping())
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
			Herd h = (Herd)World.createGroup(father, this);
			h.add(World.createGroup(mother, this));
			for (Animal a : children)
				h.add(World.createGroup(a, this));
			h.eat(pos);
		}
		else
		{
			Cell pos = World.getCell(location.getX(), location.getY());
			for(Group o : new ArrayList<Group>(pos.getGroups()))
			{
				if(o!=this)
					if(interact(o))
						break;
			}
			
			for(Group o : new ArrayList<Group>(pos.getGroups()))
			{
				if(o!=this)
				{
					Pack p =  (Pack)World.createGroup(father, this);
					if(p.hunt(o))
						break;
				}
			}
		}
		age();
	}
	
	
	
	
	
	public String toString()
	{
		if(father==null || mother==null)
			return null;
		
		String res ="";
		res+=getFormatedInfo() + "\n";
		res+="\tPère : " + father.toString();
		res+="\n\tMère : " + mother.toString();
		res+="\n\tEnfants : ";
		for(Animal a : children)
		{
			res+="\n\t\t"+a.toString();
		}
		
		return res;
	}
}
