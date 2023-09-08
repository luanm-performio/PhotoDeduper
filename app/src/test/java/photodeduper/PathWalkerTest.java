package photodeduper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PathWalkerTest {

    final List<FileMeta> allFileMetas = new ArrayList<>();
    final PathWalker pathWalker = new PathWalker(allFileMetas::add, FileChecksum.md5());

    @Test
    void shouldHandleFilesSuccessfullyWhenAFolderIsVisited() throws Exception {
        final Path folderPath = Paths.get("src/test/resources/test_images");
        final String[] expectedFiles = new String[] {
                "src/test/resources/test_images/camping DELETE ME/coves.jpg",
                "src/test/resources/test_images/camping DELETE ME/escape.jpg",
                "src/test/resources/test_images/trip to the sea/s-06927.jpg",
                "src/test/resources/test_images/sea, sand, surf/coves.jpg"
        };

        Files.walkFileTree(folderPath, pathWalker);

        assertThat(allFileMetas).hasSize(4)
                .extracting(FileMeta::filePath)
                .containsExactlyInAnyOrder(expectedFiles);
    }
}
