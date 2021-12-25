package xyz.nvda.sbnotifications.handlers;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nvda.sbnotifications.NotificationHandler;

public class GameOverlay {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private NotificationHandler notificationHandler = NotificationHandler.getInstance();

    @SubscribeEvent
    public void gameOverlay(RenderGameOverlayEvent.Text.Pre event) {
        if (event.type != RenderGameOverlayEvent.ElementType.TEXT || mc.gameSettings.showDebugInfo)
            return;
        notificationHandler.render();
    }

}
