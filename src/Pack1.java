import java.util.ArrayList;

import Animal.Animal;
import Group.Group;
import Group.Pack;
import Parameters.Parms;

public class Pack1 extends Group
{
	private Animal chief;
	private ArrayList<Group> others;
	
	public Pack1()
	{
		super(Parms.TYPE_PACK);
		chief = null;
		others = new ArrayList<Group>();
	}
	
	public Pack1(Animal a)//a single animal is consider as a pack of one animal
	{
		this();
		chief = a;
	}
	
	public int getSize() throws IllegalArgumentException
	{
		int count = 0;
		for(Group o : others)
		{
			//only single animal and family can be a member of the pack
			if(o.isAnimal() || o.isFamily())
			{
				count+=o.getSize();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
			}
		}
		
		return (chief!=null)?1:0 +count;
	}
	
	
	//chief
	public void setChief(Animal a)
	{
		chief = a;
	}
	
	public Animal getChief()
	{
		return chief;
	}
	
	//the others
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
		
		double s1 = o.getStrength();
		double s2 = (chief==null)?0:chief.getStrength();
		boolean replaceChief = false;
		
		//if the new animal is stronger than the chief, then the old chief is replace
		if(s1>=s2)
		{
			if(s1==s2)//if the two animals have the same strength, the the chief is replaced or not randomly
			{
				if(Math.random()<0.5)
				{
					replaceChief = true;
				}
			}else
			{
				replaceChief = true;
			}
		}
		
		if(replaceChief)
		{
			Animal temp = chief;
			if(o.isFamily())
			{
				Family1 f = (Family1)o;
				chief = f.getHeadForFood();
			}else if(o.isAnimal())
			{
				Pack p = (Pack)o;
				chief = p.getChief();
			}
			others.add(new Pack(temp));
		}else
		{
			others.add(o);
		}
	}
	
	
	public void setDeath(Animal a)
	{
		if(a.equals(chief))
		{
			setDeathForChief();
		}else
		{
			setDeathForMember(a);
		}
	}
	
	public void setDeathForChief() throws IllegalArgumentException
	{
		chief = null;
		if(others.size()>0)//set the new chief as the animal with the highest strength
		{
			//fin the animal with the highest strength
			int index = 0;
			double max_strength = -1;
			double temp;
			Group o;
			
			for(int i=0; i<others.size(); i++)
			{
				o = others.get(i);
				if(o.isAnimal() || o.isFamily())
				{
					if((temp = o.getStrength())>max_strength)
					{
						index = i;
						max_strength = temp;
					}
				}else
				{
					throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
				}
			}
			
			
			//set the new chief
			o = others.get(index);
			if(o.isFamily())
			{
				Family1 f = (Family1)o;
				chief = f.getHeadForFood();
			}else if(o.isAnimal())
			{
				Pack p = (Pack)o;
				chief = p.getChief();
			}
			others.remove(index);
		}
	}
	
	public void setDeathForMember(Animal a)
	{
		others.remove(a);
	}

	
	//implement
	public double getStrength() throws IllegalArgumentException
	{
		double res = (chief!=null)?(chief.getStrength()):0;
		for(Group o : others)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getStrength();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return res;
	}
	
	public double getAgility() throws IllegalArgumentException
	{
		double res = (chief!=null)?(chief.getAgility()):0;
		
		for(Group o : others)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getAgility();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return res/( ((chief!=null)?1:0) + others.size());
	}
	
	
	public double getAgressivity() throws IllegalArgumentException
	{
		double res = (chief!=null)?(chief.getAgressivity()):0;
		
		for(Group o : others)
		{
			if(o.isFamily() || o.isAnimal())
			{
				res+=o.getAgressivity();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé"); 
			}
		}
		
		return res/( ((chief!=null)?1:0) + others.size());
	}
	
	public double getSociability()
	{
		return Parms.sociabilityPack(getSize());
	}
	
	
	public String getSpecie() throws IllegalArgumentException
	{
		String specie = null;
		if(others.size()>0)
		{
			specie = others.get(0).getSpecie();
			for(Group o : others)
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
		
		if(chief!=null)
		{
			if(specie==null)//the pack is only composed by one animal
			{
				return chief.getSpecie();
			}else if(!specie.equals(chief.getSpecie()))
			{
				return null;
			}
		}
		
		return null;
	}
	
	
	public void decreaseEnergy(double ep)
	{
		if(chief!=null)
		{
			chief.decreaseEnergy(ep);
		}
		
		for(Group o : others)
		{
			o.decreaseEnergy(ep);
		}
	}
	
	public boolean needToEat()
	{
		return chief.needToEat();
	}
	
	public double getNeedsToEat() throws IllegalArgumentException
	{
		double output = 0;
		
		//check if each animal need to eat, if so, add the amount of needs to the sum
		if(chief.needToEat())
		{
			output+=chief.getNeedsToEat();
		}
		
		
		for(Group o : others)
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
		return chief.isCarnivorous();
	}
	
	public boolean isHerbivorous()
	{
		return chief.isHerbivorous();
	}
	
	public void interact(Group p)
	{
		
	}
	
}
