package io.github.wulkanowy.api.messages;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import io.github.wulkanowy.api.Client;
import io.github.wulkanowy.api.FixtureHelper;
import io.github.wulkanowy.api.NotLoggedInErrorException;

public class MessagesTest {

    private Client getFixtureAsString(String fixtureFileName) throws Exception {
        Client client = Mockito.mock(Client.class);
        Mockito.when(client.getJsonStringByUrl(Mockito.anyString()))
                .thenReturn(FixtureHelper.getAsString(getClass().getResourceAsStream(fixtureFileName)));
        Mockito.when(client.postJsonStringByUrl(Mockito.anyString(), Mockito.any()))
                .thenReturn(FixtureHelper.getAsString(getClass().getResourceAsStream(fixtureFileName)));
        return client;
    }

    @Test
    public void getMessages() throws Exception {
        Client client = getFixtureAsString("GetWiadomosciOdebrane.json");

        Messages messages = new Messages(client);
        List<Message> messageList = messages.getReceived();

        Assert.assertEquals(true, messageList.get(1).getUnread());
        Assert.assertEquals("2016-03-15 09:00:00", messageList.get(0).getDate());
        Assert.assertEquals(null, messageList.get(0).getContent());
        Assert.assertEquals("Kowalski Jan", messageList.get(0).getSender());
        Assert.assertEquals(12347, messageList.get(2).getId());
    }

    @Test
    public void getMessagesEmpty() throws Exception {
        Client client = getFixtureAsString("GetWiadomosciUsuniete-empty.json");

        Messages messages = new Messages(client);
        List<Message> messageList = messages.getSent();

        Assert.assertTrue(messageList.isEmpty());
    }

    @Test(expected = NotLoggedInErrorException.class)
    public void getMessagesError() throws Exception {
        Client client = getFixtureAsString("UndefinedError.txt");

        Messages messages = new Messages(client);
        messages.getDeleted();
    }

    @Test(expected = BadRequestException.class)
    public void getMessagesBadRequest() throws Exception {
        Client client = getFixtureAsString("PageError.html");

        Messages messages = new Messages(client);
        messages.getDeleted();
    }

    @Test
    public void getMessage() throws Exception {
        Client client = getFixtureAsString("GetTrescWiadomosci.json");

        Messages messages = new Messages(client);
        Message message = messages.getMessage(123, Messages.RECEIVED_FOLDER);
        Assert.assertEquals(12345, message.getId());
        Assert.assertEquals("Witam, …. \nPozdrawiam Krzysztof Czerkas", message.getContent());
    }

    @Test(expected = NotLoggedInErrorException.class)
    public void getMessageError() throws Exception {
        Client client = getFixtureAsString("UndefinedError.txt");

        Messages messages = new Messages(client);
        messages.getMessage(321, Messages.SENT_FOLDER);
    }

    @Test(expected = BadRequestException.class)
    public void getMessageBadRequest() throws Exception {
        Client client = getFixtureAsString("PageError.html");

        Messages messages = new Messages(client);
        messages.getMessage(1, Messages.DELETED_FOLDER);
    }
}
