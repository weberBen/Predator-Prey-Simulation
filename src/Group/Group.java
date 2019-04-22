package Group;
import Parameters.Parms;
import Cell.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import Animal.Animal;
import Interfaces.I_Living;
import Main.*;


/*
 * All the animal are included into a group
 * Each group act in a different manner. The possible group are :
 * 		- A family (herbivorous or carnivorous) made of a father (not related to its sex) who must bring back food for the whole
 * members of the family, a mother who must protect the children, and the children (the number of children created by the association
 * of a "father" and a "mother" is related to the specie of the two animal (which here must be the same) 
 * 	 	- A pack build on a chief and others animals as members of the pack. Ann the members follows the chief before there own needs
 * 		- A herd made of multiple animals
 * 
 * Note that animal in a group can't have a different diet (carnivorous and herbivorous in the same group). 
 * Moreover, a single carnivorous animal is represented by a pack of one element and a single herbivorous animal by a herd of 
 * one element.
 * 
 * Pay attention to the fact that all the members of a group move together which is justified by the age of the created child : child 
 * pop into the environment as teenage. Thus, the kid need to learn from his parents, so he need to move with them (for a family).
 * Then, as all the members of a family move together and as only the father have to go with the pack to find food, all the members of
 * a pack move together. And for a herd the move is implicitly a global move. 
 */

public abstract class Group implements I_Living
{
	/************************************************************************************************
	 * 
	 * 										ATTRIBUTES
	 * 
	 ************************************************************************************************/
	
	private static int count = 0;
	private double dir;
	//Actual direction of the group which is calculate on the type of group (herd, pack, family)
	private Group group;
	/* As an animal can be included inside a group, a group of animals can be part of another group, 
	 * for example a family inside a pack. That field held us the know in which group the current one
	 * is included in, or if it's a stand alone group (value null)
	*/
	protected Point2D location;
	/* Because all the groups are saved into a cell of the world (which reduce the time to find with
	 * which group another can interact with), we need to get the location of the group (in which cell 
	 * the group are in). That field is only updated when the group change its locations on the map
	 * (move to another cell)
	 */
	private final int ID;//identity of the group
	protected boolean acted = false;
	
	
	public Group()
	{
		group =  null;
		ID = count;
		count++;
	}
	
	/************************************************************************************************
	 * 
	 * 										GETTER/SETTER
	 * 
	 ************************************************************************************************/
	
	//get parent group
	public void setGroup(Group g)
	{
		group = g;
	}
	
	public Group getGroup()
	{
		return group;
	}
	
	
	//get direction
	public double getDir()
	{
		return dir;
	}
	
	public void setDir(double dir)
	{
		this.dir = dir;
	}
	
	
	
	public void resetActed()
	{
		acted=false;
	}

	
	public Animal getAnimal()
	{
		/* Only for a single animal return the animal represented by the current group */
		if(!isAnimal())
			return null;
		
		if(this instanceof Pack)//for a pack get the chief
			return ((Pack)this).getChief();
		else if(this instanceof Herd)//for a herd get the only member
			return ((Herd)this)._getAnimal();
		
		return null;
	}
	
	public abstract int getSize();
	/* Return the number of animal in the current group*/
	
	public int getID()
	{
		if(isAnimal())
			return getAnimal().getID();
		return ID;
	}
	
	public String getDiet()//get a string that represent the diet of the group (herbivorous or carnivorous)
	{
		if(isCarnivorous())
			return "Carnivore";
		return "Herbivore";
	}
	
	public String getFormatedInfo()//get type of the group and its id
	{
		return getType() + " (Id="+getID()+")";
	}
	
	public String getType()//return a string that represents the type of the group (Pack, Herd, Family, single animal)
	{
		if(isAnimal())
			return "Animal";
		if(isFamily())
			return "Famille";
		if(isPack())
			return "Meute";
		if(isHerd())
			return "Troupeau";
		return "Inconnu";
	}
	
	
	/************************************************************************************************
	 * 
	 * 									GET TYPE OF THE GROUP
	 * 
	 ************************************************************************************************/
	
	public boolean isFamily()
	{
		return this instanceof Family;
	}
	
	public boolean isCarnivorousFamily()
	{
		return (this instanceof Family) && (isCarnivorous());
	}
	
	public boolean isHerbivorousFamily()
	{
		return (this instanceof Family) && (isHerbivorous());
	}
	
	public boolean isInFamily(Family f, Group p)//check if the a group is a member of a family
	{
		/* A group is a member of a family only if the group represents a single animal
		 * and if it is part of the tested family
		 */
		if(f==null || p==null)
			return false;
		if(!p.isFamilyMember())
			return false;
		
		return p.getGroup()==f;
	}
	
	public boolean isHerd()
	{
		return (this instanceof Herd) && (getSize()>1);
	}
	
	public boolean isPack()
	{
		return (this instanceof Pack) && (getSize()>1);
	}
	
	public boolean isAnimal()
	{
		return (this instanceof Pack || this instanceof Herd) && (getSize()==1);
	}
	
	public boolean isCarnivorousAnimal()
	{
		return isCarnivorous() && isAnimal();
	}
	
	public boolean isHerbivorousAnimal()
	{
		return isHerbivorous() && isAnimal();
	}
	
