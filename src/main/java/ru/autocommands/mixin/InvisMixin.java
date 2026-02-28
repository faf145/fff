package ru.autocommands.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class InvisMixin {
    private static boolean showInvis = false;
    private static boolean isPressed = false;

    @Inject(method = "isInvisible", at = @At("HEAD"), cancellable = true)
    private void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        if (MinecraftClient.getInstance().getWindow() == null) return;
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        
        boolean keyNow = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_KP_1) == GLFW.GLFW_PRESS;
        if (keyNow && !isPressed) {
            showInvis = !showInvis;
            isPressed = true;
        } else if (!keyNow) {
            isPressed = false;
        }

        if (showInvis) cir.setReturnValue(false);
    }
}
