package Main;
import Group.*;
import Parameters.Parms;
import Animal.*;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import Cell.*;

public class Run 
{	
	private static final String FIELD_SEPARATOR = " ";
	private static final int DIM_WORLD = 5;
	
	public static void main(String[] args)
	{
		World.setMap(DIM_WORLD);//initialize the world
		
		//get all the type of possible species for the world
		ArrayList<Specie> arraySpecies;
		try
		{
			arraySpecies = read("Specie.txt", FIELD_SEPARATOR);
		}catch(IOException e)
		{
			System.out.println("Erreur critique : \n"+e);
			return;
		}
		
		/* Create a specific number of animal for each specie
		 * Note that the characteristics of the specie will be modulated (centered to the value of the specie)
		 * to have little variation for each animals of the same specie
		 */
		int numElem;
		Group o;
		double x, y;
		Cell cell;
		int age, teenAge, oldAge, maxAge;
		
		for(Specie s : arraySpecies)
		{
			numElem = 20 + (int)(Math.random()*Parms.INITIAL_NUMBER_PER_SPECIE);
			
			for(int k=0; k<numElem; k++)
			{
				x = Math.random()*World.getSize();
				y = Math.random()*World.getSize();
				
				cell = World.getCell(x,y);
				teenAge = (int)Parms.getValueDispersion(s.getTeenAge())*Parms.FACTOR_FOR_AGE;
				oldAge = (int)Parms.getValueDispersion(s.getOldAge())*Parms.FACTOR_FOR_AGE;;
				maxAge = (int)Parms.getValueDispersion(s.getMaxAge())*Parms.FACTOR_FOR_AGE;;
				if(oldAge<=teenAge)
					oldAge+=2*Parms.FACTOR_FOR_AGE;;
				if(maxAge<=oldAge)
					maxAge+=5*Parms.FACTOR_FOR_AGE;;
				
				age = teenAge + (int)(Math.random()*(oldAge-teenAge));
				
				if(s.isCarnivorous())
				{
					Carnivorous a = new Carnivorous(s.getName(), 1., Parms.getValueDispersion(s.getStrength()),
							Parms.getValueDispersion(s.getAgility()), Parms.getValueDispersion(s.getAgressivity()),
							Parms.getValueDispersion(s.getSociability()), cell, 1, (int)Parms.getValueDispersion(s.getNumKid()),
							age, Parms.getValueDispersion(s.getHeight()), Parms.getValueDispersion(s.getVision()),
							teenAge, oldAge, maxAge);
					a.setX(x);
					a.setY(y);
					
					o = new Pack(a);
				}else
				{
					Herbivorous a = new Herbivorous(s.getName(), 1., Parms.getValueDispersion(s.getStrength()),
							Parms.getValueDispersion(s.getAgility()), Parms.getValueDispersion(s.getAgressivity()),
							Parms.getValueDispersion(s.getSociability()), cell, 1, (int)Parms.getValueDispersion(s.getNumKid()),
							age, Parms.getValueDispersion(s.getHeight()), Parms.getValueDispersion(s.getVision()),
							teenAge, oldAge, maxAge);
					a.setX(x);
					a.setY(y);
					
					o = new Herd(a);
				}
				World.addGroup(o);
			}
		}
		
		GraphicInterface I = new GraphicInterface(World.getMap(), World.getGroups());//start graphical interface
		I.update(World.getLog());
		String log;
		int waitingTime = 500;//time to wait in ms
		while(true)
		{
			World.update();
			
			log = World.getLog();
			I.update(log);
			if(log.length()>10000)
			{
				World.clearLog();
			}
			
			try{TimeUnit.MILLISECONDS.sleep(waitingTime);}catch(Exception e){}//wait
		}
		
	}
	
	static class Specie
	{
		/* The simulation will be populated with animal from different species that have different
		 * characteristics. That static class help to store characteristics for a specie 
		 */
		private String name;
		private double strength;
		private double agility;
		private double agressivity;
		private double sociability;
		private int numKid;
		private double height;
		private double vision;
		private int teenAge;
		private int oldAge;
		private int maxAge;
		private boolean carnivorous;
		
		
		public Specie()
		{
			
		}
		
