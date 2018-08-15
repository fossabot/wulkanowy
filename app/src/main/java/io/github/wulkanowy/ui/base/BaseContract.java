package io.github.wulkanowy.ui.base;

import android.support.annotation.NonNull;

public interface BaseContract {

    interface View {

        void showMessage(@NonNull String text);

        void showNoNetworkMessage();
    }

    interface Presenter<T extends View> {

        void attachView(@NonNull T view);

        void detachView();
    }
}
