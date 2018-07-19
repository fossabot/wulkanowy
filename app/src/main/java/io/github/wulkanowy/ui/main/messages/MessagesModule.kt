package io.github.wulkanowy.ui.main.messages

import dagger.Binds
import dagger.Module

@Module
abstract class MessagesModule {

    @Binds
    internal abstract fun provideMessagesPresenter(messagesPresenter: MessagesPresenter): MessagesContract.Presenter
}
