package org.sleepyparents.littleletters.test;

import images.ImageLoader;
import images.RandomImageLoader;

import com.google.inject.AbstractModule;

public class TestModule extends AbstractModule {

	@Override
	protected void configure() {
//		bind(LocationManager.class).toInstance((LocationManager) Robolectric.application.getSystemService(Context.LOCATION_SERVICE));
//		bind(IServiceRequestIntentList.class).to(ServiceRequestIntentList.class);
//		bind(new TypeLiteral<IUkGeocoder<BmcGeocodeResponse>>(){})
//		.to(BmcGeocoder.class);
		bind(ImageLoader.class).to(RandomImageLoader.class);
	}

}
