package skosboss.poolparty.api.engine;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

class DerivePropertyName implements Function<String, String> {

	private static final List<String> prefixes = Arrays.asList("is", "get");
	
	@Override
	public String apply(String accessorName) {
		return
		prefixes.stream().map(p -> deriveName(accessorName, p))
		.filter(n -> n != null)
		.findFirst().orElse(accessorName);
	}
	
	private String deriveName(String accessorName, String prefix) {
		int prefixLength = prefix.length();
		if (accessorName.startsWith(prefix) &&
			accessorName.length() > prefixLength
		) {
			int startRemainder = prefixLength + 1;
			String nextChar = accessorName.substring(prefixLength, startRemainder);
			if (nextChar.toUpperCase().equals(nextChar))
				return nextChar.toLowerCase() + accessorName.substring(startRemainder);
		}
		return null;
	}
}
