package io.github.wulkanowy.api.messages

import com.google.gson.Gson
import com.google.gson.JsonParseException
import io.github.wulkanowy.api.Client
import io.github.wulkanowy.api.NotLoggedInErrorException
import org.slf4j.LoggerFactory

class Messages(private val client: Client) {

    companion object {

        private val logger = LoggerFactory.getLogger(Messages::class.java)

        const val RECEIVED_FOLDER = 1
        const val SENT_FOLDER = 2
        const val DELETED_FOLDER = 3

        private const val ERROR_TITLE = "Błąd strony"

        private const val BASE_URL = "{schema}://uonetplus-uzytkownik.{host}/{symbol}/"

        private const val LIST_BASE_URL = BASE_URL + "Wiadomosc.mvc/"
        private const val RECEIVED_URL = LIST_BASE_URL + "GetWiadomosciOdebrane"
        private const val SENT_URL = LIST_BASE_URL + "GetWiadomosciWyslane"
        private const val DELETED_URL = LIST_BASE_URL + "GetWiadomosciUsuniete"
        private const val MESSAGE_URL = LIST_BASE_URL + "GetTrescWiadomosci"

        private const val SEND_BASE_URL = BASE_URL + "NowaWiadomosc.mvc/"
        private const val USER_UNITS_URL = SEND_BASE_URL + "GetJednostkiUzytkownika"
        private const val RECIPIENTS_URL = SEND_BASE_URL + "GetAdresaci?Rola=2&IdJednostkaSprawozdawcza="
    }

    val received: List<Message>
        get() = getMessages(RECEIVED_URL)

    val sent: List<Message>
        get() = getMessages(SENT_URL).map {
            val messageRecipients = it.recipients.split("[").last().split("]").first().trim()

            val user = recipients.single {
                it.name.split("[").last().split("]").first().trim() == messageRecipients
            }

            it.messageID = it.id
            it.userName = user.name
            it.userId = user.loginId

            it
        }

    val deleted: List<Message>
        get() = getMessages(DELETED_URL)

    val units by lazy { getUserUnits() }

    val recipients by lazy { getUserRecipients() }

    private fun getMessages(url: String): List<Message> {
        val res = client.getJsonStringByUrl(url)

        try {
            return Gson().fromJson(res, MessagesContainer::class.java).data
        } catch (e: JsonParseException) {
            if (res.contains(ERROR_TITLE)) {
                throw BadRequestException(ERROR_TITLE, e)
            }

            throw NotLoggedInErrorException("You are probably not logged in", e)
        }
    }

    fun getMessage(id: Int, folder: Int): Message {
        val res = client.postJsonStringByUrl(MESSAGE_URL,
                arrayOf(arrayOf("idWiadomosc", id.toString()), arrayOf("Folder", folder.toString()))
        )

        try {
            return Gson().fromJson(res, MessageContainer::class.java).data
        } catch (e: JsonParseException) {
            if (res.contains(ERROR_TITLE)) {
                throw BadRequestException(ERROR_TITLE, e)
            }

            throw NotLoggedInErrorException("You are probably not logged in. Force login", e)
        }
    }

    private fun getUserUnits(): List<Unit> {
        val res = client.getJsonStringByUrl(USER_UNITS_URL)

        try {
            return Gson().fromJson(res, UnitsContainer::class.java).data
        } catch (e: JsonParseException) {
            if (res.contains(ERROR_TITLE)) {
                throw BadRequestException(ERROR_TITLE, e)
            }

            throw NotLoggedInErrorException("You are probably not logged in. Force login", e)
        }
    }

    private fun getUserRecipients(): List<Recipient> {
        return units.map {
            val res = client.getJsonStringByUrl(RECIPIENTS_URL + it.reportingUnitId)

            try {
                return Gson().fromJson(res, RecipientsContainer::class.java).data
            } catch (e: JsonParseException) {
                if (res.contains(ERROR_TITLE)) {
                    throw BadRequestException(ERROR_TITLE, e)
                }

                throw NotLoggedInErrorException("You are probably not logged in. Force login", e)
            }
        }
    }

    private class MessagesContainer {
        lateinit var data: List<Message>
    }

    private class MessageContainer {
        lateinit var data: Message
    }

    private class UnitsContainer {
        lateinit var data: List<Unit>
    }

    private class RecipientsContainer {
        lateinit var data: List<Recipient>
    }
}
