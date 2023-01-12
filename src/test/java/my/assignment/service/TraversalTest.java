package my.assignment.service;

import static org.assertj.core.api.Assertions.assertThat;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;
import my.assignment.model.EmlNode;
import my.assignment.model.Format;
import my.assignment.model.ZipNode;
import my.assignment.service.impl.ProcessorImpl;
import org.junit.jupiter.api.Test;

class TraversalTest {

    @Test
    void shouldProcessEmlFile() throws IOException {
        deleteDir(new File(Traversal.OUTPUT_DIR));
        ProcessorImpl processor = new ProcessorImpl();
        Traversal traversal = new Traversal(processor);
        InputStream is = this.getClass().getResourceAsStream("/Email 2.eml");
        traversal.traverse(new ArrayList<>(List.of(EmlNode.builder()
            .inputStream(is)
            .processor(processor)
            .build())), List.of(Format.EML, Format.ZIP, Format.EML, Format.EML));

        assertThat(Files.list(Path.of(Traversal.OUTPUT_DIR))).extracting(Path::getFileName)
            .contains(Path.of("Test 1.eml"), Path.of("Test 3.eml"));
    }

    @Test
    void shouldProcessZipFile() throws IOException {
        deleteDir(new File(Traversal.OUTPUT_DIR));
        ProcessorImpl processor = new ProcessorImpl();
        Traversal traversal = new Traversal(processor);
        InputStream is = this.getClass().getResourceAsStream("/archive.zip");
        traversal.traverse(new ArrayList<>(List.of(ZipNode.builder()
            .zipInputStream(new ZipInputStream(is))
            .processor(processor)
            .build())), List.of(Format.ZIP, Format.EML));

        assertThat(Files.list(Path.of(Traversal.OUTPUT_DIR))).extracting(Path::getFileName)
            .contains(Path.of("Email 1.eml"), Path.of("Email 2.eml"));
    }

    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
}
