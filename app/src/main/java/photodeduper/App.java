package photodeduper;

import java.util.function.Consumer;

public class App {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: folder is required");
            System.exit(1);
        }

        final Consumer<String> logConsumer = System.out::println;
        final PhotoDeduper photoDeduper = new PhotoDeduper(args[0], logConsumer);
        final FileChecksumVisitor fileChecksumVisitor = new FileChecksumVisitor(photoDeduper::add, FileChecksum.md5());

        photoDeduper.dedupe(fileChecksumVisitor);
    }
}
