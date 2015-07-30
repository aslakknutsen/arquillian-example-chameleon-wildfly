package org.arquillian.example.chameleon.wildfly;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class ManagementClientExtension implements LoadableExtension {

	public void register(ExtensionBuilder builder) {
		builder.service(ResourceProvider.class, ManagementClientProvider.class);
	}

}
