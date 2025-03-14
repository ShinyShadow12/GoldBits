package me.ShinyShadow_.GoldBits;


import com.earth2me.essentials.api.Economy;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.Ability;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import com.projectkorra.projectkorra.configuration.ConfigManager;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.earth2me.essentials.*;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;

public class GoldBits extends EarthAbility implements AddonAbility {
    private List<Block> sight;
    Listener listener;

    Set<Material> ignoredBlocks = Set.of(Material.AIR, Material.GRASS_BLOCK, Material.TALL_GRASS);
    private BlockDisplay goldblock;
    private Vector direction;
    private AxisAngle4f rotation;
    private Vector3f nugScale;
    private Vector3f scale;

    private List<FireworkEffect.Type> FireworkTypes = Arrays.asList(
            FireworkEffect.Type.CREEPER,
            FireworkEffect.Type.STAR,
            FireworkEffect.Type.BALL,
            FireworkEffect.Type.BALL_LARGE,
            FireworkEffect.Type.BURST
    );

    public GoldBits(Player player) {
        super(player);

        if(bPlayer.canBendIgnoreBinds(this)) {
            goldblock = (BlockDisplay) player.getWorld().spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
            goldblock.setBlock(Material.GOLD_BLOCK.createBlockData());
            direction = player.getEyeLocation().getDirection().multiply(1.5);

            scale = new Vector3f(0.75f, 0.75f, 0.75f);
            nugScale = new Vector3f(0.6f, 0.5f, 0.5f);

            rotation = new AxisAngle4f((float) Math.toRadians(30), 1, 1, 0);

            Transformation transformation = new Transformation(new Vector3f(), rotation, scale, new AxisAngle4f());
            goldblock.setTransformation(transformation);
            getCooldown();
            bPlayer.addCooldown(this);

            start();
        }
        if(!bPlayer.canBendIgnoreBindsCooldowns(this)){
            stop();
        }
    }

    @Override
    public void progress() {

        if(bPlayer.canBendIgnoreBindsCooldowns(this)){
            Ability ability = this;

            new BukkitRunnable() {
                int ticks = 0;
                Entity target = null;

                @Override
                public void run() {
                    if (ticks > 10 || goldblock.isDead()) {
                        goldblock.remove();
                        stop();
                        cancel();
                        return;
                    }
                    Collection<Entity> Entities = GeneralMethods.getEntitiesAroundPoint(goldblock.getLocation(),0.6);
                    rotation = new AxisAngle4f(
                            (float) Math.toRadians(Math.random() * 360),
                            (float) (Math.random() - 0.5),
                            (float) (Math.random() - 0.5),
                            (float) (Math.random() - 0.5)
                    );
                    goldblock.setTransformation(new Transformation(new Vector3f(), rotation, scale, new AxisAngle4f()));

                    for (Entity entity : Entities) {

                        if (entity instanceof LivingEntity && entity.getUniqueId() != player.getUniqueId() && !entity.isDead()) {
                            if (target == null) {

                                target = entity;
                                player.playSound(target.getLocation(), Sound.BLOCK_ANVIL_BREAK, 0.3f, 1.2f);
                                player.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.15f, 1.4f);
                                player.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1.3f);
                               // player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_BREAK, 0.3f, 0.6f);
                                Random r = new Random();
                                int index = r.nextInt(5);
//
//                                Firework firework = (Firework) player.getWorld().spawnEntity(target.getLocation().add(0, 1, 0), EntityType.FIREWORK_ROCKET);
//                                FireworkMeta meta = firework.getFireworkMeta();
//                                FireworkEffect effect = FireworkEffect.builder()
//                                        .withColor(Color.YELLOW)
//                                        .withColor(Color.LIME)
//                                        .withFade(Color.WHITE)
//                                        .with(FireworkTypes.get(index))
//                                        .build();
//                                meta.addEffect(effect);
//                                meta.setPower(1);
//                                firework.setFireworkMeta(meta);
//                                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0f, 0f);
//
//
//                                new BukkitRunnable() {
//                                    @Override
//                                    public void run() {
//                                        firework.detonate();
//                                        this.cancel();
//                                    }
//                                }.runTaskLater(ProjectKorra.plugin, 18L);

                                player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 0.5f, 1.2f);
                                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.3f, 1.4f);
                                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2f, 1.3f);
                             //   player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_BREAK, 0.25f, 0.76f);

                            }
                            DamageHandler.damageEntity(target, 0.5, ability);
                            for (int i = 0; i < 6; i++) {

                                ItemDisplay goldNugget = (ItemDisplay) player.getWorld().spawnEntity(target.getLocation().add(0, 0.7, 0), EntityType.ITEM_DISPLAY);
                                goldNugget.setTransformation(new Transformation(new Vector3f(), rotation, nugScale, new AxisAngle4f()));
                                goldNugget.setItemStack(new ItemStack(Material.GOLD_NUGGET));

                                Vector velocity = new Vector(
                                        (Math.random() - 0.5) * 1.5,
                                        Math.random() * 0.1 + 0.1,
                                        (Math.random() - 0.5) * 1.5
                                );

                                new BukkitRunnable() {
                                    int ticks = 0;

                                    @Override
                                    public void run() {
                                        if (ticks > 3 || goldNugget.isDead()) {
                                            goldNugget.remove();
                                            cancel();
                                            return;
                                        }
                                        Collection<Entity> Entities1 = GeneralMethods.getEntitiesAroundPoint(goldNugget.getLocation(),0.5);

                                        for (Entity entity1 : Entities1) {
                                            if (entity1 instanceof LivingEntity && entity1.getUniqueId() != player.getUniqueId() && !entity1.isDead()) {
                                                DamageHandler.damageEntity(entity1, 0.5, ability);
                                            }
                                        }
                                        Location newLoc = goldNugget.getLocation().add(velocity);
                                        goldNugget.teleport(newLoc);
                                        ticks++;
                                    }
                                }.runTaskTimer(ProjectKorra.plugin, 0L, 2L);
                            }
                            goldblock.remove();
                            stop();
                            cancel();
                        }
                    }
                    Location newLoc = goldblock.getLocation().add(direction);
                    goldblock.teleport(newLoc);
                    ticks++;
                }
            }.runTaskTimer(ProjectKorra.plugin, 0L, 4L);
        }
    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public boolean isIgniteAbility() {
        return false;
    }

    @Override
    public boolean isExplosiveAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return 250;
    }

    @Override
    public String getName() {
        return "GoldBits";
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void load() {
        ConfigManager.defaultConfig.save();
        this.listener = new GoldBitsListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(this.listener, (Plugin) ProjectKorra.plugin);
        ProjectKorra.log.info("Succesfully enabled " + getName() + " by " + getAuthor());
    }

    @Override
    public void stop() {

        remove();
    }

    public String getDescription() {
        return ChatColor.BOLD + " Throw bits of gold that will shatter " +
                                 "into smaller bits when hitting an enemy. " +
                                 "These smaller bits will also damage enemies hit by them.";
    }

    public String getInstructions() {
        return "Left click to shoot";
    }
    @Override
    public String getAuthor() {
        return "ShinyShadow_";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }
}
