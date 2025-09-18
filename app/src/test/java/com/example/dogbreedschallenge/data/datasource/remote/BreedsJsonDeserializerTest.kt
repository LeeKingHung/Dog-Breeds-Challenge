package com.example.dogbreedschallenge.data.datasource.remote

import com.google.common.truth.Truth
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Test

class BreedsJsonDeserializerTest {

	@Test
	fun test() {
		val json = RemoteDataSourceUtils.getJsonContentFromFile("Get All Breeds")
		val typeToken = object : TypeToken<List<String>>() {}
		val typeAdapter = BreedsJsonDeserializer()
		val gson = GsonBuilder()
			.registerTypeAdapter(typeToken.type, typeAdapter)
			.create()
		val list = gson.fromJson(json, typeToken)
		Truth.assertThat(list)
			.containsExactly(
				"affenpinscher",
				"kelpie australian",
				"shepherd australian",
				"indian bakharwal",
				"akita"
			)
	}

}