public abstract class Animal extends ObjectMap implements DecreaseEnergy
{
	protected final String specie;
	protected double energy;
	protected double strength;
	protected double agility;
	protected double aggressivity;
	protected double soc;
	protected Case zoneFood;
	protected int foodRadius;
	protected Case zoneLive;
	protected int liveRadius;
	protected final boolean isMale;
	protected final int nbPossibleKid;
	protected int age;
	protected final int tGestation;
	protected final int tParent;


	public void setStrength(double a) {this.strength = a;}
	public double getStrength() {return strength;}

	public void setSociability(double a) {this.sociability = a;}
	public double getSociability() {return sociability;}

	public void setAgility(double a) {this.agility = a;}
	public double getAgility() {return agility;}

	public void setAggressivity(double a) {this.aggressivity = a;}
	public double getAggressivity() {return aggressivity;}

	public String getSpecie() {return specie;}

	public void setStrength(double a) {this.strength = a;}
	public double getStrength() {return strength;}

	public void age() {age++;}

	public void move(int dx, int dy)
	{
		x+=dx;
		y+=dy
	}
}