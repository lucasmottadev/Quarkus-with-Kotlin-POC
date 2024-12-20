package lucas.motta.dev.resources

import io.quarkus.security.Authenticated
import io.smallrye.jwt.build.impl.JwtBuildUtils
import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.*
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import kotlinx.serialization.Serializable
import lucas.motta.dev.entities.Role
import lucas.motta.dev.entities.UserCreate
import lucas.motta.dev.entities.UserEntity
import lucas.motta.dev.repositories.UserRepository
import lucas.motta.dev.services.TokenUtils
import lucas.motta.dev.services.UserService


@Serializable
data class Credentials(val username: String, val password: String)


@Path("/api/auth")
class MainResource {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var userService: UserService


    //    Este endpoint é usado para criação de novos usuários para testar regras.
    //    Ele lógicamente não seria público em uma api real
    @Path("/test/create-user")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun testCreateUser(user: UserCreate): Response {


        val token = userService.createUser(user) ?: return Response.status(Response.Status.BAD_REQUEST).build()

        return Response.status(Response.Status.CREATED).entity(token).build()

    }


    @Path("/create-user")
    @POST
    @RolesAllowed("admin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun createUser(user: UserCreate): Response {
        val token = userService.createUser(user) ?: return Response.status(Response.Status.BAD_REQUEST).build()

        return Response.status(Response.Status.CREATED).entity(token).build()

    }

    @Path("/login")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    fun authenticate(credentials: Credentials): Response {

        val user = userRepository.findByUsername(credentials.username)
            ?: return Response.status(Response.Status.UNAUTHORIZED).build()

        val token = TokenUtils.generateTokenByCredentials(credentials.username, user.role.name)
        return Response.ok(token).build()

    }

    @Authenticated
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    fun testAuth(): Response {
        return Response.ok("Endpoint autenticado").build()
    }

    @Path("/admin")
    @GET
    @RolesAllowed("admin")
    @Produces(MediaType.TEXT_PLAIN)
    fun testAuthAdmin(): Response {
        return Response.ok("Endpoint liberado apenas ao ADMIN").build()
    }

}

