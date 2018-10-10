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

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.GradientPaint;
import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Shape;
import java.awt.RenderingHints;
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

    JPanel panel;
    JToolBar toolBar;

    JButton bConnect;
    JButton bDisconnect;
    JButton bAuto;
    JButton bManual;

    JButton bLeft;
    JButton bRight;
    JButton bUp;
    JButton bDown;

    Map mapArea;
    JTextArea logArea;
    JScrollPane scrollPane;

    //initializes GUI and its elements
    public Gui(){
        this.setSize(1000,500);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Robot Controller");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new BorderLayout());

        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new KEDispatcher());

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

        bLeft = new JButton("Left");
        bLeft.getModel().addChangeListener(new BtnModelListener());
        toolBar.add(bLeft);
        bRight = new JButton("Right");
        bRight.getModel().addChangeListener(new BtnModelListener());
        toolBar.add(bRight);
        bUp = new JButton("Up");
        bUp.getModel().addChangeListener(new BtnModelListener());
        toolBar.add(bUp);
        bDown = new JButton("Down");
        bDown.getModel().addChangeListener(new BtnModelListener());
        toolBar.add(bDown);

        mapArea = new Map();
        panel.add(mapArea, BorderLayout.CENTER);

        logArea = new JTextArea();
        scrollPane = new JScrollPane(logArea);
        scrollPane.setPreferredSize(new Dimension(scrollPane.getPreferredSize().width, 50));
        panel.add(scrollPane, BorderLayout.SOUTH);
        
        panel.add(toolBar, BorderLayout.NORTH);

        this.add(panel);
        this.setVisible(true);
    }
    //connect gui to controller
    public void init(Controller c){
        controller = c;
        setMapAngle(0);
    }
    // adds given text to end of current text in log area
    public void log(String text){
        logArea.append(text + "\n");
    }
    //replaces current text with given text
    public void setText(String text){
        logArea.setText(text + "\n");
    }
    public void setMapAngle(int a){
        mapArea.setAngle(a+90);
    }
    // canvas for the map area
    private class Map extends JComponent{
        private int angle = 0;
        public void setAngle(int a){
            angle = a;
            repaint();
        }
        //renders visual map components
        public void paint(Graphics g){
            
            Graphics2D graph2 = (Graphics2D)g;

            graph2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            Shape drawArc = new Arc2D.Double(this.getSize().width/2, this.getSize().height/2, 100, 100, angle-20, 40, Arc2D.PIE);
            graph2.draw(drawArc);
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

                        }if(model == bDisconnect.getModel()){
                            controller.disconnect();
                            setVisible(false);
                            dispose();
                            System.exit(0);

                        }if(model == bUp.getModel()){
                            controller.action("move_forward");

                        }if(model == bDown.getModel()){
                            controller.action("move_backward");

                        }if(model == bLeft.getModel()){
                            controller.action("turn_left");

                        }if(model == bRight.getModel()){
                            controller.action("turn_right");

                        }if(model == bAuto.getModel()){
                            // log("Auto Mode");

                        }if(model == bManual.getModel()){
                            // log("Manual Mode");
                        }
                    } catch (Exception ex){
                        System.out.println(ex);
                    }
                } else {
                    if(model == bUp.getModel() || model == bDown.getModel() || 
                                    model == bLeft.getModel() || model == bRight.getModel()){
                        controller.action("stop");
                    }
                }
                prevPressed = model.isPressed();
            }
        }
    }
    // Listens for key presses no matter what component is in focus
    private class KEDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                try{
                    switch(e.getKeyCode()){
                        case KeyEvent.VK_C:
                            controller.connect();
                            break;
                        case KeyEvent.VK_D:
                            controller.disconnect();
                            setVisible(false);
                            dispose();
                            System.exit(0);
                            break;
                        case KeyEvent.VK_UP:
                            controller.action("move_forward");
                            break;
                        case KeyEvent.VK_DOWN:
                            controller.action("move_backward");
                            break;
                        case KeyEvent.VK_LEFT:
                            controller.action("turn_left");
                            break;
                        case KeyEvent.VK_RIGHT:
                            controller.action("turn_right");
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
                    controller.action("stop");
                } 
            } else if (e.getID() == KeyEvent.KEY_TYPED) {
                
            }
            return false;
        }
    }
}
