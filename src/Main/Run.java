package Main;
import Group.*;
import Animal.*;

import java.util.ArrayList;

public class Run 
{
	private static ArrayList<Group> list = new ArrayList<Group>();
	
	private Run(){}
	
	public static void main(String[] args)
	{
		//pack
		Pack p = new Pack();
		for(int i =0; i<5; i++)
		{
			p.add(createCarnivorous());
		}
		//list.add(p);
		
		Herd k;
		Family f = new Family((k=createHerbivorous()), createHerbivorous());
		Animal a = k.getAnimal();
		list.add(f);
		 
		 display();
		 
		 Herd h = new Herd();
		 h.add(createHerbivorous());
		 h.add(createHerbivorous());
		 list.add(h);
		 
		 //p.add(f);
		 h.add(f);
		 
		 display();
		 
		 Herd h2 = new Herd();
		 h2.add(createHerbivorous());
		 h2.add(createHerbivorous());
		 list.add(h2);
		 display();
		 h.add(h2);
		 display();
		//Herd  h = createHerd(3);
	}
	
	public static void display()
	{
		System.out.println(Run._toString());
	}
	
	public static Herd createHerbivorous()
	{
		Herbivorous a = new Herbivorous();
		Herd h = new Herd(a);
		Run.addGroup(h);
		
		return h;
	}
	
	public static Pack createCarnivorous()
	{
		Carnivorous a = new Carnivorous();
		Pack h = new Pack(a);
		Run.addGroup(h);
		
		return h;
	}
	
	public static Herd createHerd(int numMember)
	{
		Herd h = new Herd();
		for(int i=0; i<numMember; i++)
		{
			h.add(createHerbivorous());
		}
		Run.addGroup(h);
		
		return h;
	}
	
	public static String _toString()
	{
		String res ="---------------LISTE GROUPES-------------------\n";
		for(Group o : list)
			res+="*** "+o.toString()+"\n";
		res+="\n--------------FIN LISTE GROUPES--------------------";
		return res;
	}
	
	public static void removeGroup(Group o)
	{
		list.remove(o);
	}
	
	public static void addGroup(Group o)
	{
		list.add(o);
	}
	
	public static Group createGroup(Animal a, Group reference) throws IllegalArgumentException
	{	
		Group o = null;
		if(a.isCarnivorous())
		{
			o = new Pack(a);
		}else if(a.isHerbivorous())
		{
			o = new Herd(a);
		}else
		{
			throw new IllegalArgumentException("Ajout d'un animal non autorisé");
		}
		o.setGroup(reference);
		
		return o;
	}
	
	public static void addGroup(Animal a, Group reference)
	{
		list.add(createGroup(a, reference));
	}
}
