package my.assignment;

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
    public void run(String... args) throws Exception {
        var format = Format.ZIP;
        if (args.length > 1) {
            if ("-t".equalsIgnoreCase(args[1].strip())) {
                format = Format.fromString(args[2].toLowerCase().strip());
            }
        }
        try {
            traversal.process(args[0], format);
        } catch (Exception e) {
            log.error("Unexpected exception: ", e);
        }
    }
}
