/*  
* GUI for controller running on the computer.
* Handles all user input to the system. Key Presses, Button Presses etc.
*/
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JToolBar;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.ButtonModel;
import javax.swing.SwingUtilities;

import java.lang.Runnable;

import java.util.List;
import java.util.ArrayList;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Shape;
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.BasicStroke;

import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D;
import java.awt.geom.QuadCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.RoundRectangle2D;

/* 
Creates and contains all the viewable and interactable elements of the controller.
*/
public class Gui extends JFrame{
    Controller controller;
    Navigator nav;
    boolean connected = false;

    JPanel panel;
    JToolBar toolBar;
    
    JButton bConnect;
    JButton bDisconnect;
    JButton bAuto;
    JButton bManual;
    
    JPanel navPanel;
    JButton bLeft;
    JButton bRight;
    JButton bUp;
    JButton bDown;
    JButton bClear;

    Map mapArea;
    JTextArea logArea;
    JScrollPane scrollPane;

    public int mapWidth;
    public int mapHeight;

    //initializes GUI and its elements
    public Gui(){
        this.setSize(1280,800);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Robot Controller");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KEDispatcher());

        panel = new JPanel(new BorderLayout());

            toolBar = new JToolBar("Controls");
            toolBar.setFloatable(false);

                bConnect = new JButton("Connect");
                bConnect.getModel().addChangeListener(new BtnModelListener());
                toolBar.add(bConnect);
                bDisconnect = new JButton("Disconnect");
                bDisconnect.getModel().addChangeListener(new BtnModelListener());
                toolBar.add(bDisconnect);

                bAuto = new JButton("Auto");
                bAuto.getModel().addChangeListener(new BtnModelListener());
                toolBar.add(bAuto);
                bManual = new JButton("Manual");
                bManual.getModel().addChangeListener(new BtnModelListener());
                toolBar.add(bManual);

                navPanel = new JPanel(new GridBagLayout());
                navPanel.setOpaque(false);
                GridBagConstraints gridCon = new GridBagConstraints();
                
                    bLeft = new JButton("Left");
                    bLeft.setContentAreaFilled(false);
                    bLeft.getModel().addChangeListener(new BtnModelListener());
                    gridCon.gridx = 0;
                    gridCon.gridy = 1;
                    navPanel.add(bLeft,gridCon);
                    bRight = new JButton("Right");
                    bRight.setContentAreaFilled(false);
                    bRight.getModel().addChangeListener(new BtnModelListener());
                    gridCon.gridx = 2;
                    gridCon.gridy = 1;
                    navPanel.add(bRight,gridCon);
                    bUp = new JButton("Up");
                    bUp.setContentAreaFilled(false);
                    bUp.getModel().addChangeListener(new BtnModelListener());
                    gridCon.gridx = 1;
                    gridCon.gridy = 0;
                    navPanel.add(bUp,gridCon);
                    bDown = new JButton("Down");
                    bDown.setContentAreaFilled(false);
                    bDown.getModel().addChangeListener(new BtnModelListener());
                    gridCon.gridx = 1;
                    gridCon.gridy = 2;
                    navPanel.add(bDown,gridCon);
            
                toolBar.add(navPanel);
                
                bClear = new JButton("Clear");
                bClear.getModel().addChangeListener(new BtnModelListener());
                toolBar.add(bClear);

                mapArea = new Map();
                panel.add(mapArea, BorderLayout.CENTER);
                mapArea.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e){
                        int theX = e.getX();
                        int theY = e.getY();
                        System.out.println("Adding destination at: (" + Integer.toString(theX) + ", " + Integer.toString(theY) +")");
                        //nav.addDestination(theX, theY);
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e){}
                    public void mouseEntered(MouseEvent e){}
                    public void mouseReleased(MouseEvent e){}
                    public void mousePressed(MouseEvent e){}
                });

                logArea = new JTextArea();
                scrollPane = new JScrollPane(logArea);
                scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 50));
                panel.add(scrollPane, BorderLayout.SOUTH);
                
            panel.add(toolBar, BorderLayout.NORTH);

        this.add(panel);
        // Perform shutdown tasks when window is closed
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.disconnect();
            }
        });


        this.setVisible(true);
    }

    // connect gui and navigator to controller
    public void init(Controller c, Navigator n){
        controller = c;
        nav = n;
        setMapAngle(0);
    }

    // adds given text to end of current text in log area
    public void log(String text){
        logArea.append(text + "\n");
    }
    // replaces current text with given text
    public void setText(String text){
        logArea.setText(text + "\n");
    }

    public void setMapAngle(int a){
        mapArea.setAngle(a);
    }

    // Getter for width of map component
    public int getMapWidth(){
        return mapArea.getSize().width;
    }
    // Getter for height of map component
    public int getMapHeight(){
        return mapArea.getSize().height;
    }

    // sets position of robot in map
    public void setRobotPos(double x, double y){
        mapArea.setPos(x, y);
    }
    // increments robot's x and y by given increments 0,0 is center screen
    public void incRobotPos(double incX, double incY){
        mapArea.incPos(incX, incY);
    }
    // add a point to the map at given coords
    public void addPoint(int x, int y){
        mapArea.point(x, y);
    }
    // add a line to the map at given coords
    public void addLine(int x1, int y1, int x2, int y2){
        mapArea.line(x1, y1, x2, y2);
    }
    // clear all points on the map
    public void clearPoints(){
        mapArea.clearP();
    }
    // clear all lines on the map
    public void clearLines(){
        mapArea.clearL();
    }
    // clear all persons on the map
    public void clearPersons(){
        mapArea.clearPersons();
    }
    // add a person to the map at given coords with specified colour (RED, GREEN, or BLUE)
    public void addPerson(int x, int y, String color){
        if(color.equals("RED") || color.equals("GREEN") || color.equals("BLUE")){
            mapArea.person(x, y, color);
        } else {
            System.err.println("Invalid color given for person, should be RED, GREEN, or BLUE");
        }
    }

    public double getRobotX(){
        return mapArea.getRobotX();
    }

    public double getRobotY(){
        return mapArea.getRobotY();
    }

    // canvas for the map area
    private class Map extends JComponent{
        private int angle = 0;

        private double robotX;
        private double robotY;
        
        // Width and height of vision arc if it were a full ellipse
        private final int arcWidth = 50;
        private final int arcHeight = 50;

        private final int halfArcWidth = arcWidth / 2;
        private final int halfArcHeight = arcHeight / 2;

        // Width and height of rectangle representing robot
        private int robWidth = 20;
        private int robHeight = 30;

        private final int halfRobWidth = robWidth / 2;
        private final int halfRobHeight = robHeight / 2;

        private List<Point> points = new ArrayList<Point>();
        private int pointSize = 2;
        private List<Person> persons = new ArrayList<Person>();
        private int personSize = 40;
        private List<Line> lines = new ArrayList<Line>();

        public int mapWidth;
        public int mapHeight;

        public int halfMapWidth;
        public int halfMapHeight;

        private boolean notSet = true;

        public void setAngle(int a){
            angle = a;
            repaint();
        }
        public void setPos(double xp, double yp){
            robotX = xp;
            robotY = yp;

            repaint();
        }
        public double getRobotX(){
            return robotX;
        }
        public double getRobotY(){
            return robotY;
        }
        public void incPos(double incX, double incY){
            robotX += incX;
            robotY += incY;
            
            repaint();
        }
        public void person(int x, int y, String color){
            Person p = new Person(x+halfMapWidth, y+halfMapHeight, color);
            persons.add(p);
            repaint();
        }
        public void point(int x, int y){
            Point p = new Point(x, y);
            nav.addPoint(p);
            repaint();
        }
        public void line(int x1, int y1, int x2, int y2){
            Line l = new Line(x1+halfMapWidth, y1+halfMapHeight, x2+halfMapWidth, y2+halfMapHeight);
            lines.add(l);
            repaint();
        }
        public void clearP(){
            nav.clearPoints();
            repaint();
        }
        public void clearL(){
            lines.clear();
            repaint();
        }
        public void clearPersons(){
            persons.clear();
            repaint();
        }

        // renders visual map components
        public void paint(Graphics g){
            if(notSet){
                mapWidth = mapArea.getSize().width;
                mapHeight = mapArea.getSize().height;

                halfMapWidth = mapWidth / 2;
                halfMapHeight = mapHeight / 2;

                notSet = false;
            }
            
            Graphics2D graph2 = (Graphics2D)g;

            graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            for (Line line : lines) {
                graph2.setStroke(new BasicStroke(3));
                graph2.drawLine(line.x1, line.y1, line.x2, line.y2);
                graph2.setStroke(new BasicStroke(1));
            }
            for (Point point : nav.getPoints()) {
                graph2.drawRect(point.x, point.y, pointSize, pointSize);
            }
            for (Person person : persons) {
                int personW = personSize/2;
                int headSize = personSize/5;
                graph2.setColor(person.color);
                graph2.fillOval(person.x+personW/2-headSize/2-personW/2, -personSize/2+person.y, headSize, headSize);
                graph2.fillRect(person.x+personW/4-personW/2, -personSize/2+person.y+personSize/5, personW/2, personSize/5*2);
                graph2.fillRect(person.x-personW/2, -personSize/2+person.y+personSize/5, personW/4, personSize/4);
                graph2.fillRect(person.x+personW/4*3-personW/2, -personSize/2+person.y+personSize/5, personW/4, personSize/4);
                graph2.fillRect(person.x+personW/4-personW/2, -personSize/2+person.y+personSize/5*3, personW/4, personSize/4);
                graph2.fillRect(person.x+personW/2-personW/2, -personSize/2+person.y+personSize/5*3, personW/4, personSize/4);
                graph2.setColor(new Color(0,0,0));
                graph2.drawOval(person.x+personW/2-headSize/2-personW/2, -personSize/2+person.y, headSize, headSize);
                graph2.drawRect(person.x+personW/4-personW/2, -personSize/2+person.y+personSize/5, personW/2, personSize/5*2);
                graph2.drawRect(person.x-personW/2, -personSize/2+person.y+personSize/5, personW/4, personSize/4);
                graph2.drawRect(person.x+personW/4*3-personW/2, -personSize/2+person.y+personSize/5, personW/4, personSize/4);
                graph2.drawRect(person.x+personW/4-personW/2, -personSize/2+person.y+personSize/5*3, personW/4, personSize/4);
                graph2.drawRect(person.x+personW/2-personW/2, -personSize/2+person.y+personSize/5*3, personW/4, personSize/4);
            }

            double drawX = -halfArcWidth;
            double drawY = -halfArcHeight;

            
            graph2.translate(halfMapWidth + robotX, halfMapHeight + robotY);
            graph2.rotate(Math.toRadians(-angle));
            
            graph2.setColor(new Color(140,140,140,50));
            graph2.fillRect(- halfRobWidth, - halfRobHeight, robWidth, robHeight);
            graph2.setColor(new Color(0,0,0));
            graph2.drawRect(- halfRobWidth, - halfRobHeight, robWidth, robHeight);
            Shape drawArc = new Arc2D.Double(drawX,drawY, arcWidth, arcHeight, 90-22.5, 45, Arc2D.PIE);
            graph2.setColor(new Color(140,140,140,150));
            graph2.draw(drawArc);
            graph2.rotate(Math.toRadians((angle)));
            graph2.translate(-(halfMapWidth + robotX), -(halfMapHeight + robotY));

            if(nav.currentPath != null){
                for(AStar.Point p : nav.currentPath){
                    System.out.println("Point at: " + Integer.toString(p.x) + " " + Integer.toString(p.y));
                    int[] coords = nav.gridToPoint(p.x, p.y);
                    graph2.drawRect(coords[0] + halfMapWidth, coords[1] + halfMapWidth, (int)nav.cellSize, (int)nav.cellSize);
                }
            }
        }

        private class Line{
            final int x1; 
            final int y1;
            final int x2;
            final int y2;
        
            public Line(int x1, int y1, int x2, int y2) {
                this.x1 = x1;
                this.y1 = y1;
                this.x2 = x2;
                this.y2 = y2;
            }               
        }
        private class Person{
            final int x; 
            final int y;
            final Color color; 
        
            public Person(int x, int y, String color) {
                this.x = x;
                this.y = y;
                switch(color){
                    case "RED":
                        this.color = new Color(240,20,20);
                        break;
                    case "GREEN":
                        this.color = new Color(20,240,20);
                        break;
                    case "BLUE":
                        this.color = new Color(20,20,240);
                        break;
                    default:
                        System.err.println("Invalid color given to Color constructor");
                        this.color = null;
                }
            }
        }
    }

    // determines what to do when buttons are pressed or released
    private class BtnModelListener implements ChangeListener {
        private boolean prevPressed = false;

        public void stateChanged(ChangeEvent e) {
            ButtonModel model = (ButtonModel) e.getSource();

            //if the button has changed states
            if (model.isPressed() != prevPressed) {
                // if the button has changed to pressed, else not pressed
                if(model.isPressed()){
                    try{
                        if(model == bConnect.getModel()){
                            controller.connect();
                            connected = controller.isConnected();

                        }if(model == bDisconnect.getModel()){
                            controller.disconnect();

                        }if(connected && model == bUp.getModel()){
                            controller.action("move_forward");

                        }if(connected && model == bDown.getModel()){
                            controller.action("move_backward");

                        }if(connected && model == bLeft.getModel()){
                            controller.action("turn_left");

                        }if(connected && model == bRight.getModel()){
                            controller.action("turn_right");

                        }if(model == bAuto.getModel()){
                            // log("Auto Mode");

                        }if(model == bManual.getModel()){
                            // log("Manual Mode");
                        }if(model == bClear.getModel()){
                            clearLines();
                            clearPoints();
                            clearPersons();
                        }
                    } catch (Exception ex){
                        System.out.println(ex);
                    }
                } else {
                    if(connected && (model == bUp.getModel() || model == bDown.getModel() || 
                                    model == bLeft.getModel() || model == bRight.getModel())){
                        controller.action("stop");
                    }
                }
                prevPressed = model.isPressed();
            }
        }
    }
    // Listens for key presses no matter what component is in focus
    private class KEDispatcher implements KeyEventDispatcher {
        private String prevAction = "";
        private String CONNECT = "connect";
        private String DISCONNECT = "disconnect";
        private String FORWARD = "move_forward";
        private String BACKWARD = "move_backward";
        private String LEFT = "turn_left";
        private String RIGHT = "turn_right";

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                
                try{
                    switch(e.getKeyCode()){
                        case KeyEvent.VK_C:
                            if(!prevAction.equals(CONNECT)){
                                prevAction = CONNECT;
                                controller.connect();
                                connected = controller.isConnected();
                            }
                            break;
                        case KeyEvent.VK_D:
                            if(!prevAction.equals(DISCONNECT)){
                                prevAction = DISCONNECT;
                                controller.disconnect();
                            }
                            break;
                        case KeyEvent.VK_Z:

                            addPerson(getMapWidth()/8,getMapHeight()/8, "RED");
                            addPerson(getMapWidth()/8*3,getMapHeight()/8*3, "GREEN");
                            addPerson(getMapWidth()/8*5,getMapHeight()/8*5, "BLUE");
                            addPerson(getMapWidth()/8*7,getMapHeight()/8*7, "RED");

                            addPoint(0,0);
                            addPoint(10,10);
                            addPoint(20,20);
                            addPoint(30,30);
                            addPoint(40,40);
                            addPoint(50,50);
                            addPoint(60,60);
                            addPoint(70,70);
                            addPoint(70,0);
                            addPoint(60,10);
                            addPoint(50,20);
                            addPoint(40,30);
                            addPoint(30,40);
                            addPoint(20,50);
                            addPoint(10,60);
                            addPoint(0,70);
                            addLine(0,0,70,70);
                            addLine(70,0,0,70);

                            addPoint(0,0);
                            addPoint(getMapWidth()/8,getMapHeight()/8);
                            addPoint(getMapWidth()/8*2,getMapHeight()/8*2);
                            addPoint(getMapWidth()/8*3,getMapHeight()/8*3);
                            addPoint(getMapWidth()/8*4,getMapHeight()/8*4);
                            addPoint(getMapWidth()/8*5,getMapHeight()/8*5);
                            addPoint(getMapWidth()/8*6,getMapHeight()/8*6);
                            addPoint(getMapWidth()/8*7,getMapHeight()/8*7);
                            addPoint(getMapWidth(),getMapHeight());
                            addPoint(getMapWidth(),0);
                            addPoint(getMapWidth()/8*7,getMapHeight()/8);
                            addPoint(getMapWidth()/8*6,getMapHeight()/8*2);
                            addPoint(getMapWidth()/8*5,getMapHeight()/8*3);
                            addPoint(getMapWidth()/8*4,getMapHeight()/8*4);
                            addPoint(getMapWidth()/8*3,getMapHeight()/8*5);
                            addPoint(getMapWidth()/8*2,getMapHeight()/8*6);
                            addPoint(getMapWidth()/8,getMapHeight()/8*7);
                            addPoint(0,getMapHeight());
                            addLine(0,0,getMapWidth(),getMapHeight());
                            addLine(getMapWidth(),0,0,getMapHeight());

                            break;
                        case KeyEvent.VK_X:
                            clearLines();
                            clearPoints();
                            clearPersons();
                            break;
                            
                        case KeyEvent.VK_UP:
                            if(connected && !prevAction.equals(FORWARD)){
                                prevAction = FORWARD;
                                controller.action(FORWARD);
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if(connected && !prevAction.equals(BACKWARD)){
                                prevAction = BACKWARD;
                                controller.action(BACKWARD);
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            if(connected && !prevAction.equals(LEFT)){
                                prevAction = LEFT;
                                controller.action(LEFT);
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if(connected && !prevAction.equals(RIGHT)){
                                prevAction = RIGHT;
                                controller.action(RIGHT);
                            }
                            break;
                        default:
                            System.out.println("Invalid Key Pressed");
                    }
                }
                catch (Exception ex){
                    System.out.println("key error");
                }
            } else if (e.getID() == KeyEvent.KEY_RELEASED) {
                if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN ||
                                e.getKeyCode() == KeyEvent.VK_LEFT || 
                                e.getKeyCode() == KeyEvent.VK_RIGHT ){
                    prevAction = "";
                    if(connected){
                        controller.action("stop");
                    }
                } 
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                
            }
            return false;
        }
    }
}
