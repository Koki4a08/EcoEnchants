package com.willfp.ecoenchants.endershot;

import com.willfp.ecoenchants.enchantments.EcoEnchant;
import com.willfp.ecoenchants.enchantments.EcoEnchants;
import com.willfp.ecoenchants.enchantments.util.EnchantChecks;
import com.willfp.ecoenchants.integrations.mcmmo.McmmoManager;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class Endershot extends EcoEnchant {
    public Endershot() {
        super("endershot", EnchantmentType.NORMAL, EndershotMain.class);
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onBowShoot(EntityShootBowEvent event) {
        if(McmmoManager.isFake(event))
            return;
        if (event.getProjectile().getType() != EntityType.ARROW)
            return;
        if(!(event.getEntity() instanceof Player))
            return;

        Player player = (Player) event.getEntity();

        event.setCancelled(true);

        if(!EnchantChecks.mainhand(player, this)) return;

        boolean hasInfinity = EnchantChecks.mainhand(player, ARROW_INFINITE) && this.getConfig().getBool(EcoEnchants.CONFIG_LOCATION + "work-with-infinity");
        if(!hasInfinity) {
            ItemStack pearl = new ItemStack(Material.ENDER_PEARL, 1);
            if(!player.getInventory().contains(Material.ENDER_PEARL, 1))
                return;

            player.getInventory().remove(pearl);
        }

        EnderPearl pearl = player.launchProjectile(EnderPearl.class);
        pearl.setShooter(player);
        pearl.setVelocity(event.getProjectile().getVelocity());
    }
}
