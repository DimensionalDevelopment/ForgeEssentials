package com.forgeessentials.teleport;

import com.forgeessentials.api.permissions.FEPermissions;
import com.forgeessentials.core.commands.ForgeEssentialsCommandBase;
import com.forgeessentials.core.misc.RespawnHandler;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.PlayerInfo;
import com.forgeessentials.util.UserIdent;
import com.forgeessentials.commons.selections.WarpPoint;
import com.forgeessentials.util.teleport.TeleportCenter;
import cpw.mods.fml.common.FMLCommonHandler;
import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.permissions.PermissionContext;
import net.minecraftforge.permissions.PermissionsManager;
import net.minecraftforge.permissions.PermissionsManager.RegisteredPermValue;

import com.forgeessentials.api.permissions.FEPermissions;
import com.forgeessentials.core.commands.ForgeEssentialsCommandBase;
import com.forgeessentials.core.misc.RespawnHandler;
import com.forgeessentials.core.misc.TeleportHelper;
import com.forgeessentials.util.OutputHandler;
import com.forgeessentials.util.PlayerInfo;
import com.forgeessentials.util.UserIdent;
import com.forgeessentials.util.selections.WarpPoint;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommandSpawn extends ForgeEssentialsCommandBase {

	@Override
	public String getCommandName()
	{
		return "spawn";
	}

	@Override
	public void processCommandPlayer(EntityPlayer sender, String[] args)
	{
		if (args.length >= 1)
		{
			if (!PermissionsManager.checkPermission(sender, TeleportModule.PERM_SPAWN_OTHERS))
			{
				throw new CommandException(FEPermissions.MSG_NO_COMMAND_PERM);
			}
			EntityPlayerMP player = UserIdent.getPlayerByMatchOrUsername(sender, args[0]);
			if (player == null)
			{
				throw new CommandException(String.format("Player %s does not exist, or is not online.", args[0]));
			}

			WarpPoint point = RespawnHandler.getPlayerSpawn(player, null);
			if (point == null)
			{
				throw new CommandException("There is no spawnpoint set for that player.");
			}

			PlayerInfo.getPlayerInfo(player.getPersistentID()).setLastTeleportOrigin(new WarpPoint(player));
			OutputHandler.chatNotification(player, "Teleporting to spawn.");
			TeleportHelper.teleport(player, point);
		}
		else if (args.length == 0)
		{
			EntityPlayerMP player = (EntityPlayerMP) sender;

			WarpPoint point = RespawnHandler.getPlayerSpawn(player, null);
			if (point == null)
			{
				throw new CommandException("There is no spawnpoint set for that player.");
			}

			PlayerInfo.getPlayerInfo(player.getPersistentID()).setLastTeleportOrigin(new WarpPoint(player));
			OutputHandler.chatConfirmation(player, "Teleporting to spawn.");
			TeleportHelper.teleport(player, point);
		}
	}

	@Override
	public void processCommandConsole(ICommandSender sender, String[] args)
	{
		if (args.length < 1)
		{
			throw new CommandException(FEPermissions.MSG_NOT_ENOUGH_ARGUMENTS);
		}

		if (!PermissionsManager.checkPermission(new PermissionContext().setCommandSender(sender).setCommand(this), TeleportModule.PERM_SPAWN_OTHERS))
		{
			throw new CommandException(FEPermissions.MSG_NO_COMMAND_PERM);
		}
		EntityPlayerMP player = UserIdent.getPlayerByMatchOrUsername(sender, args[0]);
		if (player == null)
		{
			throw new CommandException(String.format("Player %s does not exist, or is not online.", args[0]));
		}

		WarpPoint point = RespawnHandler.getPlayerSpawn(player, null);
		if (point == null)
		{
			throw new CommandException("There is no spawnpoint set for that player.");
		}

		PlayerInfo.getPlayerInfo(player.getPersistentID()).setLastTeleportOrigin(new WarpPoint(player));
		OutputHandler.chatNotification(player, "Teleporting to spawn.");
		TeleportHelper.teleport(player, point);
	}

	@Override
	public boolean canConsoleUseCommand()
	{
		return true;
	}

	@Override
	public String getPermissionNode()
	{
		return TeleportModule.PERM_SPAWN;
	}

    @Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
	{
		if (args.length == 1)
		{
			return getListOfStringsMatchingLastWord(args, FMLCommonHandler.instance().getMinecraftServerInstance().getAllUsernames());
		}
		else
		{
			return null;
		}
	}

	@Override
	public RegisteredPermValue getDefaultPermission()
	{
		return RegisteredPermValue.TRUE;
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		if (sender instanceof EntityPlayer)
		{
			return "/spawn [player] Teleport you or another player to their spawn point.";
		}
		else
		{
			return "/spawn <player> Teleport a player to their spawn point.";
		}
	}
}
