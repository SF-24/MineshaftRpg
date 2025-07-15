/*
 * Copyright (c) 2025. Sebastian Frynas
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.mineshaft.mineshaftRpg.manager.player_character_options.abilities.spells;

import lombok.Getter;

@Getter
public enum SpellCastResult {


    FAILS(1,2),
    FIZZLES(2,3),

    SEMI_SUCCESS(3,4),
    SUCCESS(3,5);

    final int minimumExperience;
    final int maximumExperience;

    SpellCastResult(int minimumExperience, int maximumExperience) {
        this.minimumExperience = minimumExperience;
        this.maximumExperience = maximumExperience;
    }

}
