package org.example.socialmediaspring.config;

import lombok.RequiredArgsConstructor;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.repository.BookRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.management.PlatformManagedObject;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final BookRepository repository;
    private final PlatformTransactionManager platformTransactionManager;

    @Bean
    public FlatFileItemReader<Book> itemReader() {
        FlatFileItemReader<Book> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/books.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());

        return  itemReader;
    }

    @Bean
    public  BookProcessor processor() {
        return  new BookProcessor();
    }

    @Bean
    public RepositoryItemWriter<Book> writer() {
        RepositoryItemWriter<Book> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");

        return  writer;
    }

    @Bean
    public Step importStep() {
        return new StepBuilder("csvImport", jobRepository)
                .<Book, Book>chunk(10, platformTransactionManager)
                .reader(itemReader())
                .processor(processor())
                .writer(writer())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Job runJob() {
        return new JobBuilder("importBooks", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importStep())
                .build();
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return  asyncTaskExecutor;
    }
    private LineMapper<Book> lineMapper() {
        DefaultLineMapper<Book> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "isbn", "title", "author","price", "yearOfPublish", "quantity", "quantityAvail", "filePath", "description");

        BeanWrapperFieldSetMapper<Book> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Book.class);

        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);

        return lineMapper;
    }
}
