package io.github.davidqf555.gachabot.abilities;

import java.util.List;

public class CorrosionAbility extends AbilityAbstract {

    @Override
    public void attackEffect(int damage, int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
        defend[2] *= 0.9;
    }

    @Override
    public String getDescription() {
        return "Weakens the opponent's defense by 10% each hit";
    }

}
