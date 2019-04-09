
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
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.PointerInfo;
import java.awt.RenderingHints;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Color;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.BorderLayout;

import Cell.*;
import Parameters.*;

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
    	windowsMap.cleanArea(Color.WHITE);
    	if(map==null)
    		return;
    	
    	Cell cell;
    	int numLign = map.length;
    	int numColumn =  map[0].length;
    	
    	int dimX =  (int)((windowsMap.getSize().getWidth())/(numColumn));
    	int dimY =  (int)((windowsMap.getSize().getHeight())/(numLign));
    
    	
    	for(int i=0; i<numLign; i++)
    	{
    		for(int j=0; j<numColumn; j++)
    		{
    			cell = map[i][j];	    	
    	    	
    	    	
    			drawCell(cell, i,j, dimX, dimY);
    		}
    	}
    	
    	windowsMap.refresh();
    }
    
    private static Color gradientColor(double p, Color startColor, Color endColor)
    {
    	//p = pourcentage [0,1]
    	int r = (int)(startColor.getRed()*p + endColor.getRed()*(1-p));
    	int g = (int)(startColor.getGreen()*p + endColor.getGreen()*(1-p));
    	int b = (int)(startColor.getBlue()*p + endColor.getBlue()*(1-p));
    	
    	return new Color(r, g, b);
    }
    
    private void drawCell(Cell cell, int x, int y, int dimX, int dimY)
    {
    	//get color for the current cell based on the quantity of food in it
    	double p = cell.getQuantity()/Parms.MAX_QUANTITY_CELL;
    	if(p>1)
    		p=1;
    	
    	Color c = gradientColor(p, new Color(168,145,62), new Color(54,202,68));
    	
    	//draw cell
    	windowsMap.drawRectangle(x*(dimX+BORDER_SIZE), y*(dimY+BORDER_SIZE), dimX, dimY, c);
    	
    	//draw obtscales
    	
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
    
    public int getRGB(int x, int y)
    {
        return img.getRGB(x, y);				
    }
    
    
    public void drawRectangle(int x, int y, int width, int height, Color color)
    {
    	Graphics g = img.getGraphics();
    	g.drawRect(x,y, width, height);  
    	g.setColor(color);  
    	g.fillRect(x, y, width, height);
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