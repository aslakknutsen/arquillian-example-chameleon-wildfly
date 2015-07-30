package org.arquillian.example.chameleon.wildfly;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class ManagementClientTestCase {

	@Deployment(testable = false, name = "A") @TargetsContainer("jbossas")
	public static WebArchive deploy() {
		return ShrinkWrap.create(WebArchive.class)
				.addAsWebResource(EmptyAsset.INSTANCE, "index.html");
	}
	
	@Deployment(testable = false, name = "B") @TargetsContainer("wildfly")
	public static WebArchive deploy2() {
		return ShrinkWrap.create(WebArchive.class)
				.addAsWebResource(EmptyAsset.INSTANCE, "index.html");
	}

	@ArquillianResource
	private ManagementClient mc;
	
	@Test @OperateOnDeployment("A")
	public void shouldWorkJBoss() throws Exception {
		System.out.println(mc.getWebUri());
	}

	@Test @OperateOnDeployment("B")
	public void shouldWorkWildFly() throws Exception {
		System.out.println(mc.getWebUri());
	}
}
