/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package dochub.database.schema.admin.rbac.type

import dochub.database.column.IEnumWithId

/**
 * Enum to represent the scopes that can be accessed.
 *
 * A scope can be any concept: a database table, a REST endpoint, a UI element, etc.
 * Is up to the designer to define what a scope is, and act accordingly when its
 * associated RBAC rule is verified.
 *
 * In conjunction with [RbacAccessLevel], this enum defines the access level to a scope.
 *
 * @property id The id of the RBAC scope.
 *
 * @see [RbacAccessLevel]
 */
public enum class RbacScope(override val id: Int) : IEnumWithId {
    /** RBAC dashboard, allows access to RBAC administrative functions. */
    RBAC_DASHBOARD(id = 100),

    /** System admin scope, allows access to system-wide settings. */
    SYSTEM_ADMIN(id = 101),

    /** Allows access to documents. */
    DOCUMENT(id = 102)
}
