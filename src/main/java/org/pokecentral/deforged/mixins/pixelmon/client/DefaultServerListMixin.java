/*
 * Deforged - A free-and-open-source project to enhance Pixelmon Reforged
 * Copyright (C) 2023 - Daniël Voort
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.pokecentral.deforged.mixins.pixelmon.client;

import com.pixelmonmod.pixelmon.client.DefaultServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DefaultServerList.class)
public class DefaultServerListMixin {
	/**
	 * @reason Players should not be forced to download the full Pixelmon server list. Especially not without consent OR
	 * 	easy way to delete the server list that was downloaded. If Pixelmon wants their own server list, they should add
	 * 	it to their official modpack, not to the base mod itself.
	 */
	@Inject(method = "tryFetchDefaultServers(Ljava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true, remap = false)
	private static void disableFetchingServers(Runnable runnable, CallbackInfo ci) {

		// Executing the callback will cause a StackOverflowException, because the callback causes a new CustomServerList
		// 	to open, which tries fetching the default server list with a callback that opens a new CustomServerList.
		// You might be able to see why this is bad. Like, *bad* bad.

		ci.cancel();
	}
}