		public Specie(String name, double strength, double agility, double agressivity, double sociability, int numKid,
				double height, double vision, int teenAge, int oldAge, int maxAge, boolean carnivorous) {
			super();
			this.name = name;
			this.strength = strength;
			this.agility = agility;
			this.agressivity = agressivity;
			this.sociability = sociability;
			this.numKid = numKid;
			this.height = height;
			this.vision = vision;
			this.teenAge = teenAge;
			this.oldAge = oldAge;
			this.maxAge = maxAge;
			this.carnivorous = carnivorous;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public double getStrength() {
			return strength;
		}
		public void setStrength(double strength) {
			this.strength = strength;
		}
		public double getAgility() {
			return agility;
		}
		public void setAgility(double agility) {
			this.agility = agility;
		}
		public double getAgressivity() {
			return agressivity;
		}
		public void setAgressivity(double agressivity) {
			this.agressivity = agressivity;
		}
		public double getSociability() {
			return sociability;
		}
		public void setSociability(double sociability) {
			this.sociability = sociability;
		}
		public int getNumKid() {
			return numKid;
		}
		public void setNumKid(int numKid) {
			this.numKid = numKid;
		}
		public double getHeight() {
			return height;
		}
		public void setHeight(double height) {
			this.height = height;
		}
		public double getVision() {
			return vision;
		}
		public void setVision(double vision) {
			this.vision = vision;
		}
		public int getTeenAge() {
			return teenAge;
		}
		public void setTeenAge(int teenAge) {
			this.teenAge = teenAge;
		}
		public int getOldAge() {
			return oldAge;
		}
		public void setOldAge(int oldAge) {
			this.oldAge = oldAge;
		}
		public int getMaxAge() {
			return maxAge;
		}
		public void setMaxAge(int maxAge) {
			this.maxAge = maxAge;
		}
		
		public void setCarnivorous(boolean isCanrivorous){
			this.carnivorous = isCanrivorous;
		}
		
		public boolean isCarnivorous()
		{
			return carnivorous;
		}
		
		public String toString()
		{
			return (carnivorous?"Carnivore":"Herbivore")+"(strength="+strength+", agility="+agility+", agressivity="+agressivity+", sociability="+sociability+", numKid="+numKid
					+", height="+height+", vision="+vision+", teenAge="+teenAge+", oldAge="+oldAge+", maxAge="+maxAge+")";
		}
		
	}
	
	private static ArrayList<Specie> read(String path, String fieldSeparator) throws IOException
	{
		/* read a file that contains all the characteristics for a specie that will be instantiated into
		 * the simulation. The file must be a text file (.txt) and each characteristic must be separated 
		 * by the given filed separator
		 */
		ArrayList<Specie> array = new ArrayList<Specie>();
		BufferedReader br = new BufferedReader(new FileReader(path));
		try 
		{
		    String line;
		    String[] splitLine;
		    Specie s;
		    
		    while((line = br.readLine())!=null)
		    {
		    	splitLine = line.split(fieldSeparator);
		    	
		    	
		    	s = new Specie();
		    	s.setName(splitLine[0]);
		    	s.setStrength(Double.parseDouble(splitLine[1]));
		    	s.setAgility(Double.parseDouble(splitLine[2]));
		    	s.setAgressivity(Double.parseDouble(splitLine[3]));
		    	s.setSociability(Double.parseDouble(splitLine[4]));
		    	s.setNumKid(Integer.parseInt(splitLine[5]));
		    	s.setHeight(Double.parseDouble(splitLine[6]));
		    	s.setVision(Double.parseDouble(splitLine[7]));
		    	s.setTeenAge(Integer.parseInt(splitLine[8]));
		    	s.setOldAge(Integer.parseInt(splitLine[9]));
		    	s.setMaxAge(Integer.parseInt(splitLine[10]));
		    	s.setCarnivorous(splitLine[11].equals("C"));
		    	array.add(s);
		    }
		      
		}catch(FileNotFoundException e)
		{
			System.err.println("Erreur de lecture du fichier\n"+e);
		}
		finally
		{
			br.close();
		}
	  
		return array;
	}
}