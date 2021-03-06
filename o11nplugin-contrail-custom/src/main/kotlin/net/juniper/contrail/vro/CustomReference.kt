/*
 * Copyright (c) 2018 Juniper Networks, Inc. All rights reserved.
 */

package net.juniper.contrail.vro

import net.juniper.contrail.vro.config.isDisplayableChildOf
import net.juniper.contrail.vro.config.isModelClassName
import net.juniper.contrail.vro.config.referencePatterns
import net.juniper.contrail.vro.config.returnsObjectReferences
import net.juniper.contrail.vro.config.toPluginName
import java.lang.reflect.Method

class CustomReference(val className: String, val methodName: String) {
    val pluginMethodName = methodName.replace(className, className.toPluginName)
}

fun Method.toCustomReference(): CustomReference? {
    if ( ! returnsObjectReferences) return null

    val className = referencePatterns
        .map { it.matchEntire(name) }.filterNotNull()
        .map { it.groupValues[1] }
        .filter { it.isModelClassName }
        .filter { it.isDisplayableChildOf(declaringClass.simpleName) }
        .firstOrNull() ?: return null

    return CustomReference(className, name)
}