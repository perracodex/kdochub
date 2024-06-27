/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

import kdoc.base.utils.TestUtils
import org.jetbrains.exposed.sql.Schema
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class SchemaJoinTest {

    @BeforeTest
    fun setUp() {
        TestUtils.loadSettings()
        TestUtils.setupDatabase()
    }

    @AfterTest
    fun tearDown() {
        TestUtils.tearDown()
    }

    /**
     * Test schema changes timing.
     * - Create 20 different schemas.
     * - Change between schemas 500 times, that would be equivalent to change
     *   between schemas 10_000 times within the scope of a single request call.
     */
    @Test
    fun testMultipleSchemaChangesTiming() {
        val schemas: List<Schema> = List(size = 20) { Schema(name = "SCHEMA_$it") } // Create 10 different schemas.

        val startTime: Long = System.nanoTime() // Start time measurement.
        var tested = 0

        transaction {
            schemas.forEach { schema ->
                SchemaUtils.createSchema(schema)
            }

            repeat(times = 500) {
                schemas.forEach { schema ->
                    tested++
                    println("Testing schema: ${schema.identifier}")
                    SchemaUtils.setSchema(schema = schema)
                    assertEquals(expected = schema.identifier, actual = TransactionManager.current().connection.schema)
                }
            }
        }

        val endTime: Long = System.nanoTime() // End time measurement.
        val duration: Long = (endTime - startTime) / 1_000_000 // Convert nanoseconds to milliseconds.
        println("Total time for schema changes: ${duration}ms. Tested $tested schema changes.")
    }
}
