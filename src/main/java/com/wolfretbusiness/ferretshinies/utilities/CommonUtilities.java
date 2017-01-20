package com.wolfretbusiness.ferretshinies.utilities;

import org.apache.commons.lang3.StringUtils;

public class CommonUtilities {
	private static final String OPAQUE = "FF";

	public static int fromHex(final String hexValue) {
		// hexValue is interpreted as AARRGGBB
		return (int) Long.parseLong(hexValue, 16);
	}

	public static int fromHex(final String red, final String green, final String blue) {
		return fromHex(OPAQUE, red, green, blue);
	}

	public static int fromHex(final String alpha, final String red, final String green, final String blue) {
		return fromHex(alpha + red + green + blue);
	}

	public static String camelCaseToTitle(final String displayName) {
		final String[] displayNameWords = StringUtils.splitByCharacterTypeCamelCase(displayName);
		return StringUtils.capitalize(StringUtils.join(displayNameWords, " "));
	}
}
