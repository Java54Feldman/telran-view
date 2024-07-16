package telran.view;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

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
		//TODO
		//create User object from separate fields and display out
		//username at least 6 ASCII letters, first Capital, others lower case (regEx)
		//password at least 8 symbols, at least one capital letter,
		//at least one lower case letter, at least one digit,
		//at least one symbol from "#$%&*." (method)
		//phone number - Israel mobile phone (regEx)
		//dateLastLogin not after current date
		//number of logins any positive number (>=1)
	}

}
