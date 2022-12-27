package wtf.godlydev.spotify.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	private static final Pattern time_regex = Pattern.compile("(?:(\\d{1,999})(ms|s|m))+?");
	private static final Map<String, Integer> time_dict;

	public static int timeStringConvert(String arg) {
		String args = arg.toLowerCase();
		int time = 0;
		List<String> allMatches = new ArrayList<String>();
		Matcher m = time_regex.matcher(args);
		while (m.find()) {
			allMatches.add(m.group());
		}
		for(String s : allMatches) {
			int amount = Integer.parseInt(s.replaceAll("[^\\d]", ""));
			String duration = s.replaceAll("\\d","");
			time += time_dict.get(duration) * amount;
		}
		return time;

	}

	static {
		time_dict = new HashMap<>();
		time_dict.put("ms", 1);
		time_dict.put("s", 1000);
		time_dict.put("m", 60000);
	}

}
