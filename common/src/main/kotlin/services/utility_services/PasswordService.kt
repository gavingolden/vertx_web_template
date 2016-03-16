package services.utility_services

import org.apache.logging.log4j.LogManager
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.util.encoders.Hex
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.Security

object PasswordService {
    private val logger = LogManager.getLogger(PasswordService::class)
    private val random = SecureRandom();

    init {
        Security.addProvider(BouncyCastleProvider())
    }

    final fun saltAndDigest(plainTextPW: String, size: Int = 64): DigestResult {

        val md = MessageDigest.getInstance("SHA-512", "BC")

        /**
         * hash(salt + password)
         */
        val rawSalt = getRandomBytes(size)

        val rawDigest = md.digest(rawSalt + plainTextPW.toByteArray())

        assert(rawSalt.size == size)
        assert(rawDigest.size == size)

        return DigestResult(Hex.toHexString(rawDigest), Hex.toHexString(rawSalt));
    }

    /**
     * @param plainTextPW in raw english
     * @param hexedSalt in hexed form
     */
    final fun digest(plainTextPW: String, hexedSalt: String): String {

        val md = MessageDigest.getInstance("SHA-512", "BC")

        val newDigest = md.digest(Hex.decode(hexedSalt) + plainTextPW.toByteArray())

        return Hex.toHexString(newDigest);
    }

    /**
     * Verify a password against an existing digest and salt.
     * @return true if the password matches the existing digest
     */
    final fun verified(passwordAttempt: String, hexedDigest: String, hexedSalt: String): Boolean {
        return digest(passwordAttempt, hexedSalt).equals(hexedDigest)
    }

    private fun getRandomBytes(size: Int): ByteArray {
        var bytes = ByteArray(size)
        random.nextBytes(bytes);
        return bytes;
    }
}

//private data class Hash(val size: Int, var hashResult: HashResult)

data class DigestResult(val hexDigest: String, val hexSalt: String)