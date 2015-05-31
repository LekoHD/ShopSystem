package com.lekohd.shopsystem.manager;

/*
 * Copyright (C) 2014 Leon167, XxChxppellxX, ScriptJunkie and CPx1989
 */

import com.lekohd.shopsystem.Locale;
import com.lekohd.shopsystem.ShopSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageManager {
	public static String colorizeString(String input){
		return ChatColor.translateAlternateColorCodes('&', input);
	}
	
	
	public static enum MessageType {
		INFO(ChatColor.GRAY, ""), ERROR(ChatColor.RED, "§cError: "), BAD(ChatColor.RED, "");

		private ChatColor color;
		private String prefix;

		private MessageType(ChatColor color, String prefix) {
			this.color = color;
			this.prefix = prefix;
		}

		public ChatColor getColor() {
			return this.color;
		}

		public String getPrefix() {
			return this.prefix;
		}
	}

	private static MessageManager instance = new MessageManager();
	private String messagePrefix = Locale.PLUGIN_PREFIX + " " + ChatColor.RESET;

	public static MessageManager getInstance() {
		return instance;
	}

	public void log(String message) {
		System.out.println(message);
	}

	public void msg(CommandSender sender, MessageType type, String message) {
		sender.sendMessage(this.messagePrefix + type.getPrefix() + type.getColor() + message);
	}

	public void broadcast(MessageType type, String message) {
		ShopSystem.getInstance().getServer().broadcastMessage(this.messagePrefix + type.getPrefix() + type.getColor() + message);
	}

}