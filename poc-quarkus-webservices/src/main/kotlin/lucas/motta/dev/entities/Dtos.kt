package lucas.motta.dev.entities

import kotlinx.serialization.Serializable


@Serializable
data class UserCreate(val username: String, val password: String, val role: Role)
