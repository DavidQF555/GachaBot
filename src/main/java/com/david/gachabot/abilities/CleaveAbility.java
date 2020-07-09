package com.david.gachabot.abilities;

import java.util.List;

@Ability
public class CleaveAbility extends AbilityAbstract {

	private static final long serialVersionUID = 7;

	@Override
	public int onAttack(int[] attack, List<int[]> attackTeam, int[] defend, List<int[]> defendTeam) {
		int damage = super.onAttack(attack, attackTeam, defend, defendTeam);
		for(int[] stats : defendTeam) {
			if(!stats.equals(defend)) {
				stats[0] -= 0.05 * damage;
			}
		}
		return damage;
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
