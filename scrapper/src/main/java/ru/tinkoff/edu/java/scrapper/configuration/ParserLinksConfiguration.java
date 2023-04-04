package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import parserservice.ParserLinks;
import parserservice.factories.ParserLinksFactory;
import parserservice.factories.factoryimpl.ParserLinksFactoryImpl;

@ConfigurationProperties
public class ParserLinksConfiguration {
    @Bean
    public ParserLinksFactory parserLinksFactory(){
        return new ParserLinksFactoryImpl();
    }
    @Bean
    public ParserLinks parserLinks(ParserLinksFactory parserLinksFactory){
        ParserLinks firstParser = parserLinksFactory.getParserLinks();
        return firstParser;
    }
}
