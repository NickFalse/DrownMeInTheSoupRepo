package chrisPlayerA;

import battlecode.common.*;

public class HQ extends Building {
    HQ(RobotController rc){
        this.rc = rc;
    }
    public void run(){
        for (Direction dir : directions)
            this.tryBuild(RobotType.MINER, dir);
    }
}
