package org.arquillian.example.chameleon.wildfly;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.arquillian.container.chameleon.ChameleonContainer;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.context.ContainerContext;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

public class ManagementClientProvider implements ResourceProvider {

	private static final String MANAGEMENT_CLIENT_CLASS = "org.jboss.as.arquillian.container.ManagementClient"; 
	
	@Inject
	private Instance<Container> currentContainer;

	@Inject
	private Instance<ContainerContext> containerContext;

	public boolean canProvide(Class<?> type) {
		return type == ManagementClient.class &&
			   currentContainer.get().getDeployableContainer().getClass() == ChameleonContainer.class;
	}

	public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
		return Proxy.newProxyInstance(
				ManagementClientProvider.class.getClassLoader(),
				new Class<?>[] {ManagementClient.class},
				new ClientInvocationHandler(currentContainer.get(), containerContext.get()));
	}
	
	private static class ClientInvocationHandler implements InvocationHandler {
		
		private Container container;
		private ContainerContext context;

		public ClientInvocationHandler(Container container, ContainerContext context) {
			this.container = container;
			this.context = context;
		}
		
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object client = resolveClient();
			
			Class<?> clientClass = client.getClass();
			
			Method targetMethod = clientClass.getDeclaredMethod(method.getName(), method.getParameterTypes());
						
			return targetMethod.invoke(client, args);
		}

		private Object resolveClient() throws Exception {
			ChameleonContainer chameleon = getChameleonContainer();
			Class<?> managementClientClass = chameleon.resolveTargetClass(MANAGEMENT_CLIENT_CLASS);
			return context.getObjectStore().get(managementClientClass);
		}

		private ChameleonContainer getChameleonContainer() {
			return (ChameleonContainer)container.getDeployableContainer();
		}
	}
}
