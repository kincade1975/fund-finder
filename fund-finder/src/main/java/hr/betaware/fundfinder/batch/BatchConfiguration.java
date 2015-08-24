package hr.betaware.fundfinder.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoOperations;

import hr.betaware.fundfinder.domain.City;
import hr.betaware.fundfinder.domain.Nkd;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	private MongoOperations mongoOperations;

	@Bean
	@Qualifier("readerCity")
	public ItemReader<CityItem> readerCity() {
		FlatFileItemReader<CityItem> reader = new FlatFileItemReader<CityItem>();
		reader.setResource(new ClassPathResource("csv/cities.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<CityItem>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] { "id", "name", "county", "group" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<CityItem>() {{
				setTargetType(CityItem.class);
			}});
		}});
		return reader;
	}

	@Bean
	@Qualifier("readerNkd")
	public ItemReader<NkdItem> readerNkd() {
		FlatFileItemReader<NkdItem> reader = new FlatFileItemReader<NkdItem>();
		reader.setResource(new ClassPathResource("csv/nkd.csv"));
		reader.setLinesToSkip(1);
		reader.setLineMapper(new DefaultLineMapper<NkdItem>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] { "id", "sector", "sectorName", "area", "activity", "activityName" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<NkdItem>() {{
				setTargetType(NkdItem.class);
			}});
		}});
		return reader;
	}

	@Bean
	@Qualifier("processorCity")
	public ItemProcessor<CityItem, City> processorCity() {
		return new CityItemProcessor();
	}

	@Bean
	@Qualifier("processorNkd")
	public ItemProcessor<NkdItem, Nkd> processorNkd() {
		return new NkdItemProcessor();
	}

	@Bean
	@Qualifier("writerCity")
	public ItemWriter<City> writerCity() {
		MongoItemWriter<City> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoOperations);
		return writer;
	}

	@Bean
	@Qualifier("writerNkd")
	public ItemWriter<Nkd> writerNkd() {
		MongoItemWriter<Nkd> writer = new MongoItemWriter<>();
		writer.setTemplate(mongoOperations);
		return writer;
	}

	@Bean
	@Qualifier("step1")
	public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<CityItem> readerCity, ItemWriter<City> writerCity, ItemProcessor<CityItem, City> processorCity) {
		return stepBuilderFactory.get("step1")
				.<CityItem, City> chunk(10)
				.reader(readerCity)
				.processor(processorCity)
				.writer(writerCity)
				.build();
	}

	@Bean
	@Qualifier("step2")
	public Step step2(StepBuilderFactory stepBuilderFactory, ItemReader<NkdItem> readerNkd, ItemWriter<Nkd> writerNkd, ItemProcessor<NkdItem, Nkd> processorNkd) {
		return stepBuilderFactory.get("step2")
				.<NkdItem, Nkd> chunk(10)
				.reader(readerNkd)
				.processor(processorNkd)
				.writer(writerNkd)
				.build();
	}

	@Bean
	public Job importJob(JobBuilderFactory jobs, Step step1, Step step2, JobExecutionListener listener) {
		return jobs.get("importJob")
				.incrementer(new RunIdIncrementer())
				.listener(listener)
				.flow(step1).next(step2)
				.end()
				.build();
	}

}
