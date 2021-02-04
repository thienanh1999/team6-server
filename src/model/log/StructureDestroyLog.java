package model.log;

public class StructureDestroyLog extends Log{
    int type = 6;
    StructureLog structureLog;

    public StructureDestroyLog(long time, StructureLog structureLog) {
        this.time = time;
        this.structureLog = structureLog;
    }

    public String showLog() {
        return this.type + " " + this.time + " " + this.structureLog.showLog();
    }
}
