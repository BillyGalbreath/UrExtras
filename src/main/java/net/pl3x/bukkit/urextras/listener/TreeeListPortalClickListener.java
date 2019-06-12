package net.pl3x.bukkit.urextras.listener;

import com.destroystokyo.paper.block.TargetBlockInfo;
import java.util.ArrayList;
import net.pl3x.bukkit.urextras.Logger;
import net.pl3x.bukkit.urextras.configuration.Lang;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/*
 * TODO: Add per tree permission
 *
 */
public class TreeeListPortalClickListener implements Listener {
    /**
     * Checks whether or not the proper block was clicked.
     * If the the block can not spawn a tree type event will cancel.
     *
     * @param clickEvent
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onTreeeBlockSlected(PlayerInteractEvent clickEvent){
        if (clickEvent.getPlayer().getInventory().getItemInMainHand().getType() != Material.DIAMOND_AXE){
            Logger.debug("onTreeeBlockSelect | No Diamond Axe is hand, clickEvent cancelled");
            return;
        }

        /* Notice: Cancel Player Interact Event */
        clickEvent.setCancelled(true);

        /* Notice: Check for a identifier (Custom Model Data)*/
        if (!clickEvent.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasCustomModelData()){
            Logger.debug("onTreeeBlockSelect | Item does not have custom model data, clickEvent cancelled");
            return;
        }

        /* Notice: Get the Identified (Custom Model Data) */
        Integer itemInHandCustomModelData = clickEvent.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData();

        /* Notice: Check for correct Custom Model Data */
        if (!itemInHandCustomModelData.equals(0001)){
            Logger.debug("onTreeeBlockSelect | Item in hand does not equal to Tree Tool Custom Data");
            Logger.debug("onTreeeBlockSelect | Diamond Axe is has no lore which is in main hand, clickEvent cancelled");
            return;
        }

        Player target = clickEvent.getPlayer();
        ItemStack itemInHand = target.getInventory().getItemInMainHand();

        /* Notice: Check for null */
        if (clickEvent == null){
            Logger.debug("onTreeeBlockSelect | click event is null");
            return;
        }

        /* Notice: Only spawn on blocks */
        if (!clickEvent.getClickedBlock().getType().isBlock()){
            Logger.debug("onTreeeBlockSelect | clicked block is not a block");
            return;
        }

        Material clickedBlock = clickEvent.getClickedBlock().getType();

        if (clickedBlock == null){
            Logger.debug("onTreeeBlockSelect | Clicked block is null");
            return;
        }
        /* Notice: End of null check */


        /*
        * Notice: Approve only certain blocks to spawn on
        *
        * TODO: Make clicked block configurable
        * */
        if (clickedBlock.equals(Material.DIRT)
                || clickedBlock.equals(Material.GRASS_BLOCK)
                || clickedBlock.equals(Material.GRASS)
                || clickedBlock.equals(Material.COARSE_DIRT)
                || clickedBlock.equals(Material.GRASS_PATH)
                || clickedBlock.equals(Material.PODZOL)
                || clickedBlock.equals(Material.FARMLAND) ){

            /* Notice: Create Treee List Inventory */
            Inventory treeListInventory = Bukkit.createInventory(null, InventoryType.BARREL, Lang.TREEE_LIST_INVENTORY_TITLE);

            // Notice: Acacia Tree
            ItemStack treeOne = new ItemStack(Material.ACACIA_LOG, 1);
            ItemMeta treeOneMeta = treeOne.getItemMeta();
            treeOneMeta.setDisplayName(Lang.colorize("&aAcacia Tree"));
            treeOneMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            ArrayList<String> treeOneLore = new ArrayList<>();
            treeOneLore.add(Lang.colorize("&8Click to spawn your treee"));
            treeOneMeta.setLore(treeOneLore);
            treeOne.setItemMeta(treeOneMeta);
            treeListInventory.setItem(0, treeOne);

            // TODO: Add next tree

            Logger.debug("onTreeeBlockSelect | " + target.getDisplayName() + " clicked a applicable block with a " + itemInHand.getItemMeta().getDisplayName());

            target.openInventory(treeListInventory);
            return;
        }

        Logger.debug("onTreeeBlockSelect | clickEvent was cancelled, you cannot spawn a tree there");
        Lang.send(target, Lang.colorize(Lang.CANNOT_SPAWN_TREEE_HERE.replace("{getToolName}", target.getInventory().getItemInMainHand().getItemMeta().getDisplayName() )) );
        return;
    }


    /**
     * Checks what was clicked inside the UrExtras Portal Inventory
     *
     * @param inventoryClickEvent get clicked inventory.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onTreeeCreate(InventoryClickEvent inventoryClickEvent){
        String inventoryName = inventoryClickEvent.getWhoClicked().getOpenInventory().getTitle();
        if (inventoryName != Lang.TREEE_LIST_INVENTORY_TITLE){
            Logger.debug("onTreeeCreate | This inventory is not Tree List, return.");
            return;
        }

        Player target = (Player) inventoryClickEvent.getWhoClicked();
        ItemStack cursor = inventoryClickEvent.getCursor(); // Item/Block Placed
        ItemStack clicked = inventoryClickEvent.getCurrentItem();

        // Notice: Stopping all clickable events
        if (inventoryName.startsWith("Treee")){
            inventoryClickEvent.setCancelled(true);
            Logger.debug("onTreeeCreate | Cancelling inventory click for Treee List Portal");
        }

        /* Notice: NULL CHECK START **/
        if (cursor == null){
            Logger.debug("onTreeeCreate | Cursor went null onTreeeCreate");
            inventoryClickEvent.setCancelled(true);
            return;
        }

        if (cursor.getType() == null){
            Logger.debug("onTreeeCreate | Cursor getType() went null onTreeeCreate");
            inventoryClickEvent.setCancelled(true);
            return;
        }

        if(clicked == null){
            Logger.debug("onTreeeCreate | Clicked went null onTreeeCreate");
            inventoryClickEvent.setCancelled(true);
            return;
        }

        if(target == null){
            Logger.debug("onTreeeCreate | Target went null onTreeeCreate");
            inventoryClickEvent.setCancelled(true);
            return;
        }
        /* Notice: NULL CHECK END **/


        /*
         * Notice: Check if player click ACACIA LOG
         */
        if (clicked.getType() == Material.ACACIA_LOG
                && clicked.getItemMeta().getDisplayName().startsWith("Acacia", 2)
                && inventoryClickEvent.getSlot() == 0) {
            Logger.debug("onTreeeCreate | " + target.getDisplayName() + " clicked Acacia Log.");

            TargetBlockInfo blockInfo = target.getTargetBlockInfo(10);
            Location relativeBlock = blockInfo.getRelativeBlock().getLocation();

            target.getWorld().generateTree(relativeBlock, TreeType.ACACIA);

            target.closeInventory();

            Logger.debug("onTreeeCreate | Target clicked and spawned a " + clicked.getItemMeta().getDisplayName() + ".");
            Lang.send(target,Lang.TREEE_SPAWNER_ACACIA.replace("{getToolName}", clicked.getItemMeta().getDisplayName()));

            /* Notice: Remove Treee Spawner Tool from inventory */
            target.getInventory().getItemInMainHand().setAmount(target.getInventory().getItemInMainHand().getAmount() - 1);

            return;
        }
    }
}