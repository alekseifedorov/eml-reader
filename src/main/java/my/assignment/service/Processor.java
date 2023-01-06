package my.assignment.service;

import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;
import lombok.SneakyThrows;
import my.assignment.model.Node;

public interface Processor {
    List<Node> processPlainText(InputStream inputStream);

    @SneakyThrows
    List<Node> processZip(ZipInputStream zis);

    void saveEml(String fileName, InputStream inputStream);
}
