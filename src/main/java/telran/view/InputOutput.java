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
	default <T>T readObject(String prompt, String errorPrompt, 
			Function<String, T> mapper) {
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
		}while(running);
		return res;
	}
	/**
	 * 
	 * @param prompt
	 * @param errorPrompt
	 * @return Integer number
	 */
	default Integer readInt(String prompt, String errorPrompt) {
		//TODO
		//Entered string must be a number
		//otherwise errorPrompt with cycle
		return null;
	}
	default Long readLong(String prompt, String errorPrompt) {
		//TODO
		//Entered string must be a number
		//otherwise errorPrompt with cycle
		return null;
	}
	default Double readDouble(String prompt, String errorPrompt) {
		//TODO
		//Entered string must be a number
		//otherwise errorPrompt with cycle
		return null;
	}
	default Double readNumberRange(String prompt, String errorPrompt,
			double min, double max) {
		//TODO
		//Entered string must be a number in range [min, max)
		//otherwise errorPrompt with cycle
		return null;
	}
	default String readStringPredicate(String prompt, String errorPrompt,
			Predicate<String> predicate) {
		//TODO
		//Entered String must match a given predicate
		return null;
	}
	default String readStringOptions(String prompt, String errorPrompt,
			HashSet<String> options) {
		//TODO
		//Entered String must be one out of a given options
		return null;
	}
	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		//TODO
		//Entered String must be a LocalDate in format (yyy-mm-dd)
		return null;
	}
	default LocalDate readIsoDateRange(String prompt, String errorPrompt,
			LocalDate from, LocalDate to) {
		//TODO
		//Entered String must be a LocalDate in format (yyy-mm-dd)
		//in the range (from, to) (after from and before to)
		return null;
	}
			
}
