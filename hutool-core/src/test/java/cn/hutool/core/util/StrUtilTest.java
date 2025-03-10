package cn.hutool.core.util;

import java.nio.charset.StandardCharsets;
import java.util.List;

import cn.hutool.core.lang.Dict;
import org.junit.Assert;
import org.junit.Test;

/**
 * 字符串工具类单元测试
 *
 * @author Looly
 */
public class StrUtilTest {

	@Test
	public void isBlankTest() {
		final String blank = "	  　";
		Assert.assertTrue(StrUtil.isBlank(blank));
	}

	@Test
	public void trimTest() {
		final String blank = "	 哈哈 　";
		final String trim = StrUtil.trim(blank);
		Assert.assertEquals("哈哈", trim);
	}

	@Test
	public void trimNewLineTest() {
		String str = "\r\naaa";
		Assert.assertEquals("aaa", StrUtil.trim(str));
		str = "\raaa";
		Assert.assertEquals("aaa", StrUtil.trim(str));
		str = "\naaa";
		Assert.assertEquals("aaa", StrUtil.trim(str));
		str = "\r\n\r\naaa";
		Assert.assertEquals("aaa", StrUtil.trim(str));
	}

	@Test
	public void trimTabTest() {
		final String str = "\taaa";
		Assert.assertEquals("aaa", StrUtil.trim(str));
	}

	@Test
	public void cleanBlankTest() {
		// 包含：制表符、英文空格、不间断空白符、全角空格
		final String str = "	 你 好　";
		final String cleanBlank = StrUtil.cleanBlank(str);
		Assert.assertEquals("你好", cleanBlank);
	}

	@Test
	public void cutTest() {
		final String str = "aaabbbcccdddaadfdfsdfsdf0";
		final String[] cut = StrUtil.cut(str, 4);
		Assert.assertArrayEquals(new String[]{"aaab", "bbcc", "cddd", "aadf", "dfsd", "fsdf", "0"}, cut);
	}

	@Test
	public void splitTest() {
		final String str = "a,b ,c,d,,e";
		final List<String> split = StrUtil.split(str, ',', -1, true, true);
		// 测试空是否被去掉
		Assert.assertEquals(5, split.size());
		// 测试去掉两边空白符是否生效
		Assert.assertEquals("b", split.get(1));

		final String[] strings = StrUtil.splitToArray("abc/", '/');
		Assert.assertEquals(2, strings.length);

		// issue:I6FKSI
		Assert.assertThrows(IllegalArgumentException.class, () -> StrUtil.split("test length 0", 0));
	}

	@Test
	public void splitEmptyTest() {
		final String str = "";
		final List<String> split = StrUtil.split(str, ',', -1, true, true);
		// 测试空是否被去掉
		Assert.assertEquals(0, split.size());
	}

	@Test
	public void splitTest2() {
		final String str = "a.b.";
		final List<String> split = StrUtil.split(str, '.');
		Assert.assertEquals(3, split.size());
		Assert.assertEquals("b", split.get(1));
		Assert.assertEquals("", split.get(2));
	}

	@Test
	public void splitNullTest() {
		Assert.assertEquals(0, StrUtil.split(null, '.').size());
	}

	@Test(expected = IllegalArgumentException.class)
	public void splitToArrayNullTest() {
		StrUtil.splitToArray(null, '.');
	}

	@Test
	public void splitToLongTest() {
		final String str = "1,2,3,4, 5";
		long[] longArray = StrUtil.splitToLong(str, ',');
		Assert.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);

