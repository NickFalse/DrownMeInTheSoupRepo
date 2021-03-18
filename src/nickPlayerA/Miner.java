package nickPlayerA;
import battlecode.common.*;
public class Miner extends Unit{
    Miner(RobotController rc){
        this.rc = rc;
    }
    static RobotType[] spawnedByMiner = {RobotType.REFINERY, RobotType.VAPORATOR, RobotType.DESIGN_SCHOOL,
            RobotType.FULFILLMENT_CENTER, RobotType.NET_GUN};
    static RobotType randomSpawnedByMiner() {
        return spawnedByMiner[(int) (Math.random() * spawnedByMiner.length)];
    }
    boolean tryMine(Direction dir){
        try {
            if (rc.isReady() && rc.canMineSoup(dir)) {
                rc.mineSoup(dir);
                return true;
            } else return false;
        }catch(Throwable e){
            e.printStackTrace();
            return false;
        }
    }
    boolean tryRefine(Direction dir){
        try {
            if (rc.isReady() && rc.canDepositSoup(dir)) {
                rc.depositSoup(dir, rc.getSoupCarrying());
                return true;
            } else return false;
        }catch(Throwable e){
            e.printStackTrace();
            return false;
        }
    }
    public void run(){
        this.tryMove(randomDirection());
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
        // tryBuild(randomSpawnedByMiner(), randomDirection());
        for (Direction dir : directions)
            tryBuild(RobotType.FULFILLMENT_CENTER, dir);
        for (Direction dir : directions)
            if (tryRefine(dir))
                System.out.println("I refined soup! " + rc.getTeamSoup());
        for (Direction dir : directions)
            if (this.tryMine(dir))
                System.out.println("I mined soup! " + rc.getSoupCarrying());
    }
}
