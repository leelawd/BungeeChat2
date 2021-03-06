package dev.aura.bungeechat.listener;

import dev.aura.bungeechat.api.account.AccountManager;
import dev.aura.bungeechat.api.account.BungeeChatAccount;
import dev.aura.bungeechat.api.enums.ChannelType;
import dev.aura.bungeechat.api.module.ModuleManager;
import dev.aura.bungeechat.message.Messages;
import dev.aura.bungeechat.message.MessagesService;
import dev.aura.bungeechat.module.BungeecordModuleManager;
import dev.aura.bungeechat.permission.Permission;
import dev.aura.bungeechat.permission.PermissionManager;
import java.util.Optional;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChannelTypeCorrectorListener implements Listener {
  @EventHandler(priority = EventPriority.LOW)
  public void onPlayerChat(ChatEvent e) {
    if (e.isCancelled()) return;
    if (!(e.getSender() instanceof ProxiedPlayer)) return;

    ProxiedPlayer player = (ProxiedPlayer) e.getSender();
    Optional<BungeeChatAccount> bungeeChatAccountOptional =
        AccountManager.getAccount(player.getUniqueId());
    ChannelType c = bungeeChatAccountOptional.get().getChannelType();

    if ((c.equals(ChannelType.GLOBAL)
            && (!ModuleManager.isModuleActive(BungeecordModuleManager.GLOBAL_CHAT_MODULE)
                || !PermissionManager.hasPermission(player, Permission.COMMAND_GLOBAL)))
        || (c.equals(ChannelType.STAFF)
            && (!ModuleManager.isModuleActive(BungeecordModuleManager.STAFF_CHAT_MODULE)
                || !PermissionManager.hasPermission(player, Permission.COMMAND_STAFFCHAT)))) {
      e.setCancelled(true);
      bungeeChatAccountOptional.get().setChannelType(ChannelType.LOCAL);
      MessagesService.sendMessage(player, Messages.BACK_TO_LOCAL.get());
    }
  }
}
