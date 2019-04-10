
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.BorderLayout;

import Cell.*;
import Parameters.*;
import java.util.ArrayList;


public class GraphicInterface extends JFrame
{
	private JFrame windowsMain;
	private MouseWheelListenerPanel windowsMap;
	private Cell[][] map;
	
	private static final Color OBSTACLE_C = Color.BLACK;
	private static final Color VEGETATION_C = Color.GREEN;
	private static final Color PACK_C = Color.RED;
	private static final Color HERD_C = Color.BLUE;
	
	private static final Color BORDER_C = Color.WHITE;
	private static final int BORDER_SIZE = 5;//px
	
	public GraphicInterface(Cell[][] map)
	{
		this.map = map;
		createAndShowGUI();
	}
	
    public void update()
    {	
    	if(map==null)
    		return;
    	
    	Cell cell;
    	int numLign = map.length;
    	int numColumn =  map[0].length;
    	
    	double dimX =  ((windowsMap.getSize().getWidth())/(numColumn));
    	double dimY =  ((windowsMap.getSize().getHeight())/(numLign));
    
    	
    	for(int i=0; i<numLign; i++)
    	{
    		for(int j=0; j<numColumn; j++)
    		{
    			cell = map[i][j];	    	
    	    	
    	    	
    			drawCell(cell, i,j, dimX, dimY);
    		}
    	}
    	
    	drawGrid(numLign, numColumn,dimX, dimY);
    	windowsMap.refresh();
    }
    
    private static Color gradientColor(double p, Color startColor, Color endColor)
    {
    	//p = pourcentage [0,1]
    	if(p>1)
    		p=1;
    	
    	int r = (int)(startColor.getRed()*p + endColor.getRed()*(1-p));
    	int g = (int)(startColor.getGreen()*p + endColor.getGreen()*(1-p));
    	int b = (int)(startColor.getBlue()*p + endColor.getBlue()*(1-p));
    	
    	return new Color(r, g, b);
    }
    
    private void drawGrid(int numLign, int numColumn, double dimX, double dimY)
    {
    	 /* 
    	  * 				center of the border
    	  *  ----------------------   |    ----------------------
    	  * |<-------------------->|  \/  |					    | 
    	  * |		dimX		   |      |					    |
    	  * |					   |      |					    |
    	  * |					   |      |					    |
    	  * 						<---->
    	  * 					  border size
    	  * 
    	  * We draw a line at each center of the grid border with the size of the gird border
    	  */
    	
     double x1, y1;
   	 double x2, y2;
   	 
   	 //draw all the vertical lines of the grid
   	 x1 = (dimX + BORDER_SIZE/2);
   	 y1 = 0;
   	 
   	 x2 = x1;
   	 y2 = windowsMap.getHeight();
   	 
	 for(int j=0; j<numColumn-1; j++)
	 {	 
		 windowsMap.drawLine(x1, y1, x2, y2, BORDER_C, BORDER_SIZE);
		 x1 = x1 + (dimX + BORDER_SIZE);
		 x2 = x1;
	 }
	 
	 
	 //draw all the horizontal lines of the grid
	 x1 = 0;
	 y1 = (dimY + BORDER_SIZE/2);
   	 
	 y2 = y1;
   	 x2 = windowsMap.getWidth();
   	 
	 for(int j=0; j<numLign-1; j++)
	 {	 
		 windowsMap.drawLine(x1, y1, x2, y2, BORDER_C, BORDER_SIZE);
		 y1 = y1 + (dimY + BORDER_SIZE);
		 y2 = y1;
	 }
	 
	 

    }
    
    private void drawCell(Cell cell, int x, int y, double dimX, double dimY)
    {
    	double px, py;
    	px = (x*(dimX+BORDER_SIZE));
    	py = (y*(dimY+BORDER_SIZE));
    	
    	//get color for the current cell based on the quantity of food in it
    	double p = cell.getQuantity()/Parms.MAX_QUANTITY_CELL;
    	Color c = gradientColor(p, new Color(54,202,68), new Color(168,145,62));
    	
    	//draw cell
    	windowsMap.drawRectangle(px, py, dimX, dimY, 0, c);
    	
    	//draw obtscales
    	ArrayList<Obstacle> obstacles =  cell.getObstacles();
    	double oX, oY;
    	double oDimX, oDimY;
    	double factorX, factorY;
    	
    	factorX =  dimX/Parms.DIM_CELL;
    	factorY =  dimY/Parms.DIM_CELL;
    	
    	System.out.println("factorX="+factorX+"   | factorY="+factorY);
    	
    	for(Obstacle o : obstacles)
    	{
    		oX = px + o.getX()*factorX;
    		oY = py + o.getY()*factorY;
    		
    		oDimX = o.getWidthEllipse()*factorX;
    		oDimY = o.getHeightEllipse()*factorY;
    		
    		p = o.getHeight()/Parms.MAX_QUANTITY_CELL;
    		c = gradientColor(p, new Color(0,0,0), new Color(211,211,211));
    		
    		windowsMap.drawEclipse(oX, oY, oDimX, oDimY, o.getRotation(), c);
    	}
    }

