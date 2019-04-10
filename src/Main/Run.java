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
