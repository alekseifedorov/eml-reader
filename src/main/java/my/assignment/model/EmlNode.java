package my.assignment.model;

import java.io.InputStream;
import java.util.List;
import lombok.Builder;
import my.assignment.service.Processor;

@Builder
public class EmlNode implements Node {
    private InputStream inputStream;
    private String fileName;
    private Processor processor;

    @Override
    public List<Node> process() {
        processor.saveEml(fileName, inputStream);
        return List.of();
    }
}
