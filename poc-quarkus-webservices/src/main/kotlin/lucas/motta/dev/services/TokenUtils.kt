package lucas.motta.dev.services

import org.eclipse.microprofile.jwt.Claims
import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.NumericDate
import java.io.InputStream
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*

object TokenUtils {

    fun generateTokenByCredentials(roleString: String, username: String): String {
        val claims = JwtClaims().apply {
            issuer = "app"
            subject = username
            setClaim("role", roleString)
        }
        val token = generateTokenString(claims)
        return token
    }

    private fun generateTokenString(claims: JwtClaims): String {
        val privateKey = readPrivateKey("/privateKey.pem")
        return generateTokenString(privateKey, "privateKey.pem", claims)
    }

    private fun generateTokenString(
        privateKey: PrivateKey, kid: String, claims: JwtClaims
    ): String {
        return JsonWebSignature().apply {
            payload = claims.toJson()
            key = privateKey
            keyIdHeaderValue = kid
            setHeader("typ", "JWT")
            algorithmHeaderValue = AlgorithmIdentifiers.RSA_USING_SHA256
        }.compactSerialization
    }

    /**
     * Read a PEM encoded private key from the classpath
     */
    private fun readPrivateKey(pemResName: String): PrivateKey {
        val contentIS: InputStream = TokenUtils::class.java.getResourceAsStream(pemResName)
            ?: throw IllegalArgumentException("Resource not found: $pemResName")
        val pemContent = contentIS.bufferedReader().use { it.readText() }
        return decodePrivateKey(pemContent)
    }

    /**
     * Decode a PEM encoded private key string to an RSA PrivateKey
     */
    private fun decodePrivateKey(pemEncoded: String): PrivateKey {
        val encodedBytes = pemEncoded.toEncodedBytes()
        val keySpec = PKCS8EncodedKeySpec(encodedBytes)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePrivate(keySpec)
    }

    private fun String.toEncodedBytes(): ByteArray {
        val normalizedPem = this.removePemHeaders()
        return Base64.getDecoder().decode(normalizedPem)
    }

    private fun String.removePemHeaders(): String {
        return this.replace("-----BEGIN (.*)-----".toRegex(), "")
            .replace("-----END (.*)-----".toRegex(), "")
            .replace("\r\n", "")
            .replace("\n", "")
            .trim()
    }

    /**
     * @return the current time in seconds since epoch
     */
    private fun currentTimeInSecs(): Int {
        return (System.currentTimeMillis() / 1000).toInt()
    }
}

