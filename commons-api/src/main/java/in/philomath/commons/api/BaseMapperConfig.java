package in.philomath.commons.api;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingInheritanceStrategy;
import org.mapstruct.ReportingPolicy;

/**
 * BaseMapperConfig
 *
 * @author Bhagath Charan 15-12-2019
 */
@MapperConfig(unmappedTargetPolicy = ReportingPolicy.IGNORE,
		mappingInheritanceStrategy = MappingInheritanceStrategy.AUTO_INHERIT_FROM_CONFIG,
		componentModel = "spring")
public interface BaseMapperConfig {
}
