package my.assignment.model;

import java.util.List;
import java.util.zip.ZipInputStream;
import lombok.Builder;
import my.assignment.service.Processor;

@Builder
public class ZipNode implements Node {
    private Processor processor;
    private ZipInputStream zipInputStream;

    @Override
    public List<Node> process() {
        return processor.processZip(zipInputStream);
    }
}
