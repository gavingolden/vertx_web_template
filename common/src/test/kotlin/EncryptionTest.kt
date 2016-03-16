import org.apache.logging.log4j.LogManager
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.shouldEqual
import org.jetbrains.spek.api.shouldNotEqual
import org.jetbrains.spek.api.shouldThrow
import services.utility_services.PasswordService

class EncryptionTest: Spek() { init {
    val logger = LogManager.getLogger(EncryptionTest::class)
    val password = "Password123";
    given("The password $password") {
        on("salting and hashing using the SHA-256 algorithm") {
            val(originalDigest, salt) = PasswordService.saltAndDigest(password);
            val newDigest = PasswordService.digest(password, salt);
            it("should produce an identical digest after rehashing with the same salt") {
                shouldEqual(originalDigest, newDigest)
            }
        }
    }

    val differentPassword = "password"
    given("The passwords $password and $differentPassword") {
        on("salting and hashing both using the SHA-256 algorithm") {
            val(originalDigest, salt) = PasswordService.saltAndDigest(password);
            val newDigest = PasswordService.digest(differentPassword, salt);
            it("should produce different digests") {
                shouldNotEqual(originalDigest, newDigest)
            }
        }
    }

    /**
     * Use to confirm that assertions are correctly enabled
     */
    given("The passwords $password and $differentPassword") {
        on("salting and hashing both using the SHA-256 algorithm") {
            val(originalDigest, salt) = PasswordService.saltAndDigest(password);
            val newDigest = PasswordService.digest(differentPassword, salt);
            it("should fail") {
                shouldThrow(AssertionError::class.java) {
                    shouldEqual(originalDigest, newDigest)
                }
            }
        }
    }
}}
