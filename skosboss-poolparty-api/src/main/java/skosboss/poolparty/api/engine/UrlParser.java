package skosboss.poolparty.api.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class UrlParser {

	private static final Pattern pattern = Pattern.compile("\\{[^}]+\\}"); // \{[^}]+\}
	
	Supplier<Url> parseUrl(String url) {
		
		List<Segment> segments = parseSegments(url);
		
		List<String> variables = segments.stream()
			.filter(s -> s instanceof Variable)
			.map(s -> (Variable) s)
			.map(v -> v.getName())
			.collect(Collectors.toList());
		
		return () -> new UrlImpl(segments, variables);
	}
	
	private List<Segment> parseSegments(String url) {

		List<Segment> segments = new ArrayList<>();
		
		Matcher matcher = pattern.matcher(url);
		int position = 0;
		while (matcher.find()) {
			int start = matcher.start();
			if (start > position)
				segments.add(new Text(url.substring(position, start)));
			position = matcher.end();
			String match = matcher.group();
			String variable = match.substring(1, match.length() - 1);
			segments.add(new Variable(variable));
		}
		int n = url.length();
		if (position < n)
			segments.add(new Text(url.substring(position, n)));
		
		return segments;
	}
	
}
