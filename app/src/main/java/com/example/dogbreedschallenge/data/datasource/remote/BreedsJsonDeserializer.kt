package com.example.dogbreedschallenge.data.datasource.remote

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 * Convert the response like
 * ```
 * {
 *     "message": {
 *         "affenpinscher": [],
 *         "akita": [],
 *         "australian": [
 *             "kelpie",
 *             "shepherd"
 *         ],
 *         "bakharwal": [
 *             "indian"
 *         ],
 *
 *     },
 *     "status": "success"
 * }
 * ```
 * to string list
 * ```
 * ["affenpinscher", "akita", "kelpie australian", "shepherd australian", "indian bakharwal"]
 * ```
 */
class BreedsJsonDeserializer : JsonDeserializer<List<String>> {

	override fun deserialize(json: JsonElement,
	                         typeOfT: Type,
	                         context: JsonDeserializationContext): List<String>? {

		val resultList = mutableListOf<String>()
		val message = json.asJsonObject.getAsJsonObject("message")

		// Iterate through each key-value pair in the message object
		for ((breed, subBreeds) in message.entrySet()) {

			val subBreeds = subBreeds.asJsonArray

			if (subBreeds.isEmpty()) { // Breed without sub-breeds

				resultList.add(breed)

			} else {    // Breed with sub-breeds

				for (subBreed in subBreeds) {
					val formattedSubBreed = subBreed.asString
					val formattedBreed = breed
					resultList.add("$formattedSubBreed $formattedBreed")
				}

			}

		}

		return resultList

	}

}