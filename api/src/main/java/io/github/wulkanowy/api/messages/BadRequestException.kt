package io.github.wulkanowy.api.messages

import io.github.wulkanowy.api.VulcanException

internal class BadRequestException(message: String) : VulcanException(message)
