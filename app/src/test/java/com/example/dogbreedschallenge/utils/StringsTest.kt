package com.example.dogbreedschallenge.utils

import org.junit.Assert.assertEquals
import org.junit.Test

class StringsTest {
	
	@Test
	fun capitalize() {
		assertEquals("abc".capitalize() , "Abc")
		assertEquals("a".capitalize() , "A")
		assertEquals("abc def".capitalize() , "Abc def")
		assertEquals("".capitalize() , "")
	}

}