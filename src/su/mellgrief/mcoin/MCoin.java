package su.mellgrief.mcoin;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

public class MCoin extends JavaPlugin {

	public static final String OUT_CHANNEL = "mcoin";
	public static Economy eco;
	public static MCoin instance;
	public static int SCHEDULE;
	
	@Override
	public void onEnable() {
		setupEconomy();
		instance = this;
		Bukkit.getMessenger().registerOutgoingPluginChannel(instance, OUT_CHANNEL);
		
		
		SCHEDULE = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(instance, new Runnable() {
			
			@Override
			public void run() {
				try {
					for(Player p : Bukkit.getServer().getOnlinePlayers()) {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						DataOutputStream dos = new DataOutputStream(bos);
						dos.writeUTF("" + eco.getBalance(p));
						p.sendPluginMessage(instance, OUT_CHANNEL, bos.toByteArray());
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}, 20L, 20L);
	}
	
	@Override
	public void onDisable() {
		Bukkit.getServer().getScheduler().cancelTask(SCHEDULE);
	}
	
	private boolean setupEconomy()
	{
	  RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
	  if (economyProvider != null) {
	    this.eco = ((Economy)economyProvider.getProvider());
	  }
	  return this.eco != null;
	}
	
}
