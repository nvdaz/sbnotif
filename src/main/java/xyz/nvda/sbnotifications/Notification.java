package xyz.nvda.sbnotifications;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

public class Notification {

  private static final double FADE_OUT_TIME = 500;
  private static final double TRANSITION_TIME = 250;

  private static final ConfigManager configManager = ConfigManager.getInstance();
  private static final NotificationHandler notificationHandler = NotificationHandler.getInstance();
  private static final Minecraft mc = Minecraft.getMinecraft();


  private String ln1;
  private String ln2;

  public long created = System.currentTimeMillis();
  public long duration;
  public double fadeOutTime = FADE_OUT_TIME;

  private boolean removeCalled;

  public Notification(String ln1, String ln2, long duration) {
    this.ln1 = ln1;
    this.ln2 = ln2;
    this.duration = duration;
  }

  public Notification(String ln1, String ln2) {
    this(ln1, ln2, 5000);
  }

  public void forceRemove(double fadeOutTime) {
    if (this.removeCalled) return;
    this.removeCalled = true;
    this.fadeOutTime = fadeOutTime;
    this.duration = System.currentTimeMillis() - this.created - 1;
  }

  public void forceRemove() {
    this.forceRemove(FADE_OUT_TIME);
  }

  /** LEAVES FROM TOP
   public int render(int x, int y) {
   float scale = configManager.getScale();
   double timeSinceCreated = System.currentTimeMillis() - this.created;
   int alpha = timeSinceCreated > duration ? (int) (255 - (255 * ((timeSinceCreated - duration) / fadeOutTime))) : 255;
   int translationY = timeSinceCreated > duration ? (int) -((25 * ((timeSinceCreated - duration) / fadeOutTime))) : 0;

   if (alpha <= 15) {
   NotificationHandler.getInstance().remove(this);
   }

   int width = Math.max((int) Math.ceil(mc.fontRendererObj.getStringWidth(this.ln1) * 1.25), mc.fontRendererObj.getStringWidth(this.ln2)) + 3;
   GlStateManager.pushMatrix();
   GlStateManager.scale(scale, scale, scale);
   Gui.drawRect(x + 1, y + translationY + 1, width, y + translationY + 25, 0 | (alpha << 24));
   GlStateManager.enableBlend();
   GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
   mc.fontRendererObj.drawString(this.ln2, x + 3, y + translationY + 15, 0x00ddff | (alpha << 24));
   GlStateManager.scale(1.25F, 1.25F, 1.25F);
   mc.fontRendererObj.drawString(this.ln1, (int) Math.ceil((x + 3) / 1.25), (int) Math.ceil((y + translationY + 2) / 1.25), 0x00ddff | (alpha << 24));
   GlStateManager.popMatrix();

   return 25 + translationY;
   }
   */

  public boolean isRemoving() {
    return System.currentTimeMillis() - this.created > this.duration;
  }

  public int render(int x, int y) {
    float scale = configManager.getScale();
    int bgColor = configManager.getBgColor();
    int fgColor = configManager.getFgColor();
    long now = System.currentTimeMillis();
    double timeSinceCreated = now - this.created;
    int alpha = timeSinceCreated > duration ? (int) (255 - (255 * ((timeSinceCreated - duration) / fadeOutTime))) : 255;

    if (alpha <= 15) {
      notificationHandler.remove(this);
      return 0;
    }

    int height = this.ln2.length() == 0 ? 15 : 25;

    int width = Math.max((int) Math.ceil(mc.fontRendererObj.getStringWidth(this.ln1) * 1.25), mc.fontRendererObj.getStringWidth(this.ln2)) + 3;
    int translationX = timeSinceCreated > duration ? (int) -((width * ((timeSinceCreated - duration) / fadeOutTime))) : 0;

    long lastRemoved = notificationHandler.getLastRemoved();
    int translationY = (lastRemoved > now - TRANSITION_TIME && !this.isRemoving()) ? (int) (height - (height * ((now - lastRemoved) / TRANSITION_TIME))) : 0;

    GlStateManager.pushMatrix();
    GlStateManager.scale(scale, scale, scale);
    GlStateManager.translate(translationX, translationY, 0);
    Gui.drawRect(x  + 1, y  + 1, width , y + height, bgColor | (((3 * alpha) / 4) << 24));
    GlStateManager.enableBlend();
    GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    mc.fontRendererObj.drawString(this.ln2, x + 3, y + 15, fgColor | (alpha << 24));
    GlStateManager.scale(1.25F, 1.25F, 1.25F);
    mc.fontRendererObj.drawString(this.ln1, (int) Math.ceil((x + 3) / 1.25), (int) Math.ceil((y + 2) / 1.25), fgColor | (alpha << 24));
    GlStateManager.popMatrix();

    return height + translationY;
  }
}