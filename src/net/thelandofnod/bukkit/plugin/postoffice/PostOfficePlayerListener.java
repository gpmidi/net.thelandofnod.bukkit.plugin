package net.thelandofnod.bukkit.plugin.postoffice;

import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerListener;

public class PostOfficePlayerListener extends PlayerListener {
	private final PostOffice plugin;

	public PostOfficePlayerListener(PostOffice instance) {
		plugin = instance;
	}

	@Override
	public void onPlayerJoin(PlayerEvent event) {
		if (event.getType().equals(Event.Type.PLAYER_JOIN)) {
			// then lets register the current player
			plugin.assertRegisterPlayerEvent(event.getPlayer().getName());
		}
	}

	// This method is called whenever a player uses a command.
	@Override
	public void onPlayerCommand(PlayerChatEvent event) {
		// Make the message a string.
		//System.out.println("(" + event.getMessage() + ")");
		if (event.getMessage().equalsIgnoreCase("/postoffice")
				|| event.getMessage().equalsIgnoreCase("/po")) {
			// then it's a command we want to listen to
			event.setCancelled(true);

			// event.getPlayer().sendMessage("Usage: /postoffice read messageId");
			// event.getPlayer().sendMessage("Usage: /postoffice send player message");
			// event.getPlayer().sendMessage("Usage: /postoffice package send player amount materialId");
			// event.getPlayer().sendMessage("Usage: /postoffice package get");
			// event.getPlayer().sendMessage("Usage: /postoffice package recall");
			/*
			 * {blank}
			 * inbox 
			 * read {messageId} 
			 * send {player} {message} 
			 * delete message {messageId}
			 * delete package {messageId}
			 * package send {player} {amount} {materialId} 
			 * package get {packageId} 
			 * package delete {packageId}
			 * package recall 
			 * help
			 */
			// default action is to display the player inbox
			// store calling player for later reference ..
			plugin.callingPlayer = event.getPlayer();
			plugin.assertPostOfficeViewInboxEvent(event.getPlayer().getName());

		} else {
			String[] split = event.getMessage().split(" ");
			try {
				if (split[0].equalsIgnoreCase("/postoffice")
						|| split[0].equalsIgnoreCase("/po")) {

					// then it's a command we want to listen to
					event.setCancelled(true);

					// store calling player for later reference ..
					plugin.callingPlayer = event.getPlayer();

					if (split[1].equalsIgnoreCase("send")) {
						// then send a message
						String recipient = split[2];
						if (recipient.length() > 0) {
							String message = event.getMessage().substring(
									split[0].length() + split[1].length()
											+ split[2].length() + 3);
							// String message = split[3];

							plugin.assertSendMessageEvent(event.getPlayer()
									.getName(), recipient, message);
						} else {
							// else present the stand usage message
							// event.getPlayer().sendMessage("Incorrect usage syntax.");
							event.getPlayer().sendMessage(
									"Usage: /postoffice send player message");
						}

					} else if (split[1].equalsIgnoreCase("read")) {
						plugin.assertReadMessageEvent(event.getPlayer()
								.getName(), Integer.valueOf(split[2]));
					} else if (split[1].equalsIgnoreCase("delete")) {
							Integer messageId = Integer.valueOf(split[3]);
							if (split[2].equalsIgnoreCase("message")){
								plugin.assertDeleteMessageEvent(event.getPlayer()
										.getName(), messageId);
							} else if (split[2].equalsIgnoreCase("package")) {
								plugin.assertDeletePackageEvent(event.getPlayer()
										.getName(), messageId);
							} else {
								event.getPlayer()
								.sendMessage(
										"Usage: /postoffice delete {messageId}");
							}
					} else if (split[1].equalsIgnoreCase("package")) {
						if (split[2].equalsIgnoreCase("send")) {
							// then send a message
							String recipient = split[3];
							// System.out.println("recipient: " + recipient);
							int amount = Integer.parseInt(split[4].trim());
							// System.out.println("amount: " + amount);
							int materialId = Integer.parseInt(split[5].trim());
							// System.out.println("materialId: " + materialId);
							plugin.assertSendPackageEvent(event.getPlayer()
									.getName(), recipient, materialId, amount);
						} else if (split[2].equalsIgnoreCase("get")) {
							plugin.assertReceivePackageEvent(event.getPlayer()
									.getName());
						} else if (split[2].equalsIgnoreCase("recall")) {
							plugin.assertRecallPackageEvent(event.getPlayer()
									.getName());
						} 
						else {
							// else present the stand usage message
							// event.getPlayer().sendMessage("Incorrect usage syntax.");
							event.getPlayer()
									.sendMessage(
											"Usage: /postoffice package send player amount materialId");
							event.getPlayer().sendMessage(
									"Usage: /postoffice package get");
							event.getPlayer().sendMessage(
									"Usage: /postoffice package recall");
						}
					}
					else {
						// else present the stand usage message
						// event.getPlayer().sendMessage("Incorrect usage syntax.");
						event.getPlayer()
								.sendMessage("Usage: /postoffice read");
						event.getPlayer().sendMessage(
								"Usage: /postoffice send player message");
						event.getPlayer()
								.sendMessage(
										"Usage: /postoffice package send player amount materialId");
						event.getPlayer().sendMessage(
								"Usage: /postoffice package get");
						event.getPlayer().sendMessage(
								"Usage: /postoffice package recall");
					}
				}
			} catch (Exception ArrayIndexOutOfBoundsException) {
				// else present the stand usage message
				// event.getPlayer().sendMessage("Incorrect usage syntax.");
				event.getPlayer().sendMessage("Usage: /postoffice read");
				event.getPlayer().sendMessage(
						"Usage: /postoffice send player message");
				event.getPlayer()
						.sendMessage(
								"Usage: /postoffice package send player amount materialId");
				event.getPlayer().sendMessage("Usage: /postoffice package get");
				event.getPlayer().sendMessage(
						"Usage: /postoffice package recall");
			}
		}
	}
}
