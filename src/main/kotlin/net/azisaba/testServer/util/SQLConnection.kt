package net.azisaba.testServer.util

import net.azisaba.testServer.TestServer
import org.intellij.lang.annotations.Language
import xyz.acrylicstyle.sql.Sequelize
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import java.util.Properties

class SQLConnection(host: String, user: String, password: String): Sequelize("jdbc:mysql://$host", user, password) {
    companion object {
        fun logSql(s: String) {
            TestServer.instance.logger.info("Executing SQL: $s")
        }

        fun logSql(s: String, params: Array<out Any>) {
            TestServer.instance.logger.info("Executing SQL: '$s' with params: ${params.toList()}")
        }

        fun Statement.executeAndLog(@Language("SQL") sql: String): Boolean {
            logSql(sql)
            return this.execute(sql)
        }
    }

    fun isConnected() =
        try {
            connection != null && !connection.isClosed && connection.isValid(1000)
        } catch (e: SQLException) {
            false
        }

    fun connect(properties: Properties) {
        if (isConnected()) return
        this.authenticate(getMariaDBDriver(), properties)
    }

    fun execute(@Language("SQL") sql: String, vararg params: Any): Boolean {
        logSql(sql, params)
        val statement = connection.prepareStatement(sql)
        params.forEachIndexed { index, any -> statement.setObject(index + 1, any) }
        return statement.execute()
    }

    fun executeQuery(@Language("SQL") sql: String, vararg params: Any): ResultSet {
        logSql(sql, params)
        val statement = connection.prepareStatement(sql)
        params.forEachIndexed { index, any -> statement.setObject(index + 1, any) }
        return statement.executeQuery()
    }
}
