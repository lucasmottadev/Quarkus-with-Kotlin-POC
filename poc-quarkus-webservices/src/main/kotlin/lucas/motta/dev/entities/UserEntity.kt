package lucas.motta.dev.entities

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import kotlinx.serialization.Serializable

@Entity
class UserEntity : PanacheEntity() {

    @Column(nullable = false)
    lateinit var name: String

    @Column(nullable = false, unique = true)
    lateinit var username: String

    @Column(nullable = false)
    lateinit var password: String

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    lateinit var role: Role

    companion object {
        fun checkPassword(storedPassword: String, rawPassword: String): Boolean {
            return storedPassword == rawPassword
        }
    }
}
@Serializable
enum class Role {
    ADMIN, USER, GUEST
}