package Group;
import java.util.ArrayList;

import Animal.Animal;
import Parameters.Parms;
import Cell.*;
import Parameters.*;
import Main.*;

public class Pack extends Group
{
	private Animal chief;
	private Family chiefFamily;
	public ArrayList<Group> others;
	
	public Pack()
	{
		super(Parms.TYPE_PACK);
		chief = null;
		chiefFamily = null;
		others = new ArrayList<Group>();
	}
	
	public Pack(Animal a)//a single animal is consider as a pack of one animal
	{
		this();
		chief = a;
		chiefFamily = null;
	}
	
	public Pack(Pack p)//a single animal is consider as a pack of one animal
	{
		this();
		setChief(p);
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
		
		if(chiefFamily!=null)
		{
			count+=chiefFamily.getSize();
		}else if(chief!=null)
		{
			count++;
		}
		
		return count;
	}
	
	
	//chief
	public void setChief(Pack o)
	{
		chief = o.getChief();
		if(o.isFamilyMember())
			chiefFamily = (Family)o.getGroup();
	}
	
	public Animal getChief()
	{
		return chief;
	}
	
	public Family getChiefFamily()
	{
		return chiefFamily;
	}
	
	
	//the others
	public void addMember(Animal a)
	{
		Pack p = new Pack(a);
		p.setGroup(this);
		addGroup(p);
	}
	
	public void addFamily(Family f)
	{
		f.setGroup(this);
		addGroup(f);
	}
	
	public void addGroup(Group o) throws IllegalArgumentException
	{
		Run.removeGroup(o);
		if(!(o.isAnimal() || o.isFamily()))
		{
			throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
		}
		
		double s1 = o.getStrength();
		double s2 = (chief==null)?-1:chief.getStrength();
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
			Animal temp_a = chief;
			Family temp_f = chiefFamily;
			
			//set new chief
			if(o.isFamily())
			{
				Family f = (Family)o;
				Animal a = f.getFather().getChief();
				
				chief = a;
				chiefFamily = f;
			}else if(o.isAnimal())
			{
				Pack p = (Pack)o;
				chief = p.getChief();
			}
			
			//remove old chief
			if(temp_f==null)
			{
				if(temp_a!=null)
					others.add(new Pack(temp_a));
			}else
			{
				others.add(temp_f);
			}
			
		}else
		{
			others.add(o);
		}
	}
	
	
	public void setDeath(Group o)
	{
		if(o==null)
			return;
		
		if(o.isAnimal())
		{
			Pack p = (Pack)o;
			if(p.getChief()==chief)
			{
				setDeathForChief();
			}else
			{
				setDeathForMember(o);
			}
		}else if(o.isFamily())
		{
			Family f = (Family)o;
			Pack father = f.getFather();
			if(father!=null)
			{
				if(father.getChief()==chief)
				{
					Family temp = chiefFamily;
					setDeathForChief();//chief's family will be added to the list of members
					setDeathForMember(temp);//remove the chief's family
				}else
				{
					setDeathForMember(o);
				}
			}
		}
	}
	
	
	public void setDeath()
	{
		ArrayList<Group> temp = new ArrayList<Group>(others);
		int size = temp.size();
		
		for(int i=size-1; i>=0; i++)
		{
			setDeath(temp.get(i));
		}
	}
	
	public void setDeath(int number)
	{
		if(number>getSize())
		{
			int size = others.size();
			Group o;
			for(int i=size-1; i>=0; i--)
			{
				o = others.get(i);
				if(o.isAnimal())
				{
					others.remove(i);
				}else if(o.isFamily())
				{
					
				}
			}
			
			setDeathForChief();//need to be after the loop of the group (to set the new chief)
		}
	}
	
	private void setDeathForChief() throws IllegalArgumentException
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
			chief=null;
			others.add(chiefFamily);
			chiefFamily=null;
			
			o = others.get(index);
			if(o.isFamily())
			{
				Family f = (Family)o;
				Animal a = f.getFather().getChief();
				
				chief = a;
				chiefFamily = f;
			}else if(o.isAnimal())
			{
				Pack p = (Pack)o;
				chief = p.getChief();
			}
			others.remove(index);
		}
	}
	
	public void clear()
	{
		chief = null;
		if(chiefFamily!=null)
		{
			chiefFamily.setGroup(null);
			chiefFamily=null;
		}
		
		others.clear();
	}
	
	private void setDeathForMember(Group group)
	{
		if(group==null)
			return;
		System.out.println("endter");
		for(Group o : others)
		{
			if(o==group)
			{System.out.println("equal");
				if(o.isFamily())
				{
					o.setDeath();
				}else if(o.isAnimal())
				{
					System.out.println("remove");
					others.remove(o);
					return;
				}
			}
		}
	}
	
	
	public void splitFamily(Family f)
	{
		//remove family without killing animals in it
		Pack g = f.getFather();
		if(g!=null)
		{
			Animal father = f.getFather().getChief();
			if(father!=chief)
			{
					others.add(g);
			}
		}
		
		g = f.getMother();
		if(g!=null)
			others.add(g);
		
		others.remove(f);
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
		return chief.getAgressivity();
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
	
	
	public void age()
	{
		if(chief!=null)
		{
			chief.age();
		}
		
		for(Group o : others)
		{
			o.age();
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
	
	public void findFood(Cell[][] map)
	{
		double angle = Parms.getDirectionForAnimal(map, chief);
		
		for(Group o : others)
		{
			if(o.isAnimal())
			{
				Animal a = ((Pack)o).getChief();
				a.setCanEat(true);
				a.move(map, angle);
				a.setCanEat(false);
			}else if(o.isFamily())
			{
				
			}
		}
	}
	
	public boolean _fight(Pack o)
	{
		if(o.isAnimal())
		{
			if(o.isFamilyMember())
			{
				
			}else//lonely animal
			{
				Animal a = (Animal)o.getChief();
				
			}
		}
		
		return false;
	}
	
	public boolean _fight(Herd o)
	{
		
		return false;
	}
	
	public String toString()
	{
		if(isAnimal())
			return chief.toString();
		
		String res="Meute : \n";
		res+="\tChef : " + ((chief!=null)?chief.toString():"");
		res+="\n\tMembres : ";
		for(Group o : others)
		{
			res+="\n\t\t";
			if(o.isAnimal())
			{
				Pack p = (Pack)o;
				res+= p.getChief().toString();
			}else
			{
				res+= o.toString()+ "   | Size = "+o.getSize();
			}
		}
		return res;
	}
	
	
}
