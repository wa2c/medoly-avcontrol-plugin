package com.wa2c.android.medoly.plugin.action.avcontrol.ui.component

import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

/**
 * Navigate with double tap countermeasure
 */
fun Fragment.navigateSafe(directions: NavDirections) {
    val navController = findNavController()
    val actionId = directions.actionId
    val destinationId = navController.currentDestination?.getAction(actionId)?.destinationId
        ?: navController.graph.getAction(actionId)?.destinationId
    if (destinationId != null && navController.currentDestination?.id != destinationId) {
        navController.navigate(directions)
    }
}
