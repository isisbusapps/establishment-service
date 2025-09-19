package uk.ac.rl.facilities.impl.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;

import static org.mapstruct.MappingConstants.ComponentModel.JAKARTA_CDI;

@MapperConfig(componentModel = JAKARTA_CDI, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MappingConfig {
}