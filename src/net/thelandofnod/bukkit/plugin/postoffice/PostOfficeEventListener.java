package net.thelandofnod.bukkit.plugin.postoffice;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;

import net.thelandofnod.bukkit.plugin.postoffice.PostOffice.applicationState;
//import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreDBDisconnectEvent;
//import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryEvent;
import net.thelandofnod.bukkit.plugin.rdbmscore.RDBMScoreQueryResultEvent;

import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;

// implements Listener   
public class PostOfficeEventListener extends CustomEventListener {
	private final PostOffice plugin;

	public PostOfficeEventListener(PostOffice instance) {
		plugin = instance;
	}

	@Override
	public void onCustomEvent(Event customEvent) {
		if (customEvent instanceof RDBMScoreQueryResultEvent) {
			// see if the event is owned by us ..
			if (((RDBMScoreQueryResultEvent) customEvent).getOwnerPlugin()
					.equals(plugin.pdfFile.getName())) {
				onRDBMScoreQueryResultEvent((RDBMScoreQueryResultEvent) customEvent);
				((RDBMScoreQueryResultEvent) customEvent).setCancelled(true);
			}
		} else if (customEvent instanceof PostOfficeSendMessageEvent) {
			onPostOfficeSendMessageEvent((PostOfficeSendMessageEvent) customEvent);
			((PostOfficeSendMessageEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof PostOfficeReadMessageEvent) {
			onPostOfficeReadMessageEvent((PostOfficeReadMessageEvent) customEvent);
			((PostOfficeReadMessageEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof PostOfficeMarkMessagesReadEvent) {
			onPostOfficeMarkMessagesReadEvent((PostOfficeMarkMessagesReadEvent) customEvent);
			((PostOfficeMarkMessagesReadEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof PostOfficeSendPackageEvent) {
			onPostOfficeSendPackageEvent((PostOfficeSendPackageEvent) customEvent);
			((PostOfficeSendPackageEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof PostOfficeReceivePackageEvent) {
			onPostOfficeReceivePackageEvent((PostOfficeReceivePackageEvent) customEvent);
			((PostOfficeReceivePackageEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof PostOfficeMarkPackagesReadEvent) {
			onPostOfficeMarkPackagesReadEvent((PostOfficeMarkPackagesReadEvent) customEvent);
			((PostOfficeMarkPackagesReadEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof PostOfficeRegisterPlayerEvent){
			onPostOfficeRegisterPlayerEvent((PostOfficeRegisterPlayerEvent) customEvent);
			((PostOfficeRegisterPlayerEvent) customEvent).setCancelled(true);
		} else if (customEvent instanceof PostOfficeRecallPackageEvent) {
			onPostOfficeRecallPackageEvent((PostOfficeRecallPackageEvent) customEvent);
			((PostOfficeRecallPackageEvent) customEvent).setCancelled(true);
		}
	}

	private void onPostOfficeRecallPackageEvent(
			PostOfficeRecallPackageEvent customEvent) {
		String SQL = "CALL `sp_onPostOfficeRecallPackageEvent`('" + customEvent.getRecipient() + "')";
		System.out.println("[postoffice] SQL: " + SQL);
		plugin.assertRDBMScoreQueryEvent(SQL);
	}
	

	private void onPostOfficeRegisterPlayerEvent(
			PostOfficeRegisterPlayerEvent customEvent) {
		String SQL = "CALL `sp_RegisterUser`('" + customEvent.getPlayer() + "')";
		System.out.println("[postoffice] SQL: " + SQL);
		plugin.assertRDBMScoreQueryEvent(SQL);
	}

	private void onPostOfficeMarkPackagesReadEvent(
			PostOfficeMarkPackagesReadEvent customEvent) {
		String recipient = customEvent.getRecipient();
		String SQL = "CALL `sp_onPostOfficeMarkPackagesReadEvent`('" + recipient + "');";
		System.out.println("[postoffice] SQL: " + SQL);
		plugin.assertRDBMScoreQueryEvent(SQL);
	}

	private void onPostOfficeReceivePackageEvent(
			PostOfficeReceivePackageEvent customEvent) {
		String recipient = customEvent.getRecipient();
		String SQL = "CALL `sp_onPostOfficeReceivePackageEvent`('" + recipient + "');";
		System.out.println("[postoffice] SQL: " + SQL);
		plugin.setCurrentState(applicationState.RESPONSE_PACKAGE_RECEIVE);
		plugin.assertRDBMScoreQueryEvent(SQL);
	}

	private void onPostOfficeSendPackageEvent(
			PostOfficeSendPackageEvent customEvent) {

		Date dateNow = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"MM-dd-yyyy hh:mm:ss a");
		String strDateNow = formatter.format(dateNow);

		String sender = customEvent.getSender();
		String recipient = customEvent.getRecipient();
		int materialId = customEvent.getMaterialId();
		int amount = customEvent.getAmount();

		plugin.setMaterialId(materialId);
		plugin.setAmount(amount);

		// see if the sender has the package to send..
		if (senderHasPackage(materialId, amount)) {		
			String SQL="CALL `sp_onPostOfficeSendPackageEvent`('" + strDateNow + "', '" + sender + "', '" + recipient + "', '" + materialId + "', '" + amount + "');";
			System.out.println("[postoffice] SQL: " + SQL);

			// we need our 'send-receipt before we take the items from the
			// sender'
			plugin.setCurrentState(applicationState.WAIT_FOR_SEND_RECEIPT);
			plugin.assertRDBMScoreQueryEvent(SQL);

		} else {
			// the sender can't send the package
			System.out.println("[postoffice] sender " + sender + ", is unable to send "
					+ recipient + " " + amount + " of " + materialId);
			plugin.callingPlayer.sendMessage("Unable to send " + recipient + " " + amount
					+ " of " + materialId);
		}
	}

	private boolean senderHasPackage(int materialId, int amount) {
		ItemStack newPackage = new ItemStack(materialId, amount);

		if (plugin.callingPlayer.getInventory().contains(newPackage)) {
			newPackage = null;
			System.out.println("[postoffice] " + plugin.callingPlayer.getDisplayName() + " has "
					+ plugin.getAmount() + " of " + plugin.getMaterialId()
					+ " to send.");
			return true;
		}
		System.out.println("[postoffice] " + plugin.callingPlayer.getDisplayName()
				+ " can't send " + plugin.getAmount() + " of "
				+ plugin.getMaterialId() + " - not in inventory.");
		newPackage = null;
		return false;
	}

	private void TakePackageContents() {
		System.out.println("[postoffice] Taking " + plugin.getAmount() + " of "
				+ plugin.getMaterialId() + " from "
				+ plugin.callingPlayer.getDisplayName());

		ItemStack newPackage = new ItemStack(plugin.getMaterialId(),
				plugin.getAmount());

		HashMap<Integer, ItemStack> senderInventoryMatches = new HashMap<Integer, ItemStack>();
		senderInventoryMatches = (HashMap<Integer, ItemStack>) plugin.callingPlayer
				.getInventory().all(newPackage);

		plugin.callingPlayer.getInventory().remove(newPackage);

		// // now add back all the items that shouldn't have been removed...
		// // remove the one we sent
		for (int i = 1; i < senderInventoryMatches.size(); i++) {
			plugin.callingPlayer.getInventory().addItem(newPackage);
		}

		newPackage = null;
	}

	private void onPostOfficeMarkMessagesReadEvent(
			PostOfficeMarkMessagesReadEvent customEvent) {
		String recipient = customEvent.getRecipient();
		String SQL = "CALL `sp_onPostOfficeMarkMessagesReadEvent`('" + recipient + "');";
		System.out.println("[postoffice] SQL: " + SQL);
		plugin.assertRDBMScoreQueryEvent(SQL);
	}

	private void onPostOfficeReadMessageEvent(
			PostOfficeReadMessageEvent customEvent) {
		// pull mail from database
		String recipient = customEvent.getRecipient();
		String SQL = "CALL `sp_onPostOfficeReadMessageEvent`('" + recipient + "')";	
		System.out.println("[postoffice] SQL: " + SQL);
		plugin.setCurrentState(applicationState.RESPONSE_READ);
		plugin.assertRDBMScoreQueryEvent(SQL);
	}

	private void onPostOfficeSendMessageEvent(
			PostOfficeSendMessageEvent customEvent) {
		// The first thing we do is store the message for later retrieval

		Date dateNow = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(
				"MM-dd-yyyy hh:mm:ss a");
		String strDateNow = formatter.format(dateNow);

		String sender = customEvent.getSender();
		String recipient = customEvent.getRecipient();
		String message = customEvent.getMessage();

		/*
		 * \0 An ASCII NUL (0x00) character. \' A single quote (“'”) character.
		 * \"  A double quote (“"”) character. \b A backspace character. \n A
		 * newline (linefeed) character. \r A carriage return character. \t A
		 * tab character. \Z ASCII 26 (Control-Z). See note following the table.
		 * \\ A backslash (“\”) character. \% A “%” character. See note
		 * following the table. \_ A “_” character. See note following the
		 * table.
		 */
		
		
		// sanitize recipient name
		recipient = recipient.replace("\0", "");
		recipient = recipient.replace("\'" , "");
		recipient = recipient.replace("\"" , "");
		//recipient = recipient.replace("\b" , "");
		recipient = recipient.replace("\n" , "");
		recipient = recipient.replace("\t" , "");
		recipient = recipient.replace("\\" , "");
		
		
		// sanitize string
		message = message.replace("\0", "\\\0");
		message = message.replace("\'" , "\\\'");
		message = message.replace("\"" , "\\\"");
		//message = message.replace("\b" , "\\\b");
		message = message.replace("\n" , "\\\n");
		message = message.replace("\t" , "\\\t");
		message = message.replace("\\" , "\\\\");

		String SQL="CALL `sp_onPostOfficeSendMessageEvent`('" + strDateNow + "', '" + sender + "', '" + recipient + "', '" + message + "')";
		System.out.println("[postoffice] SQL: " + SQL);
		plugin.assertRDBMScoreQueryEvent(SQL);
	}

	
	private void onRDBMScoreQueryResultEvent(RDBMScoreQueryResultEvent event) {
		CachedRowSet crs = null;
		if (event.isExceptionCaught()) {
			// then we caught an exception
			System.out
					.println("[postoffice] Exception was caught by the rdbmscore plugin..");
			System.out.println("[postoffice] SQLException: " + event.getExceptionLog());

		} else {
			if (event.getEffectedRowCount() > -1) {
				// then this was either an update, insert, or delete
//				System.out
//						.println("Caught the results of an update, insert, or delete");
//				System.out.println(event.getEffectedRowCount()
//						+ " rows effected.");
				switch (plugin.getCurrentState()) {
				case WAIT_FOR_SEND_RECEIPT:
					// send the data layer has successfully responded to our
					// update
					// and request to send the other player a package
					// now we need to remove them from the player
//					System.out
//							.println("delivery confirmed, taking items from player");
					this.TakePackageContents();
					// TODO
					break;

				}
//				System.out
//						.println("Done handling the results, returning to NORMAL state..");
				plugin.setCurrentState(applicationState.NORMAL);

				// plugin.callingPlayer.sendMessage(event.getEffectedRowCount()
				// + " rows effected.");

			} else {
				// else this was a select statement
//				System.out
//						.println("Caught the results of a select statement..");
				crs = (event).getCrs();
				if (crs != null) {
					// When execution reaches this point then we have received
					// mail results from the
					// database from a waiting/requesting player - and should
					// process accordingly.

					switch (plugin.getCurrentState()) {
					case RESPONSE_READ:
						//System.out.println("State: RESPONSE_READ");
						try {
							//System.out.println("Processing mail..");
							while (crs.next()) {
								// Check status
								// are we displaying messages or giving items?

								// // attempt to send the results to the
								// player..

								String datetime = crs.getString("datetime");
								String sender = crs.getString("sender");
								String message = crs.getString("message");
								
								// unsanitize string
								message = message.replace("\\\0" , "\0");
								message = message.replace("\\\'" , "\'");
								message = message.replace("\\\"" , "\"");
								//message = message.replace("\\\b" , "\b");
								message = message.replace("\\\n" , "\n");
								message = message.replace("\\\t" , "\t");
								message = message.replace("\\\\" , "\\");
								
								plugin.callingPlayer.sendMessage("From: "
										+ sender);
								plugin.callingPlayer.sendMessage("Recieved: "
										+ datetime);
								plugin.callingPlayer.sendMessage(message);
							}
							// now that the mail has been displayed we need to
							// mark our mail read
							// read in the database
							plugin.assertMarkMessagesReadEvent(plugin.callingPlayer
									.getName());

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					case RESPONSE_PACKAGE_RECEIVE:
						//System.out.println("State: RESPONSE_PACKAGE_RECEIVE");
						// then we are receiving a package ..
						try {
							while (crs.next()) {
								// Check status
								// are we displaying messages or giving items?

								// // attempt to send the results to the
								// player..

								String datetime = crs.getString("datetime");
								String sender = crs.getString("sender");
								int materialId = Integer.parseInt(crs
										.getString("materialId"));
								int amount = Integer.parseInt(crs
										.getString("amount"));

								plugin.callingPlayer.sendMessage("From: "
										+ sender);
								plugin.callingPlayer.sendMessage("Recieved: "
										+ datetime);
								plugin.callingPlayer
										.sendMessage("Package contains "
												+ amount + " of " + materialId);

								// TODO
								// add a hashmap to hold to results of the
								// addItem method call
								ItemStack newStack = new ItemStack(materialId,
										amount);
								plugin.callingPlayer.getInventory().addItem(
										newStack);

							}
							// now that the mail has been displayed we need to
							// mark our mail read
							// read in the database
							plugin.assertMarkPackagesReadEvent(plugin.callingPlayer
									.getName());

						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				} else {
//					System.out
//							.println("Recieved a NULL set from the rdbmscore!");
				}
//				System.out
//						.println("Done handling the results, returning to NORMAL state..");
				plugin.setCurrentState(applicationState.NORMAL);
			}
		}
	}
}
