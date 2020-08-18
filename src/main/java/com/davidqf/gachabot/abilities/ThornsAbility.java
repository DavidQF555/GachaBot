package com.davidqf.gachabot.abilities;

import java.util.List;

@Ability
public class ThornsAbility extends AbilityAbstract {

    @Override
    public void defenseEffect(int damage, int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
        attack[0] -= 0.1 * damage;
    }

    @Override
    public String getName() {
        return "Thorns";
    }

    @Override
    public String getDescription() {
        return "Damages the attacker by 10% damage dealt";
    }

}