    private void createAndShowGUI()
    {
    	GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    	int screenWidth = gd.getDisplayMode().getWidth();
    	int screenHeight = gd.getDisplayMode().getHeight();
    	
    	
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.getContentPane().setLayout(new BorderLayout());

        MouseWheelListenerPanel m = new MouseWheelListenerPanel();
        //m.refresh();
        
        f.getContentPane().add(m, BorderLayout.CENTER);

        //f.pack();
        f.setSize(screenWidth,screenHeight);
        f.setLocationRelativeTo(null);
        f.setFocusable(true);
        f.setVisible(true);
        
        m.createArea(m.getSize()); // creation de l'image dans l'interface
        
        windowsMain = f;
        windowsMap = m;
    }
}

class MouseWheelListenerPanel extends JPanel implements MouseWheelListener
{
	private BufferedImage img;
	
	private static final int DEFAULT_SIZE = 4000;
	
    public MouseWheelListenerPanel(int size)
    {
    	super();
        addMouseWheelListener(this);
    }
    
    public MouseWheelListenerPanel()
    {
    	this(DEFAULT_SIZE);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		SwingUtilities.convertPointFromScreen(b, this);
		int x = (int) b.getX();
		int y = (int) b.getY();
		
        if (e.getWheelRotation() < 0)
        {
            System.out.println("mouse wheel Up (x, y)=("+x+","+y+")");
        }
        else
        {
        	 System.out.println("mouse wheel Down (x, y)=("+x+","+y+")"+this.getSize().getWidth());
        }
    }
    
    public void refresh(){
        repaint();
    }

    public void createArea(Dimension size)
    {
    	createArea((int)size.getWidth(), (int)size.getHeight());
    }
    
    public void createArea(int sizeX, int sizeY)
    {
    	img = new BufferedImage(sizeX, sizeY, BufferedImage.TYPE_INT_ARGB);
    }
    
    public Dimension getDimArea()
    {
    	return new Dimension(img.getWidth(), img.getHeight());
    }
    
    public void cleanArea(Color c)
    {
    	for(int i=0; i<img.getWidth(); i++)
    	{
    		for(int j=0; j<img.getHeight(); j++)
    		{
    			setRGB(i, j, c);
    		}
    	}
    }

    public void setRGB(int x, int y, Color c)
    {
        img.setRGB(x, y, c.getRGB());				
    }
    
    public Color getRGB(int x, int y)
    {
    	return new Color(img.getRGB(x, y), true);			
    }
    
    
    public void drawRectangle(double x, double y, double width, double height, double rotation, Color c)
    {
    	Graphics g = img.getGraphics();
    	Graphics2D g2 = (Graphics2D) g;
        Shape r1 = new Rectangle2D.Double(x, y, width, height);
        g2.rotate(rotation, x + width / 2, y + height / 2);
        g2.setPaint(c);
        g2.fill(r1);
        //draw border
        //g2.setStroke(new BasicStroke(sizeBorder));
        //g2.setPaint(border);
        
        g2.draw(r1);
    }
    
    public void drawEclipse(double x, double y, double width, double height, double rotation, Color c)
    {
    	//rotation in radian
    	Graphics g = img.getGraphics();
    	Graphics2D g2 = (Graphics2D) g;
        Shape r1 = new Ellipse2D.Double(x, y, width, height);
        g2.rotate(rotation, x + width / 2, y + height / 2);
        g2.setPaint(c);
        g2.fill(r1);
        //draw border
        //g2.setStroke(new BasicStroke(sizeBorder));
        //g2.setPaint(border);
        
        g2.draw(r1);
    }
    
    public void drawLine(double x1, double y1, double x2, double y2, Color c, int sizeBorder)
    {
    	//rotation in radian
    	Graphics g = img.getGraphics();
    	Graphics2D g2 = (Graphics2D) g;
        Shape r1 = new Line2D.Double(x1, y1, x2, y2);
        g2.setPaint(c);
        g2.fill(r1);
        //draw border
        g2.setStroke(new BasicStroke(sizeBorder));
        g2.setPaint(c);
        
        g2.draw(r1);
    }
    
    public void paint(Graphics g){			 
        if (img==null)
            return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        int h,w;
        double ri = (double) img.getHeight() / img.getWidth();
        double rp = (double) getHeight() / getWidth();
        if(ri>rp){
            h = getHeight(); 
            w = (int)(h/ri);
        }
        else{
            w = getWidth();
            h = (int)(w*ri);
        }
     
        g2.drawImage(img, 0, 0, w, h, null);
    }
}