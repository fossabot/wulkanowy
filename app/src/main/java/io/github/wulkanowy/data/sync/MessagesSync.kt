package io.github.wulkanowy.data.sync

import io.github.wulkanowy.api.Vulcan
import io.github.wulkanowy.api.messages.Messages
import io.github.wulkanowy.api.messages.Message as ApiMessage
import io.github.wulkanowy.data.db.dao.entities.DaoSession
import io.github.wulkanowy.data.db.dao.entities.Message
import io.github.wulkanowy.data.db.dao.entities.MessageDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MessagesSync @Inject constructor(private val daoSession: DaoSession, private val vulcan: Vulcan) {

    fun syncAllMessagesHeaders() {
        daoSession.messageDao.deleteAll()
        syncMessages(vulcan.messages.received, Messages.RECEIVED_FOLDER)
        syncMessages(vulcan.messages.deleted, Messages.DELETED_FOLDER)
        syncMessages(vulcan.messages.sent, Messages.SENT_FOLDER)
    }

    fun syncAllMessages() {
        daoSession.messageDao.deleteAll()
        syncMessagesContent(vulcan.messages.received, Messages.RECEIVED_FOLDER)
        syncMessagesContent(vulcan.messages.deleted, Messages.DELETED_FOLDER)
        syncMessagesContent(vulcan.messages.sent, Messages.SENT_FOLDER)
    }

    private fun syncMessages(messages: List<ApiMessage>, folder: Int) {
        daoSession.messageDao.insertInTx(messages.map {
            Message(null, it.id, it.messageID ?: it.id, it.senderID, it.sender, it.unread, it.date, it.content, it.subject, folder)
        })
    }

    private fun syncMessagesContent(messages: List<ApiMessage>, folder: Int) {
        daoSession.messageDao.insertInTx(messages.map {
            val message = vulcan.messages.getMessage(it.messageID ?: it.id, folder)
            Message(null, message.id, it.messageID ?: it.id, it.senderID, it.sender, it.unread, it.date, message.content, it.subject, folder)
        })
    }

    fun syncMessage(id: Int, folder: Int) {
        val dbMessage = daoSession.messageDao.queryBuilder().where(MessageDao.Properties.Id.eq(id)).unique()
        val apiMessage = vulcan.messages.getMessage(dbMessage.messageID, folder)
        dbMessage.content = apiMessage.content
        dbMessage.realId = apiMessage.id
        dbMessage.update()
    }
}
