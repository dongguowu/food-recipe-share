package com.dishdiscoverers.core.domain.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
class LoggedInUser(val userId: String, val displayName: String)