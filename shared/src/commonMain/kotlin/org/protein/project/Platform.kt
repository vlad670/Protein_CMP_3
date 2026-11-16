package org.protein.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform