package lucas.motta.dev.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.Response
import lucas.motta.dev.entities.UserCreate
import lucas.motta.dev.entities.UserEntity
import lucas.motta.dev.repositories.UserRepository

@ApplicationScoped
class UserService {
    @Inject
    lateinit var userRepository: UserRepository

    @Transactional
    fun createUser(user: UserCreate): String? {

        val hasUser = userRepository.findByUsername(user.username)
        if (hasUser != null) {
            return null
        }
        val newUser = UserEntity().apply {
            name = user.username
            username = user.username
            password = user.password
            role = user.role
        }

        userRepository.persist(newUser)

        val token = TokenUtils.generateTokenByCredentials(user.username, user.role.name.uppercase())

        return token
    }

}