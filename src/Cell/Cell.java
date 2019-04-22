package Cell;
import java.util.ArrayList;

import Animal.Animal;
import Group.Group;
import Group.Pack;
import Parameters.Parms;

/* A cell help us to discretise the real world. A cell represents a specif area inside which animals can move.
 * A cell contains :
 *   - food for herbivorous animals that decrease as ressources are reduced (because eaten). We suppose that 
 * the distribution of the food in each cell is uniform
 *   -  obstacles that animal could bypasses
*/

public class Cell extends ObjectMap
{
	/************************************************************************************************
	 * 
	 * 										ATTRIBUTES
	 * 
	 ************************************************************************************************/
	
	private double quantity;//ressources inside the cell
	private ArrayList<Group> groups;//list of groups of animals
	private ArrayList<Obstacle> obstacles;//list of obstacles
	/* All the groups and obstacles position are given relative the the origin of the map and not to
	 * the origin of the current cell
	 */

	public Cell(double quantity)
	{
		this.quantity =  quantity;
		this.groups = new ArrayList<Group> ();
		this.obstacles = new ArrayList<Obstacle>();
	}
	
	public Cell()
	{
		this(0);
	}
	
	
	/************************************************************************************************
	 * 
	 * 										GETTER/SETTER
	 * 
	 ************************************************************************************************/
	
	public void setQuantity(double quantity)
	{
		this.quantity = quantity;
	}
	
	public double getQuantity()
	{
		return quantity;
	}
	
	public void decreaseQuantity(double ep)
	{
		quantity-=ep;
		if(quantity<0)
		{
			quantity=0;
		}
	}
	
	public ArrayList<Obstacle> getObstacles()
	{
		return new ArrayList<Obstacle>(obstacles);
	}
	
	public ArrayList<Group> getGroups()
	{
		return groups; 
	}
	
	public int getNumberGroups()
	{
		return groups.size();
	}
	
	public double getFood(double needs)
	{
		double food;
		if(quantity<=needs)
		{
			food = quantity;
			quantity = 0;
		}else
		{
			food = needs;
			quantity = quantity - needs;
		}
		
		return food;
	}
	
	public boolean isEatable(Group o)
	{
		/* The ressources on the cell is only food for herbivorous animals*/
		return o.isHerbivorous();
	}
	
	
	/************************************************************************************************
	 * 
	 * 										ADD/REMOVE METHODS
	 * 
	 ************************************************************************************************/
	
	public void add(Group o)
	{
		groups.add(o);
	}
	
	public void add(Obstacle o)
	{
		obstacles.add(o);
	}
	
	public void remove(Group o)
	{
		groups.remove(o);
	}
	
	public void remove(Obstacle o)
	{
		obstacles.remove(o);
	}
	
	/*public void deviateAnimal(Animal o)
	{
		/* regarder la posiiton de l'obstacle et de l'animal et si pas obstacle alors rien à faire
		 * renvoie la nouvelle direction sous forme d'angle (0 si rien à faire)
		 * 
		 * Si l'obstcale doit etre contouner, changer position animal
		 * Si l'obstcale peut être franchi pop de l'animal derrière l'obstacle
		 * Sinon ne rien faire
		 
	}*/
}
