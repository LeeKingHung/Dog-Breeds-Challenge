package com.example.dogbreedschallenge.data.datasource.remote

import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

object RemoteDataSourceUtils {

	fun getJsonContentFromFile(fileName: String): String {
		return javaClass.classLoader?.getResourceAsStream("apiResponses/$fileName.json")
			?.source()?.buffer()?.readString(StandardCharsets.UTF_8)
			?: throw IllegalArgumentException("File not found: $fileName.json")
	}

}