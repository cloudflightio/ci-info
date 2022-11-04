package io.cloudflight.ci.info

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.*

@Serializable
internal class Vendor {

    lateinit var name: String
    lateinit var constant: String

    @Serializable(with = EnvironmentSerializer::class)
    @SerialName("env")
    var envMatches: Boolean = false

    @Serializable(with = PRSerializer::class)
    @SerialName("pr")
    var isPR: Boolean? = null

    companion object {

        internal var env = System.getenv()

        private val json = Json {
            ignoreUnknownKeys = false
        }

        internal fun readList(): List<Vendor> {
            return Vendor::class.java.getResourceAsStream("/vendors.json").use {
                json.decodeFromStream(it!!)
            }
        }
    }
}

private object EnvironmentSerializer : JsonTransformingSerializer<Boolean>(Boolean.serializer()) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        return transformEnv(element)
    }
}

private object PRSerializer : JsonTransformingSerializer<Boolean>(Boolean.serializer()) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        if (element is JsonObject) {
            if (element.containsKey("env")) {
                val env = Vendor.env[element.getValue("env").jsonPrimitive.content]
                return JsonPrimitive(env != null && env != element.getValue("ne").jsonPrimitive.content)
            }
            if (element.containsKey("any")) {
                return JsonPrimitive(element.getValue("any").jsonArray.any {
                    Vendor.env.containsKey(it.jsonPrimitive.content)
                })
            }
        }
        return transformEnv(element)
    }
}

private fun transformEnv(element: JsonElement): JsonElement {
    return when (element) {
        is JsonPrimitive -> {
            JsonPrimitive(Vendor.env.containsKey(element.content))
        }

        is JsonArray -> {
            JsonPrimitive(element.map { it.jsonPrimitive.content }
                .fold(true) { a, b -> a && Vendor.env[b] != null })
        }

        else -> {
            val obj = element as JsonObject
            JsonPrimitive(
                obj.entries.fold(true) { a, b -> a && Vendor.env[b.key] == b.value.jsonPrimitive.content }
            )
        }
    }
}
