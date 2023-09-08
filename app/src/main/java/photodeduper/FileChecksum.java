package photodeduper;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import photodeduper.exception.FileChecksumException;

@FunctionalInterface
public interface FileChecksum {

    FileMeta of(final Path file);

    static FileChecksum md5() {
        return (filePath) -> {
            try {
                byte[] data = Files.readAllBytes(filePath);
                byte[] hash = MessageDigest.getInstance("MD5").digest(data);

                return new FileMeta(new BigInteger(1, hash).toString(16), filePath.toString());
            } catch (IOException | NoSuchAlgorithmException e) {
                throw new FileChecksumException(e.getMessage());
            }
        };
    }
}
