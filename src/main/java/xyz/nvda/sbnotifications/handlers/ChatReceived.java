package xyz.nvda.sbnotifications.handlers;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import xyz.nvda.sbnotifications.NotificationHandler;

public class ChatReceived {

    @SubscribeEvent
    public void chatReceived(ClientChatReceivedEvent event) {
        event.setCanceled(NotificationHandler.getInstance().testChat(event.message));
    }
}
