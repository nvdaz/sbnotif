package xyz.nvda.sbnotifications.handlers;

import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xyz.nvda.sbnotifications.NotificationHandler;
import xyz.nvda.sbnotifications.RefreshResult;

public class Tick {

    private int ticks = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ticks++;
        }
    }

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        if (ticks < 6000) return;
        NotificationHandler.getInstance().loadNotificationProviders((RefreshResult r) -> {});
        ticks = 0;
    }
}
