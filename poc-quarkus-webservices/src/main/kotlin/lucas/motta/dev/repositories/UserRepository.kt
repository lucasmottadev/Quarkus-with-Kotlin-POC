package lucas.motta.dev.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped
import lucas.motta.dev.entities.UserEntity

@ApplicationScoped
class UserRepository: PanacheRepository<UserEntity> {

    fun findByUsername(username: String) = find("username", username).firstResult()

}