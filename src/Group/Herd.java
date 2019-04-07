package Group;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;

public class Herd extends Group
{
	ArrayList<Group> members;
	
	public Herd()
	{
		super(Parms.TYPE_HERD);
		members = new ArrayList<Group>();
	}
	
	public void addMember(Animal a)
	{
		addMember(new Pack(a));
	}
	
	public void addMember(Group o) throws IllegalArgumentException
	{
		if(!(o.isAnimal() || o.isFamily()))
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
		members.add(o);
	}
	
	public void setDeath(Animal a)
	{
		members.remove(a);
	}
	
	public int getSize() throws IllegalArgumentException
	{
		int count = 0;
		for(Group o : members)
		{
			if(o.isFamily() || o.isAnimal())
			{
				count+=o.getSize();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return count;
	}
	
	//implement interface
	public double getStrength() throws IllegalArgumentException
	{
		double res = 0;
		for(Group o : members)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getStrength();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return res*Parms.FACTOR_ORGANIZATION_HERD;
	}
	
	public double getAgility() throws IllegalArgumentException
	{
		double res = 0;
		
		for(Group o : members)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getAgility();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return (res/members.size())*Parms.FACTOR_ORGANIZATION_HERD;
	}
	
	
	public double getAgressivity() throws IllegalArgumentException
	{
		double res = 0;
		
		for(Group o : members)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getAgressivity();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return (res/members.size())*Parms.FACTOR_ORGANIZATION_HERD;
	}
	
	public double getSociability()
	{
		return Parms.sociabilityHerd(getSize());
	}
	
	
	public String getSpecie() throws IllegalArgumentException
	{
		String specie = null;
		if(members.size()>0)
		{
			specie = members.get(0).getSpecie();
			for(Group o : members)
			{
				if(o.isFamily() || o.isAnimal())
				{
					if(!specie.equals(o.getSpecie()))
					{
						return null;
					}
				}else
				{
					throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
				}
			}
		}
		
		return specie;
	}
	
	
	public void decreaseEnergy(double ep)
	{
		for(Group o : members)
		{
			o.decreaseEnergy(ep);
		}
	}
	
	public boolean needToEat()
	{
		return Parms.AnimalNeedToEat(getNeedsToEat());
	}
	
	public double getNeedsToEat() throws IllegalArgumentException
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		
		for(Group o : members)
		{
			if(o.isFamily() || o.isAnimal())
			{
				output+=o.getNeedsToEat();//if the group does not need to eat return 0
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return Math.abs(output);
		//if the animal need to eat, then the value of the need will be negative, so we use the absolute value of the sum
	}
	
	public boolean isCarnivorous()
	{
		return members.get(0).isCarnivorous();
	}
	
	public boolean isHerbivorous()
	{
		return members.get(0).isHerbivorous();
	}
	
	public void interact(Group p)
	{
		
	}

}
