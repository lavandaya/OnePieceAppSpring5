package be.kdg.programming5.onepiece.config;

import be.kdg.programming5.onepiece.business.service.CharacterService;
import be.kdg.programming5.onepiece.presentation.converter.StringToCrewConverter;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MessageSource messageSource;
    private final CharacterService characterService;

    public WebConfig(MessageSource messageSource,
                     CharacterService characterService) {
        this.messageSource = messageSource;
        this.characterService = characterService;}

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToCrewConverter(characterService));
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.setValidationMessageSource(messageSource);
        return validator;
    }

}