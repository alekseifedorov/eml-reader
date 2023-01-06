package my.assignment.model;

import java.io.InputStream;
import java.util.List;
import lombok.Builder;
import my.assignment.service.Processor;

@Builder
public class PlainTextNode implements Node {

    private Processor processor;
    private InputStream inputStream;

    @Override
    public List<Node> process() {
        return processor.processPlainText(inputStream);
    }
}
