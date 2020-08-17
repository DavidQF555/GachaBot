package com.david.gachabot.abilities;

import java.util.Arrays;
import java.util.List;

@Ability
public class CleaveAbility extends AbilityAbstract {

    private static final long serialVersionUID = 7;

    @Override
    public void attackEffect(int damage, int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
        for (int[] stats : defendTeam) {
            if (!Arrays.equals(stats, defend)) {
                stats[0] -= 0.05 * damage;
            }
        }
    }

    @Override
    public String getName() {
        return "Cleave";
    }

    @Override
    public String getDescription() {
        return "Damages all other characters on the opposing team by 5% of damage dealt";
    }

}
