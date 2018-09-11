import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextArea;;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

public class Gui extends JFrame {

    Controller controller;

    JPanel thePanel;
    JLabel mapArea;
    JButton bConnect;
    JButton bDeactivate;
    JButton bAuto;
    JButton bManual;
    JButton bLeft;
    JButton bRight;
    JButton bUp;
    JButton bDown;

    boolean connected = false;

    public void connect(Controller controller){
        this.controller = controller;
    }
    
    public void init() {
        this.setSize(1000,500);
        // this.pack();
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setTitle("Robot Controller");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(new KListener());

        JPanel thePanel = new JPanel();
        thePanel.addKeyListener(new KListener());
        thePanel.setLayout(new GridBagLayout());
        GridBagConstraints cons = new GridBagConstraints();
        
        mapArea = new JLabel("map");
        cons.weightx = 0.5;
        cons.weighty = 0.5;
        cons.gridx = 0;
        cons.gridy = 0;
        cons.gridheight = 3;
        cons.ipadx = this.getWidth()/2;
        cons.ipady = this.getHeight()/5*3;
        thePanel.add(mapArea,cons);

        cons.gridheight = 1;

        cons.ipadx = this.getWidth()/4;
        cons.ipady = this.getHeight()/5;

        // connect button
        bConnect = new JButton("Connect");
        cons.gridx = 2;
        cons.gridy = 0;
        thePanel.add(bConnect,cons);


        // deactivate button
        bDeactivate = new JButton("Shutdown");
        cons.gridx = 3;
        cons.gridy = 0;
        thePanel.add(bDeactivate,cons);

        // Auto mode button
        bAuto = new JButton("Auto");
        cons.gridx = 2;
        cons.gridy = 1;
        thePanel.add(bAuto,cons);

        // Manual mode button
        bManual = new JButton("Manual");
        cons.gridx = 3;
        cons.gridy = 1;
        thePanel.add(bManual,cons);

        // left turn button
        bLeft = new JButton("Left");
        cons.gridx = 0;
        cons.gridy = 2;
        cons.ipadx = 0;
        cons.ipady = 0;
        thePanel.add(bLeft,cons);

        // right turn button
        bRight = new JButton("Right");
        cons.gridx = 3;
        cons.gridy = 2;
        cons.ipadx = 0;
        cons.ipady = 0;
        thePanel.add(bRight,cons);

        // forward button
        bUp = new JButton("Up");
        cons.gridx = 0;
        cons.gridy = 1;
        cons.ipadx = 0;
        cons.ipady = 0;
        thePanel.add(bUp,cons);

        // backward button
        bDown = new JButton("Down");
        cons.gridx = 3;
        cons.gridy = 3;
        thePanel.add(bDown,cons);

        JTextArea logArea = new JTextArea("log");
        cons.ipadx = this.getWidth();
        cons.ipady = this.getHeight()/5*2;
        cons.gridx = 0;
        cons.gridy = 3;
        cons.gridheight = 2;
        cons.gridwidth = 4;
        thePanel.add(logArea,cons);
        
        this.add(thePanel);
        
        this.setVisible(true);

        bConnect.addMouseListener(new BtnListener());
        bConnect.addKeyListener(new KListener());
        bDeactivate.addMouseListener(new BtnListener());
        bDeactivate.addKeyListener(new KListener());
        bUp.addMouseListener(new BtnListener());
        bUp.addKeyListener(new KListener());
        bDown.addMouseListener(new BtnListener());
        bDown.addKeyListener(new KListener());
        bLeft.addMouseListener(new BtnListener());
        bLeft.addKeyListener(new KListener());
        bRight.addMouseListener(new BtnListener());
        bRight.addKeyListener(new KListener());
        
    }

    private class KListener implements KeyListener
    {
        public void keyPressed(KeyEvent e)
        {
            System.out.println("key pressed");
            try
            {
                switch(e.getKeyCode())
                {
                    case KeyEvent.VK_C:
                        if(connected == false){
                            connected = true;
                            controller.connect();
                        }
                        break;
                    case KeyEvent.VK_D:
                        if(connected == true){
                            connected = false;
                            controller.disconnect();
                            setVisible(false);
                            dispose();
                        }
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
            catch (Exception ex)
            {
                System.out.println("connection error");
            }

        }

        public void keyReleased(KeyEvent e)
        {
            System.out.println("Key released");
            if(!(e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_C))
            {
                controller.action("stop");
            } 
        }

        public void keyTyped(KeyEvent e)
        {
            System.out.println("Key tyrped");
        }
    }

    private class BtnListener implements MouseListener
    {
        public void mousePressed(MouseEvent e)
        {
            try{
                if(e.getSource() == bConnect)
                {
                    if(connected == false){
                        connected = true;
                        controller.connect();
                    }
                }
                if(e.getSource() == bDeactivate)
                {
                    if(connected == true){
                        connected = false;
                        controller.disconnect();
                        setVisible(false);
                        dispose();
                    }
                }
                if(e.getSource() == bUp)
                {
                    controller.action("move_forward");
                }
                if(e.getSource() == bDown)
                {
                    controller.action("move_backward");
                }
                if(e.getSource() == bLeft)
                {
                    controller.action("turn_left");
                }
                if(e.getSource() == bRight)
                {
                    controller.action("turn_right");
                }
            }
            catch (Exception ex)
            {
                System.out.println("connection error");
            }
            

        }
        public void mouseReleased(MouseEvent e)
        {
            if(!(e.getSource()==bConnect || e.getSource()==bDeactivate))
            {
                controller.action("stop");
            }
            
        }

        public void mouseClicked(MouseEvent e)
        {

        }  
        public void mouseExited(MouseEvent e)
        {

        }
        public void mouseEntered(MouseEvent e)
        {

        }  
    }

}