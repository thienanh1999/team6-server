package model;

import bitzero.util.common.business.Debug;
import model.troop.Troop;
import util.Calculate;
import util.Constant;

import java.awt.*;
import java.util.ArrayList;

public class Defense extends StructureBattle {
    private int timeToFire;
    private int damagePerShot;
    private int minRange;
    private int maxRange;
    private double attackSpeed;
    private int attackType;
    private int attackArea;
    private double attackRadius;
    private Bullet bullet;
    private Troop troop;

    public Defense(int id, String type, Point pos, int level) {
        super(id, type, pos, level);
        this.timeToFire = 0;
        this.bullet = null;
        this.troop = null;
    }

    public void loadConfig() {
        this.damagePerShot = this.getInt("damagePerShot");
        this.hitPoint = this.getInt("hitpoints");
        this.hitPointMax = this.getInt("hitpoints");
        this.minRange = this.getInt("minRange", true);
        this.maxRange = this.getInt("maxRange", true);
        this.attackSpeed = this.getDouble("attackSpeed", true);
        this.attackType = this.getInt("attackType", true);
        this.attackArea = this.getInt("attackArea", true);
        // this.damagePerSecond=this.damagePerShot*this.attackType;
        this.attackRadius = this.getDouble("attackRadius", true);
    }

    public void update() {
        if (this.destroyState) return;
        if (this.bullet != null) this.updateBullet();
        this.updateTimeToFire();
        if (this.timeToFire <= 0) {
            this.timeToFire = 0;
            this.chooseTarget();
        }
    }

    private void updateBullet() {
        ArrayList<Troop> troopList = battleController.troopList;
        this.bullet.updateBullet(Constant.GAME_TICK);
        if (this.bullet.getTime() <= 0) {
            if (this.attackRadius != 0) {
                Troop troop = this.troop;

                for (int i = 0; i < troopList.size(); i++) {
                    troop = troopList.get(i);
                    double x = troop.position.x, y = troop.position.y;

                    if (Calculate.calculateRange(x, y, this.bullet.getPosition().x, this.bullet.getPosition().y) <= this.attackRadius) {

                        this.troopDame(troop.getId(), this.bullet.getDame(), troopList);
                    }
                }
            } else {
                this.troopDame(this.bullet.getTarget(), this.bullet.getDame(), troopList);
            }
            this.bullet = null;
        }
    }

    private void troopDame(int troopId, int dame, ArrayList<Troop> troopList) {

        ArrayList<Troop> listTroop = troopList;
        Troop troopTemp = troopList.get(troopId);
        troopTemp.updateHitPoint(dame);
    }

    private void chooseTarget() {
        if (this.bullet != null) return;
        ArrayList<Troop> troopList = battleController.troopList;
        Troop troopTemp = this.troop;
        if (troopTemp != null) {
            double x = troopTemp.position.x, y = troopTemp.position.y;
            if (Calculate.calculateRange(x, y, this.getPos().x + 1.5, this.getPos().y + 1.5) < this.minRange ||
                    Calculate.calculateRange(x, y, this.getPos().x + 1.5, this.getPos().y + 1.5) > this.maxRange ||
                    troopTemp.isDied()) {

            } else {
//                Debug.warn(battleController.getGameTick(), "Defense Attack:",this.getType(),this.getId() ,troopTemp.position.x,troopTemp.position.y);
                this.resetTime();
                if (this.attackRadius != 0)
                    this.bullet = new Bullet(this.damagePerShot, troopTemp.position, (int) (this.attackSpeed / 2 * 1000));
                else {
                    this.bullet = new Bullet(this.damagePerShot, troopTemp.position, (int) (this.attackSpeed / 2 * 1000));
                    this.bullet.setTarget(troopTemp.getId());
                    this.troop = troopTemp;
                }
                return;
            }
        }

        for (int i = 0; i < troopList.size(); i++) {
            troopTemp = troopList.get(i);
            double x = troopTemp.position.x, y = troopTemp.position.y;
            if (troopTemp.getType().equals("ARM_6") && this.attackArea == 1) continue;
            if (Calculate.calculateRange(x, y, this.getPos().x + 1.5, this.getPos().y + 1.5) < this.minRange ||
                    Calculate.calculateRange(x, y, this.getPos().x + 1.5, this.getPos().y + 1.5) > this.maxRange)
                continue;
            if (troopTemp.isDied()) continue;
//            Debug.l("GameTick:" + BattleController.getInstance().tickGame + "/ Defense Attack:" + this.id + " " + troop.id);
//            Debug.warn("GameTick:", battleController.getGameTick(), "Defense Attack:" + this.getId() + " " + troopTemp.getId());

            //TODO attack troop
            this.resetTime();
            // cc.log("TARGETTROOP");
            if (this.attackRadius != 0)
                this.bullet = new Bullet(this.damagePerShot, troopTemp.position, (int) (this.attackSpeed / 2 * 1000));
            else {
                this.bullet = new Bullet(this.damagePerShot, troopTemp.position, (int) (this.attackSpeed / 2 * 1000));
                this.bullet.setTarget(troopTemp.getId());
                this.troop = troopTemp;
            }
            return;
        }
    }

    private void resetTime() {
        this.timeToFire = (int) (this.attackSpeed * 1000);
    }

    private void updateTimeToFire() {
        this.timeToFire -= Constant.GAME_TICK;
    }
}
