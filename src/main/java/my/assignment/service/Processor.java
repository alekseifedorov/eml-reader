package my.assignment.service;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;
import my.assignment.model.Node;

public interface Processor {
    List<Node> processEml(InputStream inputStream, String filename);

    List<Node> processZip(ZipInputStream zis);

    void saveEml(String fileName, InputStream inputStream);
}
