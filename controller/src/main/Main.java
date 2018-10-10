/*
* The main program that connects everything together
*/

import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;
import java.lang.Runtime;
import java.lang.Thread;
import java.lang.Runnable;

public class Main
{
    public static void main(String[] args) throws Exception {
        // Close remaining open ports after program exit
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                RobotUtility.closeAllPorts();
            }
        }));
        
        //Construct the controller
        Controller controller = new Controller();

        //Construct the display
        Gui gui = new Gui();

        //Connect the components together
        gui.init(controller);
        controller.init(gui);

    }
}
