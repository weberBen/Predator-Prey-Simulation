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
		ArrayList<Animal> temp_l = new ArrayList<Animal>();
		
		for(int i=0; i<5; i++)
		{
			Animal a = new Carnivorous();
			temp_l.add(a);
			list.add(new Pack(a));
		}
		
		System.out.println("-----------------------\nliste=\n"+Run._toString());
		
		Pack  h= new Pack();
		ArrayList<Group> temp = new ArrayList<Group>(list);
		for(Group o : temp)
		{
			h.addGroup(o);
		}
		list.add(h);
		
		System.out.println("-------------------------\nliste=\n"+Run._toString());
		
		System.out.println("anuimal="+temp.get(0));
		h.setDeath(temp.get(0));
		
		System.out.println("-------------------------\nliste=\n"+Run._toString());
		
		
		
		
		
		/*
		for (Group p : h.others)
			System.out.println(p);*/
		
	}
	
	public static String _toString()
	{
		String res ="";
		for(Group o : list)
			res+=o.toString()+"\n";
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
	
	
}
