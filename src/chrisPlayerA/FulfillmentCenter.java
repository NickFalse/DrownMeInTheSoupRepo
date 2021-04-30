package chrisPlayerA;

import battlecode.common.*;

public class FulfillmentCenter extends Building {
    static int droneCount = 0;

    FulfillmentCenter(RobotController rc) {
        this.rc = rc;
    }

    public void run() {
        if (droneCount < 1) {
            for (Direction dir : directions) {
                if (rc.getTeamSoup() > 150) {
                    tryBuild(RobotType.DELIVERY_DRONE, dir);
                    System.out.println("Successfully built a drone!");
                    droneCount++;
                }
            }
        }
    }
}
