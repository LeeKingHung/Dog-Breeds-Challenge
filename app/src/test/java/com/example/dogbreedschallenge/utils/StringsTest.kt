package com.example.dogbreedschallenge.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class StringsTest {

	@Test
	fun capitalize() {
		assertEquals("Abc", "abc".capitalize())
		assertEquals("A", "a".capitalize())
		assertEquals("Abc def", "abc def".capitalize())
		assertEquals("", "".capitalize())
	}

}