package my.assignment.model;

import java.util.List;

public interface Node {
    List<Node> process(Format format, boolean isEndNode);
}
