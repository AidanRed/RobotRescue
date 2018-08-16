/*

Testing Touch/Bump Sensor
Displays 1 or 0 depending on sensor reading until quit

*/
import lejos.hardware.Brick;
import lejos.hardware.BrickFinder;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.port.Port;
import lejos.robotics.SampleProvider;

public class TouchSensorTest{
		public static void main(String[] args){
			  Brick brick = BrickFinder.getDefault();
				Port s1 = brick.getPort("S1");
				EV3TouchSensor sensor = new EV3TouchSensor(s1);
				SampleProvider provider;
				float[] sample;

				provider = sensor.getTouchMode();
				sample = new float[provider.sampleSize()];

				while(true){
						provider.fetchSample(sample, 0);
						System.out.println(sample[0]);
						if(Button.ESCAPE.isDown()){
							sensor.close();
							System.exit(0);
						}
				} 
		}
}