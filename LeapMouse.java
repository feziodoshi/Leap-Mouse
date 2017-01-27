import com.leapmotion.leap.*;
import java.awt.Dimension;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.Scanner;

class CustomListener extends Listener {

	public Robot rob;

	public void onInit(Controller controller) {
		System.out.println("Initialized");
	}

	public void onConnect(Controller controller) {
		System.out.println("connected");
		controller.enableGesture(Gesture.Type.TYPE_CIRCLE);
		controller.enableGesture(Gesture.Type.TYPE_KEY_TAP);
		controller.enableGesture(Gesture.Type.TYPE_SCREEN_TAP);
		controller.enableGesture(Gesture.Type.TYPE_SWIPE);
	}

	public void onDisconnect(Controller controller) {
		System.out.println("Disconnected");
	}

	public void onFrame(Controller controller){
		Frame frame=controller.frame();
		try
		{
			rob=new Robot();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		InteractionBox box=frame.interactionBox();
		FingerList f_list=frame.fingers();
		for(Finger f:f_list){
			if(f.type()==Finger.Type.TYPE_INDEX)
			{
				//get the normalized position of the finger tip in the interaction box
				Vector boxfingerpos=box.normalizePoint(f.stabilizedTipPosition());
				//get the screen size upto your screen using dimension
				Dimension screen=java.awt.Toolkit.getDefaultToolkit().getScreenSize();
				//now use the java.robot, this can be used to emulate the mouse methods
				rob.mouseMove((int)(screen.width*boxfingerpos.getX()),(int)(screen.height-screen.height*boxfingerpos.getY()));
				try
				{
					Thread.sleep(10);
				}
				catch(Exception e )
				{
					e.printStackTrace();
				}
			}
			}
				
			

	// now working with gestures
	for(Gesture g:frame.gestures())
	{
		if (g.type() == Gesture.Type.TYPE_CIRCLE) {
			CircleGesture c_gesture = new CircleGesture(g);
			if (c_gesture.pointable().direction().angleTo(c_gesture.normal()) <= Math.PI / 2) {
				rob.mouseWheel(1);
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				rob.mouseWheel(-1);
				try {
					Thread.sleep(50);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else if(g.type()==Gesture.Type.TYPE_SCREEN_TAP)
		{
			ScreenTapGesture st_gesture=new ScreenTapGesture(g);
			rob.mousePress(InputEvent.BUTTON1_MASK);
			rob.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		else if(g.type()==Gesture.Type.TYPE_SWIPE && g.state()==Gesture.State.STATE_START)
		{
			rob.keyPress(KeyEvent.VK_WINDOWS);
			rob.keyRelease(KeyEvent.VK_WINDOWS);
		}
	}
}

}

public class LeapMouse {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Controller c = new Controller();
		CustomListener listener = new CustomListener();
		c.addListener(listener);
		Scanner sc = new Scanner(System.in);
		System.out.println("press q to quit");
		String option = sc.next();
		c.removeListener(listener);

	}

}
