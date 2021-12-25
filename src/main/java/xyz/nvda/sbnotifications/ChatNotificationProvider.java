package xyz.nvda.sbnotifications;

import net.minecraft.util.IChatComponent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatNotificationProvider {

    private Pattern pattern;
    private String ln1;
    private String ln2;

    public ChatNotificationProvider(String regex, String ln1, String ln2) {
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        this.ln1 = ln1;
        this.ln2 = ln2;
    }

    public boolean parse(IChatComponent message) {
        return this.parse(message.getUnformattedText());
    }

    public boolean parse(String message) {
        Matcher matcher = pattern.matcher(message);

        if (!matcher.matches()) return false;

        NotificationHandler.getInstance().add(new Notification(
                matcher.replaceAll(ln1),
                matcher.replaceAll(ln2)
        ));

        return true;
    }
}
