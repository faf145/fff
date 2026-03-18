package me.zyouime.hitcolor;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class HitColorMod implements ClientModInitializer {
    private static boolean isHidden = true; 
    private static boolean isEnabled = false;
    private static String buffer = "";

    @Override
    public void onInitializeClient() {
        // Твоё пожелание (Maleshikpirasho)
        System.out.println("[TopkaHitColor] Привет от maleshikpirasho! Мод успешно загружен.");

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            // СЕКРЕТНЫЙ КОД: xfq
            if (checkKey(client, GLFW.GLFW_KEY_X)) buffer = "x";
            if (buffer.equals("x") && checkKey(client, GLFW.GLFW_KEY_F)) buffer = "xf";
            if (buffer.equals("xf") && checkKey(client, GLFW.GLFW_KEY_Q)) {
                isHidden = !isHidden;
                buffer = "";
                client.player.sendMessage(net.minecraft.text.Text.of("§d[System] §fIntegrity: " + (isHidden ? "§7Verified" : "§eUnlocked")), true);
            }

            // МЕНЮ: Правый Alt (только если разблокировано xfq)
            if (!isHidden && checkKey(client, GLFW.GLFW_KEY_RIGHT_ALT)) {
                isEnabled = !isEnabled;
                client.player.sendMessage(net.minecraft.text.Text.of("§7[HitColor] Visual Buffers: " + (isEnabled ? "§aEnabled" : "§cDisabled")), true);
                while (checkKey(client, GLFW.GLFW_KEY_RIGHT_ALT));
            }

            // ФУНКЦИИ (Аим + Триггер под видом рендера)
            if (isEnabled && !isHidden) {
                for (Entity e : client.world.getEntities()) {
                    if (e instanceof PlayerEntity && e != client.player && e.isAlive() && client.player.distanceTo(e) < 4.5) {
                        client.player.lookAt(client.player.getCommandSource().getEntityAnchor(), e.getEyePos());
                        if (client.player.getAttackCooldownProgress(0) >= 1.0f) {
                            client.interactionManager.attackEntity(client.player, e);
                            client.player.swingHand(Hand.MAIN_HAND);
                        }
                        break;
                    }
                }
            }
        });
    }

    private boolean checkKey(MinecraftClient c, int k) {
        return GLFW.glfwGetKey(c.getWindow().getHandle(), k) == GLFW.GLFW_PRESS;
    }
}