package my.assignment.model;

import java.io.InputStream;
import java.util.List;
import lombok.Builder;
import my.assignment.service.Processor;

@Builder
public class EmlNode implements Node {

    private Processor processor;
    private InputStream inputStream;
    private String fileName;

    @Override
    public List<Node> process(Format format, boolean isEndNode) {
        if (format != Format.EML) {
            throw new IllegalArgumentException(String.format("Wrong format [%s] for eml file [%s]", format, fileName));
        }
        if (isEndNode) {
            processor.save(fileName, inputStream);
            return List.of();
        } else {
            return processor.processEml(inputStream, fileName);
        }
    }
}
