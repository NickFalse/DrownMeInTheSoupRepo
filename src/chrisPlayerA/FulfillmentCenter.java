package chrisPlayerA;

import battlecode.common.*;

public class FulfillmentCenter extends Building {
    FulfillmentCenter(RobotController rc){
        this.rc = rc;
    }
    public void run(){
        for (Direction dir : directions)
            this.tryBuild(RobotType.DELIVERY_DRONE, dir);
    }
}
