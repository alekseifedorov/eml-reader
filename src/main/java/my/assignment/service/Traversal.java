package my.assignment.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.assignment.model.PlainTextNode;
import my.assignment.model.Format;
import my.assignment.model.Node;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Traversal {

    public static final String OUTPUT_DIR = "output";
    private final Processor processor;

    @SneakyThrows
    public void process(String filepath, Format format) {
        var nodes = new ArrayList<Node>();
        switch (format) {
            case ZIP:
                processZipFile(filepath, nodes);
                break;
            case EML:
                processEmlFile(filepath, nodes);
                break;
            default:
                throw new IllegalArgumentException("Unexpected file format");
        }
        traverse(nodes);
    }

    void traverse(List<Node> nodes) {
        createOutputDirectory(nodes);
        while (!nodes.isEmpty()) {
            var next = nodes.remove(0);
            nodes.addAll(next.process());
        }
    }

    private static void createOutputDirectory(List<Node> nodes) {
        if (!nodes.isEmpty()) {
            var dir = new File(OUTPUT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    private void processEmlFile(String filepath, List<Node> nodes) throws IOException {
        var file = new File(filepath);
        var node = PlainTextNode.builder()
            .processor(processor)
            .inputStream(new FileInputStream(file))
            .build();

        nodes.add(node);
    }

    private void processZipFile(String filepath, List<Node> nodes) throws IOException {
        var file = new ZipFile(filepath);
        for (var e = file.entries(); e.hasMoreElements(); ) {
            var is = file.getInputStream(e.nextElement());
            var node = PlainTextNode.builder()
                .inputStream(is)
                .processor(processor)
                .build();
            nodes.add(node);
        }
    }
}
