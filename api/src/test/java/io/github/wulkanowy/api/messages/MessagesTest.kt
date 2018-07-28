package io.github.wulkanowy.api.messages

import io.github.wulkanowy.api.Client
import io.github.wulkanowy.api.FixtureHelper
import io.github.wulkanowy.api.NotLoggedInErrorException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class MessagesTest {

    private fun getFixtureAsString(fixtureFileName: String): Client {
        val client = Mockito.mock(Client::class.java)
        `when`(client.getJsonStringByUrl(Mockito.anyString()))
                .thenReturn(FixtureHelper.getAsString(javaClass.getResourceAsStream(fixtureFileName)))
        `when`(client.postJsonStringByUrl(Mockito.anyString(), Mockito.any()))
                .thenReturn(FixtureHelper.getAsString(javaClass.getResourceAsStream(fixtureFileName)))
        return client
    }

    @Test fun getMessages() {
        val messageList = Messages(getFixtureAsString("GetWiadomosciOdebrane.json")).received

        assertEquals(true, messageList[1].unread)
        assertEquals("2016-03-15 09:00:00", messageList[0].date)
        assertEquals(null, messageList[0].content)
        assertEquals("Kowalski Jan", messageList[0].userName)
        assertEquals(12347, messageList[2].id)
    }

    @Test fun getMessagesEmpty() {
        assertTrue(Messages(getFixtureAsString("GetWiadomosciUsuniete-empty.json")).sent.isEmpty())
    }

    @Test(expected = NotLoggedInErrorException::class)
    fun getMessagesError() {
        Messages(getFixtureAsString("UndefinedError.txt")).deleted
    }

    @Test(expected = BadRequestException::class)
    fun getMessagesBadRequest() {
        Messages(getFixtureAsString("PageError.html")).deleted
    }

    @Test fun getMessage() {
        val message = Messages(getFixtureAsString("GetTrescWiadomosci.json"))
                .getMessage(123, Messages.RECEIVED_FOLDER)

        assertEquals(12345, message.id)
        assertEquals("Witam, …. \nPozdrawiam Krzysztof Czerkas", message.content)
    }

    @Test(expected = NotLoggedInErrorException::class)
    fun getMessageError() {
        Messages(getFixtureAsString("UndefinedError.txt")).getMessage(321, Messages.SENT_FOLDER)
    }

    @Test(expected = BadRequestException::class)
    fun getMessageBadRequest() {
        Messages(getFixtureAsString("PageError.html")).getMessage(1, Messages.DELETED_FOLDER)
    }
}
