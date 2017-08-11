package skosboss.poolparty.api.engine;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class UrlImpl implements Url {

	private List<Segment> segments;
	private List<String> variables;
	
	private Map<String, String> bindings = new LinkedHashMap<>();
	
	UrlImpl(
		List<Segment> segments,
		List<String> variables
	) {
		this.segments = segments;
		this.variables = variables;
	}

	@Override
	public Url set(String variable, String value) {
		if (!variables.contains(variable))
			throw new IllegalArgumentException("variable " + variable + " does not exist in this URL");
		bindings.put(variable, value);
		return this;
	}

	@Override
	public List<String> getVariables() {
		return variables;
	}

	@Override
	public String render() {
		if (!bindings.keySet().equals(new LinkedHashSet<>(variables)))
			throw new RuntimeException("not all variables in URL are set");
		
		return
		segments.stream().map(s -> {
			
			if (s instanceof Text)
				return ((Text) s).getText();
			
			if (s instanceof Variable)
				return bindings.get(((Variable) s).getName());
			
			throw new RuntimeException("unknown segment type " + s.getClass().getCanonicalName());
			
		})
		.collect(Collectors.joining());
	}

}
