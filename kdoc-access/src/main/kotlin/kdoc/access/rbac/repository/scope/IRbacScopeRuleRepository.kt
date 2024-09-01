/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdoc.access.rbac.repository.scope

import kdoc.access.rbac.entity.scope.RbacScopeRuleEntity
import kdoc.access.rbac.entity.scope.RbacScopeRuleRequest
import kotlin.uuid.Uuid

/**
 * Repository for [RbacScopeRuleEntity] data.
 * Responsible for managing [RbacScopeRuleRequest] data.
 *
 * @see RbacScopeRuleRequest
 */
internal interface IRbacScopeRuleRepository {

    /**
     * Updates an existing role with the given set of [RbacScopeRuleRequest] entries.
     *
     * All the existing scope rules for the given [roleId] will be replaced by the new ones.
     *
     * @param roleId The id of the role for which the rules are updated.
     * @param scopeRuleRequests The new set of [RbacScopeRuleRequest] entries to set.
     * @return The new number of rows.
     */
    fun replace(roleId: Uuid, scopeRuleRequests: List<RbacScopeRuleRequest>?): Int
}