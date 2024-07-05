package org.example.socialmediaspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.service.BookService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/v1/public")
@RequiredArgsConstructor
@Slf4j
public class BulkInsertDataController {
    private final BookService bookService;

    private final ResponseFactory responseFactory;

    private final JobLauncher jobLauncher;

    private final Job job;

    private final String TEMP_STORAGE = System.getProperty("user.home") + File.separator + "Downloads" + File.separator;

    @PostMapping("/books/bulk-insert")
    public ResponseEntity bulkBookService( @RequestPart("file") MultipartFile file) throws IOException {
        return responseFactory.success(bookService.bulkBookService(file));
    }

    @PostMapping("/books/insert")
    public void importCsvToDBJob(@RequestParam("file") MultipartFile multipartFile) {

        try  {
            String originalFileName = multipartFile.getOriginalFilename();
            File fileToImport = new File(TEMP_STORAGE  + originalFileName);
            multipartFile.transferTo(fileToImport);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("fullPathFileName", TEMP_STORAGE  + originalFileName)
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 | JobParametersInvalidException
                 | IOException e) {
            e.printStackTrace();
        }
    }
}
