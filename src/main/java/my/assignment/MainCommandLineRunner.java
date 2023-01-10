package my.assignment;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.assignment.model.Format;
import my.assignment.service.Traversal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MainCommandLineRunner implements CommandLineRunner {

    private final Traversal traversal;

    @Override
    public void run(String... args) {
        var format = List.of(Format.ZIP, Format.EML, Format.EML);
        if (args.length > 1) {
            if ("-t".equalsIgnoreCase(args[1].strip())) {
                String formatStr = args[2].toLowerCase().strip();
                format = Arrays.stream(formatStr.split("-"))
                    .map(Format::fromString).collect(Collectors.toList());
            }
        }
        try {
            traversal.process(args[0], format);
        } catch (Exception e) {
            log.error("Unexpected exception: ", e);
        }
    }
}
