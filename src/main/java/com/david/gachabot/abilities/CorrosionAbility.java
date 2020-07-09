package com.david.gachabot.abilities;

import java.util.List;

@Ability
public class CorrosionAbility extends AbilityAbstract {

	private static final long serialVersionUID = 9;

	@Override
	public void attackEffect(int damage, int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
		defend[2] *= 0.9;
	}

	@Override
	public String getName() {
		return "Corrosion";
	}

	@Override
	public String getDescription() {
		return "Weakens the opponent's defense by 10% each hit";
	}

}
