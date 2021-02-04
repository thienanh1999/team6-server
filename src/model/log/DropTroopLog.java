package model.log;

public class DropTroopLog extends Log{
    int type = 1;
    TroopLog troopLog;

    public DropTroopLog(long time, TroopLog troopLog) {
        this.time = time;
        this.troopLog = troopLog;
    }

    public String showLog() {
        return this.type + " " + this.time + " " + this.troopLog.showLog();
    }
}
