package com.gmail.nossr50.commands.party;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.nossr50.mcMMO;
import com.gmail.nossr50.config.Config;
import com.gmail.nossr50.datatypes.chat.ChatMode;
import com.gmail.nossr50.datatypes.party.Party;
import com.gmail.nossr50.datatypes.party.ShareMode;
import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.locale.LocaleLoader;
import com.gmail.nossr50.party.PartyManager;
import com.gmail.nossr50.util.player.UserManager;

public class PartyInfoCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
            case 1:
                Player player = (Player) sender;
                McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);
                Party party = mcMMOPlayer.getParty();

                displayPartyHeader(player, party);
                displayShareModeInfo(player, party);
                displayPartyFeatures(player, party);
                displayMemberInfo(player, mcMMOPlayer, party);
                return true;

            default:
                sender.sendMessage(LocaleLoader.getString("Commands.Usage.1", "party", "info"));
                return true;
        }
    }

    private void displayPartyHeader(Player player, Party party) {
        player.sendMessage(LocaleLoader.getString("Commands.Party.Header"));
        player.sendMessage(LocaleLoader.getString("Commands.Party.Status", party.getName(), LocaleLoader.getString("Party.Status." + (party.isLocked() ? "Locked" : "Unlocked")), party.getLevel()));
    }

    private void displayPartyFeatures(Player player, Party party) {
        McMMOPlayer mcMMOPlayer = UserManager.getPlayer(player);

        player.sendMessage(ChatColor.GREEN + "[[RED]]-----[][[GREEN]]FEATURES[[RED]][]-----");

        int partyChatUnlockLevel = Config.getInstance().getPartyChatUnlockLevel();

        if (party.getLevel() < partyChatUnlockLevel) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Party.Feature.Locked.1", partyChatUnlockLevel)));
        }
        else {
            player.sendMessage(ChatColor.DARK_GRAY + "Party Chat: " + (mcMMOPlayer.isChatEnabled(ChatMode.PARTY) ? ChatColor.DARK_GREEN + "ON" : ChatColor.RED + "OFF"));
        }

        int partyTeleportUnlockLevel = Config.getInstance().getPartyTeleportUnlockLevel();

        if (party.getLevel() < partyTeleportUnlockLevel) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Party.Feature.Locked.2", partyTeleportUnlockLevel)));
        }
        else {
            player.sendMessage(ChatColor.DARK_GRAY + "Party Teleport: " + ChatColor.DARK_GREEN + "UNLOCKED");
        }

        int partyAllianceUnlockLevel = Config.getInstance().getPartyAllianceUnlockLevel();

        if (party.getLevel() < partyAllianceUnlockLevel) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Party.Feature.Locked.3", partyAllianceUnlockLevel)));
        }
        else {
            player.sendMessage(LocaleLoader.getString("Commands.Party.Status.Alliance", (party.getAlly() != null) ? party.getAlly().getName() : ""));
        }

        int itemShareUnlockLevel = Config.getInstance().getItemShareUnlockLevel();

        if (party.getLevel() < itemShareUnlockLevel) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Party.Feature.Locked.4", itemShareUnlockLevel)));
        }
        else {
            player.sendMessage(ChatColor.DARK_GRAY + "Party Item Share: " + ChatColor.DARK_GREEN + "UNLOCKED");
        }

        int xpShareUnlockLevel = Config.getInstance().getXpShareUnlockLevel();

        if (party.getLevel() < xpShareUnlockLevel) {
            player.sendMessage(LocaleLoader.getString("Ability.Generic.Template.Lock", LocaleLoader.getString("Party.Feature.Locked.5", xpShareUnlockLevel)));
        }
        else {
            player.sendMessage(ChatColor.DARK_GRAY + "Party XP Share: " + ChatColor.GREEN + "UNLOCKED");
        }
    }

    private void displayShareModeInfo(Player player, Party party) {
        boolean xpShareEnabled = party.getLevel() >= Config.getInstance().getXpShareUnlockLevel();
        boolean itemShareEnabled = party.getLevel() >= Config.getInstance().getItemShareUnlockLevel();
        boolean itemSharingActive = (party.getItemShareMode() != ShareMode.NONE);

        if (!xpShareEnabled && !itemShareEnabled) {
            return;
        }

        String expShareInfo = "";
        String itemShareInfo = "";
        String separator = "";

        if (xpShareEnabled) {
            expShareInfo = LocaleLoader.getString("Commands.Party.ExpShare", party.getXpShareMode().toString());
        }

        if (itemShareEnabled) {
            itemShareInfo = LocaleLoader.getString("Commands.Party.ItemShare", party.getItemShareMode().toString());
        }

        if (xpShareEnabled && itemShareEnabled) {
            separator = ChatColor.DARK_GRAY + " || ";
        }

        player.sendMessage(LocaleLoader.getString("Commands.Party.ShareMode") + expShareInfo + separator + itemShareInfo);

        if (itemSharingActive) {
            player.sendMessage(LocaleLoader.getString("Commands.Party.ItemShareCategories", party.getItemShareCategories()));
        }
    }

    private void displayMemberInfo(Player player, McMMOPlayer mcMMOPlayer, Party party) {
        int membersNear = PartyManager.getNearMembers(mcMMOPlayer).size();
        int membersOnline = party.getOnlineMembers().size() - 1;

        player.sendMessage(LocaleLoader.getString("Commands.Party.Members.Header"));
        player.sendMessage(LocaleLoader.getString("Commands.Party.MembersNear", membersNear, membersOnline));
        player.sendMessage(createMembersList(party));
    }

    private String createMembersList(Party party) {
        StringBuilder memberList = new StringBuilder();

        for (String memberName : party.getMembers()) {
            Player member = mcMMO.p.getServer().getPlayerExact(memberName);

            if (party.getLeader().equalsIgnoreCase(memberName)) {
                memberList.append(ChatColor.GOLD);
            }
            else if (member != null) {
                memberList.append(ChatColor.WHITE);
            }
            else {
                memberList.append(ChatColor.GRAY + "" + ChatColor.ITALIC);
            }

            memberList.append(memberName).append(ChatColor.RESET + " ");
        }

        return memberList.toString();
    }
}
