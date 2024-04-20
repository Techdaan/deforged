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

package org.pokecentral.deforged.mixins.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.client.gui.PixelmonOverlayScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * This mixin moves the Pixelmon overlay behind the Vanilla overlay, to make the chat appear over the Pokémon sidebar if
 * the chat is focused. If not, chat will appear behind the Pokémon sidebar.
 */
@Mixin(PixelmonOverlayScreen.class)
public class PixelmonOverlayScreenMixin {
    /**
     * Whether if the next invocation of the 'onRenderGameOverlay' method should actually render the overlay. This is a
     * cheap workaround to avoid having to copy-paste the entirety of the old event handler.
     */
    @Unique
    private boolean allowRender = false;

    @SubscribeEvent
    public void onPreRender(RenderGuiEvent.Pre event) {
        allowRender = true;

        // Render the UI early if the chat is focused, otherwise
        if (Minecraft.getInstance().screen instanceof ChatScreen)
            ((PixelmonOverlayScreen) (Object) (this)).onRenderGameOverlay(new RenderGuiEvent.Post(event.getWindow(), event.getGuiGraphics(), event.getPartialTick()));
    }

    @Inject(method = "onRenderGameOverlay(Lnet/minecraftforge/client/event/RenderGuiEvent$Post;)V", remap = false, at = @At("HEAD"), cancellable = true)
    public void onPostRenderMixin(RenderGuiEvent.Post event, CallbackInfo ci) {
        if (!allowRender) {
            if (ci != null) ci.cancel();
            return;
        }

        allowRender = false;
    }
}
