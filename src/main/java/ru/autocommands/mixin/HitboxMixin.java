package ru.autocommands.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class HitboxMixin {
    private static float currentBonus = 0.0f;

    @Inject(method = "getTargetingMargin", at = @At("HEAD"), cancellable = true)
    private void onGetMargin(CallbackInfoReturnable<Float> cir) {
        if (MinecraftClient.getInstance().getWindow() == null) return;
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_KP_9) == GLFW.GLFW_PRESS) currentBonus += 0.01f;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_KP_6) == GLFW.GLFW_PRESS) currentBonus -= 0.01f;
        if (GLFW.glfwGetKey(window, GLFW.GLFW_KEY_KP_3) == GLFW.GLFW_PRESS) currentBonus = 0.0f;

        if (currentBonus < 0) currentBonus = 0;
        cir.setReturnValue(currentBonus);
    }
}
