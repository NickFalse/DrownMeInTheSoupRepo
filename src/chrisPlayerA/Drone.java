package chrisPlayerA;

import battlecode.common.*;

public class Drone extends Unit {
    Drone(RobotController rc){
        this.rc = rc;
    }
    public void run(){
        try {
            Team enemy = rc.getTeam().opponent();
            if (!rc.isCurrentlyHoldingUnit()) {
                // See if there are any enemy robots within capturing range
                RobotInfo[] robots = rc.senseNearbyRobots(GameConstants.DELIVERY_DRONE_PICKUP_RADIUS_SQUARED, enemy);

                if (robots.length > 0) {
                    // Pick up a first robot within range
                    rc.pickUpUnit(robots[0].getID());
                    System.out.println("I picked up " + robots[0].getID() + "!");
                }
            } else {
                // No close robots, so search for robots within sight radius
                tryMove(randomDirection());
            }
        }catch(Throwable e){
            e.printStackTrace();
        }
    }
}