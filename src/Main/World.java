package Main;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import Animal.*;
import Cell.*;
import Group.*;
import Parameters.Parms;

/* The class allow the group to interact with the map and the list of groups by themself
 * The class contains a static field for the map, which need to be initialized and a static field
 * for the list of the whole alive group inside the map, and a static field for the log (operations that are made inside
 * groups
*/


public class World 
{
	/************************************************************************************************
	 * 
	 * 										ATTRIBUTES
	 * 
	 ************************************************************************************************/
	
	private static ArrayList<Group> list = new ArrayList<Group>();//list of all the living groups inside the map
	private static Cell[][] map = null;//the map
	private static String log ="";//the log
	
	public static final double MAX_SIZE_OBSTACLE_IN_CELL = Parms.DIM_CELL/5;
	public static final double MAX_NUMBER_OBSTACLE_IN_CELL = 0;
	
	private static final String LOG_SEPARATOR = "\n\n";//for display
	
	private World(){}
	
	
	public static void update()
	{
		/* Update all the groups inside the map*/
		for (Group o : new ArrayList<Group>(list))
		{
			o.resetActed();
		}
		for (Group o : new ArrayList<Group>(list))
		{
			if(list.contains(o))
				o.update(map);
		}
	}
	
	
	/************************************************************************************************
	 * 
	 * 										GETTER/SETTER
	 * 
	 ************************************************************************************************/
	
	public static void setMap(int numCell)
	{
		if(map!=null)
			return; 
		
		map = new Cell[numCell][numCell];
		
		for(int i =0; i<map.length; i++)
		{
			for(int j=0; j<map[i].length; j++)
			{
				map[i][j] = new Cell(Parms.getValueDispersion(Parms.MAX_QUANTITY_FOOD_CELL));
				//setObstacles(map[i][j], i, j);
			}
		}
	}
	
	public static void setObstacles(Cell cell, int i, int j)
	{
		/* add obstacles into a cell*/
		double numObstacles = Math.random()*MAX_NUMBER_OBSTACLE_IN_CELL;
		double x, y;
		double radius;
		double actual_x, actual_y;
		Obstacle ob;
		
		for(int k=0; k<numObstacles; k++)
		{
			//create a new obstacle for the cell
			actual_x = i*Parms.DIM_CELL;
			actual_y = j*Parms.DIM_CELL;
			x = actual_x + Math.random()*Parms.DIM_CELL;//X center obstacle
			y = actual_y + Math.random()*Parms.DIM_CELL;//Y center obstacle
			
			
			radius = Math.random()*MAX_SIZE_OBSTACLE_IN_CELL;
			if((x+radius/2.)>=actual_x+Parms.DIM_CELL)
			{
				x -= radius/2.;
			}
			
			if((y+radius/2.)>=actual_y+Parms.DIM_CELL)
			{
				y -= radius/2.;
			}
			
			
			ob = new Obstacle(x, y, radius);
			cell.add(ob);
		}
	}
	
	public static Cell getCell(int i, int j)
	{
		if(map==null)
			return null;
		return map[i][j];
	}
	
	public static Cell getCell(double x, double y)
	{
		Point pt = Parms.getCellCoordinate(x, y);
		return map[pt.x][pt.y];
	}
	
	public static Cell getCell(Animal a)
	{
		Point pt = Parms.getCellCoordinate(a.getX(), a.getY());
		return map[pt.x][pt.y];
	}
	
	public static double getSize()
	{
		return (map.length*Parms.DIM_CELL);
	}
	
	public static String getLog()
	{
		return log;
	}
	
	public static void addToLog(String txt)
	{
		log+=txt+LOG_SEPARATOR;
	}
	
	public static void clearLog()
	{
		log ="";
	}
	
	public static Cell[][] getMap()
	{
		return map;
	}
	
	public static ArrayList<Group> getGroups()
	{
		return list;
	}
	
	public static Group getGroup(int elem)
	{
		return list.get(elem);
	}
	
	
	/************************************************************************************************
	 * 
	 * 										CREATION METHODS
	 * 
	 ************************************************************************************************/
	
	public static Herd createHerbivorous()
	{
		return createHerbivorous(0,0);
	}
	
	public static Herd createHerbivorous(double x, double y)
	{
		Herbivorous a = new Herbivorous();
		a.setX(x);
		a.setY(y);
		Herd h = new Herd(a);
		addGroup(h);
		
		return h;
	}
	
	public static Pack createCarnivorous(double x, double y)
	{
		Carnivorous a = new Carnivorous();
		a.setX(x);
		a.setY(y);
		Pack h = new Pack(a);
		addGroup(h);
		
		return h;
	}
	
