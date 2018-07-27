package io.github.wulkanowy.api.messages

import com.google.gson.JsonParseException
import io.github.wulkanowy.api.VulcanException

internal class BadRequestException(message: String, e: JsonParseException) : VulcanException(message, e)
