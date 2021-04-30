package chrisPlayerA;

import battlecode.common.*;

import static chrisPlayerA.Unit.secretNumba;

public class HQ extends Building {
    static int numMiners = 0;
    HQ(RobotController rc){
        this.rc = rc;
    }

    static boolean startupMessage = false;

    //Initially we spawn 10 miners.
    public void run() throws GameActionException {
        if(!startupMessage){
            int[] message = new int[7];
            message[0] = secretNumba;
            //Zero is our key for HQ location.
            message[1] = 0;
            MapLocation location = rc.getLocation();
            message[2] = location.x; // x coord of HQ
            message[3] = location.y; // y coord of HQ
            if (rc.canSubmitTransaction(message, 5)) {
                rc.submitTransaction(message, 5);
                startupMessage = true;
            }

        }
        if(numMiners < 4) {
            for (Direction dir : directions)
                if (this.tryBuild(RobotType.MINER, dir)) {
                    numMiners++;
                }
        }
    }
}
