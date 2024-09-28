/*
 * Copyright (c) 2024-Present Perracodex. Use of this source code is governed by an MIT license.
 */

package kdoc.core.settings.parser

import kdoc.core.settings.annotation.ConfigurationAPI
import kotlin.reflect.KClass

/**
 * Represents a mapping between a configuration path in the configuration file
 * and a corresponding data class to which the configuration values will be mapped.
 *
 * Each instance of this class defines how a specific section of the configuration
 * file is mapped to a property within the [IConfigCatalog].
 *
 * @property keyPath The hierarchical key-path in the configuration file from which to parse, (e.g., `"ktor.deployment"`).
 * @property catalogProperty The property name in the [IConfigCatalog] implementation.
 * @property propertyClass The [catalogProperty] class to instantiate.
 *
 * @see [IConfigCatalog]
 * @see [IConfigCatalogSection]
 */
@ConfigurationAPI
internal data class ConfigClassMap<T : IConfigCatalogSection>(
    val keyPath: String,
    val catalogProperty: String,
    val propertyClass: KClass<T>
)
