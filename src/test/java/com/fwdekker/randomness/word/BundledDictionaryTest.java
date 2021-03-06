package com.fwdekker.randomness.word;

import com.intellij.openapi.ui.ValidationInfo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * Unit tests for {@link Dictionary.BundledDictionary}.
 */
final class BundledDictionaryTest {
    @Test
    void testInitDoesNotExist() {
        assertThatThrownBy(() -> Dictionary.BundledDictionary.get("invalid_resource"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Failed to read dictionary into memory.")
                .hasNoCause();
    }

    @Test
    void testInitEmpty() {
        assertThatThrownBy(() -> Dictionary.BundledDictionary.get("dictionaries/empty.dic"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dictionary must be non-empty.")
                .hasNoCause();
    }

    @Test
    void testInitTwiceEquals() {
        final Dictionary dictionaryA = Dictionary.BundledDictionary.get("dictionaries/varied.dic");
        final Dictionary dictionaryB = Dictionary.BundledDictionary.get("dictionaries/varied.dic");

        assertThat(dictionaryA).isSameAs(dictionaryB);
    }

    @Test
    void testInitTwiceNoCacheEqualButNotSame() {
        final Dictionary dictionaryA = Dictionary.BundledDictionary.get("dictionaries/simple.dic");
        final Dictionary dictionaryB = Dictionary.BundledDictionary.get("dictionaries/simple.dic", false);

        assertThat(dictionaryB).isEqualTo(dictionaryA);
        assertThat(dictionaryB).isNotSameAs(dictionaryA);
    }

    @Test
    void testInitNoCacheStoresAnyway() {
        final Dictionary dictionaryA = Dictionary.BundledDictionary.get("dictionaries/simple.dic", false);
        final Dictionary dictionaryB = Dictionary.BundledDictionary.get("dictionaries/simple.dic");

        assertThat(dictionaryB).isSameAs(dictionaryA);
    }


    @Test
    void testValidateInstanceSuccess() {
        final Dictionary dictionary = Dictionary.BundledDictionary.get("dictionaries/simple.dic");

        final ValidationInfo validationInfo = dictionary.validate();

        assertThat(validationInfo).isNull();
    }

    @Test
    void testValidateStaticSuccess() {
        final ValidationInfo validationInfo = Dictionary.BundledDictionary.validate("dictionaries/simple.dic");

        assertThat(validationInfo).isNull();
    }

    @Test
    void testValidateStaticFileDoesNotExist() {
        final ValidationInfo validationInfo = Dictionary.BundledDictionary.validate("invalid_resource");

        assertThat(validationInfo).isNotNull();
        assertThat(validationInfo.message).isEqualTo("The dictionary resource for invalid_resource no longer exists.");
        assertThat(validationInfo.component).isNull();
    }

    @Test
    void testValidateStaticFileEmpty() {
        final ValidationInfo validationInfo = Dictionary.BundledDictionary.validate("dictionaries/empty.dic");

        assertThat(validationInfo).isNotNull();
        assertThat(validationInfo.message).isEqualTo("The dictionary resource for empty.dic is empty.");
        assertThat(validationInfo.component).isNull();
    }


    @Test
    void testToString() {
        final Dictionary dictionary = Dictionary.BundledDictionary.get("dictionaries/simple.dic");

        assertThat(dictionary.toString()).isEqualTo("[bundled] simple.dic");
    }
}
