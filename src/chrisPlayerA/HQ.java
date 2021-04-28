package chrisPlayerA;

import battlecode.common.*;

public class HQ extends Building {
    static int numMiners = 0;
    HQ(RobotController rc){
        this.rc = rc;
    }
    public void run(){
        if(numMiners < 10) {
            for (Direction dir : directions)
                if (this.tryBuild(RobotType.MINER, dir)) {
                    numMiners++;
                }
        }
    }
}
