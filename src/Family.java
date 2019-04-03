import java.util.ArrayList;

public class Family extends Group
{
	private Animal headForFood;
	private Animal headForChildren;
	private ArrayList<Animal> children;
	
	
	public Family()
	{
		super(Parms.TYPE_FAMILY);
		this.headForFood = null;
		this.headForChildren = null;
		this.children = new ArrayList<Animal>();
	}
	
	//animal in charge of food
	public void setHeadForFood(Animal a)
	{
		headForFood = a;
	}
	
	public Animal getHeadForFood()
	{
		return headForFood;
	}
	
	//animal in charge of children
	public void setHeadForChildren(Animal a)
	{
		headForChildren = a;
	}
	
	public Animal getHeadForChildren()
	{
		return headForChildren;
	}
	
	//children
	public void addChildren(Animal a)
	{
		children.add(a);
	}
	
	public void setChildren(ArrayList<Animal> l)
	{
		children = new ArrayList<Animal>(l);
	}
	
	
	//implement interface
	public void setStrength()
	{
		strength = headForFood.getStrength();
	}
	
	public void setSociability()
	{
		sociability = headForFood.getSociability();
	}
	
	public void setAgility()
	{
		agility = headForFood.getAgility();
	}
	
	public void setAgresivity()
	{
		agressivity = headForFood.getAgressivity();
	}
	
	public void setSpecie()
	{
		specie = headForFood.getSpecie();
	}
	
	
	//extends
	public void setNeedsToEat()
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		if(Parms.AnimalNeedToEat(headForFood))
		{
			output+=headForFood.getNeedsToEat();
		}
		
		if(Parms.AnimalNeedToEat(headForChildren))
		{
			output+=headForChildren.getNeedsToEat();
		}
		
		for(Animal a : children)
		{
			if(Parms.AnimalNeedToEat(a))
			{
				output+=a.getNeedsToEat();
			}
		}
		
		needsToEat = Math.abs(output);
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	
	public void decreaseEnergy(double ep)
	{
		headForFood.decreaseEnergy(ep);
		headForChildren.decreaseEnergy(ep);
		for(Animal a : children)
		{
			a.decreaseEnergy(ep);
		}
	}
}
