package com.xsmart.camera.util;
 
import java.io.IOException;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.stereotype.Component;


@Component
public class YamlPropertySourceFactory implements PropertySourceFactory {
 
	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		PropertySource propertySource  = name != null ? new YamlPropertySourceLoader().load(name,resource.getResource()).get(0) :
				new YamlPropertySourceLoader().load(getNameForResource(resource.getResource()),resource.getResource()).get(0);
		return propertySource;

		//return new YamlPropertySourceLoader().load(name,resource.getResource()).get(0);
	}
	private static String getNameForResource(Resource resource) {
		String name = resource.getDescription();
		if (!org.springframework.util.StringUtils.hasText(name)) {
			name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
		}
		return name;
	}


}