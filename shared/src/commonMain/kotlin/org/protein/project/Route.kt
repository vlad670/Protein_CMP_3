package org.protein.project

sealed interface Route {
    data object ProteinList : Route
    data class ProteinDetail(val accession: String) : Route
}
