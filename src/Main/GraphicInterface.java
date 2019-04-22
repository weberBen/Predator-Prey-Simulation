package Main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.BorderLayout;

import Cell.*;
import Group.Group;
import Parameters.*;
import java.util.ArrayList;


public class GraphicInterface extends JFrame
{
	private JFrame windowsMain;
	private MouseWheelListenerPanel windowsMap;
	private JLabel console;
	private Cell[][] map;
	private ArrayList<Group> listGroup;
	
	private double zoomFactor;
	private int startColumn;
	private int startLine;
	private double translationX;
	private double translationY;
	
	private static final Color OBSTACLE_C = Color.BLACK;
	private static final Color VEGETATION_C = Color.GREEN;
	private static final Color PACK_C = Color.RED;
	private static final Color HERD_C = Color.BLUE;
	
	private static final Color BORDER_C = new Color(189,193,191);
	private static final int BORDER_SIZE = 5;//px
	
	private static final double SIZE_ANIMAL_ON_MAP = 0.3;
	
	private static final Color CARNIVOROUS_COLOR =  new Color(0,0,0);
	private static final Color HERBIIVOROUS_COLOR =  new Color(255,255,255);
	private static final Color FAMILY_COLOR =  new Color(248,248,98);
	private static final Color PACK_COLOR =  new Color(217,86,189);
	private static final Color HERD_COLOR =  new Color(173,86,217);
	private static final Color ANIMAL_COLOR =  new Color(227,52,86);
	
	private static final Color[] GRADIENT_COLORS_GRASS = {new Color(54,202,68), new Color(168,145,62)};
	private static final Color[] GRADIENT_COLORS_HEIGHT = {new Color(0,0,0), new Color(211,211,211)};
	
	private static final int WIDTH_LOG_LABEL = 300; 
	private static final int HEIGHT_LOG_LABEL = 500; 
	
	public GraphicInterface(Cell[][] map, ArrayList<Group> listGroup)
	{
		this.map = map;
		this.listGroup = listGroup;
		this.zoomFactor = 1;
		createAndShowGUI();
	}
	
    public void update()
    {	
    	repainView();
    	//zoomInMapView(windowsMap.getMouseX(),windowsMap.getMouseY(), windowsMap.getWheelFactor(), windowsMap.getHorizontalArrowFactor(),windowsMap.getVerticalArrowFactor());
    }
    
    public void update(String txt)
    {	
    	txt = txt.replace("\n", "<br/>");
    	txt = "<html>"+txt+"</html>";
    	console.setText(txt);
    	repainView();
    	
    }
    
    private void drawDefaultView()
    {
    	reset();
    	changeMapView(0,0, 1, 0, 0);
    }
    
    private Color getColorForAnimal(Group o)
    {
    	if(o.isAnimal())
    	{
    		return ANIMAL_COLOR;
    	}else if(o.isPack())
    	{
    		return PACK_COLOR;
    	}else if(o.isFamily())
    	{
    		return FAMILY_COLOR;
    	}else if(o.isHerd())
    	{
    		return HERD_COLOR;
    	}
    	
    	return null;
    }
    
    private Color getColorForAnimalDiet(Group o)
    {
    	if(o.isCarnivorous())
    	{
    		return CARNIVOROUS_COLOR;
    	}else
    	{
    		return HERBIIVOROUS_COLOR;
    	}
    }
    
    private String getInfo(Group o, int size)
    {
    	String res= o.getType();
    	res+=" ";
    	
    	if(size==-1)
    		size = o.getSize();    	
    	res+="(Id="+o.getID()+", Size="+size+")\n"+o.getDiet();
    	
    	return res;
    }
    
