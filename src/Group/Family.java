package Group;
import java.util.ArrayList;
import Animal.Animal;
import Cell.Cell;
import Parameters.Parms;
import Main.*;

public class Family extends Group
{
	private Animal father;
	private Animal mother;
	private ArrayList<Animal> children;
	
	private Family(){};
	
	public Family(Group g_father, Group g_mother) throws IllegalArgumentException
	{
		super();
		
		if(!g_father.isAnimal() || !g_mother.isAnimal())
			throw new IllegalArgumentException("Group incompatible avec une famille");
		
		children = new ArrayList<Animal>();
		setFather(g_father);
		setMother(g_mother);
		addChildren();
	}
	
	private void addChildren()
	{
		int numChildren = 1;//(int)(1 + Math.random()*(mother.NB_POSSIBLE_KID));
		
		Animal a;
		
		for(int i=0; i<numChildren; i++)
		{
			a = mother.createChild();
			addChild(a);
		}
	}

	private void addChild(Animal a)
	{
		children.add(a);
	}
	
	
	
	//animal in charge of food

	private void setFather(Group o)//remove later
	{
		father = o.getAnimal();
		if(o.getGroup()==null)
		{
			Run.removeGroup(o);
		}
	}
	
	
	public Group getFather()
	{
		if(father==null)
			return null;
		
		return Run.createGroup(father, this);
	}
	
	//animal in charge of children
	
	private void setMother(Group o)
	{
		mother = o.getAnimal();
		if(o.getGroup()==null)
		{
			Run.removeGroup(o);
		}
	}
	
	public Group getMother()
	{
		if(mother==null)
			return null;
		
		return Run.createGroup(mother, this);
	}
	
	
	public ArrayList<Animal> getChildren()
	{
		return new ArrayList<Animal>(children);
	}
	
	//implement interface
	public double getStrength()
	{
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
			res+=a.getStrength();
		}
		
		return res;
	}	

	
	public double getSociability()
	{
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
		return father.getSpecie();
	}
	
	
	public void age()
	{//verifier si enfant trop grand (comme pere mere ou "enfant" =>plus de mineur dans la famille) on la split
		//si enfant trop grand on le sort de la famille
		if(father!=null)
		{
			father.age();
		}
		
		if(mother!=null)
		{
			mother.age();
		}
		
		for(Animal a : children)
		{
			a.age();
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
	
	public int getSize()
	{
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
	

	private void setDeathRandom()
	{
		int index = (int)(Math.random()*getSize());
		if(index==0)
		{
			setDeathForFather();
		}else if(index==1)
		{
			setDeathForMother();
		}else
		{
			children.remove(index-2);
		}
		//do not check integrety after that function (if the function is called to kill more than one member in a loop
	}
	
	public void setDeath(int number)
	{
		if(number>getSize())
		{
			setDeath();
			return;
		}
		
		for(int i=0; i<number; i++)
		{
			setDeathRandom();
		}
		
		manageIntegrity();
	}
	
	public void setDeath()
	{	
		if(getGroup()!=null)
		{
			father = null;
			mother = null;
			
			Pack p = (Pack)getGroup();
			p.splitFamily(this);
		}else
		{
			Run.removeGroup(this);
		}
	}
	
	public void setDeath(Animal a)
	{
		if(a==father)
		{
			setDeathForFather();
		}else if(a==mother)
		{
			setDeathForMother();
		}else 
		{
			setDeathForChild(a);
		}
		
		manageIntegrity();
	}
	
	
	public void setDeath(Group o)
	{
		if(!o.isAnimal())
			return;
		
		Animal a = ((Pack)o).getChief();
		setDeath(a);
	}
	
	
	private void setDeathForFather()
	{
		father = mother;
		setDeathForMother();
	}
	
	
	private void setDeathForMother()
	{
		if(children.size()==0)
		{
			mother = null;
			return ;
		}
		
		int index = getNewMother();
		mother = children.get(index);
		children.remove(index);
	}
	
	
	private void setDeathForChild(Animal a)
	{
		children.remove(a);
	}
	
	
	private int getNewMother()
	{
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
		return father.isCarnivorous();
	}
	
	
	public boolean isHerbivorous()
	{
		return father.isHerbivorous();
	}

	public void findFood(Cell[][] map)
	{
		double angle = Parms.getDirectionForAnimal(map, father);
		eat(map, angle);
	}
	
	public void eat(Cell[][] map, double dir)
	{
		father.eat(map, dir);
		mother.eat(map, dir);
		for(Animal a : children)
		{
			a.eat(map, dir);
			a.moveTo(mother.getX(), mother.getY());//children go back with the mother
		}
	}
	
	
	public void comeBack(Cell[][] map)
	{
		
	}
	
	public boolean _fight(Pack o)
	{
		return false;
	}
	
	public boolean _fight(Herd o)
	{
		return false;
	}
	
	private void manageIntegrity()
	{
		if(father==null)
		{
			if(getGroup()!=null)//family is part of an other group
			{
				father = null;
				mother = null;
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				Run.removeGroup(this);
			}
		}else if(mother==null)
		{
			if(getGroup()!=null)//family is part of an other group
			{
				mother = null;
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				Run.addGroup(father, null);
				Run.removeGroup(this);
			}	
		}else if(children.size()==0)
		{
			if(getGroup()!=null)//family is part of an other group
			{System.out.println("oki");
				Pack p = (Pack)getGroup();
				p.splitFamily(this);
			}else//family is a stand alone group
			{
				Run.addGroup(father, null);
				Run.addGroup(mother, null);
				Run.removeGroup(this);
			}
		}
	}
	
	public String toString()
	{
		String res ="";
		res+="Famille : \n";
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
