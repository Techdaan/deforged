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

import com.pixelmonmod.pixelmon.entities.npcs.IndexedNPCEntity;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.trading.Merchant;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Fixes Pixelmon NPCs not downloading player skins due to an oversight in updating between Minecraft 1.16.5 and 1.20.2.
 * <p>
 * In 1.16.5 the {@link TextureManager#getTexture(ResourceLocation)} method would return null if the texture did not
 *  exist, but in 1.20.2, the method returns a default/new texture instead.
 * <p>
 * The fix redirects the method to {@link TextureManager#getTexture(ResourceLocation, AbstractTexture)} instead, with
 *  a second argument of null. This method returns a {@link java.util.Map#getOrDefault(Object, Object)} call using the
 *  provided resource location and texture, returning null if the texture does not exist, which is the intended
 *  behaviour of the {@link NPCChatting#getTexture()} method.
 */
@Mixin(NPCChatting.class)
public abstract class NPCChattingMixin extends IndexedNPCEntity implements Merchant {
    NPCChattingMixin() {
        super(null, null);
    }

    @Redirect(method = "getTexture()Ljava/lang/String;", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/TextureManager;getTexture(Lnet/minecraft/resources/ResourceLocation;)Lnet/minecraft/client/renderer/texture/AbstractTexture;"))
    public AbstractTexture playerSkinFix(TextureManager manager, ResourceLocation location) {
        //noinspection DataFlowIssue
        return manager.getTexture(location, null);
    }
}
