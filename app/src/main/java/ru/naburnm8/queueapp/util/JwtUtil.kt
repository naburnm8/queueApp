package ru.naburnm8.queueapp.util

import android.util.Base64
import org.json.JSONObject

object JwtUtil {
    fun getRoles(token: String): List<String> {
        val parts = token.split(".")
        if (parts.size < 2) return emptyList()

        val payload = String(
            Base64.decode(parts[1], Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING)
        )

        val json = JSONObject(payload)
        val rolesArray = json.optJSONArray("roles") ?: return emptyList()

        return buildList {
            for (i in 0 until rolesArray.length()) {
                add(rolesArray.getString(i))
            }
        }
    }
}