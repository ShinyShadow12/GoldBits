package me.ShinyShadow_.GoldBits;


import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.projectkorra.projectkorra.BendingPlayer;
import me.ShinyShadow_.GoldBits.*;
import net.ess3.api.MaxMoneyException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.entity.Entity;

public class GoldBitsListener implements Listener {

    public Entity target;

    @EventHandler
    public void onClick(PlayerAnimationEvent event) {


        Player player = event.getPlayer();
        BendingPlayer bPlayer = BendingPlayer.getBendingPlayer(player);
        if (event.isCancelled() || bPlayer == null)
            return;
        if (bPlayer.getBoundAbilityName().equalsIgnoreCase(null))
            return;
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && bPlayer.getBoundAbilityName().equalsIgnoreCase("GoldBits")) {
            try {
                Economy.add(player.getName(), -0.0350 );
                new GoldBits(player);
            } catch (UserDoesNotExistException e) {
                throw new RuntimeException(e);
            } catch (NoLoanPermittedException e) {
                throw new RuntimeException(e);
            } catch (MaxMoneyException e) {
                throw new RuntimeException(e);
            }

            // new GoldBits(player);
        }
    }
}
