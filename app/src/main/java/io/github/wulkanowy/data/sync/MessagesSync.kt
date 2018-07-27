package io.github.wulkanowy.data.sync

import io.github.wulkanowy.api.Vulcan
import io.github.wulkanowy.api.messages.Messages
import io.github.wulkanowy.data.db.dao.entities.DaoSession
import io.github.wulkanowy.data.db.dao.entities.Message
import io.github.wulkanowy.data.db.dao.entities.MessageDao
import io.github.wulkanowy.data.db.shared.SharedPrefContract
import javax.inject.Inject
import javax.inject.Singleton
import io.github.wulkanowy.api.messages.Message as ApiMessage

@Singleton
class MessagesSync @Inject constructor(private val daoSession: DaoSession, private val sharedPref: SharedPrefContract, private val vulcan: Vulcan) {

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
            Message(null, sharedPref.currentUserId, it.id, it.messageID ?: it.id, it.senderID, it.sender, it.unread, it.date, it.content, it.subject, folder)
        })
    }

    private fun syncMessagesContent(messages: List<ApiMessage>, folder: Int) {
        daoSession.messageDao.insertInTx(messages.map {
            val message = vulcan.messages.getMessage(it.messageID ?: it.id, folder)
            Message(null, sharedPref.currentUserId, message.id, it.messageID ?: it.id, it.senderID, it.sender, it.unread, it.date, message.content, it.subject, folder)
        })
    }

    fun syncMessage(id: Int, folder: Int) {
        val dbMessage = daoSession.messageDao.queryBuilder().where(MessageDao.Properties.Id.eq(id)).unique()
        val apiMessage = vulcan.messages.getMessage(dbMessage.messageID, folder)
        dbMessage.content = apiMessage.content
        dbMessage.realId = apiMessage.id
        dbMessage.update()
    }

    fun syncMessagesBySender(senderId: Int) {
        val dbMessages = daoSession.messageDao.queryBuilder().where(
                MessageDao.Properties.SenderID.eq(senderId),
                MessageDao.Properties.UserId.eq(sharedPref.currentUserId)
        ).orderDesc(MessageDao.Properties.Date).list()

        dbMessages.map {
            val apiMessage = vulcan.messages.getMessage(it.messageID, it.folderId)
            it.content = apiMessage.content
            it.realId = apiMessage.id
            it.update()
        }
    }
}
