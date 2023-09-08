package photodeduper;

import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import photodeduper.exception.FileChecksumException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileChecksumTest {

    @Test
    void shouldGetMd5Checksum() {
        final String filePath = "src/test/resources/test_images/trip to the sea/s-06927.jpg";

        final String expectedChecksum = "325082be668188abfdd73c28a4a8f71";

        final FileMeta fileMeta = FileChecksum.md5().of(Paths.get(filePath));

        assertThat(fileMeta).hasFieldOrPropertyWithValue("checksum", expectedChecksum);
    }

    @Test
    void shouldThrowAnExceptionIfFileNotExists() {
        final String filePath = "src/test/resources/test_images/trip to the sea/s-06927.tmp";

        assertThatThrownBy(() -> FileChecksum.md5().of(Paths.get(filePath)))
                .isInstanceOf(FileChecksumException.class)
                .hasMessage(filePath);

    }
}
