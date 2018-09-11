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
        gui.connect(controller);
        controller.init(gui);

        //Initalise the components
        gui.init();

    }
}