    private void drawAnimals(double dimX, double dimY, double tx, double ty)
    {
    	double factorX, factorY;
    	factorX =  dimX/Parms.DIM_CELL;
    	factorY =  dimY/Parms.DIM_CELL;
    	
    	int size;
    	double x, y;
    	Shape s;
    	Point2D p;
    	Color inside, outside;
    	Rectangle r;
    	for(Group o : listGroup)
    	{
    		if(o==null)
    			continue;
    		
    		p = o.getLocation();
    		if(p==null)
    			continue;
    		
    		size = o.getSize();
    		
    		x = p.getX()*factorX;
    		y = p.getY()*factorY;
    		
    		s = new Rectangle2D.Double(x, y, size*factorX*SIZE_ANIMAL_ON_MAP, size*factorY*SIZE_ANIMAL_ON_MAP);
    		s = transform(s,tx,ty,1,1);
    		
    		r = s.getBounds();
    		inside = getColorForAnimalDiet(o);
    		outside = getColorForAnimal(o);
    		
    		windowsMap.drawShape(s, inside, (int)(((factorX+factorY)/2*SIZE_ANIMAL_ON_MAP)/3), outside);
    		windowsMap.displayText(getInfo(o, size), (int)r.getX(), (int)r.getY());
    	}
        
    }
    
    private int getXIndex(double x, double dimX)
    {
    	int numberCells = (int)(x/dimX);
    	return numberCells;
    }
    
    
    private void reset()
    {
    	this.startColumn = 0;
    	this.startLine = 0;
    	this.zoomFactor = 1;
    	this.translationX = 0;
    	this.translationY = 0;
    	windowsMap.resetParms();
    }
    
