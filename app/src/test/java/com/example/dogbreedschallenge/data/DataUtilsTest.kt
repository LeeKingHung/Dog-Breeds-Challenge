package com.example.dogbreedschallenge.data

import org.junit.Assert.assertEquals
import org.junit.Test

class DataUtilsTest {
	
	@Test
	fun getBreedPathForImageLink() {
		assertEquals("abc", DataUtils.getBreedPathForImageLink("abc"))
		assertEquals("main/sub", DataUtils.getBreedPathForImageLink("sub main"))
	}

}