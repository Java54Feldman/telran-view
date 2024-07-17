package telran.view;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
record User(String username, String password, LocalDate dateLastLogin,
		String phoneNumber, int numberOfLogins) {}

class InputOutputTest {
	InputOutput io = new SystemInputOutput();

	@Test
	void readObjectTest() {
		User user = io.readObject("Enter user in format <username>#<password>"
				+ "#<dateLastLogin>#<phone number>#<number of logins>",
				"Wrong user input format", 
				str -> {
					String[] tokens = str.split("#");
					return new User(tokens[0], tokens[1],
							LocalDate.parse(tokens[2]),
							tokens[3], Integer.parseInt(tokens[4]));
				});
		io.writeLine(user);
	}
	@Test
	void readUserByFields() {
		//create User object from separate fields and display out
		//username at least 6 ASCII letters, first Capital, others lower case (regEx)
		//password at least 8 symbols, at least one capital letter,
		//at least one lower case letter, at least one digit,
		//at least one symbol from "#$%&*." (method)
		//phone number - Israel mobile phone (regEx)
		//dateLastLogin not after current date
		//number of logins any positive number (>=1)
		String username = io.readStringPredicate(
				"Enter username at least 6 ASCII letters, first Capital, others lower case", 
				"Wrong username format.", 
				str -> str.matches("^[A-Z][a-z]{5,}$"));
	    HashSet<String> passwordOptions = new HashSet<>(Arrays.asList(
	    		".*[A-Z].*", // at least one capital letter
	    		".*[a-z].*", // at least one lower case letter
	    		".*\\d.*", // at least one digit
	    		".*[#$%&*.].*", // at least one special character
	    		".{8,}" // at least 8 symbols
	    ));
		String password = io.readStringOptions(
				"Enter password at least 8 symbols, at least one capital letter,\n"
				+ "at least one lower case letter, at least one digit, at least one symbol from \"#$%&*.\"", 
				"Wrong password format.", 
				passwordOptions);
		String phoneNumber = io.readStringPredicate(				
				"Enter Israel mobile phone number", 
				"Wrong phone number format.", 
				str -> str.matches("(\\+972-?|0)5\\d-?\\d{7}"));
		LocalDate dateLastLogin = io.readIsoDateRange(
				"Enter date of last login in format yyyy-MM-dd", 
				"Wrong date format.", 
				LocalDate.MIN, LocalDate.now().plusDays(1));
		int numberOfLogins = io.readObjectWithPredicate(
				"Enter number of logins", 
				"Wrong number format.", 
				Integer::parseInt, num -> num >= 1);
		User user = new User(username, password, dateLastLogin, phoneNumber, numberOfLogins);
		io.writeLine(user);
	}


}
