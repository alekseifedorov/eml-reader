package my.assignment.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import my.assignment.model.EmlNode;
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
    public void process(String filepath, List<Format> formats) {
        if (formats.size() == 1) {
            throw new IllegalArgumentException("The format list size must be greater than 1");
        }
        var nodes = new ArrayList<Node>();
        switch (formats.remove(0)) {
            case ZIP:
                processZipFile(filepath, nodes);
                break;
            case EML:
                processEmlFile(filepath, nodes);
                break;
            default:
                throw new IllegalArgumentException("Unexpected file format");
        }
        traverse(nodes, formats);
    }

    void traverse(List<Node> nodes, List<Format> formats) {
        var curFormat = 0;
        createOutputDirectory(nodes);
        int nodesOnLevel = nodes.size();
        int curNode = 0;
        while (!nodes.isEmpty()) {
            var next = nodes.remove(0);
            nodes.addAll(next.process(formats.get(curFormat), curFormat == formats.size() - 1));
            curNode++;
            if (curNode == nodesOnLevel) {
                curNode = 0;
                nodesOnLevel = nodes.size();
                curFormat++;
            }
        }
    }

    private void createOutputDirectory(List<Node> nodes) {
        if (!nodes.isEmpty()) {
            var dir = new File(OUTPUT_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

    private void processEmlFile(String filepath, List<Node> nodes) throws IOException {
        var file = new File(filepath);
        var node = EmlNode.builder()
            .processor(processor)
            .inputStream(new FileInputStream(file))
            .fileName(file.getName())
            .build();

        nodes.add(node);
    }

    private void processZipFile(String filepath, List<Node> nodes) throws IOException {
        var file = new ZipFile(filepath);
        for (var e = file.entries(); e.hasMoreElements(); ) {
            ZipEntry entryZip = e.nextElement();
            var is = file.getInputStream(entryZip);
            var node = EmlNode.builder()
                .inputStream(is)
                .processor(processor)
                .fileName(entryZip.getName())
                .build();
            nodes.add(node);
        }
    }
}
