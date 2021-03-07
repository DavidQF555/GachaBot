package com.davidqf.gachabot.abilities;

import java.util.List;

public abstract class AbilityAbstract {

    public void defenseEffect(int damage, int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
    }

    public void attackEffect(int damage, int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
    }

    public int calculateDamage(int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam, AbilityAbstract defAb) {
        int damage = (int) (attack[1] * getAttackFactor() * defAb.getOpponentAttackFactor() / 10.0 + getAttackFactor() * defAb.getOpponentAttackFactor() * attack[1] / defend[2] * getOpponentDefenseFactor() * defAb.getDefenseFactor() + 0.5);
        if (damage < 1) {
            damage = 1;
        }
        return damage;
    }

    public double getOpponentDefenseFactor() {
        return 1;
    }

    public double getOpponentAttackFactor() {
        return 1;
    }

    public double getDefenseFactor() {
        return 1;
    }

    public double getAttackFactor() {
        return 1;
    }

    public abstract String getDescription();
}
