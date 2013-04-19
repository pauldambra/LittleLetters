package org.sleepyparents.littleletters;

import images.ImageLoader;
import images.RandomImageLoader;

import com.google.inject.AbstractModule;

public class RoboGuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ImageLoader.class).to(RandomImageLoader.class);
	}

}
