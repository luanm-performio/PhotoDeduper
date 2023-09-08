package photodeduper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.Getter;
import photodeduper.exception.PhotoDeduperException;

public class PhotoDeduper {

    private final Path folderPath;
    @Getter
    private final Consumer<String> logConsumer;
    private Map<String, List<FileMeta>> allFileMetas;

    public PhotoDeduper(final String folderPath, final Consumer<String> logConsumer) {
        this.folderPath = Paths.get(folderPath);

        if (!Files.exists(this.folderPath)) {
            throw new PhotoDeduperException(folderPath + " not exists");
        }

        this.logConsumer = logConsumer;
        this.allFileMetas = new HashMap<>();
    }

    public void dedupe(final FileChecksumVisitor fileChecksumVisitor) {
        logConsumer.accept("Checking " + folderPath);

        try {
            Files.walkFileTree(folderPath, fileChecksumVisitor);
        } catch (IOException e) {
            throw new PhotoDeduperException(e.getMessage());
        }

        retainDuplicates();
        printDuplicates();
    }

    public void add(final FileMeta fileMeta) {
        final List<FileMeta> files = allFileMetas.getOrDefault(fileMeta.checksum(), new ArrayList<>());
        files.add(fileMeta);
        allFileMetas.put(fileMeta.checksum(), files);
    }

    private void retainDuplicates() {
        allFileMetas = allFileMetas.entrySet().stream().filter(entrySet -> entrySet.getValue().size() > 1).collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue
        ));
    }

    private void printDuplicates() {
        allFileMetas.entrySet().forEach(entry -> {
            logConsumer.accept("Checksum: " + entry.getKey());
            entry.getValue().forEach(fileMeta -> logConsumer.accept("|----> " + fileMeta.filePath()));
        });
    }
}
