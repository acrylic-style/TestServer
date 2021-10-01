package net.azisaba.testServer.commands

import net.azisaba.testServer.TestServer
import net.azisaba.testServer.util.Util
import net.md_5.bungee.api.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

object CreateDatabaseCommand: CommandExecutor, TabCompleter {
    @Suppress("SqlResolve")
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        TestServer.sqlPool.execute {
            sender.sendMessage("${ChatColor.GREEN}データベースを作成中...")
            val name = Util.generateRandomCharacters(Util.charPool, 16)
            val password = Util.generateRandomCharacters(Util.charPoolWithSomeSpecialChars, 16)
            try {
                TestServer.connection.execute("create user ?@'%' identified by ?", "user_$name", password)
                TestServer.connection.execute("create database database_$name")
                TestServer.connection.execute("grant all on database_$name.* to user_$name@'%'")
            } catch (e: Exception) {
                sender.sendMessage("${ChatColor.RED}データベースの作成に失敗しました。このスクショを持って統括開発者に泣きつきましょう！")
                e.printStackTrace()
                return@execute
            }
            sender.sendMessage("${ChatColor.GREEN}データベースを作成しました。")
            sender.sendMessage("${ChatColor.GOLD} ホスト名: ${ChatColor.YELLOW}${TestServer.instance.config.getString("database.host")}")
            sender.sendMessage("${ChatColor.GOLD} データベース名: ${ChatColor.YELLOW}database_$name")
            sender.sendMessage("${ChatColor.GOLD} ユーザー名: ${ChatColor.YELLOW}user_$name")
            sender.sendMessage("${ChatColor.GOLD} パスワード: ${ChatColor.YELLOW}$password")
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<String>) =
        emptyList<String>()
}
