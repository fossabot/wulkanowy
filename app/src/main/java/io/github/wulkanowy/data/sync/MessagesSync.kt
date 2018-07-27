package io.github.wulkanowy.data.sync

import io.github.wulkanowy.api.Vulcan
import io.github.wulkanowy.api.messages.Messages
import io.github.wulkanowy.data.db.dao.entities.DaoSession
import io.github.wulkanowy.data.db.dao.entities.Message
import io.github.wulkanowy.data.db.dao.entities.MessageDao
import io.github.wulkanowy.data.db.shared.SharedPrefContract
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import io.github.wulkanowy.api.messages.Message as ApiMessage

@Singleton
class MessagesSync @Inject constructor(private val daoSession: DaoSession, private val sharedPref: SharedPrefContract, private val vulcan: Vulcan) {

    fun syncAllMessagesHeaders() {
        syncMessages(vulcan.messages.received, Messages.RECEIVED_FOLDER)
        syncMessages(vulcan.messages.sent, Messages.SENT_FOLDER)
    }

    fun syncMessageById(id: Int) {
        val message = daoSession.messageDao.queryBuilder().where(
                MessageDao.Properties.RealId.eq(id),
                MessageDao.Properties.UserId.eq(sharedPref.currentUserId)
        ).uniqueOrThrow()

        val apiMessage = vulcan.messages.getMessage(message.messageID, message.folderId)
        message.content = apiMessage.content
        message.realId = apiMessage.id
        message.update()
    }

    fun syncAllFirstMessagesFromSenders() {
        val senderList = daoSession.messageDao.queryBuilder()
                .where(MessageDao.Properties.UserId.eq(sharedPref.currentUserId))
                .list()
        senderList.sortByDescending { it.date }

        senderList.groupBy { it.senderID }.map {
            val m = it.value.first()
            val apiMessage = vulcan.messages.getMessage(m.messageID, m.folderId)
            m.content = apiMessage.content
            m.realId = apiMessage.id
            m.update()
        }
    }

    private fun syncMessages(messages: List<ApiMessage>, folder: Int) {
        val messageList = messages.map {
            val fromDb = getMessageById(it.id)
            if (fromDb != null) {
                fromDb.unread = it.unread
                return@map Message()
            }

            Message(null, sharedPref.currentUserId, it.id, it.messageID ?: it.id,
                    it.senderID, it.sender, it.unread, it.date, it.content, it.subject, folder)
        }.filter { it.subject != null }

        daoSession.messageDao.insertInTx(messageList)
        Timber.d("Messages synchronization complete (%s)", messageList.size)
    }

    private fun getMessageById(id: Int): Message? {
        return daoSession.messageDao.queryBuilder().where(
                MessageDao.Properties.RealId.eq(id),
                MessageDao.Properties.UserId.eq(sharedPref.currentUserId)
        ).unique()
    }
}