		longArray = StrUtil.splitToLong(str, ",");
		Assert.assertArrayEquals(new long[]{1, 2, 3, 4, 5}, longArray);
	}

	@Test
	public void splitToIntTest() {
		final String str = "1,2,3,4, 5";
		int[] intArray = StrUtil.splitToInt(str, ',');
		Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);

		intArray = StrUtil.splitToInt(str, ",");
		Assert.assertArrayEquals(new int[]{1, 2, 3, 4, 5}, intArray);
	}

	@Test
	public void formatTest() {
		final String template = "你好，我是{name}，我的电话是：{phone}";
		final String result = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", "13888881111"));
		Assert.assertEquals("你好，我是张三，我的电话是：13888881111", result);

		final String result2 = StrUtil.format(template, Dict.create().set("name", "张三").set("phone", null));
		Assert.assertEquals("你好，我是张三，我的电话是：{phone}", result2);
	}

	@Test
	public void stripTest() {
		String str = "abcd123";
		String strip = StrUtil.strip(str, "ab", "23");
		Assert.assertEquals("cd1", strip);

		str = "abcd123";
		strip = StrUtil.strip(str, "ab", "");
		Assert.assertEquals("cd123", strip);

		str = "abcd123";
		strip = StrUtil.strip(str, null, "");
		Assert.assertEquals("abcd123", strip);

		str = "abcd123";
		strip = StrUtil.strip(str, null, "567");
		Assert.assertEquals("abcd123", strip);

		Assert.assertEquals("", StrUtil.strip("a", "a"));
		Assert.assertEquals("", StrUtil.strip("a", "a", "b"));
	}

	@Test
	public void stripIgnoreCaseTest() {
		String str = "abcd123";
		String strip = StrUtil.stripIgnoreCase(str, "Ab", "23");
		Assert.assertEquals("cd1", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, "AB", "");
		Assert.assertEquals("cd123", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, "ab", "");
		Assert.assertEquals("cd123", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, null, "");
		Assert.assertEquals("abcd123", strip);

		str = "abcd123";
		strip = StrUtil.stripIgnoreCase(str, null, "567");
		Assert.assertEquals("abcd123", strip);
	}

	@Test
	public void indexOfIgnoreCaseTest() {
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase(null, "balabala", 0));
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("balabala", null, 0));
		Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("", "", 0));
		Assert.assertEquals(0, StrUtil.indexOfIgnoreCase("aabaabaa", "A", 0));
		Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 0));
		Assert.assertEquals(1, StrUtil.indexOfIgnoreCase("aabaabaa", "AB", 0));
		Assert.assertEquals(5, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 3));
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "B", 9));
		Assert.assertEquals(2, StrUtil.indexOfIgnoreCase("aabaabaa", "B", -1));
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("aabaabaa", "", 2));
		Assert.assertEquals(-1, StrUtil.indexOfIgnoreCase("abc", "", 9));
	}

	@Test
	public void lastIndexOfTest() {
		final String a = "aabbccddcc";
		final int lastIndexOf = StrUtil.lastIndexOf(a, "c", 0, false);
		Assert.assertEquals(-1, lastIndexOf);
	}

	@Test
	public void lastIndexOfIgnoreCaseTest() {
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase(null, "balabala", 0));
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("balabala", null));
		Assert.assertEquals(0, StrUtil.lastIndexOfIgnoreCase("", ""));
		Assert.assertEquals(7, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "A"));
		Assert.assertEquals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B"));
		Assert.assertEquals(4, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "AB"));
		Assert.assertEquals(2, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 3));
		Assert.assertEquals(5, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", 9));
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "B", -1));
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("aabaabaa", "", 2));
		Assert.assertEquals(-1, StrUtil.lastIndexOfIgnoreCase("abc", "", 9));
		Assert.assertEquals(0, StrUtil.lastIndexOfIgnoreCase("AAAcsd", "aaa"));
	}

	@Test
	public void replaceTest() {
		String string = StrUtil.replace("aabbccdd", 2, 6, '*');
		Assert.assertEquals("aa****dd", string);
		string = StrUtil.replace("aabbccdd", 2, 12, '*');
		Assert.assertEquals("aa******", string);
	}

	@Test
	public void replaceTest2() {
		final String result = StrUtil.replace("123", "2", "3");
		Assert.assertEquals("133", result);
	}

	@Test
	public void replaceTest3() {
		final String result = StrUtil.replace(",abcdef,", ",", "|");
		Assert.assertEquals("|abcdef|", result);
	}

	@Test
	public void replaceTest4() {
		final String a = "1039";
		final String result = StrUtil.padPre(a, 8, "0"); //在字符串1039前补4个0
		Assert.assertEquals("00001039", result);

		final String aa = "1039";
		final String result1 = StrUtil.padPre(aa, -1, "0"); //在字符串1039前补4个0
		Assert.assertEquals("103", result1);
	}

	@Test
	public void replaceTest5() {
		final String a = "\uD853\uDC09秀秀";
		final String result = StrUtil.replace(a, 1, a.length(), '*');
		Assert.assertEquals("\uD853\uDC09**", result);

		final String aa = "规划大师";
		final String result1 = StrUtil.replace(aa, 2, a.length(), '*');
		Assert.assertEquals("规划**", result1);
	}

	@Test
	public void upperFirstTest() {
		final StringBuilder sb = new StringBuilder("KEY");
		final String s = StrUtil.upperFirst(sb);
		Assert.assertEquals(s, sb.toString());
	}

	@Test
	public void lowerFirstTest() {
		final StringBuilder sb = new StringBuilder("KEY");
		final String s = StrUtil.lowerFirst(sb);
		Assert.assertEquals("kEY", s);
	}

	@Test
	public void subTest() {
		final String a = "abcderghigh";
		final String pre = StrUtil.sub(a, -5, a.length());
		Assert.assertEquals("ghigh", pre);
	}

	@Test
	public void subByCodePointTest() {
		// 🤔👍🍓🤔
		final String test = "\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53\uD83E\uDD14";

		// 不正确的子字符串
		final String wrongAnswer = StrUtil.sub(test, 0, 3);
		Assert.assertNotEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", wrongAnswer);

		// 正确的子字符串
		final String rightAnswer = StrUtil.subByCodePoint(test, 0, 3);
		Assert.assertEquals("\uD83E\uDD14\uD83D\uDC4D\uD83C\uDF53", rightAnswer);
	}

	@Test
	public void subBeforeTest() {
		final String a = "abcderghigh";
		String pre = StrUtil.subBefore(a, "d", false);
		Assert.assertEquals("abc", pre);
		pre = StrUtil.subBefore(a, 'd', false);
		Assert.assertEquals("abc", pre);
		pre = StrUtil.subBefore(a, 'a', false);
		Assert.assertEquals("", pre);

		//找不到返回原串
		pre = StrUtil.subBefore(a, 'k', false);
		Assert.assertEquals(a, pre);
		pre = StrUtil.subBefore(a, 'k', true);
		Assert.assertEquals(a, pre);
	}

	@Test
	public void subAfterTest() {
		final String a = "abcderghigh";
		String pre = StrUtil.subAfter(a, "d", false);
		Assert.assertEquals("erghigh", pre);
		pre = StrUtil.subAfter(a, 'd', false);
		Assert.assertEquals("erghigh", pre);
		pre = StrUtil.subAfter(a, 'h', true);
		Assert.assertEquals("", pre);

		//找不到字符返回空串
		pre = StrUtil.subAfter(a, 'k', false);
		Assert.assertEquals("", pre);
		pre = StrUtil.subAfter(a, 'k', true);
		Assert.assertEquals("", pre);
	}

	@Test
	public void subSufByLengthTest() {
		Assert.assertEquals("cde", StrUtil.subSufByLength("abcde", 3));
		Assert.assertEquals("", StrUtil.subSufByLength("abcde", -1));
		Assert.assertEquals("", StrUtil.subSufByLength("abcde", 0));
		Assert.assertEquals("abcde", StrUtil.subSufByLength("abcde", 5));
		Assert.assertEquals("abcde", StrUtil.subSufByLength("abcde", 10));
	}

	@Test
	public void repeatAndJoinTest() {
		String repeatAndJoin = StrUtil.repeatAndJoin("?", 5, ",");
		Assert.assertEquals("?,?,?,?,?", repeatAndJoin);

		repeatAndJoin = StrUtil.repeatAndJoin("?", 0, ",");
		Assert.assertEquals("", repeatAndJoin);

		repeatAndJoin = StrUtil.repeatAndJoin("?", 5, null);
		Assert.assertEquals("?????", repeatAndJoin);
	}

	@Test
	public void moveTest() {
		final String str = "aaaaaaa22222bbbbbbb";
		String result = StrUtil.move(str, 7, 12, -3);
		Assert.assertEquals("aaaa22222aaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -4);
		Assert.assertEquals("aaa22222aaaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -7);
		Assert.assertEquals("22222aaaaaaabbbbbbb", result);
		result = StrUtil.move(str, 7, 12, -20);
		Assert.assertEquals("aaaaaa22222abbbbbbb", result);

		result = StrUtil.move(str, 7, 12, 3);
		Assert.assertEquals("aaaaaaabbb22222bbbb", result);
		result = StrUtil.move(str, 7, 12, 7);
		Assert.assertEquals("aaaaaaabbbbbbb22222", result);
		result = StrUtil.move(str, 7, 12, 20);
		Assert.assertEquals("aaaaaaab22222bbbbbb", result);

		result = StrUtil.move(str, 7, 12, 0);
		Assert.assertEquals("aaaaaaa22222bbbbbbb", result);
	}

	@Test
	public void removePrefixIgnorecaseTest() {
		final String a = "aaabbb";
		String prefix = "aaa";
		Assert.assertEquals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

		prefix = "AAA";
		Assert.assertEquals("bbb", StrUtil.removePrefixIgnoreCase(a, prefix));

		prefix = "AAABBB";
		Assert.assertEquals("", StrUtil.removePrefixIgnoreCase(a, prefix));
	}

	@Test
	public void maxLengthTest() {
		final String text = "我是一段正文，很长的正文，需要截取的正文";
		String str = StrUtil.maxLength(text, 5);
		Assert.assertEquals("我是一段正...", str);
		str = StrUtil.maxLength(text, 21);
		Assert.assertEquals(text, str);
		str = StrUtil.maxLength(text, 50);
		Assert.assertEquals(text, str);
	}

	@Test
	public void containsAnyTest() {
		//字符
		boolean containsAny = StrUtil.containsAny("aaabbbccc", 'a', 'd');
		Assert.assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'e', 'd');
		Assert.assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", 'd', 'c');
		Assert.assertTrue(containsAny);

		//字符串
		containsAny = StrUtil.containsAny("aaabbbccc", "a", "d");
		Assert.assertTrue(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "e", "d");
		Assert.assertFalse(containsAny);
		containsAny = StrUtil.containsAny("aaabbbccc", "d", "c");
		Assert.assertTrue(containsAny);

		// https://gitee.com/dromara/hutool/issues/I7WSYD
		containsAny = StrUtil.containsAny("你好啊", "嗯", null);
		Assert.assertFalse(containsAny);
	}

	@Test
	public void centerTest() {
		Assert.assertNull(StrUtil.center(null, 10));
		Assert.assertEquals("    ", StrUtil.center("", 4));
		Assert.assertEquals("ab", StrUtil.center("ab", -1));
		Assert.assertEquals(" ab ", StrUtil.center("ab", 4));
		Assert.assertEquals("abcd", StrUtil.center("abcd", 2));
		Assert.assertEquals(" a  ", StrUtil.center("a", 4));
	}

	@Test
	public void padPreTest() {
		Assert.assertNull(StrUtil.padPre(null, 10, ' '));
		Assert.assertEquals("001", StrUtil.padPre("1", 3, '0'));
		Assert.assertEquals("12", StrUtil.padPre("123", 2, '0'));

		Assert.assertNull(StrUtil.padPre(null, 10, "AA"));
		Assert.assertEquals("AB1", StrUtil.padPre("1", 3, "ABC"));
		Assert.assertEquals("12", StrUtil.padPre("123", 2, "ABC"));
	}

	@Test
	public void padAfterTest() {
		Assert.assertNull(StrUtil.padAfter(null, 10, ' '));
		Assert.assertEquals("100", StrUtil.padAfter("1", 3, '0'));
		Assert.assertEquals("23", StrUtil.padAfter("123", 2, '0'));
		Assert.assertEquals("", StrUtil.padAfter("123", -1, '0'));

		Assert.assertNull(StrUtil.padAfter(null, 10, "ABC"));
		Assert.assertEquals("1AB", StrUtil.padAfter("1", 3, "ABC"));
		Assert.assertEquals("23", StrUtil.padAfter("123", 2, "ABC"));
	}

	@Test
	public void subBetweenAllTest() {
		Assert.assertArrayEquals(new String[]{"yz", "abc"}, StrUtil.subBetweenAll("saho[yz]fdsadp[abc]a", "[", "]"));
		Assert.assertArrayEquals(new String[]{"abc"}, StrUtil.subBetweenAll("saho[yzfdsadp[abc]a]", "[", "]"));
		Assert.assertArrayEquals(new String[]{"abc", "abc"}, StrUtil.subBetweenAll("yabczyabcz", "y", "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll(null, "y", "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll("", "y", "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", null, "z"));
		Assert.assertArrayEquals(new String[0], StrUtil.subBetweenAll("abc", "y", null));
	}

	@Test
	public void subBetweenAllTest2() {
		//issue#861@Github，起始不匹配的时候，应该直接空
		final String src1 = "/* \n* hutool  */  asdas  /* \n* hutool  */";
		final String src2 = "/ * hutool  */  asdas  / * hutool  */";

		final String[] results1 = StrUtil.subBetweenAll(src1, "/**", "*/");
		Assert.assertEquals(0, results1.length);

		final String[] results2 = StrUtil.subBetweenAll(src2, "/*", "*/");
		Assert.assertEquals(0, results2.length);
	}

	@Test
	public void subBetweenAllTest3() {
		final String src1 = "'abc'and'123'";
		String[] strings = StrUtil.subBetweenAll(src1, "'", "'");
		Assert.assertEquals(2, strings.length);
		Assert.assertEquals("abc", strings[0]);
		Assert.assertEquals("123", strings[1]);

		final String src2 = "'abc''123'";
		strings = StrUtil.subBetweenAll(src2, "'", "'");
		Assert.assertEquals(2, strings.length);
		Assert.assertEquals("abc", strings[0]);
		Assert.assertEquals("123", strings[1]);

		final String src3 = "'abc'123'";
		strings = StrUtil.subBetweenAll(src3, "'", "'");
		Assert.assertEquals(1, strings.length);
		Assert.assertEquals("abc", strings[0]);
	}

	@Test
	public void subBetweenAllTest4() {
		final String str = "你好:1388681xxxx用户已开通,1877275xxxx用户已开通,无法发送业务开通短信";
		final String[] strings = StrUtil.subBetweenAll(str, "1877275xxxx", ",");
		Assert.assertEquals(1, strings.length);
		Assert.assertEquals("用户已开通", strings[0]);
	}

	@Test
	public void briefTest() {
		// case: 1 至 str.length - 1
		final String str = RandomUtil.randomString(RandomUtil.randomInt(1, 100));
		for (int maxLength = 1; maxLength < str.length(); maxLength++) {
			final String brief = StrUtil.brief(str, maxLength);
			Assert.assertEquals(brief.length(), maxLength);
		}

		// case: 不会格式化的值
		Assert.assertEquals(str, StrUtil.brief(str, 0));
		Assert.assertEquals(str, StrUtil.brief(str, -1));
		Assert.assertEquals(str, StrUtil.brief(str, str.length()));
		Assert.assertEquals(str, StrUtil.brief(str, str.length() + 1));
	}

	@Test
	public void briefTest2() {
		final String str = "123";
		int maxLength = 3;
		String brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("123", brief);

		maxLength = 2;
		brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("1.", brief);

		maxLength = 1;
		brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("1", brief);
	}

	@Test
	public void briefTest3() {
		final String str = "123abc";

		int maxLength = 6;
		String brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals(str, brief);

		maxLength = 5;
		brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("1...c", brief);

		maxLength = 4;
		brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("1..c", brief);

		maxLength = 3;
		brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("1.c", brief);

		maxLength = 2;
		brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("1.", brief);

		maxLength = 1;
		brief = StrUtil.brief(str, maxLength);
		Assert.assertEquals("1", brief);
	}

	@Test
	public void filterTest() {
		final String filterNumber = StrUtil.filter("hutool678", CharUtil::isNumber);
		Assert.assertEquals("678", filterNumber);
		final String cleanBlank = StrUtil.filter("	 你 好　", c -> !CharUtil.isBlankChar(c));
		Assert.assertEquals("你好", cleanBlank);
	}

	@Test
	public void wrapAllTest() {
		String[] strings = StrUtil.wrapAll("`", "`", StrUtil.splitToArray("1,2,3,4", ','));
		Assert.assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));

		strings = StrUtil.wrapAllWithPair("`", StrUtil.splitToArray("1,2,3,4", ','));
		Assert.assertEquals("[`1`, `2`, `3`, `4`]", StrUtil.utf8Str(strings));
	}

	@Test
	public void startWithTest() {
		final String a = "123";
		final String b = "123";

		Assert.assertTrue(StrUtil.startWith(a, b));
		Assert.assertFalse(StrUtil.startWithIgnoreEquals(a, b));
	}

	@Test
	public void indexedFormatTest() {
		final String ret = StrUtil.indexedFormat("this is {0} for {1}", "a", 1000);
		Assert.assertEquals("this is a for 1,000", ret);
	}

	@Test
	public void hideTest() {
		Assert.assertNull(StrUtil.hide(null, 1, 1));
		Assert.assertEquals("", StrUtil.hide("", 1, 1));
		Assert.assertEquals("****duan@163.com", StrUtil.hide("jackduan@163.com", -1, 4));
		Assert.assertEquals("ja*kduan@163.com", StrUtil.hide("jackduan@163.com", 2, 3));
		Assert.assertEquals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 3, 2));
		Assert.assertEquals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 16, 16));
		Assert.assertEquals("jackduan@163.com", StrUtil.hide("jackduan@163.com", 16, 17));
	}


	@Test
	public void isCharEqualsTest() {
		final String a = "aaaaaaaaa";
		Assert.assertTrue(StrUtil.isCharEquals(a));
	}

	@Test
	public void isNumericTest() {
		final String a = "2142342422423423";
		Assert.assertTrue(StrUtil.isNumeric(a));
	}

	@Test
	public void containsAllTest() {
		final String a = "2142342422423423";
		Assert.assertTrue(StrUtil.containsAll(a, "214", "234"));
	}

	@Test
	public void replaceLastTest() {
		final String str = "i am jackjack";
		final String result = StrUtil.replaceLast(str, "JACK", null, true);
		Assert.assertEquals(result, "i am jack");
	}

	@Test
	public void replaceFirstTest() {
		final String str = "yesyes i do";
		final String result = StrUtil.replaceFirst(str, "YES", "", true);
		Assert.assertEquals(result, "yes i do");
	}

	@Test
	public void issueI5YN49Test() {
		final String str = "A5E6005700000000000000000000000000000000000000090D0100000000000001003830";
		Assert.assertEquals("38", StrUtil.subWithLength(str,-2,2));
	}

	@Test
	public void issueI6KKFUTest() {
		// https://gitee.com/dromara/hutool/issues/I6KKFU
		final String template = "I''m {0} years old.";
		final String result = StrUtil.indexedFormat(template, 10);
		Assert.assertEquals("I'm 10 years old.", result);
	}

	@Test
	public void truncateUtf8Test() {
		final String str = "这是This一段中英文";
		String ret = StrUtil.truncateUtf8(str, 12);
		Assert.assertEquals("这是Thi...", ret);

		ret = StrUtil.truncateUtf8(str, 13);
		Assert.assertEquals("这是This...", ret);

		ret = StrUtil.truncateUtf8(str, 14);
		Assert.assertEquals("这是This...", ret);

		ret = StrUtil.truncateUtf8(str, 999);
		Assert.assertEquals(str, ret);
	}

	@Test
	public void truncateByByteLengthTest() {
		final String str = "This is English";
		final String ret = StrUtil.truncateByByteLength(str, StandardCharsets.ISO_8859_1,10, 1, false);
		Assert.assertEquals("This is En", ret);
	}
}
