import java.util.ArrayList;

public class Pack extends Group
{
	private Animal chief;
	private ArrayList<Object> others;
	
	public Pack()
	{
		super(Parms.TYPE_PACK);
		chief = null;
		others = new ArrayList<Object>();
	}
	
	public int getSize() throws IllegalArgumentException
	{
		int count = 0;
		for(Object o : others)
		{
			//only single animal and family can be a member of the pack
			if(o instanceof Animal)
			{
				count++;
			}else if(o instanceof Family)
			{
				Family f = (Family)o;
				count+=f.getSize();
			}else
			{
				throw new IllegalArgumentException("Le membre de la meute n'est pas reconnu comme type autorisé");
			}
		}
		
		return 1+count;
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
	public void setOthers(ArrayList<Object> l)
	{
		others = new ArrayList<Object>(l);
	}
	
	public void addMember(Animal a)
	{
		double s1 = a.getStrength();
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
			chief = a;
			others.add(temp);
		}else
		{
			others.add(a);
		}
	}
	
	
	//implement
	public void setSociability()
	{
		
	}
}