    private void repainView()
    {	
    	if(map==null)
    		return;
    	
    	windowsMap.cleanArea(BORDER_C);
    	
    	Cell cell;
    	int numLign = map.length;
    	int numColumn =  map[0].length;
    	
    	double dimX =  ((windowsMap.getSize().getWidth()*this.zoomFactor)/(numColumn));
    	double dimY =  ((windowsMap.getSize().getHeight()*this.zoomFactor)/(numLign));

    	for(int i=0; i<numLign; i++)
    	{
    		for(int j=0; j<numColumn; j++)
    		{
    			cell = map[i][j];	    	
    	    	
    			drawCell(cell, i,j, dimX, dimY, this.translationX, this.translationY);
    		}
    	}
    	
    	drawAnimals(dimX, dimY, this.translationX,this.translationY);
    	
    	windowsMap.refresh();
    }
    
    
    private void changeMapView(double x, double y, double zoom, double factorX, double factorY)
    {
    	if(map==null)
    		return;
    	
    	windowsMap.cleanArea(BORDER_C);
    	
    	Cell cell;
    	int numLign = map.length;
    	int numColumn =  map[0].length;
    	
    	double tempX = ((windowsMap.getSize().getWidth()*this.zoomFactor)/(numColumn));
    	double tempY = ((windowsMap.getSize().getHeight()*this.zoomFactor)/(numLign));
    	
    	int columnCursor = this.startColumn + getXIndex(x, tempX);
    	int lineCursor =  this.startLine + getXIndex(y, tempY);
    	
    	this.startColumn = columnCursor;
    	this.startLine = lineCursor;
    	
    	double dimX =  ((windowsMap.getSize().getWidth()*zoom)/(numColumn));
    	double dimY =  ((windowsMap.getSize().getHeight()*zoom)/(numLign));

    	double tx = - (columnCursor*(dimX+BORDER_SIZE) + factorX*(dimX/(Math.exp(zoom/3))));
    	this.translationX = tx;
    	double ty = - (lineCursor*(dimY+BORDER_SIZE) + factorY*(dimY/(Math.exp(zoom/3))));
    	this.translationY = ty;
    	
    	if(columnCursor>=numColumn || lineCursor>=numLign)
    	{
    		tx = 0;
    		ty = 0;
    		reset();
    	}

    	for(int i=0; i<numLign; i++)
    	{
    		for(int j=0; j<numColumn; j++)
    		{
    			cell = map[i][j];	    	
    	    	
    			drawCell(cell, i,j, dimX, dimY, tx, ty);
    		}
    	}
    	
    	drawAnimals(dimX, dimY, tx,ty);
    	
    	this.zoomFactor = zoom; 
    	windowsMap.refresh();
    }
    
    
    private void moveMapView(double x, double y, double zoom, double factorX, double factorY)
    {	
    	if(map==null)
    		return;
    	
    	windowsMap.cleanArea(BORDER_C);
    	
    	Cell cell;
    	int numLign = map.length;
    	int numColumn =  map[0].length;
    	
    	double tempX = ((windowsMap.getSize().getWidth()*this.zoomFactor)/(numColumn));
    	double tempY = ((windowsMap.getSize().getHeight()*this.zoomFactor)/(numLign));
    	
    	int columnCursor = this.startColumn + getXIndex(x, tempX);
    	int lineCursor =  this.startLine + getXIndex(y, tempY);
    	
    	double dimX =  ((windowsMap.getSize().getWidth()*zoom)/(numColumn));
    	double dimY =  ((windowsMap.getSize().getHeight()*zoom)/(numLign));

    	double tx = - (columnCursor*(dimX+BORDER_SIZE) + factorX*(dimX/(Math.exp(zoom/3))));
    	double ty = - (lineCursor*(dimY+BORDER_SIZE) +  factorY*(dimY/Math.exp(zoom/3)));
    	if(columnCursor>=numColumn || lineCursor>=numLign)
    	{
    		tx = 0;
    		ty = 0;
    	}
    	
    	for(int i=0; i<numLign; i++)
    	{
    		for(int j=0; j<numColumn; j++)
    		{
    			cell = map[i][j];	    	
    	    	
    			drawCell(cell, i,j, dimX, dimY, tx, ty);
    		}
    	}
    	drawAnimals(dimX, dimY, tx,ty);
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
    	double p = cell.getQuantity()/Parms.MAX_QUANTITY_FOOD_CELL;
    	Color c = gradientColor(p, GRADIENT_COLORS_GRASS[0], GRADIENT_COLORS_GRASS[1]);
    	
    	//draw cell
    	windowsMap.drawRectangle(px, py, dimX, dimY, 0, c);
    	
    	//draw obtscales
    	/*ArrayList<Obstacle> obstacles =  cell.getObstacles();
    	double factorX, factorY;
    	
    	factorX =  dimX/Parms.DIM_CELL;
    	factorY =  dimY/Parms.DIM_CELL;
    	
    	for(Obstacle o : obstacles)
    	{
    		p = o.getHeight()/Parms.MAX_QUANTITY_CELL;
    		c = gradientColor(p, GRADIENT_COLORS_HEIGHT[0], GRADIENT_COLORS_HEIGHT[1]);
    		
    		AffineTransform tx = new AffineTransform();
    	    tx.scale(factorX,factorY);
    	    Shape s = tx.createTransformedShape(o.getShape());

    		windowsMap.drawShape(s, c);
    	}*/
    }
    
    private Shape transform(Shape s, double tx, double ty, double factorX, double factorY)
    {
    	AffineTransform at = new AffineTransform();
    	at.scale(factorX,factorY);
    	s = at.createTransformedShape(s);
    	
    	at = new AffineTransform();
    	at.translate(tx, ty);
    	s = at.createTransformedShape(s);
    	
    	return s;
    }

