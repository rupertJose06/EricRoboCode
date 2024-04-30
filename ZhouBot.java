package ZhouBot;

import robocode.*;
import robocode.util.Utils;
// Import trigonometry functions from java.lang.Math
import java.lang.Math;

/**
 * ZhouBot - a robot by (your name here)
 */
public class ZhouBot extends Robot {
    /**
     * run: ZhouBot's default behavior
     */
    public void run() {
        // Uncomment the line below to set the colors of the robot (body, gun, radar)
        // setColors(Color.red, Color.blue, Color.green);

        // Robot main loop
        while (true) {
            // Move ahead 100 units
            ahead(100);
            // Turn gun right 360 degrees to scan for other robots
            turnGunRight(360);
            // Move back 100 units
            back(100);
            // Turn gun right 360 degrees again to scan for other robots
            turnGunRight(360);
        }
    }

    /**
     * onScannedRobot: What to do when you see another robot
     */
    public void onScannedRobot(ScannedRobotEvent e) {
        // Calculate the absolute bearing of the scanned robot
        double absoluteBearing = getHeading() + e.getBearing();

        // Calculate the bearing from the gun to the target
        double bearingFromGun = Utils.normalRelativeAngleDegrees(absoluteBearing - getGunHeading());

        // Calculate the lead angle using trigonometry
        // Calculate the target's future position
        double targetFutureX = getX() + e.getDistance() * Math.sin(Math.toRadians(absoluteBearing));
        double targetFutureY = getY() + e.getDistance() * Math.cos(Math.toRadians(absoluteBearing));
        double targetFutureVelocity = e.getVelocity();
        double targetHeading = e.getHeading();
        
        // Predict future position of the target
        double predictedX = targetFutureX + targetFutureVelocity * Math.sin(Math.toRadians(targetHeading));
        double predictedY = targetFutureY + targetFutureVelocity * Math.cos(Math.toRadians(targetHeading));

        // Calculate the angle to the predicted position from the robot
        double angleToPredictedPosition = Math.toDegrees(Math.atan2(predictedX - getX(), predictedY - getY()));
        double gunTurnAngle = Utils.normalRelativeAngleDegrees(angleToPredictedPosition - getGunHeading());

        // Turn the gun to the predicted position
        turnGunRight(gunTurnAngle);

        // Fire at the target with a power of 1.5
        fire(1.5);
		
		if(e.getVelocity() == 0) {
			fire(3);
		}
    }

	public void onHitRobot(HitRobotEvent e) {
	    // Check if the collision was the robot's fault
	    if (e.isMyFault()) {
	        // Perform evasive maneuvers to escape the collision
	        back(100);
	        turnRight(90);
	        ahead(100);
	    } else {
	        // If the opponent collided with you, fire at maximum firepower (3.0)
	        // and continue firing rapidly if you have enough energy
	        while (getEnergy() >= 3) {
	            fire(3.0);
	        }
	
	        // Optional: After firing at max power, perform evasive maneuvers
	        // to avoid the opponent's retaliation
	        if (Math.random() > 0.5) {
	            turnRight(90);
	        } else {
	            turnLeft(90);
	        }
	        ahead(50);
	    }
	}


    /**
     * onHitByBullet: What to do when you're hit by a bullet
     */
	public void onHitByBullet(HitByBulletEvent e) {
	    // Perform random movements to dodge future attacks
	    int moveDirection = (Math.random() < 0.5) ? 1 : -1; // Randomly choose to move forward or backward
	    int distance = (int)(Math.random() * 50) + 50; // Random distance between 50 and 100 units
	    
	    // Move randomly in one direction
	    ahead(distance * moveDirection);
	    
	    // Turn randomly between -45 and 45 degrees to change direction
	    int turnAngle = (int)(Math.random() * 91) - 45;
	    turnRight(turnAngle);
	    
	    // Move again in the same direction to complete the evasion maneuver
	    ahead(distance * moveDirection);
	}

    /**
     * onHitWall: What to do when you hit a wall
     */
    public void onHitWall(HitWallEvent e) {
        // Reverse the robot by 20 units to avoid being stuck on the wall
        back(20);
        // Turn 90 degrees to avoid hitting the wall again
        turnLeft(90);
    }
}