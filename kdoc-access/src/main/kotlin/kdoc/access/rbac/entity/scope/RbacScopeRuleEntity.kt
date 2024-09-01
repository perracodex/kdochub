/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdoc.access.rbac.entity.scope

import kdoc.access.rbac.entity.field.RbacFieldRuleEntity
import kdoc.access.rbac.entity.role.RbacRoleEntity
import kdoc.base.database.schema.admin.rbac.RbacScopeRuleTable
import kdoc.base.database.schema.admin.rbac.types.RbacAccessLevel
import kdoc.base.database.schema.admin.rbac.types.RbacScope
import kdoc.base.persistence.entity.Meta
import kdoc.base.persistence.serializers.UuidS
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.ResultRow

/**
 * Represents a single RBAC scope rule for a concrete [RbacRoleEntity].
 *
 * A scope can be any concept: a database table, a REST endpoint, a UI element, etc.
 * Is up to the designer to define what a scope is, and act accordingly when its
 * associated RBAC rule is verified.
 *
 * @property id The unique id of the scope rule record.
 * @property roleId The associated parent [RbacRoleEntity] id.
 * @property scope The [RbacScope] to which the scope rule belong.
 * @property accessLevel The required [RbacAccessLevel] for the [RbacScope].
 * @property fieldRules Optional list of [RbacFieldRuleEntity] associated with the scope rule.
 * @property meta The metadata of the record.
 */
@Serializable
public data class RbacScopeRuleEntity(
    val id: UuidS,
    val roleId: UuidS,
    val scope: RbacScope,
    val accessLevel: RbacAccessLevel,
    val fieldRules: List<RbacFieldRuleEntity>?,
    val meta: Meta
) {
    internal companion object {
        /**
         * Maps a [ResultRow] to a [RbacScopeRuleEntity] instance.
         *
         * @param row The [ResultRow] to map.
         * @param fieldRules The list of [RbacFieldRuleEntity] to associate with the [RbacScopeRuleEntity].
         * @return The mapped [RbacScopeRuleEntity] instance.
         */
        fun from(row: ResultRow, fieldRules: List<RbacFieldRuleEntity>): RbacScopeRuleEntity {
            return RbacScopeRuleEntity(
                id = row[RbacScopeRuleTable.id],
                roleId = row[RbacScopeRuleTable.roleId],
                scope = row[RbacScopeRuleTable.scope],
                accessLevel = row[RbacScopeRuleTable.accessLevel],
                fieldRules = fieldRules,
                meta = Meta.from(row = row, table = RbacScopeRuleTable)
            )
        }
    }
}