	public boolean sameFoodDiet(Group o)
	{
		//check if the current group has the same diet (carnivorous or herbivorous) than the given group
		return (isCarnivorous() && o.isCarnivorous()) || (isHerbivorous() && o.isHerbivorous());
	}
	
	public boolean isFamilyMember()
	{
		/* check if the current group is part of a family*/
		if(!isAnimal())
			return false;
		
		if(getSize()!=1)//must be a single animal
			return false;
		
		Group o = getGroup();//check if the current group is a part another group that is a family
		return (o instanceof Family);
	}
	
	
	
	/************************************************************************************************
	 * 
	 * 									DEATH METHODS
	 * 
	 ************************************************************************************************/
	
	public abstract double setDeath();
	/* Kill all the animal in the group and remove the group from the map
	 * In fact all functions that alter the map (added a new group to the environment
	 * or remove one) deal with the map itself. In other words, all the communication between
	 * the environment and the group are made inside the group
	 */
	public abstract double setDeath(int number);
	/* Kill a specific number of animal inside the group*/
	public abstract double setDeath(Group o);
	/* Kill a specific group inside the current one*/
	
	
	
	/************************************************************************************************
	 * 
	 * 									MERGE METHODS
	 * 
	 ************************************************************************************************/
	
	public abstract void add(Group o);
	/* Add a new group to the current one*/
	
	
	/************************************************************************************************
	 * 
	 * 									FOOD METHODS
	 * 
	 ************************************************************************************************/
	
	public abstract double getNeedsToEat();	
	/* Return the need to eat for the whole group which is calculate
	 * as the sum of the needs to eat for each animal if the current group
	 */
	public abstract void findFood(Cell[][] map);
	/* given the map, the group will move to a desired direction 
	 * (based on the type of group and how animal interact inside that group)
	 * and eat if there is food
	 */
	public abstract double setSatiety();
	public abstract double feed(double dispo);
	
	
	/************************************************************************************************
	 * 
	 * 									LOCATION METHODS
	 * 
	 ************************************************************************************************/
	
	public abstract Point2D getLocation();
	/* Return the current position of the group*/
	public Point2D getLastKnownLocation()
	{
		/* Because the location is given temporarily based on an animal of the group (which is different
		 * for a pack, a herd or a family) when the group dies there will be no more animals inside the group. Then 
		 * the location of the group will be lost and we will not be able to remove the dead group from the map.
		 * So for that case we need to get the previous position of the group or in others words the last know position
		 * of the group before it disappears. 
		 * 
		 * That solution avoid the track each modification of the position for each group.
		 */
		if(location==null)
			return null;
		return new Point2D.Double(location.getX(), location.getY());
	}
	
	public void setLocation()
	{
		/* If the position of the group is known (the current position can be computed) return that one
		 * and set the location to that new position
		 */
		Point2D p = getLocation();
		if(p!=null)
			location = p;
	}
	
	public void setLocation(Point2D p)
	{
		if(p!=null)
			location = p;
	}
	
	public void updateLocationOnMap(Point2D old_pos, Point2D new_pos)
	{
		/* When the current group move to another cell, we need to add it
		 * to the new cell and remove it from the previous one
		 */
		location = new_pos;	
		
		/* All the animal as real position because a cell represents a large area in which animal
		 * can move. To get the cell that contains the group we just have to take the integer part
		 * of the real position
		 */
		
		Cell c1 = World.getCell(old_pos.getX(), old_pos.getY());
		Cell c2 = World.getCell(new_pos.getX(), new_pos.getY());
		
		if(c1!=c2)////cell has been changed
		{
			World.removeFromMap(this,old_pos.getX(),old_pos.getY());
			World.addToMap(this,new_pos.getX(),new_pos.getY());
		}
	}
	
	
	/************************************************************************************************
	 * 
	 * 									MOVE METHODS
	 * 
	 ************************************************************************************************/
	
	
	public abstract void moveTo(double x, double y);
	/* Move all the animals inside the current group to a new location*/
	
	/* given a part of the map the group will decide if it need to move or eat*/
	public void moveTo(double angle)
	{
		double futurX = getLocation().getX()+Math.cos(angle)*(Parms.DELTA_MOVE+getStrength());
		double futurY = getLocation().getY()+Math.sin(angle)*(Parms.DELTA_MOVE+getStrength());
		if (futurX<0)
			futurX= 0 + Math.random();
		if (futurY<0)
			futurY= 0 + Math.random();
		if (futurX > World.getSize())
			futurX = World.getSize()- Math.random();
		if (futurY > World.getSize())
			futurY = World.getSize() - Math.random();
		moveTo(futurX ,futurY );
	}
	
	public void move(Cell[][] map)
	{	
		Point2D old_pos = getLocation();
		if(needToEat())
		{//the function return true if the group need to eat (which is not the same answer following the type of the group)
			findFood(map);
		}
		
		Point2D new_pos = getLocation();
		new_pos = new Point2D.Double(3.5,0);
		
		updateLocationOnMap(old_pos, new_pos);
		setLocation();
		/* To avoid tracking each modification of the internal structure of the group
		 * we update the location only when the group move. In fact when a group dies the position
		 * of the group will be the last known position because the group does not move
		 */
	}
	
	public abstract void update(Cell[][] map);
	public abstract boolean interact(Group o);
}