    private void drawCell(Cell cell, int x, int y, double dimX, double dimY, double tx, double ty)
    {
    	double px, py;
    	px = (x*(dimX+BORDER_SIZE));
    	py = (y*(dimY+BORDER_SIZE));
    	
    	double factorX, factorY;
    	
    	factorX =  dimX/Parms.DIM_CELL;
    	factorY =  dimY/Parms.DIM_CELL;
    	
    	//get color for the current cell based on the quantity of food in it
    	double p = cell.getQuantity()/Parms.MAX_QUANTITY_FOOD_CELL;
    	Color c = gradientColor(p, GRADIENT_COLORS_GRASS[0], GRADIENT_COLORS_GRASS[1]);
    	
    	//draw cell
    	
    	Shape r1 = new Rectangle2D.Double(px, py, dimX, dimY);
    	r1 = transform(r1,tx,ty,1,1);
    	windowsMap.drawShape(r1, c);
    	
    	
    	//draw obtscales
    	/*ArrayList<Obstacle> obstacles =  cell.getObstacles();
    	
    	
    	for(Obstacle o : obstacles)
    	{
    		p = o.getHeight()/Parms.MAX_QUANTITY_CELL;
    		c = gradientColor(p, GRADIENT_COLORS_HEIGHT[0], GRADIENT_COLORS_HEIGHT[1]);
    		
    	    Shape s = transform(o.getShape(),tx,ty,factorX,factorY);

    		windowsMap.drawShape(s, c);
    	}*/
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
        
        f.getContentPane().add(m, BorderLayout.CENTER);

        //-------------------------- PANEL --------------------------
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        JButton b1 = new JButton(); 
        b1.addActionListener(new ActionListener()
        {
        	  public void actionPerformed(ActionEvent e)
        	  {
        		  drawDefaultView();
        	  }
        });
        //b1.setSize(400,400);
        b1.setVisible(true);
        b1.setText("Vue globale");
        panel.add(b1, BorderLayout.WEST);
        
        f.add(panel, BorderLayout.NORTH);
       //---------------------------------------------------------
        JLabel label = new JLabel("");
        this.console = label;
        JScrollPane scrollF = new JScrollPane(label,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
	    	       JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollF.setPreferredSize(new Dimension(WIDTH_LOG_LABEL,HEIGHT_LOG_LABEL));
        
        f.add(scrollF, BorderLayout.EAST);
        
        
        //---------------------- CREATE WINDOWS ----------------------
        //f.pack();
        f.setSize(screenWidth,screenHeight);
        f.setLocationRelativeTo(null);
        f.setFocusable(true);
        f.setVisible(true);
        
        m.createArea(m.getSize()); // creation de l'image dans l'interface
        
        windowsMain = f;
        windowsMap = m;
        
    }
    
    class MouseWheelListenerPanel extends JPanel implements MouseWheelListener, KeyListener, MouseMotionListener
    {
    	private BufferedImage img;
    	
    	private static final int DEFAULT_SIZE = 4000;
    	private int wheelFactor;
    	private int horizontalArrowFactor;
    	private int verticalArrowFactor;
    	private double x;
    	private double y;
    	
        public MouseWheelListenerPanel(int size)
        {
        	super();
        	wheelFactor = 1;
        	horizontalArrowFactor = 0;
        	verticalArrowFactor = 0;
            addMouseWheelListener(this);
            addKeyListener(this);
            addMouseMotionListener(this);
        }
        
        public MouseWheelListenerPanel()
        {
        	this(DEFAULT_SIZE);
        }

        public void addNotify() 
        {
            super.addNotify();
            requestFocus();
        }
        
        public void resetParms()
        {
        	wheelFactor = 1;
        	horizontalArrowFactor = 0;
        	verticalArrowFactor = 0;
        	x = 0;
        	y = 0;
        	requestFocus();
        }
        
        public int getWheelFactor()
        {
        	return wheelFactor;
        }
        
        public int getHorizontalArrowFactor()
        {
        	return horizontalArrowFactor;
        }
        
        public int getVerticalArrowFactor()
        {
        	return verticalArrowFactor;
        }
        
        public double getMouseX()
        {
        	return x;
        }
        
        public double getMouseY()
        {
        	return y;
        }
        
        public void mouseMoved(MouseEvent e) 
        {
           // x= e.getX();
            //y=e.getY();
        }

        public void mouseDragged(MouseEvent e) {}
         
        @Override
        public void mouseWheelMoved(MouseWheelEvent e)
        {
    		PointerInfo a = MouseInfo.getPointerInfo();
    		Point b = a.getLocation();
    		SwingUtilities.convertPointFromScreen(b, this);
    		x = b.getX();
    		y = b.getY();
    		
            if (e.getWheelRotation() < 0)//mouse wheel Up
            {
            	wheelFactor++;
            }
            else//mouse wheel Down
            {
            	wheelFactor = Math.max(1, --wheelFactor);
            }
            
            changeMapView(x, y, wheelFactor, horizontalArrowFactor, verticalArrowFactor);
        }
        
        public void keyPressed(KeyEvent event)
        {
        	if(event.getKeyCode() == KeyEvent.VK_LEFT || event.getKeyCode() == KeyEvent.VK_RIGHT 
        	   || event.getKeyCode() == KeyEvent.VK_DOWN || event.getKeyCode() == KeyEvent.VK_UP)
        	{
        		switch(event.getKeyCode())
        		{
        			case KeyEvent.VK_UP :
        			{
        				verticalArrowFactor++;
        			}
        				break;
        			case KeyEvent.VK_DOWN :
        			{
        				verticalArrowFactor--;
        			}
        				break;
        			case KeyEvent.VK_RIGHT :
        			{
        				horizontalArrowFactor++;
        			}
        				break;
        			case KeyEvent.VK_LEFT :
        			{
        				horizontalArrowFactor--;
        			}
        				break;
        		}
        		
        		changeMapView(x, y, wheelFactor,horizontalArrowFactor,  verticalArrowFactor);
        	}
        }

        public void keyReleased(KeyEvent event){} 

        public void keyTyped(KeyEvent event){}
        
        
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
        
        public void drawRectangle(double x, double y, double width, double height, double rotation, Color c, int borderSize, Color cBorder)
        {
        	Graphics g = img.getGraphics();
        	Graphics2D g2 = (Graphics2D) g;
            Shape r1 = new Rectangle2D.Double(x, y, width, height);
            g2.rotate(rotation, x + width / 2, y + height / 2);
            g2.setPaint(c);
            g2.fill(r1);
            //draw border
            g2.setStroke(new BasicStroke(borderSize));
            g2.setPaint(cBorder);
            
            g2.draw(r1);
        }
        
        public void drawEllipse(double x, double y, double width, double height, double rotation, Color c)
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
        
        public void drawShape(Shape s, Color c)
        {
        	Graphics g = img.getGraphics();
        	Graphics2D g2 = (Graphics2D) g;
            
        	g2.setPaint(c);
            g2.fill(s);
            //g2.draw(s);
        }
        
        public void drawShape(Shape s, Color c, int borderSize, Color cBorder)
        {
        	Graphics g = img.getGraphics();
        	Graphics2D g2 = (Graphics2D) g;
            
        	g2.setPaint(c);
            g2.fill(s);
            
            //draw border
            g2.setStroke(new BasicStroke(borderSize));
            g2.setPaint(cBorder);
            
            g2.draw(s);
            //g2.draw(s);
        }
        
        public void displayText(String txt, int x, int y)
        {
        	String [] lines = txt.split("\n"); //breaking the lines into an array
        	
            Graphics g = img.getGraphics();
            Font f = new Font("Dialog", Font.PLAIN, 12);
            g.setFont(f);
            int lineHeight = g.getFontMetrics().getHeight();
            //here comes the iteration over all lines
            for(int lineCount = 0; lineCount < lines.length; lineCount ++){ //lines from above
                int xPos = x;
                int yPos = (y- f.getSize()*lines.length) + lineCount * lineHeight;
                if(lines.length==0)
                	yPos = y;
                String line = lines[lineCount];
                g.drawString(line, xPos, yPos);
            }
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
}

/*public void update44(double x, double y, double factor)
{	
	if(map==null)
		return;
	
	windowsMap.cleanArea(BORDER_C);
	
	System.out.println("-----------------------");
	Cell cell;
	int numLign = map.length;
	int numColumn =  map[0].length;
	
	double tempX = ((windowsMap.getSize().getWidth()*this.zoomFactor)/(numColumn));
	double tempY = ((windowsMap.getSize().getHeight()*this.zoomFactor)/(numLign));
	
	int columnCursor = this.startColumn + getXIndex(x, tempX);
	int lineCursor =  this.startLine + getXIndex(y, tempY);
	
	System.out.println("factor="+this.zoomFactor);
	System.out.println("dimX="+tempX+"   | dimY="+tempY);
	System.out.println("wiodth="+windowsMap.getSize().getWidth()+"   |  height="+windowsMap.getSize().getHeight());
	System.out.println("x="+x+"  | y="+y);
	
	double dimX =  ((windowsMap.getSize().getWidth()*factor)/(numColumn));
	double dimY =  ((windowsMap.getSize().getHeight()*factor)/(numLign));
	
	
	System.out.println("i_cursor="+lineCursor+"   |  j_corsuro="+columnCursor);
	
	int start_i, end_i;
	int start_j, end_j;
	
	int actual_numColumn = Math.min(numColumn, (int)Math.round(windowsMap.getSize().getWidth()/dimX));
	int actual_numLine = Math.min(numLign, (int)Math.round(windowsMap.getSize().getHeight()/dimY));
	System.out.println("*****");
	System.out.println("actual_numLine="+actual_numColumn+"   |  actual_numCOlumn="+actual_numLine);
	
	start_i = Math.max(0, lineCursor-actual_numLine/2);
	end_i = Math.min(numLign, lineCursor+actual_numLine/2+1);
	
	start_j = Math.max(0, columnCursor-actual_numColumn/2);
	end_j = Math.min(numColumn, columnCursor+actual_numColumn/2+1);
	
	this.startColumn = start_i;
	this.startLine = start_j;
	
	System.out.println("start_i="+start_i+"   [  end_i="+end_i);
	System.out.println("start_j="+start_j+"   [  end_j="+end_j);
			
	for(int i=start_i; i<end_i; i++)
	{
		for(int j=start_j; j<end_j; j++)
		{
			cell = map[i][j];	    	
	    	
			drawCell(start_i,start_j,cell, i,j, dimX, dimY);
		}
	}
	
	//drawGrid(numLign, numColumn,dimX, dimY);

	this.zoomFactor = factor; 
	windowsMap.refresh();
	
	private void drawCell(int start_x, int start_y, Cell cell, int x, int y, double dimX, double dimY)
{
	double px, py;
	px = ((x-start_x)*(dimX+BORDER_SIZE));
	py = ((y-start_y)*(dimY+BORDER_SIZE));
	
	//get color for the current cell based on the quantity of food in it
	double p = cell.getQuantity()/Parms.MAX_QUANTITY_CELL;
	Color c = gradientColor(p, new Color(54,202,68), new Color(168,145,62));
	
	//draw cell
	windowsMap.drawRectangle(px, py, dimX, dimY, 0, c);
	
	//draw obtscales
	ArrayList<Obstacle> obstacles =  cell.getObstacles();
	double factorX, factorY;
	
	factorX =  dimX/Parms.DIM_CELL;
	factorY =  dimY/Parms.DIM_CELL;
	
	for(Obstacle o : obstacles)
	{
		p = o.getHeight()/Parms.MAX_QUANTITY_CELL;
		c = gradientColor(p, new Color(0,0,0), new Color(211,211,211));
		
	    //resize
		AffineTransform tx = new AffineTransform();
	    tx.scale(factorX,factorY);
	    Shape s = tx.createTransformedShape(o.getShape());
	    
	    //translate
	    tx = new AffineTransform();
	    tx.translate(-start_x*dimX,-start_y*dimY);
	    s = tx.createTransformedShape(s);
	    
		windowsMap.drawShape(s, c);
	}
}
}


private void update()
    {
    	reset();
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
    	drawAnimals(dimX, dimY, 0,0);
    	
    	windowsMap.refresh();
    }


*/