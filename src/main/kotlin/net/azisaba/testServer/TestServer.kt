package net.azisaba.testServer

import net.azisaba.testServer.commands.CreateDatabaseCommand
import net.azisaba.testServer.util.SQLConnection
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import java.sql.SQLException
import java.util.Properties
import java.util.Timer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicLong
import kotlin.concurrent.scheduleAtFixedRate

class TestServer: JavaPlugin() {
    companion object {
        lateinit var instance: TestServer
        lateinit var connection: SQLConnection
        private val sqlPoolNum = AtomicLong()
        val sqlPool: ExecutorService = Executors.newCachedThreadPool { Thread(it).apply { name = "TestServer SQL Pool #${sqlPoolNum.getAndIncrement()}" } }
    }

    private val timer = Timer()

    init {
        instance = this
    }

    override fun onEnable() {
        logger.info("Connecting to database")
        connection = SQLConnection(
            config.getString("database.host")!!,
            config.getString("database.user")!!,
            config.getString("database.password")!!,
        )
        val props = Properties()
        props.setProperty("verifyServerCertificate", "false")
        props.setProperty("useSSL", config.getString("database.ssl") ?: "true")
        connection.connect(props)
        logger.info("Connected to database")
        timer.scheduleAtFixedRate(1000 * 60 * 5L, 1000 * 60 * 5L) {
            try {
                connection.execute("SELECT 1")
            } catch (e: SQLException) {
                logger.warning("Failed to execute keep-alive ping")
                e.printStackTrace()
            }
        }
        Bukkit.getPluginCommand("createdatabase")?.setExecutor(CreateDatabaseCommand)
        Bukkit.getPluginCommand("createdatabase")?.tabCompleter = CreateDatabaseCommand
        logger.info("Enabled TestServer")
    }

    override fun onDisable() {
        timer.cancel()
        sqlPool.shutdownNow()
        sqlPool.awaitTermination(1, TimeUnit.MINUTES)
    }
}
