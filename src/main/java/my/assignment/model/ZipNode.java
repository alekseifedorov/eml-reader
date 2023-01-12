package my.assignment.model;

import java.util.List;
import java.util.zip.ZipInputStream;
import lombok.Builder;
import my.assignment.service.Processor;

@Builder
public class ZipNode implements Node {
    private Processor processor;
    private ZipInputStream zipInputStream;
    private String fileName;

    @Override
    public List<Node> process(Format format, boolean isEndNode) {
        if (format != Format.ZIP) {
            throw new IllegalArgumentException(String.format("Wrong format [%s] for zip file [%s]", format, fileName));
        }
        if (isEndNode) {
            processor.save(fileName, zipInputStream);
            return List.of();
        } else {
            return processor.processZip(zipInputStream);
        }
    }
}
