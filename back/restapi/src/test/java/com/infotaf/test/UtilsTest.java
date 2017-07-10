package com.infotaf.test;

import java.util.Map;

import com.infotaf.common.utils.Utils;

import junit.framework.TestCase;

public class UtilsTest extends TestCase {
	
	public UtilsTest(String name) {
	  super(name);
	}
	
	public void testParsePg()
	{
		try {
			Map<String, String> pgParsed = Utils.ParsePg("80Li212");
			assertEquals("80", pgParsed.get("nums"));
			assertEquals("Li", pgParsed.get("tbk"));
			assertEquals("212", pgParsed.get("proms"));
			
			pgParsed = Utils.ParsePg("144-03Li212");
			assertEquals("144-03", pgParsed.get("nums"));
			assertEquals("Li", pgParsed.get("tbk"));
			assertEquals("212", pgParsed.get("proms"));
			
			pgParsed = Utils.ParsePg("");
			assertEquals("", pgParsed.get("nums"));
			assertEquals("", pgParsed.get("tbk"));
			assertEquals("", pgParsed.get("proms"));
			
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	public void testTryParseInt()
	{
		try {
			assertTrue(Utils.tryParseInt("500"));
			assertFalse(Utils.tryParseInt("abc"));
			assertFalse(Utils.tryParseInt(""));
			assertFalse(Utils.tryParseInt("55.2"));
			assertFalse(Utils.tryParseInt("55,2"));
			assertFalse(Utils.tryParseInt("55aaa"));
			
		} catch (Exception e) {
			assertTrue(false);
		}
	}
}