	public static Pack createCarnivorous()
	{
		return createCarnivorous(0,0);
	}
	
	public static Herd createHerd(int numMember)
	{
		Herd h = new Herd(new Herbivorous());
		for(int i=0; i<numMember; i++)
		{
			h.add(createHerbivorous());
		}
		addGroup(h);
		
		return h;
	}
	
	public static Group createGroup(Animal a, Group reference) throws IllegalArgumentException
	{	
		/* Create a single animal represented by a group based on its diet (carnivorous or herbivorous)
		 * and its parent group
		*/
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
		o.setLocation();//needed
		
		return o;
	}
	
	
	/************************************************************************************************
	 * 
	 * 										ADD/REMOVE METHODS
	 * 
	 ************************************************************************************************/
	
	
	public static void removeGroup(Group o)
	{
		/* When a group is dead or need to be split that function is called.
		 * The function remove the group from the list of groups and remove the group from the map
		 */
		list.remove(o);
		removeFromMap(o);
		String s = o.toString();
		if(s!=null)
			log+="- Disparition de "+s+"\n\n";
	}
	
	public static void addGroup(Group o)
	{
		/* When a group is appears that function is called.
		 * The function add the group to the list of groups and add the group to the map
		 * (at the position of the group)
		 */
		if(o==null || o.getSize()==0)
			return;
		
		list.add(o);
		o.setGroup(null);
		o.setLocation();//update location before adding group to the map (needed)
		addToMap(o);
		log+="+ Apparition de "+o+LOG_SEPARATOR;
	}
	
	
	public static void addGroup(Animal a)
	{
		/* When an animal need to be add to the list of group we need to create
		 * a new group and add it to the list of groups and to the map
		*/
		if(a==null)
			return ;
		
		Group o = createGroup(a, null);
		list.add(o);
		o.setLocation();//update location before adding group to the map (needed)
		addToMap(o);
		log+="+ Apparition de "+o+LOG_SEPARATOR;
	}
	
	private static void removeFromMap(Group o) throws IllegalArgumentException
	{
		Point2D p = o.getLastKnownLocation();
		if(p==null)
			return;
		/* Because that function could be called when a group dies, it implies that the current
		 * location will be unknown because there would be no more animals inside the group. Thus,
		 * we need to get the last known location of the group
		 */
		double x = p.getX();
		double y = p.getY();
		
		if(map==null || Parms.isOutOfBounds(x, y))
			throw new IllegalArgumentException("Manipulation de la map incorecte");
		
		Point pt = Parms.getCellCoordinate(x, y);
		map[pt.x][pt.y].remove(o);
	}
	
	public static void removeFromMap(Group o, double x, double y)  throws IllegalArgumentException
	{
		/* Remove a group from a cell */
		if(map==null || Parms.isOutOfBounds(x, y))
			throw new IllegalArgumentException("Manipulation de la map incorecte");
		
		Point pt = Parms.getCellCoordinate(x, y);
		map[pt.x][pt.y].remove(o);
	}
	
	private static void addToMap(Group o)  throws IllegalArgumentException
	{
		Point2D p = o.getLocation();
		/* Because the function is called only when a group must be added we can use the current location*/
		double x = p.getX();
		double y = p.getY();
		
		if(map==null || Parms.isOutOfBounds(x, y))
			throw new IllegalArgumentException("Manipulation de la map incorecte");
		
		Point pt = Parms.getCellCoordinate(x, y);
		map[pt.x][pt.y].add(o);
	}
	
	public static void addToMap(Group o, double x, double y)  throws IllegalArgumentException
	{
		if(map==null || Parms.isOutOfBounds(x, y))
			throw new IllegalArgumentException("Manipulation de la map incorecte");
		
		Point pt = Parms.getCellCoordinate(x, y);
		map[pt.x][pt.y].add(o);
	}
	
	
	
	/************************************************************************************************
	 * 
	 * 										DISPLAY METHODS
	 * 
	 ************************************************************************************************/
	
	public static String _toString()
	{
		String res ="---------------LISTE GROUPES-------------------\n";
		for(Group o : list)
			res+="*** "+o.toString()+"\n";
		res+="\n--------------FIN LISTE GROUPES--------------------";
		return res;
	}
	
	public static void displayGroups()
	{
		System.out.println(_toString());
	}
	
	
	public static void displayMap()
	{
		Cell cell;
		System.out.println("--------------------- MAP ---------------------");
		for(int i=0; i<map.length; i++)
		{
			for(int j=0; j<map[i].length; j++)
			{
				cell = map[i][j];
				System.out.print("|"+cell.getNumberGroups()+"|");
			}
			System.out.print("\n");
		}
		System.out.println("--------------------- END MAP ---------------------");
	}
}