package Cell;
import Parameters.Parms;

public class VegetalFood extends Cell
{
	private double quantity;
	
	public VegetalFood(double rigidity, double height, double quantity)
	{
		super(rigidity, height, Parms.DIM_CELL);
		this.quantity = quantity;
	}
	
	public double getQuantity()
	{
		return quantity;
	}
	
	public void decreaseQuantity(double ep)
	{
		quantity-=ep;
		if(quantity<0)
		{
			quantity=0;
		}
	}
	
	public double getFood(double needs)
	{
		double food;
		if(quantity>=needs)
		{
			food = quantity;
			quantity = 0;
		}else
		{
			food = needs;
			quantity = quantity - needs;
		}
		
		return food;
	}
}
