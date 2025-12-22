package address_utils.storage;

import address_book.Contact;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;
import static org.junit.jupiter.api.Assertions.*;

class AddressBookStorageTest {

    @Test
    void saveThenLoad_roundTripMatches() throws Exception {
        withEnvironmentVariable("APP_DATA_DIR", "target/test-data")
                .execute(() -> {
                    AddressBookStorage storage = new AddressBookStorage();

                    List<Contact> input = List.of(
                            new Contact("James", "Stevens", "123 Main St", "Chicago", "Illinois", "60601", "3125551212", "james@example.com"),
                            new Contact("Ada", "Lovelace", "42 Code Ave", "London", "England", "12345", "2125551212", "ada@example.com")
                    );

                    storage.save(input);
                    List<Contact> output = storage.load();

                    assertEquals(2, output.size());
                    assertEquals("James", output.get(0).getFirstName());
                    assertEquals("Stevens", output.get(0).getLastName());
                });
    }
}
