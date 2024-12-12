/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdochub.server.health.checks

import kdochub.core.env.HealthCheckApi
import kdochub.core.security.snowflake.SnowflakeData
import kdochub.core.security.snowflake.SnowflakeFactory
import kotlinx.serialization.Serializable

/**
 * A health check that generates a snowflake id and parses it.
 *
 * @property errors A list of errors that occurred during the check.
 * @property testId A generated snowflake id at the time of the check.
 * @property testResult The parsed snowflake data from the testId.
 * @property timestampEpoch The timestamp epoch used to generate the snowflake id.
 * @property nanoTimeStart The nano time start used to generate the snowflake id.
 */
@HealthCheckApi
@Serializable
public data class SnowflakeHealth private constructor(
    val errors: MutableList<String>,
    var testId: String?,
    var testResult: SnowflakeData?,
    val timestampEpoch: Long,
    val nanoTimeStart: Long,
) {
    internal constructor() : this(
        errors = mutableListOf(),
        testId = null,
        testResult = null,
        timestampEpoch = SnowflakeFactory.timestampEpoch,
        nanoTimeStart = SnowflakeFactory.nanoTimeStart
    )

    init {
        // Attempt to generate testId and handle any exceptions.
        try {
            val generatedId: String = SnowflakeFactory.nextId()
            testId = generatedId
            testResult = SnowflakeFactory.parse(id = generatedId)
        } catch (ex: Exception) {
            errors.add(
                "${SnowflakeHealth::class.simpleName}: ${ex.message} - " +
                        "timestampEpoch: $timestampEpoch, nanoTimeStart: $nanoTimeStart."
            )
            // If any step fails, assign null to both to ensure consistency.
            testId = null
            testResult = null
        }
    }
}