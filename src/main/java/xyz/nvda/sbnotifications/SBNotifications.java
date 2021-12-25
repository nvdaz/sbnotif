package xyz.nvda.sbnotifications;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import xyz.nvda.sbnotifications.handlers.ChatReceived;
import xyz.nvda.sbnotifications.handlers.GameOverlay;
import xyz.nvda.sbnotifications.handlers.Tick;


@Mod(modid = SBNotifications.MODID, version = SBNotifications.VERSION)
public class SBNotifications {

    public static final String MODID = "sbnotifications";
    public static final String VERSION = "0.0.1";

    private static final NotificationHandler notificationHandler = NotificationHandler.getInstance();
    private static final ConfigManager configManager = ConfigManager.getInstance();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        notificationHandler.loadNotificationProviders((RefreshResult result) -> {
            if (result == RefreshResult.FAILED) System.err.println("Failed to load notification providers.");
        });

        MinecraftForge.EVENT_BUS.register(new GameOverlay());
        MinecraftForge.EVENT_BUS.register(new ChatReceived());
        MinecraftForge.EVENT_BUS.register(new Tick());
        ClientCommandHandler.instance.registerCommand(new NotificationsCommand(this));
    }

}
