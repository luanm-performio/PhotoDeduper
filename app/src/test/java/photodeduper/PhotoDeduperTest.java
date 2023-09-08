package photodeduper;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import photodeduper.exception.PhotoDeduperException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhotoDeduperTest {

    final List<String> logMessages = new ArrayList<>();

    @Test
    void shouldPrintListOfDuplicateFiles() {
        final String folderPath = "src/test/resources/test_images";

        final PhotoDeduper photoDeduper = new PhotoDeduper(folderPath, logMessages::add);
        final PathWalker pathWalker = new PathWalker(photoDeduper::add, FileChecksum.md5());
        photoDeduper.dedupe(pathWalker);

        assertThat(logMessages).containsExactly(
                "Checking " + folderPath,
                "Checksum: 325082be668188abfdd73c28a4a8f71",
                "|----> src/test/resources/test_images/camping DELETE ME/coves.jpg",
                "|----> src/test/resources/test_images/trip to the sea/s-06927.jpg",
                "|----> src/test/resources/test_images/sea, sand, surf/coves.jpg"
        );
    }

    @Test
    void shouldNotPrintListOfDuplicatesWhenNoDuplicatesFound() {
        final String folderPath = "src/test/resources/test_images/camping DELETE ME";

        final PhotoDeduper photoDeduper = new PhotoDeduper(folderPath, logMessages::add);
        final PathWalker pathWalker = new PathWalker(photoDeduper::add, FileChecksum.md5());
        photoDeduper.dedupe(pathWalker);

        assertThat(logMessages).containsExactly("Checking " + folderPath);
    }

    @Test
    void shouldThrowExceptionWhenPathNotFound() {
        final String folderPath = "src/test/resources/test_images/not_exists";

        assertThatThrownBy(() -> new PhotoDeduper(folderPath, logMessages::add))
                .isInstanceOf(PhotoDeduperException.class)
                .hasMessage(folderPath + " not exists");

    }

    @Test
    void shouldThrowExceptionWhenPathWalkerFails() {
        final String folderPath = "src/test/resources/test_images/camping DELETE ME";

        final PhotoDeduper photoDeduper = new PhotoDeduper(folderPath, logMessages::add);
        final PathWalker pathWalker = new PathWalker(photoDeduper::add, (file) -> {throw new RuntimeException("Boom!");});

        assertThatThrownBy(() -> photoDeduper.dedupe(pathWalker))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Boom!");

    }
}
