package org.sleepyparents.littleletters;

import java.lang.reflect.Method;

import org.robolectric.Robolectric;
import org.robolectric.TestLifecycleApplication;
import org.sleepyparents.littleletters.test.TestModule;

import roboguice.RoboGuice;
import android.app.Application;

import com.google.inject.Injector;
import com.google.inject.util.Modules;

/***
 * Robolectric will discover the Application class in the AndroidManifest.xml
 * and use this class in its place. This allows us to setup injection before each test.
 * 
 * from http://robolectric.blogspot.co.uk/2013/04/the-test-lifecycle-in-20.html
 *
 * The overall steps taken when running each test, then, are:
 * Create your application.
 * Call application.onCreate().
 * Call application.beforeTest().
 * Call application.prepareTest().
 * Run the test.
 * Call application.onTerminate().
 * Call application.afterTest().
 */
public class TestLittleLettersApplication extends Application
	implements TestLifecycleApplication {
	@Override public void beforeTest(Method method) {
	}
	
	@Override public void prepareTest(Object test) {
		Application app = Robolectric.application;
		RoboGuice.setBaseApplicationInjector(app, RoboGuice.DEFAULT_STAGE, 
				Modules.override(RoboGuice.newDefaultRoboModule(app)).with(new TestModule()));
		Injector injector = RoboGuice.getInjector(app);
		injector.injectMembers(test);
	}
	
	@Override public void afterTest(Method method) {
	}
}