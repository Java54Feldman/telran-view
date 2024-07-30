package telran.view;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);
	void writeString(String str);

	default void writeLine(Object obj) {
		writeString(obj.toString() + "\n");
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		T res = null;
		boolean running = false;
		do {
			String str = readString(prompt);
			running = false;
			try {
				res = mapper.apply(str);
			} catch (RuntimeException e) {
				writeLine(errorPrompt + " " + e.getMessage());
				running = true;
			}
		} while (running);
		return res;
	}

	/**
	 * 
	 * @param prompt
	 * @param errorPrompt
	 * @return Integer number
	 */
	default Integer readInt(String prompt, String errorPrompt) {
		// Entered string must be a number
		// otherwise errorPrompt with cycle
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}

	default Long readLong(String prompt, String errorPrompt) {
		// Entered string must be a number
		// otherwise errorPrompt with cycle
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	default Double readDouble(String prompt, String errorPrompt) {
		// Entered string must be a number
		// otherwise errorPrompt with cycle
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}

	default Double readNumberRange(String prompt, String errorPrompt, double min, double max) {
		// Entered string must be a number in range [min, max]
		// otherwise errorPrompt with cycle
		return readObject(prompt, errorPrompt,
				string -> {

			double res = Double.parseDouble(string);
			if (res < min) {
				throw new IllegalArgumentException("must be not less than " + min);
			}
			if (res > max) {
				throw new IllegalArgumentException("must be not greater than " + max);
			}
			return res;

		});
	}

	default String readStringPredicate(String prompt, String errorPrompt, Predicate<String> predicate) {
		// Entered String must match a given predicate
		return readObject(prompt, errorPrompt, string -> {
			if(!predicate.test(string)) {
				throw new IllegalArgumentException("");
			}
			return string;
		});
	}

	default String readStringOptions(String prompt, String errorPrompt, HashSet<String> options) {
		// Entered String must be one out of a given options
		return readStringPredicate(prompt, errorPrompt, options::contains);
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		// Entered String must be a LocalDate in format (yyyy-MM-dd)
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}

	default LocalDate readIsoDateRange(String prompt, String errorPrompt, LocalDate from, LocalDate to) {
		// Entered String must be a LocalDate in format (yyy-mm-dd)
		// in the range (from, to) (after from and before to)
		return readObject(prompt, errorPrompt, string -> {
			LocalDate res = LocalDate.parse(string);
			if(!(res.isAfter(from)&& res.isBefore(to))) {
				throw new IllegalArgumentException
				(String.format("Date must be after %s before %s", from, to));
			}
			return res;
		});
	}
		   

}
