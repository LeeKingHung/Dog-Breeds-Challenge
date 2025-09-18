package com.example.dogbreedschallenge.data

import org.junit.Assert.assertEquals
import org.junit.Test

class DataUtilsTest {
	
	@Test
	fun getBreedPathForImageLink() {
		assertEquals(DataUtils.getBreedPathForImageLink("abc"), "abc")
		assertEquals(DataUtils.getBreedPathForImageLink("sub main"), "main/sub")
	}

}