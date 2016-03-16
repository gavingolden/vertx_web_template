package services.utility_services

import objects.table_types.User
import org.apache.commons.lang3.RandomStringUtils
import org.joda.time.DateTime
import java.util.*

object RandDataService {

    private val firstNames = arrayListOf("Rick", "Felicia", "Elliott", "Cassandra", "Denita", "Joette", "Tara", "Bridget", "Coleman", "Nicholle", "Mitchell", "Della", "Hai", "Van", "Otto", "Trinh", "Danette", "Madelene", "Howard", "Randa", "Yuko", "Cassey", "Sophie", "Theda", "Leatrice", "Marietta", "Danille", "Kenny", "Synthia", "Casimira", "Moon", "Len", "Alanna", "Latoria", "Analisa", "Lin", "Ozie", "Pauline", "Collene", "Dominique", "Veronika", "Leisha", "Genoveva", "Duane", "Kirk", "Betsy", "Alexander", "Zandra", "Christy", "Temple")
    private val lastNames = arrayListOf("Neill", "Ayoub", "Elston", "Jezierski", "Hopkin", "Bergan", "Vandam", "Millman", "Troiano", "Raisor", "Fester", "Langan", "Dubreuil", "Wortham", "Joye", "Tankersley", "Tuley", "Ehrlich", "Kenton", "Stransky", "Magee", "Cudd", "Conover", "Steffes", "Oyola", "Hirano", "Kingsland", "Click", "Mcgahee", "Appel", "Trembath", "Sauro", "Lastra", "Schlosser", "Toon", "Rodrique", "Harvard", "Burden", "Quinton", "Tift", "Ronco", "Bethea", "Dau", "Alkire", "Bejarano", "Eisele", "Wismer", "Rabalais", "Reichard", "Gunderson")

    fun randomNames(): HumanName {
        val random = Random(DateTime.now().millis)
        return HumanName(firstNames[random.nextInt(firstNames.size)], lastNames[random.nextInt(lastNames.size)])
    }

    fun randomTestUser(createRandomPassword: Boolean = false): User {
        val TEST_PREFIX = "__TEST__"
        val (first, last) = RandDataService.randomNames()
        val password = if(createRandomPassword) { RandomStringUtils.random(16) } else "Password123"
        return User(
                firstname = "$TEST_PREFIX$first", lastname = "$TEST_PREFIX$last", username = "$first-$last".toLowerCase(), password = password
        )
    }

    data class HumanName(val first: String, val last: String)
}