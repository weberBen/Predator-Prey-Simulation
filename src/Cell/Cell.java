package Cell;
import java.util.ArrayList;

import Animal.Animal;
import Group.Group;
import Group.Pack;
import Parameters.Parms;

public abstract class Cell extends ObjectMap
{
	private double quantity;
	/*Représente l'énergie disponible sur la case comme nourriture*/
	private ArrayList<Group> animals;
	private ArrayList<Obstacle> obstacles;
	
	/* Toutes les cases sont potentiellement de la nourriture végétale avec des obstacles dessus
	 * Et la quantity de nourriture végétale sur une case est uniforme
	 */
	public Cell(double quantity)
	{
		this.quantity =  quantity;
		this.animals = new ArrayList<Group> ();
		this.obstacles = new ArrayList<Obstacle>();
	}
	
	public Cell()
	{
		this(0);
	}
	
	public void addAnimals(Group o)
	{
		animals.add(o);
	}
	
	public void removeAnimals(Group o)
	{
		animals.remove(o);
	}
	
	public void addObstacle(Obstacle o)
	{
		obstacles.add(o);
	}
	
	public void removeObstacle(Obstacle o)
	{
		obstacles.remove(o);
	}
	
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
	
	public void deviateAnimal(Animal o)
	{
		/* regarder la posiiton de l'obstacle et de l'animal et si pas obstacle alors rien à faire
		 * renvoie la nouvelle direction sous forme d'angle (0 si rien à faire)
		 * 
		 * Si l'obstcale doit etre contouner, changer position animal
		 * Si l'obstcale peut être franchi pop de l'animal derrière l'obstacle
		 * Sinon ne rien faire
		 */
	}
	
	public boolean isEatable(Group o)
	{
		return o.isHerbivorous();
	}
	
}
