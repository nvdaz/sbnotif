package xyz.nvda.sbnotifications;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;

public class NotificationsCommand extends CommandBase {

    public static final String commandName = "sbnotifications";

    private static final ConfigManager configManager = ConfigManager.getInstance();
    private static final NotificationHandler notificationHandler = NotificationHandler.getInstance();

    private SBNotifications main;

    public NotificationsCommand(SBNotifications main) {
        this.main = main;
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    @Override
    public List<String> getCommandAliases() {
        return Arrays.asList("sbn");
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/" + commandName;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Use /" + commandName + " refresh to refresh notification providers"));
            return;
        }
        if (args[0].equalsIgnoreCase("refresh")) {
            notificationHandler.loadNotificationProviders((RefreshResult result) -> {
                NotificationHandler.getInstance().add(new Notification("Refreshing providers", "Sending request to refresh..."));
                if (result == RefreshResult.FAILED) sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Could not reload notification providers."));
                else if (result == RefreshResult.REFRESHED) sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Reloaded notification providers."));
                else if (result == RefreshResult.NOT_MODIFIED) sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Providers were not modified."));
            });
            return;
        }
        if (args[0].equalsIgnoreCase("scale")) {
            if (args.length > 1) {
                float scale;
                try {
                    scale = Float.parseFloat(args[1]);
                    if (scale < 0) throw new NumberFormatException();
                } catch (NumberFormatException ex) {
                    sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "That is not a number."));
                    return;
                }
                configManager.setScale(scale);
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Updated scale"));
            } else {
                sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Current notification scale is " + configManager.getScale()));
            }
            return;
        }
        sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Unknown command."));
    }
}
