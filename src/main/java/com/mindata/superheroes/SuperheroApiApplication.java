package com.mindata.superheroes;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.sql.DataSource;

@SpringBootApplication
@EnableCaching
public class SuperheroApiApplication implements CommandLineRunner {

	private final DataSource dataSource;

	public SuperheroApiApplication(@Autowired final DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(SuperheroApiApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Flyway.configure().baselineOnMigrate(true).dataSource(dataSource).load().migrate();
	}

}
