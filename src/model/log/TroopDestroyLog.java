package model.log;

public class TroopDestroyLog extends Log{
    int type = 5;
    TroopLog troopLog;

    public TroopDestroyLog(long time, TroopLog troopLog) {
        this.time = time;
        this.troopLog = troopLog;
    }

    public String showLog() {
        return this.type + " " + this.time + " " + this.troopLog.showLog();
    }
}
