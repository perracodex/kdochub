/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package dochub.access.domain.rbac.model.role

import dochub.access.domain.rbac.model.scope.RbacScopeRule
import dochub.access.domain.rbac.model.scope.RbacScopeRuleRequest
import kotlinx.serialization.Serializable

/**
 * Represents the request to create/update a concrete RBAC Role.
 *
 * @property roleName The unique role name.
 * @property description Optional description of the role.
 * @property isSuper Whether this is a super-role, in which case it has all permissions granted.
 * @property scopeRules The list of [RbacScopeRule] entries for the role.
 */
@Serializable
public data class RbacRoleRequest internal constructor(
    val roleName: String,
    val description: String? = null,
    val isSuper: Boolean,
    val scopeRules: List<RbacScopeRuleRequest>? = null
)
