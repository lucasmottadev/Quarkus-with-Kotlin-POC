package lucas.motta.dev.resources

import io.quarkus.security.Authenticated
import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.validation.Valid
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import lucas.motta.dev.entities.Role
import lucas.motta.dev.entities.UserEntity
import lucas.motta.dev.repositories.UserRepository
import lucas.motta.dev.services.TokenUtils
import lucas.motta.dev.services.TokenUtils.generateTokenByCredentials
import org.jose4j.jwt.JwtClaims


data class Credentials(val username: String, val password: String)
data class UserCreate(val username: String, val password: String, val role: String)

@Path("/api/auth")
class Test {

    @Inject
    lateinit var userRepository: UserRepository

    @Path("/create-user")
    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(user: UserCreate): Response {
        val hasUser = userRepository.findByUsername(user.username)
        if (hasUser != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Usuário já existente")
                .build()
        }

        val newUser = UserEntity().apply {
            name = user.username
            username = user.username
            password = user.password
            role = Role.valueOf(user.role)
        }

        userRepository.persist(newUser)

        val token = generateTokenByCredentials(user.username, user.role)

        return Response.status(Response.Status.CREATED).entity(token).build()

    }

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun authenticate(credentials: Credentials): Response {
        val user = userRepository.findByUsername(credentials.username)
            ?: return Response.status(Response.Status.UNAUTHORIZED).build()

        val token = generateTokenByCredentials(credentials.username, user.role.name)
        return Response.ok(token).build()

    }

    @Path("/auth")
    @Authenticated
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun testAuth(): Response {
        return Response.ok("Endpoint autenticado").build()
    }

    @Path("/auth/admin")
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    fun testAuthAdmin(): Response {
        return Response.ok("Olá admin").build()
    }

}

