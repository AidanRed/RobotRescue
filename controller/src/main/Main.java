/*
* Author: Abdul Mohsi Jawaid
* THe main program that connects everything together
*/

import java.lang.reflect.Constructor;
import java.util.Timer;
import java.util.TimerTask;

public class Main
{
    public static void main(String[] args)
      throws Exception
    {
        //Construct the controller
        Controller controller = new Controller();

        //Construct the display
        Gui gui = new Gui();

        //Connect the components together
        gui.init(controller);
        controller.init(gui);

    }
}
