package telran.view;


import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
record User(String username, String password, LocalDate dateLastLogin,
		String phoneNumber, int numberOfLogins) {}

class InputOutputTest {
	InputOutput io = new SystemInputOutput();
	private static final int LEAST_PASSWORD_LENGTH = 8;

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
		String username = io.readStringPredicate("Enter username",
				"Wrong username format (at least 6 ASCII letters - first Capital, others Lower case)",
				str -> str.matches("[A-Z][a-z]{5,}"));
		String password = io.readStringPredicate("Enter password (at least 8 symbols, at least one capital letter "
				+ "at least one lower case letter, at least one digit, at least one symbol from \"#$*&%\"",
				"Error ", this::passwordValidation);
		LocalDate lastLoginDate =
				io.readIsoDateRange("Enter Last Login Date", "Wrong date",
						LocalDate.MIN, LocalDate.now().plusDays(1));
		String phoneNumber =
				io.readStringPredicate("Enter phone number", "Wrong phone number", str -> 
				str.matches("(\\+972-?|0)5\\d-?(\\d{3}-\\d{2}-|\\d{2}-?\\d{3}-?)\\d{2}"));
		int numberOfLogins = io.readNumberRange("Enter number of logins","must be positive integer" , 1, Integer.MAX_VALUE)
				.intValue();
		io.writeLine(new User(username, password, lastLoginDate,
				phoneNumber, numberOfLogins));
	}
	static class CharacterRuleState {
		boolean flag;
		Predicate<Character> predicate;
		String errorMessage;
		CharacterRuleState(boolean flag, Predicate<Character> predicate, String errorMessage) {
			this.flag = flag;
			this.predicate = predicate;
			this.errorMessage = errorMessage;
		}
		
		
	}
	boolean passwordValidation(String password) {
		if (password.length() < LEAST_PASSWORD_LENGTH) {
			throw new RuntimeException(String.format("less than %d characters", LEAST_PASSWORD_LENGTH));
		}
		List<CharacterRuleState> passwordRules = getPasswordCharacterRules();
		for(char symbol: password.toCharArray()) {
			updateRulesState(symbol, passwordRules);
		}
		String errorMessage = checkRulesState(passwordRules);
		if(!errorMessage.isEmpty()) {
			throw new RuntimeException(errorMessage);
		}
		return true;
	}
	private String checkRulesState(List<CharacterRuleState> passwordRules) {
		
		return passwordRules.stream().filter(r -> !r.flag)
				.map(r -> r.errorMessage)
				.collect(Collectors.joining(";"));
	}
	private void updateRulesState(char symbol, List<CharacterRuleState> passwordRules) {
		Iterator<CharacterRuleState> it = passwordRules.iterator();
		boolean isNotFound = true;
		do {
			CharacterRuleState rule = it.next();
			if (rule.predicate.test(symbol)) {
				rule.flag = true;
				isNotFound = false;
			}
		}while(it.hasNext() && isNotFound);
		if(isNotFound) {
			throw new RuntimeException("disallowed symbol " + symbol);
		}
	}
	private List<CharacterRuleState> getPasswordCharacterRules() {
		String symbols = "#$*&%.-";
		CharacterRuleState[] rulesArray = {
			new CharacterRuleState(false, Character::isUpperCase, "no capital letter"),
			new CharacterRuleState(false, Character::isLowerCase, "no lower case letter"),
			new CharacterRuleState(false, Character::isDigit, "no digit letter"),
			new CharacterRuleState(false, c -> symbols.contains("" + c), "no symbol from " + symbols),
		};
		return List.of(rulesArray);
	}

}
