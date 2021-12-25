package xyz.nvda.sbnotifications;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.util.IChatComponent;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NotificationHandler {

    private static final ConfigManager configManager = ConfigManager.getInstance();
    private static final NotificationHandler instance = new NotificationHandler();

    private String providersEtag = "";

    private List<Notification> notifications = new ArrayList<>();
    private List<ChatNotificationProvider> notificationProviders = new ArrayList<>();

    private long lastRemoved = 0;

    public static NotificationHandler getInstance() {
        return instance;
    }

    public void removeProviders() {
        notificationProviders.clear();
    }

    public boolean addProvider(ChatNotificationProvider notificationProvider) {
        return notificationProviders.add(notificationProvider);
    }

    public boolean testChat(IChatComponent chatComponent) {
        for (ChatNotificationProvider notificationProvider : this.notificationProviders) {
            if (notificationProvider.parse(chatComponent)) return true;
        }

        return false;
    }

    public boolean add (Notification notification) {
        return notifications.add(notification);
    }

    public boolean remove (Notification notification) {
        this.lastRemoved = System.currentTimeMillis();
        return notifications.remove(notification);
    }

    public int size () {
        int count = 0;
        for (Notification notification : this.notifications)
            if (!notification.isRemoving())
                count++;
        return count;
    }

    public void render() {
        int maxNotificationCount = configManager.getMaxNotifications();
        int y = 0;
        for (Notification notification: new ArrayList<>(this.notifications)) {
            if (notification.created + notification.duration + notification.fadeOutTime < System.currentTimeMillis()) continue;
            int size = this.size();
            if (size > maxNotificationCount + 1) notification.forceRemove(50);
            else if (size > maxNotificationCount) notification.forceRemove(500);
            y += notification.render(0, y);
        }
    }

    public long getLastRemoved() {
        return this.lastRemoved;
    }

    public void loadNotificationProviders(Consumer<RefreshResult> cb) {
        NotificationHandler self = NotificationHandler.getInstance();
        (new Thread() {
            @Override
            public void run() {
                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL("https://sbn.nvda.xyz/providers.json").openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("If-None-Match", self.providersEtag);
                    if (conn.getResponseCode() == 304) {
                        cb.accept(RefreshResult.NOT_MODIFIED);
                        return;
                    } else if (conn.getResponseCode() != 200) {
                        cb.accept(RefreshResult.FAILED);
                        return;
                    }

                    self.providersEtag = conn.getHeaderField("ETag");
                    self.removeProviders();
                    JsonArray providers = new JsonParser().parse(new InputStreamReader(conn.getInputStream())).getAsJsonArray();
                    for (JsonElement element : providers) {
                        if (!(element instanceof JsonObject)) continue;
                        JsonObject provider = element.getAsJsonObject();
                        self.addProvider(new ChatNotificationProvider(
                                provider.get("regex").getAsString(),
                                provider.get("ln1").getAsString(),
                                provider.get("ln2").getAsString()
                        ));
                    }
                    System.out.println(self.notificationProviders.size());
                    cb.accept(RefreshResult.REFRESHED);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    cb.accept(RefreshResult.FAILED);
                }
            }
        }).start();
    }
}
