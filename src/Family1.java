import java.util.ArrayList;

import Animal.Animal;
import Group.Group;
import Parameters.Parms;

public class Family1 extends Group
{
	private Animal headForFood;
	private Animal headForChildren;
	private ArrayList<Animal> children;
	
	
	public Family1()
	{
		super(Parms.TYPE_FAMILY);
		this.headForFood = null;
		this.headForChildren = null;
		this.children = new ArrayList<Animal>();
	}
	
	//animal in charge of food
	public void setHeadForFood(Animal a)
	{
		a.setGroup(this);
		headForFood = a;
	}
	
	public Animal getHeadForFood()
	{
		return headForFood;
	}
	
	//animal in charge of children
	public void setHeadForChildren(Animal a)
	{
		a.setGroup(this);
		headForChildren = a;
	}
	
	public Animal getHeadForChildren()
	{
		return headForChildren;
	}
	
	//children
	public void addChildren(Animal a)
	{
		a.setGroup(this);
		children.add(a);
	}
	
	//implement interface
	public double getStrength()
	{
		return headForFood.getStrength();
	}
	
	public double getSociability()
	{
		return headForFood.getSociability();
	}
	
	public double getAgility()
	{
		return headForFood.getAgility();
	}
	
	public double getAgressivity()
	{
		return headForFood.getAgressivity();
	}
	
	public String getSpecie()
	{
		return headForFood.getSpecie();
	}
	
	public void decreaseEnergy(double ep)
	{
		if(headForFood!=null)
		{
			headForFood.decreaseEnergy(ep);
		}
		
		if(headForChildren!=null)
		{
			headForChildren.decreaseEnergy(ep);
		}
		
		for(Animal a : children)
		{
			a.decreaseEnergy(ep);
		}
	}
	
	
	//extends
	public boolean needToEat()
	{
		return Parms.AnimalNeedToEat(getNeedsToEat());
	}
	
	public double getNeedsToEat()
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		if(headForFood.needToEat())
		{
			output+=headForFood.getNeedsToEat();
		}
		
		if(headForChildren.needToEat())
		{
			output+=headForChildren.getNeedsToEat();
		}
		
		for(Animal a : children)
		{
			if(a.needToEat())
			{
				output+=a.getNeedsToEat();
			}
		}
		
		return Math.abs(output);
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	
	public void setDeath(Animal a)
	{
		if(a.equals(headForFood))
		{
			setDeathHeadForFood();
		}else if(a.equals(headForChildren))
		{
			setDeathHeadForChildren();
		}else
		{
			setDeathForChild(a);
		}
	}
	
	private void setDeathHeadForFood()
	{
		if(headForChildren != null)
		{
			headForFood = headForChildren;
			headForChildren = null;
		}else
		{
			if(children.size()>0)
			{
				headForFood = children.get(0);
				children.remove(0);
			}
		}
	}
	
	private void setDeathHeadForChildren()
	{
		headForChildren = null;
	}
	
	private void setDeathForChild(Animal a)
	{
		children.remove(a);
	}
	
	public boolean isCarnivorous()
	{
		return headForFood.isCarnivorous();
	}
	
	public boolean isHerbivorous()
	{
		return headForFood.isCarnivorous();
	}
	
	public int getSize()
	{
		return ((headForFood!=null)?1:0) + ((headForChildren!=null)?1:0)  + children.size();
	}
	
	public void interact(Group p)
	{
		
	}
}
