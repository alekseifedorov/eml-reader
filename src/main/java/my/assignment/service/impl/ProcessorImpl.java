package my.assignment.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import my.assignment.model.PlainTextNode;
import my.assignment.model.Node;
import my.assignment.model.EmlNode;
import my.assignment.model.ZipNode;
import my.assignment.service.Processor;
import my.assignment.service.Traversal;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessorImpl implements Processor {

    @SneakyThrows
    @Override
    public List<Node> processPlainText(InputStream inputStream) {
        var result = new ArrayList<Node>();
        MimeMessage message = new MimeMessage(null, inputStream);
        var type = message.getContentType();
        if (type.contains("multipart")) {
            Multipart multiPart = (Multipart) message.getContent();
            for (int partCount = 0; partCount < multiPart.getCount(); partCount++) {
                MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                    if (part.getContentType().contains("zip")) {
                        var node = ZipNode.builder()
                            .zipInputStream(new ZipInputStream(part.getInputStream()))
                            .processor(this)
                            .build();

                        result.add(node);
                    }

                    if (isEml(part)) {
                        var node = EmlNode.builder()
                            .fileName(part.getFileName())
                            .inputStream(part.getInputStream())
                            .processor(this)
                            .build();
                        result.add(node);
                    }
                }
            }
        }

        return result;
    }

    @SneakyThrows
    @Override
    public List<Node> processZip(ZipInputStream zis) {
        var buffer = new byte[1024];
        var result = new ArrayList<Node>();
        var zipEntry = zis.getNextEntry();
        while (zipEntry != null) {
            if (!zipEntry.isDirectory()) {
                var newFile = File.createTempFile(UUID.randomUUID().toString(), null);
                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                }

                var plainTextNode = PlainTextNode.builder()
                    .processor(this)
                    .inputStream(new FileInputStream(newFile))
                    .build();
                result.add(plainTextNode);
            }
            zis.closeEntry();
            zipEntry = zis.getNextEntry();
        }
        zis.close();

        return result;
    }

    @Override
    @SneakyThrows
    public void saveEml(String fileName, InputStream inputStream) {
        var buffer = new byte[1024];
        var newFile = new File(Traversal.OUTPUT_DIR, fileName);
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
        }
    }

    private boolean isEml(MimeBodyPart part) throws MessagingException {
        return part.isMimeType("message/rfc822") && part.getFileName().endsWith(".eml");
    }
}
