package io.github.wulkanowy.data.repositories

import android.content.Context
import android.content.res.Resources
import io.github.wulkanowy.R
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ResourceRepository @Inject constructor(context: Context) {

    private val resources: Resources = context.resources

    fun getSymbolsKeysArray(): Array<String> {
        return resources.getStringArray(R.array.symbols)
    }

    fun getSymbolsValuesArray(): Array<String> {
        return resources.getStringArray(R.array.symbols_values)
    }
}