/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdoc.database.plugins

import io.ktor.server.application.*
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kdoc.core.settings.AppSettings
import kdoc.database.service.DatabaseService
import org.jetbrains.exposed.sql.Table

/**
 * Custom Ktor plugin to configure the database.
 */
internal val DbPlugin: ApplicationPlugin<DbPluginConfig> = createApplicationPlugin(
    name = "DbPlugin",
    createConfiguration = ::DbPluginConfig
) {
    DatabaseService.init(
        settings = AppSettings.database,
        telemetryRegistry = pluginConfig.telemetryRegistry
    ) {
        pluginConfig.tables.forEach { table ->
            addTable(table)
        }
    }
}

/**
 * Configuration for the [DbPlugin].
 */
internal class DbPluginConfig {
    /** List of tables to be registered with the database. */
    val tables: MutableList<Table> = mutableListOf()

    /** Optional metrics registry for telemetry monitoring. */
    var telemetryRegistry: PrometheusMeterRegistry? = null
}