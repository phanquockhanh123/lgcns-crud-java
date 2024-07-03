package org.example.socialmediaspring.controller;

import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.service.BookService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/public")
@RequiredArgsConstructor
public class BulkInsertDataController {
    private final BookService bookService;

    private final ResponseFactory responseFactory;

    private final JobLauncher jobLauncher;

    private final Job job;

    @PostMapping("/books/bulk-insert")
    public ResponseEntity bulkBookService( @RequestPart("file") MultipartFile file) throws IOException {
        return responseFactory.success(bookService.bulkBookService(file));
    }

    @PostMapping("/books/insert")
    public void importCsvToDBJob() {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        try  {
            jobLauncher.run(job, jobParameters);
        } catch (JobExecutionAlreadyRunningException
                 | JobRestartException
                 | JobInstanceAlreadyCompleteException
                 |JobParametersInvalidException e) {
            e.printStackTrace();
        }

    }
}
